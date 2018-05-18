package perriobarreteau.apprentissagemusique;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private static final String TABLE_NAME = "MFCCs";
    public static final String KEY_cle = "cle";
    public static final String KEY_classe = "classe";
    public static final String KEY_mfcc = "mfcc";
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_cle+" INTEGER primary key," +
            " "+KEY_classe+" INTEGER," +
            " "+KEY_mfcc+" TEXT" +
            ");";
    private MySQLite mySQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    public DBManager(Context context) {
        mySQLite = MySQLite.getInstance(context);
    }

    public void open() {
        db = mySQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public Cursor getAll() {
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

}
