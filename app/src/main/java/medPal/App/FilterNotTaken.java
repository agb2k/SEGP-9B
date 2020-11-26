package medPal.App;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FilterNotTaken {
    LocalTime now = LocalTime.now();

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
