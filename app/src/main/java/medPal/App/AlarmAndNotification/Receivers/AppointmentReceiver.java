package medPal.App.AlarmAndNotification.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.SQLiteHelper;
import medPal.App.App;

public class AppointmentReceiver extends BroadcastReceiver {

    private static final String TITLE = "Title for Notification Type 1";
    private static final String TEXT = "Text for Notification Type 1";
    private static final String INFO = "Info for Notification Type 1";
    private static final String CHANNEL_ID = App.CHANNEL_APPOINTMENT;

    @Override
    public void onReceive(Context context, Intent intent) {
        SQLiteHelper dbHelper = new SQLiteHelper(context,SQLiteHelper.TABLE_NOTIFICATION_ID);
        int id = dbHelper.getNotificationId(AlarmHelper.APPOINTMENT);
        NotificationHelper.createNotification(context,id,CHANNEL_ID,TITLE,TEXT,INFO);
    }

}
