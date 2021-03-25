package medPal.App.AlarmAndNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Make sure alarms are set after reboot.
 *
 * Source - https://github.com/stacktipslab/Android-Alarm-Service-Example
 */
public class RebootSetAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmHelper.recreateAllAlarm(context);
        }
    }
}
