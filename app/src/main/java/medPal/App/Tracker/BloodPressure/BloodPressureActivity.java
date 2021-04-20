package medPal.App.Tracker.BloodPressure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

public class BloodPressureActivity extends AppCompatActivity {
    private Button b3;
    private Button b4;

    public static final int UPDATE_BLODD_PRESSURE_REQUEST_CODE = 10006;
    private static final String apiurl ="https://bulacke.xyz/medpal-db/getPressureRecord.php";
    ListView lv2;

    private static String date[];
    private static String time[];
    private static String sys[];
    private static String dia[];

    public static ArrayList<String> x_axis=new ArrayList<String>();
    public static ArrayList<String> y_axis=new ArrayList<String>();
    public static ArrayList<String> y_axis2=new ArrayList<String>();



    private LineChart lineChart;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        getSupportActionBar().setTitle("Blood Pressure");

        x_axis.clear();
        y_axis.clear();
        y_axis2.clear();
        lv2 = (ListView) findViewById(R.id.lv2);
        try {
            fetch_data_into_array(lv2);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        lv2.setOnItemClickListener((parent, view, position, id) -> {
            String s = lv2.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        });


        // Click to edit record
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent(BloodPressureActivity.this, EditPressureActivity.class);
                intent3.putExtra("Date", date[position]);
                intent3.putExtra("Time", time[position]);
                intent3.putExtra("Sys", sys[position]);
                intent3.putExtra("Dia", dia[position]);
                startActivity(intent3);
            }
        });


        // Go to add new record
        b3 = (Button) findViewById(R.id.NewPressureRecordButton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPressureRecord();
            }
        });

        // Go to Reminder Page
        b4 = (Button) findViewById(R.id.NewPressureReminderButton);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPressureReminderList();
            }
        });

        lineChart = (LineChart) findViewById(R.id.pressureGraph);
        lineChart.setNoDataText("No data available");

        lineChart.setVisibleXRangeMaximum(3);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(5);

        //Get data from database for X-axis
        if (x_axis.size() == 0) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float index, AxisBase axis) {

                    return x_axis.get((int) index);

                }


                public int getDecimalDigits() {
                    return 0;
                }
            });
        } else if (x_axis.size() == 1) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float index, AxisBase axis) {
                        index = 0;

                        return x_axis.get((int) index);
                    }




                public int getDecimalDigits() {
                    return 0;
                }
            });
        }else {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float index, AxisBase axis) {

                    return x_axis.get((int) index);
                }


                public int getDecimalDigits() {
                    return 0;
                }
            });
        }


        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Entry> yValues2 = new ArrayList<>();

        int n = x_axis.size();

        // plotting data to the graph， limit to 5 maximum
        if (n >= 5) {
            for (int i = n - 5; i < n; i++) {
                yValues.add(new Entry(i, Float.parseFloat(y_axis.get(i))));
            }

            for (int i = n - 5; i < n; i++) {
                yValues2.add(new Entry(i, Float.parseFloat(y_axis2.get(i))));
            }
        } else if (n == 0) {

        } else {
            for (int i = 0; i < n; i++) {
                yValues.add(new Entry(i, Float.parseFloat(y_axis.get(i))));
            }

            for (int i = 0; i < n; i++) {
                yValues2.add(new Entry(i, Float.parseFloat(y_axis2.get(i))));
            }
        }


        // Design of plotting
        if (n != 0) {
            LineDataSet set1 = new LineDataSet(yValues, "SYS");
            set1.setFillAlpha(110);
            set1.setColor(Color.RED);
            set1.setLineWidth(1.75f);
            set1.setCircleRadius(5f);
            set1.setCircleHoleRadius(2.5f);
            set1.setValueTextSize(10);
            set1.setCircleColor(Color.BLACK);
            set1.setHighLightColor(Color.BLACK);
            set1.setDrawValues(true);

            LineDataSet set2 = new LineDataSet(yValues2, "DIA");
            set2.setFillAlpha(110);
            set2.setColor(Color.BLUE);
            set2.setLineWidth(1.75f);
            set2.setCircleRadius(5f);
            set2.setCircleHoleRadius(2.5f);
            set2.setValueTextSize(10);
            set2.setCircleColor(Color.BLACK);
            set2.setHighLightColor(Color.BLACK);
            set2.setDrawValues(true);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);


            if (dataSets.isEmpty() == false) {
                LineData data = new LineData(dataSets);

                lineChart.setData(data);
                lineChart.animateX(3000, Easing.EasingOption.EaseInCirc);
            }

        }
    }






    public void fetch_data_into_array(View view) throws ExecutionException, InterruptedException, UnsupportedEncodingException {

        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.GET, DatabaseHelper.BLOOD_PRESSURE);
        dbHelper.setUserInfo();
        String data = dbHelper.send();


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
                y_axis2.add(dia[i]);
            }
            BloodPressureActivity.myadapter adptr = new BloodPressureActivity.myadapter(getApplicationContext(), date, time, sys, dia);
            lv2.setAdapter(adptr);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //create listview of records with data from database
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



    public void openNewPressureRecord() {
        Intent intent1 = new Intent(this, NewPressureRecord.class);
        startActivityForResult(intent1, UPDATE_BLODD_PRESSURE_REQUEST_CODE);
    }

    public void openPressureReminderList() {
        Intent intent = new Intent(this, PressureReminderList.class);
        startActivity(intent);
    }

}