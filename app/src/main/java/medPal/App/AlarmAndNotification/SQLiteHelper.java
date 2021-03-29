package medPal.App.AlarmAndNotification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class SQLiteHelper {

    private final SQLiteDatabase myDB;
    private static String DB_NAME = "AlarmAndNotificationDB";
    private static String TABLE_NAME = "RequestCodeTable";

    public SQLiteHelper(Context context) {
        File DB_PATH = context.getDatabasePath(SQLiteHelper.DB_NAME);

        myDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"(" +
                        "type INTEGER NOT NULL," +
                        "time INTEGER," +
                        "repeating_interval INTEGER," +
                        "reference_id INTEGER," +
                        "isActive INTEGER" +
                        ");"
        );
    }

    public int getRequestCode(int type, long time, long repeating_interval, int reference_id) {
        myDB.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" + type + "," + time + "," + repeating_interval + "," + reference_id + ",1)");

        int last_id = 0;
        Cursor cursor = myDB.rawQuery("SELECT rowid FROM " + TABLE_NAME +
                        " ORDER BY rowid DESC LIMIT 1",
                null);
        if(cursor != null && cursor.moveToFirst()) {
            last_id = (int)cursor.getLong(0);
        }
        if(cursor != null)
            cursor.close();
        return last_id;
    }

    public int getRequestCode(int type, long time, int reference_id) {
        myDB.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                type + "," +
                time + "," +
                0 + "," +
                reference_id +
                ",1)"
        );

        int last_id = 0;
        Cursor cursor = myDB.rawQuery("SELECT rowid FROM " + TABLE_NAME +
                        " ORDER BY rowid DESC LIMIT 1",
                null);
        if(cursor != null && cursor.moveToFirst()) {
            last_id = (int)cursor.getLong(0);
        }
        if(cursor != null)
            cursor.close();
        return last_id;
    }

    public ArrayList<RequestCodeItem> deactivateAll(int reference_id) {
        ArrayList<RequestCodeItem> list = new ArrayList<>();

        // Find all requestCode records related to reference_id
        Cursor cursor = myDB.rawQuery("SELECT rowid, * FROM " + TABLE_NAME +
                        " WHERE reference_id = " + reference_id +
                        " AND isActive = 1;"
                ,null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int row_id = cursor.getInt(cursor.getColumnIndex("rowid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                long repeating_interval = cursor.getLong(cursor.getColumnIndex("repeating_interval"));

                RequestCodeItem item = new RequestCodeItem(row_id,type,time,repeating_interval,reference_id);
                list.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();

        // Deactivate ALL requestCode records related to reference_id
        myDB.execSQL("UPDATE " + TABLE_NAME + " SET isActive = 0" +
                " WHERE reference_id = " + reference_id +
                " AND isActive = 1;");

        return list;
    }

    public ArrayList<RequestCodeItem> deactivateSpecificTime(int reference_id, long time, long interval) {
        ArrayList<RequestCodeItem> list = new ArrayList<>();

        // Find all requestCode records related to reference_id on the specific time
        Cursor cursor = myDB.rawQuery("SELECT rowid, * FROM " + TABLE_NAME +
                " WHERE reference_id = " + reference_id +
                " AND isActive = 1" +
                " AND time = " + time +
                " AND repeating_interval = " + interval +
                ";",null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int row_id = cursor.getInt(cursor.getColumnIndex("rowid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                long repeating_interval = cursor.getLong(cursor.getColumnIndex("repeating_interval"));

                RequestCodeItem item = new RequestCodeItem(row_id,type,time,repeating_interval,reference_id);
                list.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();

        // Deactivate ALL requestCode records related to reference_id on the specific time
        myDB.execSQL("UPDATE " + TABLE_NAME + " SET isActive = 0 " +
                "WHERE reference_id = " + reference_id +
                " AND isActive = 1" +
                " AND time = " + time +
                " AND repeating_interval = " + interval +
                ";");

        return list;
    }

    public ArrayList<RequestCodeItem> getAllRecord() {
        ArrayList<RequestCodeItem> list = new ArrayList<>();

        Cursor cursor = myDB.rawQuery("SELECT rowid, * FROM " + TABLE_NAME + " WHERE isActive = 1;",null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                int row_id = cursor.getInt(cursor.getColumnIndex("rowid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                long time = cursor.getLong(cursor.getColumnIndex("time"));
                long repeating_interval = cursor.getLong(cursor.getColumnIndex("repeating_interval"));
                int reference_id = cursor.getInt(cursor.getColumnIndex("reference_id"));

                RequestCodeItem item = new RequestCodeItem(row_id,type,time,repeating_interval,reference_id);
                list.add(item);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

}
