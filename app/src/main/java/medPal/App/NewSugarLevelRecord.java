package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewSugarLevelRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sugar_level_record);

        getSupportActionBar().setTitle("New Blood Sugar Level Record");
    }
}