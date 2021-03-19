package medPal.App.PillReminder;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.time.LocalDateTime;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.TimeAndIntervalHelper;

public class PillReminderAlarmHelper {

    private Context context;
    private int type;
    private int pill_reminder_id;
    private String week_bit;
    private long repeat_interval;
    private long time;
    private LocalDateTime ldt;

    @RequiresApi(api = Build.VERSION_CODES.O)
    PillReminderAlarmHelper(Context context, int type, int freq, String week_bit, String time, String startDate, int pill_reminder_id) {
        this.context = context;
        this.type = type;
        this.pill_reminder_id = pill_reminder_id;

        int year = Integer.parseInt(startDate.substring(0,4));
        int month = Integer.parseInt(startDate.substring(4,6));
        int day = Integer.parseInt(startDate.substring(6,8));
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(2,4));
        this.ldt = LocalDateTime.of(year,month,day,hour,min);
        this.time = TimeAndIntervalHelper.getMilli(ldt);

        if(type == 1) {
            repeat_interval = TimeAndIntervalHelper.getDayInterval();
        }else if(type == 2) {
            repeat_interval = TimeAndIntervalHelper.getDayInterval(freq);
        }else {
            this.week_bit = week_bit;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlarm() {
        AlarmHelper alarmHelper = new AlarmHelper(context, AlarmHelper.PILL_REMINDER);
        if(type != 3)
            alarmHelper.setAlarm(time, repeat_interval, pill_reminder_id);
        else
            alarmHelper.setAlarm(ldt, week_bit, pill_reminder_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteAlarm() {
        AlarmHelper alarmHelper = new AlarmHelper(context, AlarmHelper.PILL_REMINDER);
        if(type != 3)
            alarmHelper.cancelAlarm(pill_reminder_id, time, repeat_interval);
        else
            alarmHelper.cancelAlarm(ldt, week_bit, pill_reminder_id);
    }

}
