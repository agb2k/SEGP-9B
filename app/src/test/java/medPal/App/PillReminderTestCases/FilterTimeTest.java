package medPal.App.PillReminderTestCases;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import medPal.App.PillReminder.Filters.FilterTime;
import medPal.App.PillReminder.Medicine;
import medPal.App.PillReminder.PillReminder;

import static org.junit.Assert.assertEquals;

public class FilterTimeTest {

    /**
     * Testing FilterTime
     */
    @Test
    public void TestFilterTime() {
        FilterTime filterTime = new FilterTime();

        TreeMap<LocalTime, ArrayList<PillReminder>> result = new TreeMap<>();
        result = filterTime.meetsCriteria(prepareData());

        assertEquals(3, result.size());

        int totalCount = 0;
        for(LocalTime key : result.keySet()) {
            totalCount += result.get(key).size();
        }

        assertEquals(4, totalCount);
    }

    /**
     * Testing toLocalTime() function
     */
    @Test
    public void TestStringToLocalTime() {
        FilterTime filterTime = new FilterTime();

        LocalTime t1 = LocalTime.of(1,30);
        assertEquals(t1, filterTime.toLocalTime("0130"));
        LocalTime t2 = LocalTime.of(23,0);
        assertEquals(t2, filterTime.toLocalTime("2300"));
    }

    public ArrayList<PillReminder> prepareData() {
        ArrayList<PillReminder> data = new ArrayList<>();

        // Data 1
        Medicine medicine1 = new Medicine(1,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder1 = new PillReminder(1,"0030",1,0,"",2,"20210321",true,"",medicine1);
        data.add(pillReminder1);
        // Data 2
        Medicine medicine2 = new Medicine(2,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder2 = new PillReminder(2,"0830",1,0,"",2,"20210321",true,"",medicine2);
        data.add(pillReminder2);
        // Data 3
        Medicine medicine3 = new Medicine(3,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder3 = new PillReminder(3,"0830",1,0,"",2,"20210321",true,"",medicine3);
        data.add(pillReminder3);
        // Data 4
        Medicine medicine4 = new Medicine(4,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder4 = new PillReminder(4,"1700",1,0,"",2,"20210321",true,"",medicine4);
        data.add(pillReminder4);

        return data;
    }

}
