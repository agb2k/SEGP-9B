package medPal.App.Appointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;

public class RetrieveAppointments {
    private ArrayList<Appointment> AppointmentList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrieveAppointments() throws UnsupportedEncodingException {

        String jsonStr;
        JSONArray jsonArr;
        JSONObject jsonObj;

        Appointment aObj;

        DatabaseHelper appointmentDbHelper = new DatabaseHelper(DatabaseHelper.GET,DatabaseHelper.APPOINTMENT);
        appointmentDbHelper.setUserInfo();

        try{
            jsonStr = appointmentDbHelper.send();
            jsonArr = new JSONArray(jsonStr);

            for(int i=0; i<jsonArr.length(); i++){
                jsonObj = (JSONObject) jsonArr.get(i);
                aObj = makeAppointmentObject(jsonObj);
                AppointmentList.add(aObj);
            }
//            System.out.println(AppointmentList);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Appointment makeAppointmentObject(JSONObject jsonObj) throws JSONException {
        int appointmentID = jsonObj.getInt("appointment_id");
        String date = jsonObj.getString("date");
        String time = jsonObj.getString("time");
        String doctor = jsonObj.getString("doctor");
        String venue = jsonObj.getString("venue");
        int contact = jsonObj.getInt("contact");
        String email = jsonObj.getString("email");
        String purpose = jsonObj.getString("purpose");
        String remark = jsonObj.getString("remark");

        return new Appointment(appointmentID, date, time, doctor, venue, contact, email, purpose, remark);
    }

    public ArrayList<Appointment> getAllAppointment() {
        return AppointmentList;
    }

}
