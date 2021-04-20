package medPal.App.Homepage.NextAppointment;

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

import medPal.App.DatabaseHelper;


public class RetrieveNextAppoinment {

    private ArrayList<NextAppointment> NextApptList = new ArrayList<NextAppointment>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    RetrieveNextAppoinment() {
        String jsonStr;
        JSONArray jsonArray;
        JSONObject jsonObject;
        NextAppointment nextAppointment;
        DatabaseHelper nextAptDbhelper = new DatabaseHelper("https://sayft1nottingham.000webhostapp.com/getNextAppointment.php");

        try {
            //Retrieve Next Appointment
            jsonStr = nextAptDbhelper.send();
            jsonArray = new JSONArray(jsonStr);
            for(int i=0; i<jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                nextAppointment =  makeNextAppointmentObject(jsonObject);
                NextApptList.add(nextAppointment);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NextAppointment makeNextAppointmentObject(JSONObject jsonObject) throws JSONException {
        int apptId = jsonObject.getInt("apptID");
        String date = jsonObject.getString("date");
        String time = jsonObject.getString("time");
        String hospital = jsonObject.getString("hospital");
        String docName = jsonObject.getString("docName");

        return new NextAppointment(apptId, date, time, hospital, docName);
    }

    public ArrayList<NextAppointment> getNextApptList() {
        return NextApptList;
    }


}
