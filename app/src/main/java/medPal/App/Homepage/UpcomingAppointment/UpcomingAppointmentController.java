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

/***
 * Class to handle Upcoming Appointment list
 */
public class UpcomingAppointmentController implements Serializable {
    private Context context;
    private ArrayList<Appointment> upcomingAppointmentsList = new ArrayList<Appointment>();

    /***
     * Initialise and get list of upcoming appointment
     * @throws UnsupportedEncodingException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public UpcomingAppointmentController() throws UnsupportedEncodingException {
        RetrieveAppointments getDB = new RetrieveAppointments();
        upcomingAppointmentsList = getDB.getAllAppointment();
    }

    /***
     * Get upcoming 'planned' appointment list
     * @return Upcoming 'planned' appointment list
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Appointment> getUpcomingAppointmentsList() {
        ArrayList<Appointment> resultList = new ArrayList<>();
        for(int i=0; i<upcomingAppointmentsList.size(); i++ ){
            LocalDate tempDate = upcomingAppointmentsList.get(i).getDate();
            LocalTime tempTime = stringToLocalTime(upcomingAppointmentsList.get(i).getTime());
            String status = upcomingAppointmentsList.get(i).getRemark();

            //If the date is today and status is not confirmed then check if the time larger than now
            if (tempDate.compareTo(LocalDate.now())==0  && !checkStatus(status)) {
                //if time is larger than now then add to list
                if(tempTime.compareTo(LocalTime.now())>=0){
                    resultList.add(upcomingAppointmentsList.get(i));
                }
            }
            //Add appointment to list if the date is larger than today and status is not confirmed
            else if (tempDate.compareTo(LocalDate.now())>0 && !checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }

        }
        return resultList;
    }

    /***
     * Get upcoming 'confirmed' appointment list
     * @return Get upcoming confirmed' appointment list
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Appointment> getConfirmedAppointment() {
        ArrayList<Appointment> resultList = new ArrayList<>();
        for(int i=0; i<upcomingAppointmentsList.size(); i++ ){
            LocalDate tempDate = upcomingAppointmentsList.get(i).getDate();
            LocalTime tempTime = stringToLocalTime(upcomingAppointmentsList.get(i).getTime());
            String status = upcomingAppointmentsList.get(i).getRemark();

            //If the date is today and status is confirmed then check if the time larger than now
            if (tempDate.compareTo(LocalDate.now())==0 && tempTime.compareTo(LocalTime.now())>=0 && checkStatus(status)) {
                //if time is larger than now then add to list
                if(tempTime.compareTo(LocalTime.now())>=0){
                    resultList.add(upcomingAppointmentsList.get(i));
                }
            }
            //Add appointment to list if the date is larger than today and status is confirmed
            else if (tempDate.compareTo(LocalDate.now())>0 && checkStatus(status)) {
                resultList.add(upcomingAppointmentsList.get(i));
            }


        }
        return resultList;
    }

    /***
     * Convert time string to LocalTime format
     * @param time Time string
     * @return time
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalTime stringToLocalTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3));
        //String result =
        LocalTime localTime = LocalTime.of(hour, min);
        return localTime;
    }

    /***
     * Function to check status
     * @param status String of remark in appointment
     * @return True if 'confirmed', False if 'not confirmed'
     */
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
