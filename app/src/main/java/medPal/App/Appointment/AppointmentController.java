package medPal.App.Appointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AppointmentController implements Serializable {
    private ArrayList<Appointment> AppointmentList = new ArrayList<>();

    /**
     * Initialize and get list of pill reminders from database.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public AppointmentController() throws UnsupportedEncodingException {
        RetrieveAppointments getDB = new RetrieveAppointments();
        AppointmentList = getDB.getAllAppointment();
    }

    protected ArrayList<Appointment> getAllAppointments() {
        return AppointmentList;
    }
}
