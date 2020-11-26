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
import java.util.concurrent.ExecutionException;


public class RetrieveNextAppointment{

    private static final String URL = "http://192.168.0.105/SEGP/nextAppointment.php";
    private ArrayList<NextAppointment> nextAppointmentArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RetrieveNextAppointment() {

        String jsonStr;
        JSONArray jsonArr;
        JSONObject jsonObj;
        NextAppointment nObj;


        // Extract data from JSON
        try{
            jsonStr = new ConnectDB().execute(URL).get();
            jsonArr = new JSONArray(jsonStr);
            for(int i=0; i<jsonArr.length(); i++) {
                jsonObj = (JSONObject) jsonArr.get(i);
                nObj = makeNextAppointmentObject(jsonObj);
                nextAppointmentArrayList.add(nObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    private NextAppointment makeNextAppointmentObject(JSONObject jsonObj) throws JSONException {
        String email = jsonObj.getString("email");
        String apptID = jsonObj.getString("apptID");
        String date = jsonObj.getString("date");
        String time = jsonObj.getString("time");
        String hospital = jsonObj.getString("hospital");
        String docName = jsonObj.getString("docName");
        return new NextAppointment(email,apptID,date,time,hospital,docName);
    }

    public String getNextApptTime(JSONObject jsonObj)throws JSONException{
        String nextApptTime = jsonObj.getString("time");
        return nextApptTime;
    }

    public ArrayList<NextAppointment> getNextAppointmentArrayList(){
        return nextAppointmentArrayList;
    }

    private static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... source) {
            StringBuilder total = new StringBuilder();

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
            } catch (IOException ioE) {
                ioE.printStackTrace();
            }
            return total.toString();
        }
    }

    }




