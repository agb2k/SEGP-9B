package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class LastBloodRecordController implements Serializable {
    private ArrayList<LastBloodPressure> lastBloodPressureList = new ArrayList<>();
    private ArrayList<LastBloodGlucose> lastBloodGlucoseList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LastBloodRecordController() throws UnsupportedEncodingException {
        RetrieveLastBloodRecord getDB = new RetrieveLastBloodRecord();
        lastBloodPressureList = getDB.getLastBloodPressureList();
        lastBloodGlucoseList = getDB.getLastBloodGlucoseList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData() throws UnsupportedEncodingException {
        RetrieveLastBloodRecord getDB = new RetrieveLastBloodRecord();
        lastBloodPressureList = getDB.getLastBloodPressureList();
        lastBloodGlucoseList = getDB.getLastBloodGlucoseList();
    }

    public ArrayList<LastBloodPressure> getLastBloodPressureList() {
        return lastBloodPressureList;
    }

    public ArrayList<LastBloodGlucose> getLastBloodGlucoseList() {
        return lastBloodGlucoseList;
    }
}
