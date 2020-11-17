package medPal.App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SugarLevelActivity extends AppCompatActivity {
    private Button b4;
    private Button b5;
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listItem;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_level);
        getSupportActionBar().setTitle("Blood Sugar Level");

        expandableListView = findViewById(R.id.expandablelistview2);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        initListData();

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

    private void initListData(){
        listGroup.add(getString(R.string.novemberSugar));

        String[] array;

        List<String> list = new ArrayList<>();
        array = getResources().getStringArray(R.array.novemberSugar);
        for (String item : array){
            list.add(item);
        }

        listItem.put(listGroup.get(0),list);
        adapter.notifyDataSetChanged();

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