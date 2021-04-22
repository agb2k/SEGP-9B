package medPal.App.Appointment;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDelete extends AppCompatActivity {

    public AppointmentDelete() {

    }

    public void refresh() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
