package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SugarReminderList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button b6;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_reminder_list);
        getSupportActionBar().setTitle("Blood Sugar Reminder List");

        b6 = (Button) findViewById(R.id.AddNewSugarReminder);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSugarReminder();
            }
        });
    }

    public void createNewSugarReminder() {
        Intent intent2 = new Intent(this, NewSugarReminder.class);
        startActivity(intent2);
    }

}