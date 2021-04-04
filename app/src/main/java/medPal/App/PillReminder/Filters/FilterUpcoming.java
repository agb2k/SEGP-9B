package medPal.App.PillReminder.Filters;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

import medPal.App.PillReminder.PillReminder;
import medPal.App.PillReminder.PillReminderPopUp.PillTakingDBHelper;

/**
 * A class to filter upcoming pill reminders.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class FilterUpcoming {
    LocalTime now = LocalTime.now();

    /**
     * Filter out upcoming pill reminders.
     * @param prByTime Treemap of pill reminders grouped by time.
     * @return Treemap of upcoming pill reminders.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime, ArrayList<PillReminder>> meetsCriteria(Context context, TreeMap<LocalTime, ArrayList<PillReminder>> prByTime){

        // Check which pill reminders have been handled (taken/skipped)
        ArrayList<String> takenList = new ArrayList<>();
        PillTakingDBHelper dbHelper = new PillTakingDBHelper(context);
        LocalDate d = LocalDate.now();
        String date = String.valueOf(d.getYear());
        if(d.getMonthValue() < 10)
            date += "0";
        date += String.valueOf(d.getMonthValue());
        if(d.getDayOfMonth() < 10)
            date += "0";
        date += String.valueOf(d.getDayOfMonth());
        takenList = dbHelper.getTakenTime(date);

        // Ignore those that have been taken/skipped
        for(String t : takenList) {
            int h = Integer.parseInt(t.substring(0,2));
            int m = Integer.parseInt(t.substring(2));
            LocalTime time = LocalTime.of(h,m);
            prByTime.remove(time);
        }

        return prByTime;
    }
}
