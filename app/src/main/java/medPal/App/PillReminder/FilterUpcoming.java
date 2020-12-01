package medPal.App.PillReminder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FilterUpcoming {
    LocalTime now = LocalTime.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime, ArrayList<PillReminder>> meetsCriteria(TreeMap<LocalTime, ArrayList<PillReminder>> prByTime){
        TreeMap<LocalTime, ArrayList<PillReminder>> result = new TreeMap<LocalTime, ArrayList<PillReminder>>();
        result.putAll(prByTime);

        int delay = 5; // Show pill reminders that is <delay> minutes past

        for(LocalTime t : prByTime.keySet()) {
            if(t.isBefore(now.minusMinutes(delay))) {
                result.remove(t);
            }
        }

        return result;
    }
}
