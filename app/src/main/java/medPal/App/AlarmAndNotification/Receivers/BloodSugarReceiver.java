package medPal.App.AlarmAndNotification.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import medPal.App.AlarmAndNotification.AlarmHelper;
import medPal.App.AlarmAndNotification.SQLiteHelper;
import medPal.App.App;
import medPal.App.MainActivity;
import medPal.App.Tracker.BloodSugarLevel.NewSugarLevelRecord;

public class BloodSugarReceiver extends BroadcastReceiver {

    private static final String TITLE = "Title for Notification Type 1";
    private static final String TEXT = "Text for Notification Type 1";
    private static final String INFO = "Info for Notification Type 1";
    private static final String CHANNEL_ID = App.CHANNEL_SUGAR_LEVEL;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = NotificationHelper.BLOOD_SUGAR_NOTIFICATION_REQUEST_CODE;
        Intent targetIntent = new Intent(context, NewSugarLevelRecord.class);
        PendingIntent targetPendingIntent = PendingIntent.getActivity(context,NotificationHelper.BLOOD_SUGAR_ONCLICK_REQUEST_CODE,targetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationHelper.createNotification(context,id,CHANNEL_ID,TITLE,TEXT,INFO,targetPendingIntent);
    }

}
