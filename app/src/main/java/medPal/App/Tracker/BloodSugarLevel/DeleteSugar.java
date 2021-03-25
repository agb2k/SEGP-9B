package medPal.App.Tracker.BloodSugarLevel;

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

public class DeleteSugar {

    private String encodedData = "";
    private String returnStatus;

    DeleteSugar(String date, String time) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        encodedData += URLEncoder.encode("Date","UTF-8")+ "=" + URLEncoder.encode(String.valueOf(date), "UTF-8");
        encodedData += "&" + URLEncoder.encode("Time", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(time), "UTF-8");
        String result = new medPal.App.Tracker.BloodSugarLevel.DeleteSugar.ConnectDB().execute(encodedData).get();

        returnStatus = result.substring(1, result.length()-1);
        Log.d("result", result);
        Log.d("returnStatus", returnStatus);
    }

    public boolean success() {
        return (returnStatus.equals("Successfully saved"));
    }

    private static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... encodedData) {
            BufferedReader reader = null;
            String text = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("https://bulacke.xyz/medpal-db/deleteSugarRecord.php");

                // Send POST data request
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(encodedData[0]);
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line);
                }

                text = sb.toString();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    reader.close();
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

            return text;
        }
    }

}
