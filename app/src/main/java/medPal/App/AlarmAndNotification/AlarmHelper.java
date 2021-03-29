package medPal.App.AlarmAndNotification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;

import medPal.App.AlarmAndNotification.Receivers.AppointmentReceiver;
import medPal.App.AlarmAndNotification.Receivers.BloodPressureReceiver;
import medPal.App.AlarmAndNotification.Receivers.BloodSugarReceiver;
import medPal.App.AlarmAndNotification.Receivers.PillReminderReceiver;

/**
 * AlarmHelper class
 *
 * This is where you create alarm.
 * Remember to edit your Receiver to show correct notification content.
 * Read README.txt to learn how to use this class.
 */
public class AlarmHelper {

    public static int PILL_REMINDER = 1;
    public static int APPOINTMENT = 2;
    public static int BLOOD_PRESSURE = 3;
    public static int BLOOD_SUGAR = 4;
    public static int SNOOZE_ALARM_REFERENCE_CODE = 50001; // For creating new alarm when user snooze previous alarm

    private AlarmManager alarmManager;
    private Context context;
    private int type;

    public AlarmHelper(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    /**
     * Set one-time alarm.
     * @param alarmTime Time in milli
     * @param reference_id Reference ID
     */
    public void setAlarm(long alarmTime, int reference_id) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(alarmTime, reference_id);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    /**
     * Set repeating alarm
     * @param firstAlarmTime First alarm time in milli
     * @param repeatInterval Repeating interval in milli
     * @param reference_id Reference ID
     */
    public void setAlarm(long firstAlarmTime, long repeatInterval, int reference_id) {
        PendingIntent pendingIntent = getPendingIntent(firstAlarmTime, repeatInterval, reference_id);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstAlarmTime, repeatInterval, pendingIntent);
    }

    /**
     * Set repeating alarm for days of week.
     * @param startingDateTime Starting time in milli.
     * @param daysOfWeek String that represents days of week.
     * @param reference_id Reference ID.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlarm(LocalDateTime startingDateTime, String daysOfWeek, int reference_id) {
        long firstAlarmTime;
        for(int i=0; i<daysOfWeek.length(); i++) {
            if(daysOfWeek.indexOf(i) == '1') {
                LocalDateTime date = LocalDateTime.now();
                while(date.isBefore(startingDateTime)) {
                    date.plusWeeks(1);
                }
                firstAlarmTime = TimeAndIntervalHelper.getMilli(date);
                setAlarm(firstAlarmTime,TimeAndIntervalHelper.getWeekInterval(),reference_id);
            }
        }
    }

    /**
     * Cancel repeating alarm. Note that this class will delete all alarm with provided ref id
     * @param reference_id  Reference ID
     */
    public void cancelAlarm(int reference_id) {
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        pendingIntents = deleteAllPendingIntent(reference_id);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(PendingIntent pendingIntent : pendingIntents) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    /**
     * Cancel repeating alarm. This class will delete alarm with provided ref id, starting time and interval.
     * @param reference_id Reference ID
     * @param time Starting time
     * @param interval Repeating Interval
     */
    public void cancelAlarm(int reference_id, long time, long interval) {
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        pendingIntents = deletePendingIntentOfSpecificTime(reference_id,time,interval);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(PendingIntent pendingIntent : pendingIntents) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    /**
     * Cancel one-time alarm.
     * @param reference_id Reference ID
     * @param time  Time
     */
    public void cancelAlarm(int reference_id, long time) {
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        pendingIntents = deletePendingIntentOfSpecificTime(reference_id,time,0);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(PendingIntent pendingIntent : pendingIntents) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cancelAlarm(LocalDateTime startingDateTime, String daysOfWeek, int reference_id) {
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();

        long firstAlarmTime;
        for(int i=0; i<daysOfWeek.length(); i++) {
            if(daysOfWeek.indexOf(i) == '1') {
                LocalDateTime date = LocalDateTime.now();
                while(date.isBefore(startingDateTime)) {
                    date.plusWeeks(1);
                }
                firstAlarmTime = TimeAndIntervalHelper.getMilli(date);
                pendingIntents = deletePendingIntentOfSpecificTime(reference_id,firstAlarmTime,TimeAndIntervalHelper.getWeekInterval());
            }
        }

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for(PendingIntent pendingIntent : pendingIntents) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    /**
     * Recreate all alarm after reboot
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void recreateAllAlarm(Context context) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        ArrayList<RequestCodeItem> list = dbHelper.getAllRecord();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        LocalDateTime t = LocalDateTime.now();
        long now = TimeAndIntervalHelper.getMilli(t);

        for(RequestCodeItem item : list) {
            if(item.repeating_interval != 0) {
                // Reset firstAlarmTime
                long firstAlarmTime = item.time;
                while(firstAlarmTime < now) {
                    firstAlarmTime += item.repeating_interval;
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.row_id, getReceiver(context, item.type), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstAlarmTime, item.repeating_interval, pendingIntent);
            }else {
                // One-time alarm
                // Only set the alarm that is in the future
                if (item.time >= now) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, item.row_id, getReceiver(context, item.type), PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, item.time, pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, item.time, pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, item.time, pendingIntent);
                    }
                }
            }
        }
    }

    private PendingIntent getPendingIntent(long alarmTime, int reference_id) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        int requestCode = dbHelper.getRequestCode(type, alarmTime, reference_id);
        return PendingIntent.getBroadcast(context, requestCode, getReceiver(context, type), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getPendingIntent(long firstAlarmTime, long repeatInterval, int reference_id) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        int requestCode = dbHelper.getRequestCode(type, firstAlarmTime, repeatInterval, reference_id);
        return PendingIntent.getBroadcast(context, requestCode, getReceiver(context, type), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private ArrayList<PendingIntent> deleteAllPendingIntent(int reference_id) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);

        ArrayList<RequestCodeItem> requestCodes = new ArrayList<>();
        requestCodes = dbHelper.deactivateAll(reference_id);

        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        for(RequestCodeItem item : requestCodes) {
            pendingIntents.add(PendingIntent.getBroadcast(context,item.row_id,getReceiver(context,item.type),PendingIntent.FLAG_UPDATE_CURRENT));
        }

        return pendingIntents;
    }

    private ArrayList<PendingIntent> deletePendingIntentOfSpecificTime(int reference_id, long time, long interval) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);

        ArrayList<RequestCodeItem> requestCodes = new ArrayList<>();
        requestCodes = dbHelper.deactivateSpecificTime(reference_id,time,interval);

        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        for(RequestCodeItem item : requestCodes) {
            pendingIntents.add(PendingIntent.getBroadcast(context,item.row_id,getReceiver(context,item.type),PendingIntent.FLAG_UPDATE_CURRENT));
        }

        return pendingIntents;
    }

    private static Intent getReceiver(Context context, int type) {
        switch(type){
            case 1:
                return new Intent(context, PillReminderReceiver.class);
            case 2:
                return new Intent(context, AppointmentReceiver.class);
            case 3:
                return new Intent(context, BloodPressureReceiver.class);
            case 4:
                return new Intent(context, BloodSugarReceiver.class);
            default:
                return null;
        }
    }

}
