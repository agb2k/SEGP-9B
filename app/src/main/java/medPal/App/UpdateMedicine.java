package medPal.App;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class UpdateMedicine {

    private String id;
    private String manufacturer;
    private String dosage;
    private String purpose;
    private String remark;
    private String encodedData = "";
    private int exitStatus = 0;

    UpdateMedicine(int id, String medicineManufacturer, int dosage, String purpose, String remark) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        this.id = String.valueOf(id);
        this.manufacturer = medicineManufacturer;
        this.dosage = String.valueOf(dosage);
        this.purpose = purpose;
        this.remark = remark;

        encodeData();
        String result = new UpdateMedicine.ConnectDB().execute(encodedData).get();
        exitStatus = Integer.parseInt(result);
    }

    public int getStatus() {
        return exitStatus;
    }

    public void encodeData() throws UnsupportedEncodingException {
        encodedData += URLEncoder.encode("id", "UTF-8")+ "=" + URLEncoder.encode(id, "UTF-8");
        encodedData += "&" + URLEncoder.encode("manufacturer", "UTF-8") + "=" + URLEncoder.encode(manufacturer, "UTF-8");
        encodedData += "&" + URLEncoder.encode("dosage", "UTF-8") + "=" + URLEncoder.encode(dosage, "UTF-8");
        encodedData += "&" + URLEncoder.encode("purpose", "UTF-8") + "=" + URLEncoder.encode(purpose, "UTF-8");
        encodedData += "&" + URLEncoder.encode("remark", "UTF-8") + "=" + URLEncoder.encode(remark, "UTF-8");
    }

    static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... encodedData) {
            BufferedReader reader = null;
            String text = null;

            // Send data
            try {
                // Defined URL  where to send data
                URL url = new URL("https://bulacke.xyz/medpal-db/updateMedicine.php");

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
