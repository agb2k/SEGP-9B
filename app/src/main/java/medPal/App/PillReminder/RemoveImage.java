package medPal.App.PillReminder;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class RemoveImage {

    private static String medicineId;
    private static final String source = "https://bulacke.xyz/medpal-db/removeMedicineImage.php";
    private int exitStatus = 0;

    RemoveImage(String medicineId) throws ExecutionException, InterruptedException {
        RemoveImage.medicineId = medicineId;

        String result = new RemoveImage.ConnectDB().execute(source).get();
        Log.v("Bulacke",result);
        exitStatus = Integer.parseInt(result);
    }

    public int getStatus() {
        return exitStatus;
    }

    static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... source) {
            BufferedReader reader = null;
            String text = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL(source[0]);

                // Send POST data request
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);

                // Prepare the bitmap parameter to be parsed
                String encodedData = URLEncoder.encode("medId", "UTF-8") + "=" + URLEncoder.encode(medicineId, "UTF-8");

                // Parse data
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(encodedData);
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null){
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
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

            return text;
        }
    }
}
