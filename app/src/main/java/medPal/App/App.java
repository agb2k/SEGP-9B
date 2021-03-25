package medPal.App;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_PILL_REMINDER = "Pill Reminder Channel";
    public static final String CHANNEL_APPOINTMENT = "Appointment Channel";
    public static final String CHANNEL_SUGAR_LEVEL = "Sugar Level Channel";
    public static final String CHANNEL_PRESSURE_LEVEL = "Pressure Level Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    public void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= 26) {
            // Creating channel for pill reminder.
            NotificationChannel channelPillReminder = new NotificationChannel(CHANNEL_PILL_REMINDER,"Channel for Pill Reminder", NotificationManager.IMPORTANCE_HIGH);
            channelPillReminder.setDescription("This channel reminds you to take pills.");
            channelPillReminder.enableVibration(true);
            // Creating channel for pill reminder.
            NotificationChannel channelAppointment = new NotificationChannel(CHANNEL_APPOINTMENT,"Channel for Appointment",NotificationManager.IMPORTANCE_HIGH);
            channelAppointment.setDescription("This channel reminds you your appointments.");
            channelAppointment.enableVibration(true);
            // Creating channel for sugar level.
            NotificationChannel channelSugarLevel = new NotificationChannel(CHANNEL_SUGAR_LEVEL,"Channel for Sugar Level",NotificationManager.IMPORTANCE_HIGH);
            channelSugarLevel.setDescription("This channel reminds you to record blood sugar level.");
            channelSugarLevel.enableVibration(true);
            // Creating channel for pressure level.
            NotificationChannel channelPressureLevel = new NotificationChannel(CHANNEL_PRESSURE_LEVEL,"Channel for Pressure Level",NotificationManager.IMPORTANCE_HIGH);
            channelPressureLevel.setDescription("This channel reminds you to record blood pressure level.");
            channelPressureLevel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelPillReminder);
            notificationManager.createNotificationChannel(channelAppointment);
            notificationManager.createNotificationChannel(channelSugarLevel);
            notificationManager.createNotificationChannel(channelPressureLevel);
        }
    }
}
