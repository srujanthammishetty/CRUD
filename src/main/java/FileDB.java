import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by srujant on 24/6/16.
 */
public class FileDB {


    private String dbPath;

    FileDB(String dbPath) {
        this.dbPath = dbPath;
        createDb(dbPath);
    }


    public String getDbPath() {
        return dbPath;
    }


    public File getPersistenceFile(FileDB fileDb, Class entityClass) {

        File file = new File(fileDb.getDbPath() + File.separator +entityClass.getName().toString());
        return file;
    }

    private void createDb(String dbPath) {
        File file = new File(dbPath);
        try {
            file.mkdir();
        }catch (Exception e){
            throw new RuntimeException("Denied to create Directory",e);
        }
    }
}
