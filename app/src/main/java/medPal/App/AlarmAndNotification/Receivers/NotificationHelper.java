package medPal.App.AlarmAndNotification.Receivers;

import android.app.Notification;
import android.content.Context;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import medPal.App.R;

public class NotificationHelper {

    private String title = "";
    private String text = "";
    private String info = "";

    public static void createNotification(Context context, int id, String channelId, String title, String text, String info) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
                .setWhen(System.currentTimeMillis())
                .setTicker("Hearty365")
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo(info);

        notificationManager.notify(id, notificationBuilder.build());
    }

}
