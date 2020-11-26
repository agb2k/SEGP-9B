package medPal.App;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FilterDay {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<PillReminder> meetsCriteria(ArrayList<PillReminder> pillReminders) {
        LocalDate now = LocalDate.now();

        return onCertainDate(pillReminders,now);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<PillReminder> onCertainDate(ArrayList<PillReminder> pillReminders, LocalDate date) {
        ArrayList<PillReminder> todayPillReminders = new ArrayList<PillReminder>();
        int weekOfDay = date.getDayOfWeek().getValue();

        LocalDate prStartDate;
        LocalDate prEndDate;
        int prType;

        for(PillReminder prObj: pillReminders) {

            prStartDate = prObj.getStart_date();
            if(prObj.isNoEndDate()){
                int year = LocalDate.now().getYear() + 1;
                prEndDate = LocalDate.of(year,1,1);
            }else{
                prEndDate = prObj.getEnd_date();
            }
            boolean isBetween = date.isAfter(prStartDate) && date.isBefore(prEndDate);
            boolean isToday = date.equals(prStartDate) || date.equals(prEndDate);

            // Check if today is between start date and end date
            if(isBetween || isToday) {
                // Get pill reminder type
                prType = prObj.getType();
                /* Type Declaration
                 * 1 : Everyday
                 * 2 : Every <frequency> days
                 * 3 : Every <Day(s) of Week>
                 */
                if(prType == 1) {
                    todayPillReminders.add(prObj);
                }else if(prType == 2) {
                    // If today is a multiple of <frequency> after the start date, user needs to take this pill today
                    if(ChronoUnit.DAYS.between(date,prStartDate)%(prObj.getFrequency()) == 0) {
                        todayPillReminders.add(prObj);
                    }
                }else {
                    // If today's <week_bit> entry is 1, user needs to take this pill
                    if(prObj.getWeek_bit().charAt(weekOfDay-1) == '1') {
                        todayPillReminders.add(prObj);
                    }
                }
            }
        }

        return todayPillReminders;
    }
}
