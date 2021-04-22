package medPal.App.Appointment;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.TimeAndIntervalHelper;

public class AppointmentAlarmHelper {
    private Context context;
    private int appointment_id;
    private long time;
    private LocalDateTime ldt;

    @RequiresApi(api = Build.VERSION_CODES.O)
    AppointmentAlarmHelper(Context context, String time, String startDate) {
        this.context = context;

        int year = Integer.parseInt(startDate.substring(0,4));
        int month = Integer.parseInt(startDate.substring(4,6));
        int day = Integer.parseInt(startDate.substring(6,8));
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(2,4));
        this.ldt = LocalDateTime.of(year,month,day,hour,min);
        this.time = TimeAndIntervalHelper.getMilli(ldt);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlarm() {
        AlarmHelper alarmHelper = new AlarmHelper(context, AlarmHelper.APPOINTMENT);
        alarmHelper.setAlarm(time, appointment_id);
    }
}
