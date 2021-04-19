package medPal.App.Homepage.UpcomingAppointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;

public class RetrieveUpcomingAppointment {
    private ArrayList<UpcomingAppointment> upcomingAppointmentsList = new ArrayList<UpcomingAppointment>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrieveUpcomingAppointment() {
        String jsonStr;
        JSONArray jsonArr;
        JSONObject jsonObj;
        UpcomingAppointment aptObj;

        DatabaseHelper UpAptDbHelper = new DatabaseHelper("https://bulacke.xyz/medpal-db/getAppointment.php");

        try {

            jsonStr = UpAptDbHelper.send();
            jsonArr = new JSONArray(jsonStr);
            for(int i=0; i<jsonArr.length(); i++) {
                jsonObj = (JSONObject) jsonArr.get(i);
                aptObj = makeUpAptObj(jsonObj);
                upcomingAppointmentsList.add(aptObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private UpcomingAppointment makeUpAptObj(JSONObject jsonObject) throws JSONException {
        int aptId = jsonObject.getInt("appointment_id");
        String date = jsonObject.getString("date");
        String time = jsonObject.getString("time");
        String doctor = jsonObject.getString("doctor");
        String venue = jsonObject.getString("venue");
        int contact = jsonObject.getInt("contact");
        String email = jsonObject.getString("email");
        String purpose = jsonObject.getString("purpose");
        String remark = jsonObject.getString("remark");

        return new UpcomingAppointment(aptId, date, time, doctor, venue, contact, email, purpose, remark);
    }

    public ArrayList<UpcomingAppointment> getUpcomingAppointmentsList() {
        return upcomingAppointmentsList;
    }
}
