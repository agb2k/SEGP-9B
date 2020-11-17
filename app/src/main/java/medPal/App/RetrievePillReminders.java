package medPal.App;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class RetrievePillReminders {
    private ArrayList<PillReminder> pillReminderList = new ArrayList<PillReminder>();
    private HashMap<Integer, Medicine> medicineById = new HashMap<>();
    private ArrayList<Medicine> medicineList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrievePillReminders() {

        String jsonStr;
        JSONArray jsonArr;
        JSONObject jsonObj;
        Medicine mObj;
        PillReminder prObj;

        // Extract data from JSON
        try {
            // Get medicine
            jsonStr = new ConnectDB().execute("https://bulacke.xyz/medpal-db/getMedicine.php").get();
            jsonArr = new JSONArray(jsonStr);
            for(int i=0; i<jsonArr.length(); i++) {
                jsonObj = (JSONObject) jsonArr.get(i);
                mObj = makeMedicineObject(jsonObj);
                medicineById.put(mObj.getMedicineId(), mObj);
                medicineList.add(mObj);
            }
            // Get pill reminders
            jsonStr = new ConnectDB().execute("https://bulacke.xyz/medpal-db/getPillReminder.php").get();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private PillReminder makePillReminderObject(JSONObject jsonObj) throws JSONException {
        int pillReminderId = jsonObj.getInt("pillreminder_id");
        String time = jsonObj.getString("time");
        int type = jsonObj.getInt("type");
        int frequency = (type==2)?(jsonObj.getInt("frequency")):0; //may be null
        String week_bit = (type==3)?(jsonObj.getString("week_bit")):""; //may be null
        String pillReminderRemarks = (jsonObj.isNull("pillreminder_remark"))?"":jsonObj.getString("pillreminder_remark"); //may be null
        String start_date = jsonObj.getString("start_date");
        String end_date = (jsonObj.isNull("end_date"))?"":jsonObj.getString("end_date"); //may be null
        int medicine = jsonObj.getInt("medicineId");
        Medicine mObj = medicineById.get(medicine);

        return new PillReminder(pillReminderId,time,type,frequency,week_bit,pillReminderRemarks,start_date,end_date,mObj);
    }

    public ArrayList<PillReminder> getAllPillReminder() {
        return pillReminderList;
    }

    public ArrayList<Medicine> getAllMedicine() { return medicineList; }

    private static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... source) {
            StringBuilder total = new StringBuilder();;
            try {
                // Create a neat value object to hold the URL
                URL url = new URL(source[0]);
                // Open a connection(?) on the URL(?) and cast the response(??)
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Now it's "open", we can set the request method, headers etc.
                connection.setRequestProperty("accept", "application/json");
                // This line makes the request
                InputStream responseStream = connection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(responseStream));
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
            }catch(IOException ioE){
                ioE.printStackTrace();
            }
            return total.toString();
        }
    }
}
