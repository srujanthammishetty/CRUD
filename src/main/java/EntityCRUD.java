import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by srujant on 24/6/16.
 */
public class EntityCRUD<T> {

    private Class entityClass;
    private File file;
    private FileDB fileDb;
    private static final Logger logger = LoggerFactory.getLogger(EntityCRUD.class.getName());

    EntityCRUD(FileDB fileDb, Class entityClass) {
        this.entityClass = entityClass;
        this.fileDb = fileDb;
        this.file = fileDb.getPersistenceFile(fileDb, entityClass);
        if (!file.exists()) {
            Field[] fields = entityClass.getDeclaredFields();
            String[] columnNames = new String[fields.length];
            int i = 0;
            for (Field field : fields) {
                columnNames[i++] = field.getName();
            }
            writeAttributes(file, columnNames, fields);
        }
    }




    public void add(T entity) {

        String record ;
        Writer writer = null;
        try {
            record = objectToString(entity);
            if (file.exists() && file.length() >= Math.pow(2, 30)) {
                file = garbageCollector(file);
            }
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException while closing buffer", e);
                }
            }
        }
    }


    public List<T> getAll() {

        List<T> resultList = new ArrayList();
        String record;
        String[] rowData;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String[] columnNames = getColumns(bufferedReader);
            Class<?>[] columnTypes = getColumnTypes();
            while ((record = bufferedReader.readLine()) != null) {
                if(record.charAt(0)=='Y') {
                    rowData = getRowData(record);
                    resultList.add(getInstance(columnNames, columnTypes, rowData));
                }
            }
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.warn("IOException while closing file", e);
                }
            }
        }
        return resultList;
    }


    public List<T> get(Criteria criteria) {

        String record ;
        List<T> resultList = new ArrayList();
        String[] rowData;
        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new FileReader(file));
            String[] columnNames = getColumns();
            Class<?>[] columnTypes = getColumnTypes();
            while ((record = bufferedReader.readLine()) != null) {
                if(record.charAt(0)=='Y') {
                    rowData = getRowData(record);
                    if (criteria.isMatching(columnNames, rowData)) {
                        resultList.add(getInstance(columnNames, columnTypes, rowData));
                    }
                }
            }
        } catch (IOException fe) {
            throw new RuntimeException("Failed to read the file", fe);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.warn("IOException while closing file", e);
                }
            }
        }
        return resultList;
    }

    public void update(T entity, Criteria criteria) {
        String newRecord = objectToString(entity);
        update(criteria, "update", newRecord);
    }

    public void delete(Criteria criteria) {
        update(criteria, "delete", " ");
    }


    private void update(Criteria criteria, String operation, String newRecord) {
        RandomAccessFile randomAccessFile = null;
        String record;
        String rowData[];
        long previousOffset;
        long currentOffset;
        long fileLength;
        long endOfFile;
        try {
            if (file.exists() && file.length() >= Math.pow(2, 30)) {
                file = garbageCollector(file);
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            String[] columnNames = getColumns();
            previousOffset = randomAccessFile.getFilePointer();
            endOfFile = randomAccessFile.length();
            fileLength = randomAccessFile.length();
            while (randomAccessFile.getFilePointer() < fileLength) {
                record = randomAccessFile.readLine();
                rowData = getRowData(record);
                if (criteria.isMatching(columnNames, rowData) && (record.charAt(0) == 'Y')) {
                    currentOffset = randomAccessFile.getFilePointer();
                    randomAccessFile.seek(previousOffset);
                    randomAccessFile.write("N".getBytes());
                    if (operation.equals("update")) {
                        append(randomAccessFile, endOfFile, newRecord);
                    }
                    randomAccessFile.seek(currentOffset);
                    endOfFile += newRecord.length();
                }
                previousOffset = randomAccessFile.getFilePointer();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void append(RandomAccessFile randomAccessFile, long endOfFile, String newRecord) throws IOException {
        randomAccessFile.seek(endOfFile);
        randomAccessFile.write(newRecord.getBytes());
    }

 

    private T getInstance(String[] columnNames, Class[] columnTypes, String[] rowdata) {
        int i = 0;
        T t;
        try {
            t = (T) entityClass.newInstance();
            for (String columnName : columnNames) {
                Method m = t.getClass().getDeclaredMethod(getSetterMethodName(columnName), columnTypes[i]);
                Object value = ObjectType.getValueByType(columnTypes[i].getName(), rowdata[i]);
                m.invoke(t, value);
                i++;
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Matching Method wasn't found", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to Instantiate Object", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(" Exception while accessing class", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception due to invoked method or Constructor", e);
        }
        return t;
    }


    private String[] getColumns() {
        String[] columnNames;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            columnNames = getColumns(bufferedReader);
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException Occured", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException Occured", e);
                }
            }
        }
        return columnNames;
    }


    private Class[] getColumnTypes() {
        int i = 0;
        Field[] fields = entityClass.getDeclaredFields();
        Class<?>[] columnTypes = new Class<?>[fields.length];
        for (Field column : fields) {
            columnTypes[i++] = column.getType();
        }
        return columnTypes;
    }

    private String[] getColumns(BufferedReader bufferedReader) throws IOException {
        String record;
        record = bufferedReader.readLine();
        return record.split(",");
    }

    private String[] getRowData(String record) {
        return record.substring(2).split(",");
    }

    private String getSetterMethodName(String columnName) {
        return "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    }

    private String getGetterMethodName(String columnName) {
        return "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    }

    private void writeAttributes(File file, String[] columnNames, Field[] fields) {

        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            int columnsCount = columnNames.length;
            for (String columnName : columnNames) {
                columnsCount--;
                writer.write(columnName);
                if (columnsCount == 0) {
                    writer.write("\n");
                } else {
                    writer.write(",");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String objectToString(T entity) {

        String columnNames[] = null;
        String record = "Y,";
        int columnsCount;
        columnNames = getColumns();
        try {
            columnsCount = columnNames.length;
            for (String columnName : columnNames) {
                Method m = entity.getClass().getDeclaredMethod(getGetterMethodName(columnName), null);
                columnsCount--;
                if (columnsCount == 0) {
                    record += m.invoke(entity, null).toString() + '\n';
                } else {
                    record += m.invoke(entity, null).toString() + ",";
                }
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Matching Method wasn't found", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            new RuntimeException(e);
        }
        return record;
    }

    private File garbageCollector(File file) {

        BufferedWriter writer = null;
        BufferedReader reader = null;
        File newFile =null;
        String record;
        try {
            newFile= new File(file.getAbsolutePath() + file.getName() +File.separator+ "new");;
            writer = new BufferedWriter(new FileWriter(newFile, true));
            reader = new BufferedReader(new FileReader(file));
            while ((record = reader.readLine()) != null) {
                if (record.charAt(0) == 'Y') {
                    writer.write(record);
                }
            }
            try {
                file.delete();
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file",e);
            }
            try {
                newFile.renameTo(file);
            } catch (Exception e) {
                throw new RuntimeException("Denied to rename the file",e);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return newFile;
    }

}


