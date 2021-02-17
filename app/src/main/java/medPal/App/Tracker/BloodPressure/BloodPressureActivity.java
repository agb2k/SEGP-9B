package medPal.App.Tracker.BloodPressure;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import medPal.App.R;

public class BloodPressureActivity extends AppCompatActivity {
    private Button b3;
    private Button b4;

    private static final String apiurl ="https://bulacke.xyz/medpal-db/getPressureRecord.php";
    ListView lv2;

    private static String date[];
    private static String time[];
    private static String sys[];
    private static String dia[];

    public static ArrayList<String> x_axis=new ArrayList<String>();
    public static ArrayList<String> y_axis=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        getSupportActionBar().setTitle("Blood Pressure");

        x_axis.clear();
        y_axis.clear();
        lv2 = (ListView) findViewById(R.id.lv2);
        try {
            fetch_data_into_array(lv2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lv2.setOnItemClickListener((parent, view, position, id) -> {
            String s = lv2.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        });



        

        b3 = (Button) findViewById(R.id.NewPressureRecordButton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPressureRecord();
            }
        });

        b4 = (Button) findViewById(R.id.NewPressureReminderButton);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPressureReminderList();
            }
        });


        GraphView graph = (GraphView) findViewById(R.id.pressureGraph);
        //fetch_data_into_array(graph);
        LineGraphSeries<DataPoint> series;
        series= new LineGraphSeries<>(data());
        series.setThickness(8);
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface DataPoint) {
                Toast.makeText(BloodPressureActivity.this,"On Data Point clicked:"+DataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        //series.setShape(PointsGraphSeries.Shape.POINT);
        //series.setSize(5);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(BloodPressureActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);



    }

    public void fetch_data_into_array(View view) throws ExecutionException, InterruptedException {

        class dbManager extends AsyncTask<String, Void, String>
        {
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
        String data = obj.execute(apiurl).get();

        try {
            JSONArray ja = new JSONArray(data);
            JSONObject jo = null;

            date = new String[ja.length()];
            time = new String[ja.length()];
            sys = new String[ja.length()];
            dia = new String[ja.length()];

            for (int i = 0; i<ja.length(); i++){
                jo = ja.getJSONObject(i);
                date[i] = jo.getString("Date");
                time[i] = jo.getString("Time");
                sys[i] = jo.getString("SYS");
                dia[i] = jo.getString("DIA");
                x_axis.add(date[i]);
                y_axis.add(sys[i]);
            }
            BloodPressureActivity.myadapter adptr = new BloodPressureActivity.myadapter(getApplicationContext(), date, time, sys, dia);
            lv2.setAdapter(adptr);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    class myadapter extends ArrayAdapter<String>
    {
        Context context;
        String tt1[];
        String dsc[];
        String rimg[];
        String abc[];

        myadapter(Context c , String tt1[], String dsc[], String rimg[], String abc[]){
            super(c, R.layout.row2, R.id.display_Date_pressure, tt1);
            context=c;
            this.tt1=tt1;
            this.dsc=dsc;
            this.rimg=rimg;
            this.abc=abc;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row2 = inflater.inflate(R.layout.row2, parent, false);
            TextView date = row2.findViewById(R.id.display_Date_pressure);
            TextView time = row2.findViewById(R.id.display_Time_pressure);
            TextView sys = row2.findViewById(R.id.display_SYS);
            TextView dia = row2.findViewById(R.id.display_DIA);
            date.setText(tt1[position]);
            time.setText(dsc[position]);
            sys.setText(rimg[position]);
            dia.setText(abc[position]);
            return row2;
        }

    }

    public DataPoint[] data(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int n=x_axis.size();     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i=0;i<n;i++){

            DataPoint v = null;
            try {
                v = new DataPoint(sdf.parse(x_axis.get(i)),Integer.parseInt(y_axis.get(i)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            values[i] = v;
        }
        return values;
    }


    public void openNewPressureRecord() {
        Intent intent1 = new Intent(this, NewPressureRecord.class);
        startActivity(intent1);
    }

    public void openPressureReminderList() {
        Intent intent = new Intent(this, PressureReminderList.class);
        startActivity(intent);
    }
}