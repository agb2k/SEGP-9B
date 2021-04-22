package medPal.App;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import medPal.App.UserIdentification.UserIdentity;

/**
 * DatabaseHelper Class
 * Handle database connections and operations, including establishing HttpsUrlConnection, encoding data, encoding image, send request and get return message.
 *
 * How to use this class:
 *
 * 1. Initialize DatabaseHelper object
 * ---> DatabaseHelper dbHelper = new DatabaseHelper(operation,type);
 * ---> or DatabaseHelper dbHelper = new DatabaseHelper(url);
 * ---> where 'operation' is defined below, i.e DatabaseHelper.INSERT, DatabaseHelper.DELETE, etc.
 * ---> type is also defined below, i.e DatabaseHelper.PILL_REMINDER, DatabaseHelper.MEDICINE, etc.
 * ---> (You can directly put strings(eg. 'insert','PressureRecord.php') as parameters if you are sure that it can generate the correct URL.)
 * ---> Same as above, if you are sure about the correct url, your can directly put url as parameter.
 *
 * 2. Encode data or image (if any)
 * ---> dbHelper.encodeData(key,value);
 * ---> dbHelper.encodeImage(bitmapImage);
 * ---> for encoding data, both key and value are String type. (you can use String.valueOf(..) to convert to String type)
 * ---> if you have multiple data, use encodeData() for each key and value. eg. encodeData("key1","val1"); encodeData("key2","val2");
 * ---> for encoding image, parameter is Bitmap type (key of the image is 'image' by default, because currently we only accept one image at a time, so we only allow one fixed key for image)
 * ---> (If you're not sure what is 'key', google POST or GET method, or you can ask me)
 *
 * 3. Send and Get Return Message/Result
 * ---> dbHelper.send();
 * ---> This method will return a string.
 * ---> Used for getting result from SQL queries or error/success message(depends on how you implement the PHP file).
 * ---> For those who are getting result from SQL queries, the returned string is JSON, you can use org.json library to extract data from JSON.
 */
public class DatabaseHelper {
    // URL
    private static final String url = "http://hpyzl1.jupiter.nottingham.edu.my/";
    private static final String DIR_PHP = "medpal-db/";
    private static final String DIR_IMG = "medpal-img/";

    // User ID
    public static final String USER_ACCOUNT = UserIdentity.getInstance().getEmail();

    // Operations
    public static String INSERT = "insert";
    public static String DELETE = "delete";
    public static String UPDATE = "update";
    public static String GET = "get";

    // Type
    public static String PILL_REMINDER = "PillReminder.php";
    public static String MEDICINE = "Medicine.php";
    public static String APPOINTMENT = "Appointment.php";
    public static String BLOOD_PRESSURE = "PressureRecord.php";
    public static String SUGAR_LEVEL = "SugarRecord.php";
    public static String MEDICINE_IMAGE = "MedicineImage.php";

    private String completeUrl = "";
    private String encodedData = "";

    /**
     * Constructor
     * @param operation Operation to be performed (INSERT / DELETE / UPDATE / GET).
     * @param type Type (PILL_REMINDER / MEDICINE / APPOINTMENT / PRESSURE_RECORD / SUGAR_LEVEL / MEDICINE_IMAGE)
     */
    public DatabaseHelper(String operation, String type) throws UnsupportedEncodingException {
        completeUrl = url + DIR_PHP + operation + type;
    }

    /**
     * Constructor
     * @param url Complete URL of the target PHP file.
     */
    public DatabaseHelper(String url) throws UnsupportedEncodingException {
        completeUrl = url;
    }

    /**
     * Put user id
     */
    public void setUserInfo() throws UnsupportedEncodingException {
        encodeData("user", USER_ACCOUNT);
    }

    /**
     * Encode data.
     * @param key Key of the data.
     * @param attribute Data.
     * @throws UnsupportedEncodingException
     */
    public void encodeData(String key, String attribute) throws UnsupportedEncodingException {
        if(encodedData.length() > 0) {
            encodedData += "&";
        }
        encodedData += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(attribute, "UTF-8");
    }

    /**
     * Encode image.
     * @param image Bitmap image.
     * @throws UnsupportedEncodingException
     */
    public void encodeImage(Bitmap image) throws UnsupportedEncodingException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, output);
        byte[] byteArray = output.toByteArray();
        String convertedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        encodeData("image",convertedImage);
    }

    /**
     * Send request and get return message.
     * @return Return message.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String send() throws ExecutionException, InterruptedException {
        String result = "";

        if(encodedData.length() > 0) {
            result = new DatabaseHelper.ConnectDB().execute(completeUrl,encodedData).get();
            result = result.replace("/home/hpyzl1jupiter/public_html/medpal-db","");
        }else{
            result = new DatabaseHelper.ConnectDB().execute(completeUrl).get();
            result = result.replace("/home/hpyzl1jupiter/public_html/medpal-db","");
        }

        return result;
    }

    /**
     * HttpsUrlConnection and Data Parsing.
     */
    static class ConnectDB extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... input) {
            BufferedReader reader = null;
            String text = null;
            String dest = input[0];
            String data = null;
            if(input.length > 1){
                data = input[1];
            }

            try {
                URL url = new URL(dest);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                if(input.length > 1) {
                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(data);
                    wr.flush();
                }
                // Get the server response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                text = sb.toString();
            }catch(Exception ex) {
                ex.printStackTrace();
            }finally {
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
