package medPal.App.UserIdentification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class UserIdentityDBHelper {

    private final SQLiteDatabase myDB;
    private static String DB_NAME = "UserIdentityDB";
    private static String TABLE_NAME = "MedicationRecord";

    public UserIdentityDBHelper(Context context) {
        File DB_PATH = context.getDatabasePath(DB_NAME);

        myDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"(" +
                        "user_id INTEGER NOT NULL," +
                        "username TEXT NOT NULL," +
                        "email TEXT NOT NULL" +
                        ");"
        );
    }

    public void insertRecord(int user_id, String username, String email) {
        myDB.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                user_id + "," +
                "'" + username + "'," +
                "'" + email + "'" +
                ")"
        );
    }

    /**
     * Check if table is empty
     * @return True if table is empty
     */
    public boolean isEmpty() {
        boolean isEmpty = true;

        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_NAME + ";",null);
        if (cursor.getCount() == 0)
            isEmpty = true;
        else
            isEmpty = false;
        cursor.close();

        return isEmpty;
    }

    /**
     * Set information for UserIdentity instance
     */
    public void getRecord() {
        // Get instance of UserIdentity
        UserIdentity identity = UserIdentity.getInstance();

        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_NAME + ";",null);
        if (cursor.moveToFirst()){
            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            // Set information
            identity.setInformation(user_id,username,email);
        }
        cursor.close();
    }

}
