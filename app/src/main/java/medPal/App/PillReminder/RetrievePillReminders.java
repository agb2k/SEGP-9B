package medPal.App.PillReminder;

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
import medPal.App.PillReminder.PillReminder;

/**
 * Retrieve pill reminder and medicine data from database.
 */
public class RetrievePillReminders {
    private ArrayList<PillReminder> pillReminderList = new ArrayList<PillReminder>();
    private HashMap<Integer, Medicine> medicineById = new HashMap<>();
    private ArrayList<Medicine> medicineList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrievePillReminders() throws UnsupportedEncodingException {

        String jsonStr;
        JSONArray jsonArr;
        JSONObject jsonObj;
        Medicine mObj;
        PillReminder prObj;

        DatabaseHelper medDbHelper = new DatabaseHelper(DatabaseHelper.GET,DatabaseHelper.MEDICINE);
        DatabaseHelper pillDbHelper = new DatabaseHelper(DatabaseHelper.GET,DatabaseHelper.PILL_REMINDER);

        // Extract data from JSON
        try {
            // Get medicine
            jsonStr = medDbHelper.send();
            jsonArr = new JSONArray(jsonStr);
            for(int i=0; i<jsonArr.length(); i++) {
                jsonObj = (JSONObject) jsonArr.get(i);
                mObj = makeMedicineObject(jsonObj);
                medicineById.put(mObj.getMedicineId(), mObj);
                medicineList.add(mObj);
            }
            // Get pill reminders
            jsonStr = pillDbHelper.send();
            jsonArr = new JSONArray(jsonStr);
            for(int i=0; i<jsonArr.length(); i++){
                jsonObj = (JSONObject) jsonArr.get(i);
                prObj = makePillReminderObject(jsonObj);
                pillReminderList.add(prObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create medicine instance from JSON object.
     * @param jsonObj JSON object.
     * @return Medicine instance.
     * @throws JSONException
     */
    private Medicine makeMedicineObject(JSONObject jsonObj) throws JSONException {
        int medicineId = jsonObj.getInt("medicineId");
        String medicineName = jsonObj.getString("medicineName");
        String manufacturer = (jsonObj.isNull("manufacturer")) ? "" : jsonObj.getString("manufacturer");
        int dosage = jsonObj.getInt("dosage");
        String imagePath = (jsonObj.isNull("imagePath")) ? "" : jsonObj.getString("imagePath");
        String purpose = (jsonObj.isNull("purpose")) ? "" : jsonObj.getString("purpose");
        String medicineRemarks = (jsonObj.isNull("medicineRemarks")) ? "" : jsonObj.getString("medicineRemarks");
        return new Medicine(medicineId,medicineName,manufacturer,dosage,imagePath,purpose,medicineRemarks);
    }

    /**
     * Create pill reminder instance from JSON object.
     * @param jsonObj JSON object.
     * @return Pill reminder instance.
     * @throws JSONException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private PillReminder makePillReminderObject(JSONObject jsonObj) throws JSONException {
        int pillReminderId = jsonObj.getInt("pillreminder_id");
        String time = jsonObj.getString("time");
        int type = jsonObj.getInt("type");
        int frequency = (type==2)?(jsonObj.getInt("frequency")):0; //may be null
        String week_bit = (type==3)?(jsonObj.getString("week_bit")):""; //may be null
        int quantity = jsonObj.getInt("quantity");
        String start_date = jsonObj.getString("start_date");
        boolean noEndDate = (jsonObj.isNull("end_date"));
        String end_date = noEndDate?"":jsonObj.getString("end_date"); //may be null
        int medicine = jsonObj.getInt("medicineId");
        Medicine mObj = medicineById.get(medicine);

        return new PillReminder(pillReminderId,time,type,frequency,week_bit,quantity,start_date,noEndDate,end_date,mObj);
    }

    public ArrayList<PillReminder> getAllPillReminder() {
        return pillReminderList;
    }

    public ArrayList<Medicine> getAllMedicine() { return medicineList; }
}
