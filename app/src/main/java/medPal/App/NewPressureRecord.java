package medPal.App;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewPressureRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pressure_record);

        getSupportActionBar().setTitle("New Blood Pressure Record");

    }



}