package medPal.App.AlarmAndNotification.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.SQLiteHelper;
import medPal.App.App;

public class BloodPressureReceiver extends BroadcastReceiver {

    private static final String TITLE = "Title for Notification Type 1";
    private static final String TEXT = "Text for Notification Type 1";
    private static final String INFO = "Info for Notification Type 1";
    private static final String CHANNEL_ID = App.CHANNEL_PRESSURE_LEVEL;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = NotificationHelper.BLOOD_PRESSURE_NOTIFICATION_REQUEST_CODE;
        NotificationHelper.createNotification(context,id,CHANNEL_ID,TITLE,TEXT,INFO);
    }

}
