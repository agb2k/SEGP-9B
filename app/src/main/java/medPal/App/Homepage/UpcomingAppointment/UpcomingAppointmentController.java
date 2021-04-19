package medPal.App.Homepage.UpcomingAppointment;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class UpcomingAppointmentController implements Serializable {
    private Context context;
    private ArrayList<UpcomingAppointment> upcomingAppointmentsList = new ArrayList<UpcomingAppointment>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UpcomingAppointmentController() {
        RetrieveUpcomingAppointment getDB = new RetrieveUpcomingAppointment();
        upcomingAppointmentsList = getDB.getUpcomingAppointmentsList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData() {
        RetrieveUpcomingAppointment getDB = new RetrieveUpcomingAppointment();
        upcomingAppointmentsList = getDB.getUpcomingAppointmentsList();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<UpcomingAppointment> getUpcomingAppointmentsList() {
        ArrayList<UpcomingAppointment> resultList = new ArrayList<>();
        for(int i=0; i<upcomingAppointmentsList.size(); i++ ){
            LocalDate tempDate = upcomingAppointmentsList.get(i).getDate();
            LocalTime tempTime = stringToLocalTime(upcomingAppointmentsList.get(i).getTime());

            if (tempDate.compareTo(LocalDate.now())>=0 && tempTime.compareTo(LocalTime.now())>=0) {
                resultList.add(upcomingAppointmentsList.get(i));
            }
        }
        return resultList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalTime stringToLocalTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(2));
        //String result =
        LocalTime localTime = LocalTime.of(hour, min);
        return localTime;
    }
}
