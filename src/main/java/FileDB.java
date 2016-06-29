import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by srujant on 24/6/16.
 */
public class FileDB {


    private File dbPath;

    FileDB(String dbPath) {
        this(new File(dbPath));
    }

    FileDB(File dbPath) {
        this.dbPath = dbPath;
        createDb();
    }


    public File getPersistenceFile(FileDB fileDb, Class entityClass) {

        File file = new File(dbPath.getAbsolutePath() + File.separator + entityClass.getName().toString());
        return file;
    }

    private void createDb() {
        try {
            this.dbPath.mkdir();
        } catch (Exception e) {
            throw new RuntimeException("Denied to create Directory", e);
        }
    }


}
