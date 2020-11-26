package medPal.App;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import static android.view.View.GONE;

public class PostNewPillReminder {

    String medicineName;
    String manufacturer;
    String dosage;
    // Image
    String prType;
    String daysInterval;
    String week_bit;
    String time;
    String quantity;
    String startDate;
    String endDate;
    String purpose;
    String remark;

    public String encodedData = "";
    public int exitStatus = 0;

    PostNewPillReminder(String medicineName, String manufacturer, int dosage, int prType, int daysInterval, String week_bit, String time, int quantity, String startDate, String endDate, String purpose, String remark) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        this.medicineName = medicineName;
        this.manufacturer = manufacturer;
        this.dosage = String.valueOf(dosage);
        this.prType = String.valueOf(prType);
        this.daysInterval = String.valueOf(daysInterval);
        this.week_bit = week_bit;
        this.time = time;
        this.quantity = String.valueOf(quantity);
        this.startDate = startDate;
        this.endDate = endDate;
        this.purpose = purpose;
        this.remark = remark;

        encodeData();
        String result = new PostNewPillReminder.ConnectDB().execute(encodedData).get();
        exitStatus = Integer.parseInt(result);
    }

    public int getStatus() {
        return exitStatus;
    }

    public void encodeData() throws UnsupportedEncodingException {
        encodedData += URLEncoder.encode("medicine", "UTF-8")+ "=" + URLEncoder.encode(medicineName, "UTF-8");
        encodedData += "&" + URLEncoder.encode("manufacturer", "UTF-8") + "=" + URLEncoder.encode(manufacturer, "UTF-8");
        encodedData += "&" + URLEncoder.encode("dosage", "UTF-8") + "=" + URLEncoder.encode(dosage, "UTF-8");
        encodedData += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(prType, "UTF-8");
        encodedData += "&" + URLEncoder.encode("frequency", "UTF-8")+ "=" + URLEncoder.encode(daysInterval, "UTF-8");
        encodedData += "&" + URLEncoder.encode("week_bit", "UTF-8") + "=" + URLEncoder.encode(week_bit, "UTF-8");
        encodedData += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
        encodedData += "&" + URLEncoder.encode("quantity", "UTF-8") + "=" + URLEncoder.encode(quantity, "UTF-8");
        encodedData += "&" + URLEncoder.encode("start_date", "UTF-8")+ "=" + URLEncoder.encode(startDate, "UTF-8");
        encodedData += "&" + URLEncoder.encode("end_date", "UTF-8") + "=" + URLEncoder.encode(endDate, "UTF-8");
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
                URL url = new URL("https://bulacke.xyz/medpal-db/insertPillReminder.php");

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