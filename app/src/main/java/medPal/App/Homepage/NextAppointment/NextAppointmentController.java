package medPal.App.Homepage.NextAppointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

public class NextAppointmentController implements Serializable{
    private ArrayList<NextAppointment> NextApptList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NextAppointmentController() {
        RetrieveNextAppoinment getDB = new RetrieveNextAppoinment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData(){
        RetrieveNextAppoinment getDB = new RetrieveNextAppoinment();
    }

    public ArrayList<NextAppointment> getNextApptList() {
        return NextApptList;
    }
}
