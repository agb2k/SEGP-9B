package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class BloodPressureActivity extends AppCompatActivity {
    private Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        getSupportActionBar().setTitle("Blood Pressure");

        b3 = (Button) findViewById(R.id.NewPressureRecordButton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPressureRecord();
            }
        });




        GraphView graph = (GraphView) findViewById(R.id.pressuregraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


    }
    public void openNewPressureRecord() {
        Intent intent1 = new Intent(this, NewPressureRecord.class);
        startActivity(intent1);
    }

}