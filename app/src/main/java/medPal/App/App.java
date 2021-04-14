package medPal.App;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import medPal.App.UserIdentification.UserIdentity;
import medPal.App.UserIdentification.UserIdentityDBHelper;

public class App extends Application {
    public static final String CHANNEL_PILL_REMINDER = "Pill Reminder Channel";
    public static final String CHANNEL_APPOINTMENT = "Appointment Channel";
    public static final String CHANNEL_SUGAR_LEVEL = "Sugar Level Channel";
    public static final String CHANNEL_PRESSURE_LEVEL = "Pressure Level Channel";

    @Override
    public void onCreate() {
        super.onCreate();
/*
        // Check if user has logged in
        UserIdentity identity = UserIdentity.getInstance();
        if(!identity.loggedIn()) {
            // If not logged in
            // Check if the log in detail is in database
            UserIdentityDBHelper dbHelper = new UserIdentityDBHelper(this.getApplicationContext());
            if(dbHelper.isEmpty()) {
                // If no login information, go to login page
                // TODO Go to login page //
            }else{
                // Initialize UserIdentity with the data from database
                dbHelper.getRecord();
            }
        }
*/
        // Create notification channels
        createNotificationChannels();
    }

    /**
     * Initialise notification channel
     */
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
