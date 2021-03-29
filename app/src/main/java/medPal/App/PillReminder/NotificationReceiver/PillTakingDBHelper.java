package medPal.App.PillReminder.NotificationReceiver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

import medPal.App.AlarmAndNotification.RequestCodeItem;
import medPal.App.AlarmAndNotification.SQLiteHelper;

public class PillTakingDBHelper {

    private final SQLiteDatabase myDB;
    private static String DB_NAME = "MedicationDB";
    private static String TABLE_NAME = "MedicationRecord";

    public PillTakingDBHelper(Context context) {
        File DB_PATH = context.getDatabasePath(PillTakingDBHelper.DB_NAME);

        myDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"(" +
                        "month INTEGER NOT NULL," +
                        "date TEXT NOT NULL," +
                        "time TEXT NOT NULL," +
                        "medicine TEXT NOT NULL," +
                        "dose INTEGER NOT NULL," +
                        "status INTEGER NOT NULL" +
                        ");"
        );
    }

    public void insertRecord(int month, String date, String time, String medicine, int dose, int status) {
        myDB.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(" +
                month + "," +
                "'" + date + "'," +
                "'" + time + "'," +
                "'" + medicine + "'," +
                dose + "," +
                status +
                ")"
        );
    }

    public ArrayList<MedicationRecord> getRecord(int month) {
        ArrayList<MedicationRecord> list = new ArrayList<>();

        Cursor cursor = myDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE month = " + month + ";",null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String medicine = cursor.getString(cursor.getColumnIndex("medicine"));
                int dose = cursor.getInt(cursor.getColumnIndex("dose"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));

                MedicationRecord record = new MedicationRecord(date,time,medicine,dose,status);
                list.add(record);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

    public ArrayList<String> getTakenTime(String date) {
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = myDB.rawQuery("SELECT time FROM " + TABLE_NAME + " WHERE date LIKE '" + date + "';",null);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));

                list.add(time);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

}
