package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewSugarReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sugar_reminder);
        getSupportActionBar().setTitle("Add Blood Sugar Reminder");
    }
}