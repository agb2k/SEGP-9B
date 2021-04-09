package medPal.App.Appointment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import medPal.App.R;

public class NewAppointment extends AppCompatActivity {
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    EditText e5;
    EditText e6;
    EditText e7;
    EditText e8;
    Button b1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        getSupportActionBar().setTitle("New Appointment");

        e1 = (EditText) findViewById(R.id.NewAppointmentDate);
        e2 = (EditText) findViewById(R.id.NewAppointmentTime);
        e3 = (EditText) findViewById(R.id.NewAppointmentDoctor);
        e4 = (EditText) findViewById(R.id.NewAppointmentVenue);
        e5 = (EditText) findViewById(R.id.NewAppointmentContact);
        e6 = (EditText) findViewById(R.id.NewAppointmentEmail);
        e7 = (EditText) findViewById(R.id.NewAppointmentPurpose);
        e8 = (EditText) findViewById(R.id.NewAppointmentRemark);

        b1 = (Button) findViewById(R.id.ConfirmPressureRecordButton);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_confirm:
                if(checked)
                    //confirm
                    System.out.println("Confirmed");
                break;
            case R.id.radio_follow_up:
                if (checked)
                    //follow up
                    System.out.println("Follow up");
                break;


        }
    }
}
