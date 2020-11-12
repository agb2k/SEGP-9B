package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BloodPressureActivity extends AppCompatActivity {
    private Button b3;
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listItem;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        getSupportActionBar().setTitle("Blood Pressure");

        expandableListView = findViewById(R.id.expandablelistview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        initListData();
        

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

    private void initListData() {
        listGroup.add(getString(R.string.november));

        String[] array;

        List<String> list = new ArrayList<>();
        array = getResources().getStringArray(R.array.november);
        for (String item : array){
            list.add(item);
        }

        listItem.put(listGroup.get(0),list);
        adapter.notifyDataSetChanged();

    }

    public void openNewPressureRecord() {
        Intent intent1 = new Intent(this, NewPressureRecord.class);
        startActivity(intent1);
    }

}