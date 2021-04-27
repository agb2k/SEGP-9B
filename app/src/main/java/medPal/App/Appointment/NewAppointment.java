package medPal.App.Appointment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.PillReminder.PillReminderAlarmHelper;
import medPal.App.R;

public class NewAppointment extends AppCompatActivity {
    EditText date;
    EditText time;
    EditText doctor;
    EditText venue;
    EditText contact;
    EditText email;
    EditText purpose;
    EditText remark;

    String dateStr;
    String timeStr;
    String doctorStr;
    String venueStr;
    String contactStr;
    String emailStr;
    String purposeStr;
    String remarkStr;
    String radioStr = "";

    Button confirmBtn;

    Boolean checked;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        getSupportActionBar().setTitle("New Appointment");

        date = (EditText) findViewById(R.id.NewAppointmentDate);
        time = (EditText) findViewById(R.id.NewAppointmentTime);
        doctor = (EditText) findViewById(R.id.NewAppointmentDoctor);
        venue = (EditText) findViewById(R.id.NewAppointmentVenue);
        contact = (EditText) findViewById(R.id.NewAppointmentContact);
        email = (EditText) findViewById(R.id.NewAppointmentEmail);
        purpose = (EditText) findViewById(R.id.NewAppointmentPurpose);
        remark = (EditText) findViewById(R.id.NewAppointmentRemark);

        confirmBtn = (Button) findViewById(R.id.newAppointmentConfirm);
        confirmBtn.setOnClickListener(v -> {
            try {
                getUserInput();
            } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_confirm:
                if(checked)
                    radioStr = "(Confirmed)";
                break;
            case R.id.radio_follow_up:
                if (checked)
                    radioStr = "(Planned)";
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getUserInput() throws InterruptedException, UnsupportedEncodingException, ExecutionException {
        dateStr = date.getText().toString();
        timeStr = time.getText().toString();
        doctorStr = doctor.getText().toString();
        venueStr = venue.getText().toString();
        contactStr = contact.getText().toString();
        emailStr = email.getText().toString();
        purposeStr = purpose.getText().toString();
        remarkStr = remark.getText().toString() + radioStr;

        boolean valid = true;
        if ( TextUtils.isEmpty(dateStr) ) {
            date.setError("Date cannot be empty. Format eg. 20120922");
            valid = false;
        }

        if ( dateStr.length() != 8 ) {
            date.setError("Date must consist of exactly 8 digits. Format eg. 20120922");
            valid = false;
        }

        if ( TextUtils.isEmpty(timeStr) ) {
            time.setError("Time cannot be empty. Format eg. 1400");
            valid = false;
        }

        if ( timeStr.length() != 4 ) {
            time.setError("Time must consist of exactly 4 digits. Format eg. 1400");
            valid = false;
        }

        if ( TextUtils.isEmpty(doctorStr) ) {
            doctor.setError("Doctor cannot be empty");
            valid = false;
        }

        if ( TextUtils.isEmpty(venueStr) ) {
            venue.setError("Venue cannot be empty");
            valid = false;
        }

        if ( TextUtils.isEmpty(contactStr) ) {
            contact.setError("Contact cannot be empty");
            valid = false;
        }

        if ( TextUtils.isEmpty(emailStr) ) {
            email.setError("Email cannot be empty");
            valid = false;
        }

        if ( TextUtils.isEmpty(purposeStr) ) {
            purposeStr = "N/A";
        }

        if ( TextUtils.isEmpty(remarkStr) ) {
            remarkStr = "N/A" + radioStr;
        }

        if (checked == null){
            valid=false;
        }

        if ( valid ) {
            sendData(dateStr, timeStr, doctorStr, venueStr, contactStr, emailStr, purposeStr, remarkStr);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendData(String dateStr, String timeStr, String doctorStr, String venueStr, String contactStr, String emailStr, String purposeStr, String remarkStr) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper insertDB = new DatabaseHelper(DatabaseHelper.INSERT, DatabaseHelper.APPOINTMENT);

        insertDB.setUserInfo();
        insertDB.encodeData("date",dateStr);
        insertDB.encodeData("time",timeStr);
        insertDB.encodeData("doctor", doctorStr);
        insertDB.encodeData("venue", venueStr);
        insertDB.encodeData("contact", contactStr);
        insertDB.encodeData("email",emailStr);
        insertDB.encodeData("purpose", purposeStr);
        insertDB.encodeData("remark", remarkStr);

        String message = insertDB.send();
        setAlarm(timeStr,dateStr);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarm(String time, String startDate) {
        AppointmentAlarmHelper alarmHelper = new AppointmentAlarmHelper(getApplicationContext(), time, startDate);
        alarmHelper.setAlarm();
    }
}
