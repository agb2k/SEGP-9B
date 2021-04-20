package medPal.App.AlarmAndNotification.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import medPal.App.App;
import medPal.App.Tracker.BloodSugarLevel.NewSugarLevelRecord;

public class BloodSugarReceiver extends BroadcastReceiver {

    private static final String TITLE = "medPal - Blood Sugar";
    private static final String TEXT = "Have you done sugar recording?";
    private static final String INFO = "Click here to do the recording now.";
    private static final String CHANNEL_ID = App.CHANNEL_SUGAR_LEVEL;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = NotificationHelper.BLOOD_SUGAR_NOTIFICATION_REQUEST_CODE;
        Intent targetIntent = new Intent(context, NewSugarLevelRecord.class);
        PendingIntent targetPendingIntent = PendingIntent.getActivity(context,NotificationHelper.BLOOD_SUGAR_ONCLICK_REQUEST_CODE,targetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationHelper.createNotification(context,id,CHANNEL_ID,TITLE,TEXT,INFO,targetPendingIntent);
    }

}
