package medPal.App.PillReminder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;

import medPal.App.PillReminder.Filters.FilterDay;
import medPal.App.PillReminder.Filters.FilterTime;
import medPal.App.PillReminder.Filters.FilterUpcoming;

/**
 * A class to handle the pill reminders list.
 */
public class PillReminderController implements Serializable {
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private ArrayList<PillReminder> pillReminderList = new ArrayList<PillReminder>();
    private ArrayList<PillReminder> todayPillReminder = new ArrayList<PillReminder>();
    private TreeMap<LocalTime,ArrayList<PillReminder>> pillReminderByTime = new TreeMap<LocalTime,ArrayList<PillReminder>>();

    /**
     * Initialize and get list of pill reminders from database.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PillReminderController() {
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

    // Get upcoming pill reminders
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeMap<LocalTime,ArrayList<PillReminder>> getUpcomingPillReminder(){
        FilterUpcoming upcoming = new FilterUpcoming();
        return upcoming.meetsCriteria(pillReminderByTime);
    }

}
