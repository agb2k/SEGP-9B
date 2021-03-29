package medPal.App.AlarmAndNotification;

import android.app.AlarmManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * You can use this class to convert different time format into milli.
 */
public class TimeAndIntervalHelper {

    /**
     * Get milli using year, month, day, hour and minute.
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return Time in milli
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getMilli(int year, int month, int day, int hour, int minute) {
        LocalDateTime time = LocalDateTime.of(year,month,day,hour,minute);
        return getMilli(time);
    }

    /**
     * Get milli from LocalDateTime format
     * @param date
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getMilli(LocalDateTime date) {
        ZonedDateTime millis = ZonedDateTime.of(date, ZoneId.systemDefault());
        return millis.toInstant().toEpochMilli();
    }

    /**
     * Get time in milli from Calendar type
     * @param date
     * @return
     */
    public static long getMilli(Calendar date) {
        return date.getTimeInMillis();
    }

    /**
     * One hour interval in milli.
     * @return
     */
    public static long getHourInterval() {
        return AlarmManager.INTERVAL_HOUR;
    }

    /**
     * Get interval based on one-hour-interval.
     * Eg.
     * Put parameter 2 for two hour.
     * Put parameter 0.5 to get 30 minutes (not 100% accurate)
     * @param hour_interval
     * @return
     */
    public static long getHourInterval(double hour_interval) {
        return (long)(hour_interval * AlarmManager.INTERVAL_HOUR);
    }

    public static long getDayInterval() {
        return AlarmManager.INTERVAL_DAY;
    }

    public static long getDayInterval(int day_interval) {
        return day_interval*AlarmManager.INTERVAL_DAY;
    }

    public static long getWeekInterval() {
        return 7*AlarmManager.INTERVAL_DAY;
    }
}
