package medPal.App.PillReminderTestCases;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import medPal.App.PillReminder.Filters.FilterDay;
import medPal.App.PillReminder.Medicine;
import medPal.App.PillReminder.PillReminder;

public class FilterDayTest {

    /**
     * Testing filter current day.
     */
    @Test
    public void TestFilterToday() {
        FilterDay filterToday = new FilterDay();
        ArrayList<PillReminder> pillReminders = new ArrayList<>(filterToday.meetsCriteria(prepareData()));
        assertEquals(4, pillReminders.size());
    }

    /**
     * Testing filter certain day.
     */
    @Test
    public void TestFilterCertainDay() {
        LocalDate date = LocalDate.of(2021,4,10);

        FilterDay filterToday = new FilterDay();
        ArrayList<PillReminder> pillReminders = new ArrayList<>(filterToday.onCertainDate(prepareData(), date));
        assertEquals(4, pillReminders.size());
    }

    /**
     * Preparing data
     * @return Data
     */
    public ArrayList<PillReminder> prepareData() {
        ArrayList<PillReminder> data = new ArrayList<>();

        // Data 1 and 2 always pass, Data 3 and 4 always fail
        // Data 1 : Everyday, After start date, No end date
        Medicine medicine1 = new Medicine(1,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder1 = new PillReminder(1,"1830",1,0,"",2,"20210321",true,"",medicine1);
        data.add(pillReminder1);
        // Data 2 : Everyday, After start date, before end date
        Medicine medicine2 = new Medicine(2,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder2 = new PillReminder(2,"1830",1,0,"",2,"20210321",false,"22221231",medicine2);
        data.add(pillReminder2);
        // Data 3 : Everyday, After start date, after end date
        Medicine medicine3 = new Medicine(3,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder3 = new PillReminder(3,"1830",1,0,"",2,"20210321",false,"20210322",medicine3);
        data.add(pillReminder3);
        // Data 4 : Everyday, Before start date
        Medicine medicine4 = new Medicine(4,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder4 = new PillReminder(4,"1830",1,0,"",2,"20211231",true,"",medicine4);
        data.add(pillReminder4);

        // Either data 5 or 6 passes, but not both
        // Data 5 : Every two days, before end date
        Medicine medicine5 = new Medicine(5,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder5 = new PillReminder(5,"1830",2,2,"",2,"20210321",true,"",medicine5);
        data.add(pillReminder5);
        // Data 6 : Every two days, starts one day after data5, before end date
        Medicine medicine6 = new Medicine(6,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder6 = new PillReminder(6,"1830",2,2,"",2,"20210322",true,"",medicine6);
        data.add(pillReminder6);

        // Either data 7 or 8 passes, but not both. Data 9 and 10 will always fail.
        // Data 7 : Every Mon, Tue, Wed, Thurs, Fri, after start date, before end date
        Medicine medicine7 = new Medicine(7,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder7 = new PillReminder(7,"1830",3,0,"1111100",2,"20210321",true,"",medicine7);
        data.add(pillReminder7);
        // Data 8 : Every Sat, Sun, after start date, before end date
        Medicine medicine8 = new Medicine(8,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder8 = new PillReminder(8,"1830",3,0,"0000011",2,"20210321",true,"",medicine8);
        data.add(pillReminder8);
        // Data 9 : Every Mon, Tue, Wed, Thurs, Fri, after start date, after end date
        Medicine medicine9 = new Medicine(9,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder9 = new PillReminder(9,"1830",3,0,"1111100",2,"20210321",false,"20210322",medicine9);
        data.add(pillReminder9);
        // Data 10 : Every Sat, Sun, after start date, after end date
        Medicine medicine10 = new Medicine(10,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder10 = new PillReminder(10,"1830",3,0,"0000011",2,"20210321",false,"20210322",medicine10);
        data.add(pillReminder10);

        return data;
    }

}
