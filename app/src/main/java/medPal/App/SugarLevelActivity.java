package medPal.App;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SugarLevelActivity extends AppCompatActivity {
    private Button b4;
    private Button b5;
    private static final String apiurl = "https://bulacke.xyz/medpal-db/getSugarRecord.php";
    ListView lv;

    private static String date[];
    private static String time[];
    private static String level[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_level);
        getSupportActionBar().setTitle("Blood Sugar Level");

        lv = (ListView) findViewById(R.id.lv);
        fetch_data_into_array(lv);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            String s = lv.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        });

        b4 = (Button) findViewById(R.id.NewSugarLevelRecordButton);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewSugarLevelRecord();
            }
        });

        b5 = (Button) findViewById(R.id.NewSugarLevelReminderButton);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSugarReminderList();
            }
        });

    }

    public void fetch_data_into_array(View view){

        class dbManager extends AsyncTask<String, Void, String>
        {
            protected void onPostExecute (String data){
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    date = new String[ja.length()];
                    time = new String[ja.length()];
                    level = new String[ja.length()];

                    for (int i = 0; i<ja.length(); i++){
                        jo = ja.getJSONObject(i);
                        date[i] = jo.getString("Date");
                        time[i] = jo.getString("Time");
                        level[i] = jo.getString("Level");
                    }
                    myadapter adptr = new myadapter(getApplicationContext(), date, time, level);
                    lv.setAdapter(adptr);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            protected String doInBackground(String... strings){
                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while((line = br.readLine()) != null){
                        data.append(line + "\n");
                    }
                    br.close();
                    return data.toString();
                } catch (Exception ex) {
                    return ex.getMessage();
                }
            }
        }
        dbManager obj = new dbManager();
        obj.execute(apiurl);

    }

    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String tt1[];
        String dsc[];
        String rimg[];

        myadapter(Context c , String tt1[], String dsc[], String rimg[]){
            super(c, R.layout.row, R.id.display_Date, tt1);
                context=c;
                this.tt1=tt1;
                this.dsc=dsc;
                this.rimg=rimg;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row, parent, false);
            TextView date = row.findViewById(R.id.display_Date);
            TextView time = row.findViewById(R.id.display_Time);
            TextView level = row.findViewById(R.id.display_Level);
            date.setText(tt1[position]);
            time.setText(dsc[position]);
            level.setText(rimg[position]);
            return row;
        }

    }


    public void openNewSugarLevelRecord() {
        Intent intent2 = new Intent(this, NewSugarLevelRecord.class);
        startActivity(intent2);
    }

    public void openSugarReminderList() {
        Intent intent = new Intent(this, SugarReminderList.class);
        startActivity(intent);
    }

}