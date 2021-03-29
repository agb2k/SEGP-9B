package medPal.App.Tracker.BloodSugarLevel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.series.DataPoint;

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

public class SugarLevelActivity extends AppCompatActivity {
    private Button b4;
    private Button b5;
    private static final String apiurl = "https://bulacke.xyz/medpal-db/getSugarRecord.php";
    //public static final int ACTIVITY_REQUEST_CODE = 10005;
    ListView lv;

    private static String date[];
    private static String time[];
    private static String level[];

    public static ArrayList<String> x_axis=new ArrayList<String>();
    public static ArrayList<String> y_axis=new ArrayList<String>();

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_level);
        getSupportActionBar().setTitle("Blood Sugar Level");

        //Intent intent = new Intent(SugarLevelActivity.this, NewSugarLevelRecord.class);
        //startActivityForResult(intent,ACTIVITY_REQUEST_CODE);

        x_axis.clear();
        y_axis.clear();

        lv = (ListView) findViewById(R.id.lv);
        try {
            fetch_data_into_array(lv);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String s = lv.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent(SugarLevelActivity.this, EditSugarLevelActivity.class);
                intent3.putExtra("Date", date[position]);
                intent3.putExtra("Time", time[position]);
                intent3.putExtra("Level", level[position]);
                startActivity(intent3);
            }
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

        lineChart = (LineChart) findViewById(R.id.sugarGraph);
        //lineChart.setVisibleXRangeMaximum(5);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float index, AxisBase axis) {
                //return xVal[(int) value]; // xVal is a string array
                return x_axis.get((int) index);
            }


            public int getDecimalDigits() {
                return 0;
            }
        });

        ArrayList<Entry> yValues = new ArrayList<>();

        int n = x_axis.size();

        if(n>=5) {
            for (int i = n - 5; i < n; i++) {
                //yValues.add(new Entry(Float.parseFloat(x_axis.get(i)), Float.parseFloat(y_axis.get(i))));
                yValues.add(new Entry(i, Float.parseFloat(y_axis.get(i))));
            }
        } else {
            for (int i = 0; i < n; i++) {
                //yValues.add(new Entry(Float.parseFloat(x_axis.get(i)), Float.parseFloat(y_axis.get(i))));
                yValues.add(new Entry(i, Float.parseFloat(y_axis.get(i))));
            }
        }

        LineDataSet set1 = new LineDataSet(yValues, "Sugar Level");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setValueTextSize(10);
        set1.setCircleColor(Color.BLACK);
        set1.setHighLightColor(Color.BLACK);
        set1.setDrawValues(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.animateX(3000, Easing.EasingOption.EaseInCirc);

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
            level = new String[ja.length()];

            for (int i = 0; i<ja.length(); i++){
                jo = ja.getJSONObject(i);
                date[i] = jo.getString("Date");
                time[i] = jo.getString("Time");
                level[i] = jo.getString("Level");
                x_axis.add(date[i]);
                y_axis.add(level[i]);
            }
            myadapter adptr = new myadapter(getApplicationContext(), date, time, level);
            lv.setAdapter(adptr);
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




    public void openNewSugarLevelRecord() {
        Intent intent2 = new Intent(this, NewSugarLevelRecord.class);
        startActivity(intent2);
    }

    public void openSugarReminderList() {
        Intent intent = new Intent(this, SugarReminderList.class);
        startActivity(intent);
    }

}