package medPal.App.AlarmAndNotification.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import medPal.App.App;
import medPal.App.MainActivity;

public class PillReminderReceiver extends BroadcastReceiver {

    private static final String TITLE = "MedPal - Pill Reminder";
    private static final String TEXT = "Time to take pill.";
    private static final String INFO = "Info for Notification Type 1";
    private static final String CHANNEL_ID = App.CHANNEL_PILL_REMINDER;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = NotificationHelper.PILL_REMINDER_NOTIFICATION_REQUEST_CODE;

        Intent targetIntent = new Intent(context, MainActivity.class);
        targetIntent.putExtra("showPillReminderPopUp",true);
        PendingIntent targetPendingIntent = PendingIntent.getActivity(context,NotificationHelper.PILL_REMINDER_ONCLICK_REQUEST_CODE,targetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationHelper.createNotification(context,id,CHANNEL_ID,TITLE,TEXT,INFO,targetPendingIntent);
    }

}
