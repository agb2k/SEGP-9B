package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/***
 * Class to handle last Blood Pressure list and Last Blood Sugar level list
 */
public class LastBloodRecordController implements Serializable {
    private ArrayList<LastBloodPressure> lastBloodPressureList = new ArrayList<>();
    private ArrayList<LastBloodGlucose> lastBloodGlucoseList = new ArrayList<>();

    /***
     * Initialise and get list of last blood pressure and last blood glucose
     * @throws UnsupportedEncodingException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LastBloodRecordController() throws UnsupportedEncodingException {
        RetrieveLastBloodRecord getDB = new RetrieveLastBloodRecord();
        lastBloodPressureList = getDB.getLastBloodPressureList();
        lastBloodGlucoseList = getDB.getLastBloodGlucoseList();
    }

    /***
     * Get last blood pressure list
     * @return last blood pressure list
     */
    public ArrayList<LastBloodPressure> getLastBloodPressureList() {
        return lastBloodPressureList;
    }

    /***
     * Get last blood glucose list
     * @return last blood glucose list
     */
    public ArrayList<LastBloodGlucose> getLastBloodGlucoseList() {
        return lastBloodGlucoseList;
    }
}
