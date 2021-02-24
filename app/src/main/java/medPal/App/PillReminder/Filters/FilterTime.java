package medPal.App.PillReminder.Filters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import medPal.App.PillReminder.PillReminder;

/**
 * A class the group the pill reminders by time.
 */
public class FilterTime {
    HashMap<LocalTime, ArrayList<PillReminder>> prByTime = new HashMap<LocalTime,ArrayList<PillReminder>>();

    /**
     * Group the pill reminders by time.
     * @param pillReminders Arraylist of pill reminders.
     * @return Treemap of pill reminders grouped by time.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime,ArrayList<PillReminder>> meetsCriteria(ArrayList<PillReminder> pillReminders) {

        String timeStr;
        LocalTime localtime;

        // Bind pill reminders with their LocalTime
        for(PillReminder prObj : pillReminders) {
            timeStr = prObj.getTime();
            localtime = toLocalTime(timeStr);

            if(prByTime.isEmpty() || !prByTime.containsKey(localtime)) {
                ArrayList<PillReminder> list = addNewEntry(localtime);
                list.add(prObj);
            }else {
                prByTime.get(localtime).add(prObj);
            }
        }

        // The reason we need to convert to treemap is that treemap can be sorted.
        TreeMap<LocalTime,ArrayList<PillReminder>> sorted = new TreeMap<>();
        sorted.putAll(prByTime);

        return sorted;
    }

    /**
     * Convert String type time to LocalTime type.
     * @param time String type time.
     * @return LocalTime type time.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalTime toLocalTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(2));
        return LocalTime.of(hour, min);
    }

    /**
     * Add new key entry to the hashmap.
     * @param t LocalTime that will be used as key.
     * @return The empty arraylist of this entry.
     */
    public ArrayList<PillReminder> addNewEntry(LocalTime t){
        // Make sure we added an empty list to the hashmap
        ArrayList<PillReminder> tempList = new ArrayList<PillReminder>();
        tempList.clear();
        prByTime.put(t,tempList);

        return tempList;
    }
}
