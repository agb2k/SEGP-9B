package medPal.App.PillReminder.Filters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

import medPal.App.PillReminder.PillReminder;

/**
 * A class to filter the pill reminders that haven be taken.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class FilterNotTaken {
    LocalTime now = LocalTime.now();

    /**
     * Filter out the pill reminders that have not be taken by user.
     * @param prByTime Pill reminders grouped by time.
     * @param bit Bit that represents whether the user has taken the pill, grouped by time.
     * @return A treemap of pill reminders that have bot be taken by the user.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime, ArrayList<PillReminder>> meetsCriteria(TreeMap<LocalTime,ArrayList<PillReminder>> prByTime, TreeMap<LocalTime,Integer> bit){
        TreeMap<LocalTime,ArrayList<PillReminder>> result = new TreeMap<LocalTime,ArrayList<PillReminder>>();
        ArrayList<PillReminder> notTaken = new ArrayList<PillReminder>();

        for(LocalTime t : prByTime.keySet()) {
            if(t.isBefore(now) || t.equals(now)) {
                if(bit.get(t) != 1) {
                    for(PillReminder pr : prByTime.get(t)) {
                        if(!pr.hasTaken()) {
                            notTaken.add(pr);
                        }
                    }
                    result.put(t,notTaken);
                }
            }
        }

        return result;
    }
}
