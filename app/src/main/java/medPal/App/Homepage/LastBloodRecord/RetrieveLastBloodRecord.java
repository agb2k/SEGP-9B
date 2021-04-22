package medPal.App.Homepage.LastBloodRecord;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;


public class RetrieveLastBloodRecord {

    private ArrayList<LastBloodPressure> lastBloodPressureList = new ArrayList<>();
    private ArrayList<LastBloodGlucose> lastBloodGlucoseList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrieveLastBloodRecord() throws UnsupportedEncodingException {
        String jsonStr;
        JSONArray jsonArray;
        JSONObject jsonObject;
        LastBloodPressure bpObj;
        LastBloodGlucose bgObj;

        DatabaseHelper BpDbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/getLastBloodPressure.php");
        BpDbHelper.setUserInfo();
        DatabaseHelper BgDbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/getLastSugarRecord.php");
        BgDbHelper.setUserInfo();

        try {
            //Get last blood pressure record
            jsonStr = BpDbHelper.send();
            jsonArray = new JSONArray(jsonStr);
            for(int i=0; i<jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                bpObj = makeBPobj(jsonObject);
                lastBloodPressureList.add(bpObj);
            }

            //get last blood glucose record
            jsonStr = BgDbHelper.send();
            jsonArray = new JSONArray(jsonStr);
            for(int i=0; i<jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                bgObj = makeBGobj(jsonObject);
                lastBloodGlucoseList.add(bgObj);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LastBloodPressure makeBPobj(JSONObject jsonObject) throws JSONException {
        String date = jsonObject.getString("Date");
        String time = jsonObject.getString("Time");
        int SYS = jsonObject.getInt("SYS");
        int DIA = jsonObject.getInt("DIA");

        return new LastBloodPressure(date, time, SYS, DIA);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LastBloodGlucose makeBGobj(JSONObject jsonObject) throws JSONException {
        String date = jsonObject.getString("Date");
        String time = jsonObject.getString("Time");
        int level = jsonObject.getInt("Level");

        return new LastBloodGlucose(date, time, level);
    }

    public ArrayList<LastBloodPressure> getLastBloodPressureList() {
        return lastBloodPressureList;
    }

    public ArrayList<LastBloodGlucose> getLastBloodGlucoseList() {
        return lastBloodGlucoseList;
    }

}
