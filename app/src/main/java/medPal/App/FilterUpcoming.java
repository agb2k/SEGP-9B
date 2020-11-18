package medPal.App;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FilterUpcoming {
    LocalTime now = LocalTime.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<LocalTime, ArrayList<PillReminder>> meetsCriteria(HashMap<LocalTime, ArrayList<PillReminder>> prByTime){
        HashMap<LocalTime, ArrayList<PillReminder>> result = new HashMap<LocalTime, ArrayList<PillReminder>>();
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
