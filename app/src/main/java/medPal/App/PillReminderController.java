package medPal.App;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class PillReminderController implements Serializable {
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private ArrayList<PillReminder> pillReminderList = new ArrayList<PillReminder>();
    private ArrayList<PillReminder> todayPillReminder = new ArrayList<PillReminder>();
    private TreeMap<LocalTime,ArrayList<PillReminder>> pillReminderByTime = new TreeMap<LocalTime,ArrayList<PillReminder>>();
    public TreeMap<LocalTime,Integer> takenBit = new TreeMap<LocalTime,Integer>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    PillReminderController() {
        RetrievePillReminders getDB = new RetrievePillReminders();
        medicineList = getDB.getAllMedicine();
        pillReminderList = getDB.getAllPillReminder();
        formTodaysPillReminder();
        groupPillReminderByTime();
        setAllNotTaken();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshData() {
        RetrievePillReminders getDB = new RetrievePillReminders();
        medicineList = getDB.getAllMedicine();
        pillReminderList = getDB.getAllPillReminder();
        formTodaysPillReminder();
        groupPillReminderByTime();
    }

    public ArrayList<Medicine> getAllMedicine() {
        return medicineList;
    }

    public Medicine getMedicineById(int id) {
        for(Medicine m : medicineList) {
            if(m.getMedicineId() == id) {
                return m;
            }
        }
        return null;
    }

    protected ArrayList<PillReminder> getAllPillReminder() {
        return pillReminderList;
    }

    public PillReminder getPillReminderById(int id) {
        for(PillReminder pr : pillReminderList) {
            if(pr.getPillReminderId() == id) {
                return pr;
            }
        }
        return null;
    }

    // Group today's pill reminder
    // Store today's pill reminder in todaysPillReminder ( ArrayLst<PillReminder> )
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void formTodaysPillReminder() {
        FilterDay isToday = new FilterDay();
        todayPillReminder = isToday.meetsCriteria(pillReminderList);
    }

    // Group pill reminders by time
    // Store them by time in pillReminderByTime ( HashMap<LocalDate, ArrayList<PillReminder>> )
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void groupPillReminderByTime() {
        FilterTime byTime = new FilterTime();
        pillReminderByTime = byTime.meetsCriteria(todayPillReminder);
    }

    // Set all pill reminder as not taken
    private void setAllNotTaken() {
        for(LocalTime t : pillReminderByTime.keySet()) {
            takenBit.put(t,0);
        }
    }

    // Get today's pill reminder
    public ArrayList<PillReminder> getTodaysPillReminder() {
        return todayPillReminder;
    }

    // Get today's pill reminder, group by time
    public TreeMap<LocalTime,ArrayList<PillReminder>> getPillReminderByTime() {
        return pillReminderByTime;
    }

    // Get pill reminders of a specific day
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<PillReminder> getPillReminderOnSpecificDay(LocalDate d){
        ArrayList<PillReminder> pr = new ArrayList<PillReminder>();
        FilterDay isThatDay = new FilterDay();
        pr.addAll(isThatDay.onCertainDate(pillReminderList,d));

        return pr;
    }

    // Get current pill reminders
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime,ArrayList<PillReminder>> getNotTaken(){
        FilterNotTaken notTaken = new FilterNotTaken();
        return notTaken.meetsCriteria(pillReminderByTime,takenBit);
    }

    // Get upcoming pill reminders
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime,ArrayList<PillReminder>> getUpcomingPillReminder(){
        FilterUpcoming upcoming = new FilterUpcoming();
        return upcoming.meetsCriteria(pillReminderByTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void takePill(ArrayList<PillReminder> prList, LocalTime t) {
        // Take a whole list of pills of the same time
        for(PillReminder p : prList) {
            p.takePill();
        }
        // Set takenBit for time t as taken
        takenBit.replace(t,1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void takePill(PillReminder p, LocalTime t) {
        // Take pill one by one
        p.takePill();

        PillReminder pr;
        for(int i=0; i<getNotTaken().get(t).size(); i++) {
            pr = getNotTaken().get(t).get(i);
            if(!pr.hasTaken()) {
                // If one of the pill reminder has not be taken, break the loop
                break;
            }else if(i == getNotTaken().get(t).size()-1) {
                // All has been taken, replace taken bit to 1
                takenBit.replace(t,1);
            }
        }
    }
}
