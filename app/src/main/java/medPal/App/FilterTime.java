package medPal.App;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class FilterTime {
    HashMap<LocalTime, ArrayList<PillReminder>> prByTime = new HashMap<LocalTime,ArrayList<PillReminder>>();

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

        TreeMap<LocalTime,ArrayList<PillReminder>> sorted = new TreeMap<>();
        sorted.putAll(prByTime);

        return sorted;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalTime toLocalTime(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(2));
        return LocalTime.of(hour, min);
    }

    public ArrayList<PillReminder> addNewEntry(LocalTime t){
        // Make sure we added an empty list to the hashmap
        ArrayList<PillReminder> tempList = new ArrayList<PillReminder>();
        tempList.clear();
        prByTime.put(t,tempList);

        return tempList;
    }
}
