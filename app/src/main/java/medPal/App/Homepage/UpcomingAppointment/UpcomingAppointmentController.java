package medPal.App.Homepage.UpcomingAppointment;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import medPal.App.Appointment.Appointment;
import medPal.App.Appointment.RetrieveAppointments;

public class UpcomingAppointmentController implements Serializable {
    private Context context;
    private ArrayList<Appointment> upcomingAppointmentsList = new ArrayList<Appointment>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UpcomingAppointmentController() throws UnsupportedEncodingException {
        //RetrieveUpcomingAppointment getDB = new RetrieveUpcomingAppointment();
        //upcomingAppointmentsList = getDB.getUpcomingAppointmentsList();
        RetrieveAppointments getDB = new RetrieveAppointments();
        upcomingAppointmentsList = getDB.getAllAppointment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData() throws UnsupportedEncodingException {
        //RetrieveUpcomingAppointment getDB = new RetrieveUpcomingAppointment();
        //upcomingAppointmentsList = getDB.getUpcomingAppointmentsList();
        RetrieveAppointments getDB = new RetrieveAppointments();
        upcomingAppointmentsList = getDB.getAllAppointment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Appointment> getUpcomingAppointmentsList() {
        ArrayList<Appointment> resultList = new ArrayList<>();
        for(int i=0; i<upcomingAppointmentsList.size(); i++ ){
            LocalDate tempDate = upcomingAppointmentsList.get(i).getDate();
            LocalTime tempTime = stringToLocalTime(upcomingAppointmentsList.get(i).getTime());
            String status = upcomingAppointmentsList.get(i).getRemark();

            //Add appointment to list if the date is larger than today and status is not confirmed
            if (tempDate.compareTo(LocalDate.now())>0 && !checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }
            //If the date is today then check if the time larger than now
            else if (tempDate.compareTo(LocalDate.now())==0 && tempTime.compareTo(LocalTime.now())>=0 && !checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }

        }
        return resultList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Appointment> getConfirmedAppointment() {
        ArrayList<Appointment> resultList = new ArrayList<>();
        for(int i=0; i<upcomingAppointmentsList.size(); i++ ){
            LocalDate tempDate = upcomingAppointmentsList.get(i).getDate();
            LocalTime tempTime = stringToLocalTime(upcomingAppointmentsList.get(i).getTime());
            String status = upcomingAppointmentsList.get(i).getRemark();

            //Add appointment to list if the date is larger than today and status is not confirmed
            if (tempDate.compareTo(LocalDate.now())>0 && checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }
            //If the date is today then check if the time larger than now
            else if (tempDate.compareTo(LocalDate.now())==0 && tempTime.compareTo(LocalTime.now())>=0 && checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }

        }
        return resultList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalTime stringToLocalTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3));
        //String result =
        LocalTime localTime = LocalTime.of(hour, min);
        return localTime;
    }

    private Boolean checkStatus(String status) {
        boolean check=false;
        for(int i=0; i<(status.length()-8); i++) {

            if(status.substring(i,i+9).compareTo("Confirmed")==0) {
                check = true;
                break;
            }
            else{
                check=false;
            }
        }
        return check;
    }


}
