package medPal.App.Tracker.BloodPressure;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import medPal.App.DatabaseHelper;

/**
 * This class helps to delete medicine from database.
 */
public class DeletePressure {

    private String encodedData = "";
    private String returnStatus;

    DeletePressure(String date, String time) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.DELETE, DatabaseHelper.BLOOD_PRESSURE);
        dbHelper.setUserInfo();
        dbHelper.encodeData("Date",date);
        dbHelper.encodeData("Time",time);

        String result = dbHelper.send();

        returnStatus = result.substring(1, result.length()-1);
        Log.d("result", result);
        Log.d("returnStatus", returnStatus);
    }

    public boolean success() {
        return (returnStatus.equals("Successfully saved"));
    }

}
