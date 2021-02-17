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

/**
 * Upload image to server and update database.
 */
public class UploadImage {

    private static Bitmap bitmap = null;
    private static String medicineId;
    private static final String source = "https://bulacke.xyz/medpal-db/uploadMedicineImage.php";
    private int exitStatus = 0;

    UploadImage(Bitmap bitmap, String medicineId) throws ExecutionException, InterruptedException {
        UploadImage.bitmap = bitmap;
        UploadImage.medicineId = medicineId;
        if (bitmap != null) {
            String result = new UploadImage.ConnectDB().execute(source).get();
            exitStatus = Integer.parseInt(result);
        }else{
            exitStatus = 1;
        }
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
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
                byte[] byteArray = output.toByteArray();
                String convertedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String encodedData = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(convertedImage, "UTF-8");
                encodedData += "&" + URLEncoder.encode("medId", "UTF-8") + "=" + URLEncoder.encode(medicineId, "UTF-8");

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
