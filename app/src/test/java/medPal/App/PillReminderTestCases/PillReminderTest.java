package medPal.App.PillReminderTestCases;

import org.junit.Test;

import java.time.LocalDate;

import medPal.App.PillReminder.Medicine;
import medPal.App.PillReminder.PillReminder;

import static org.junit.Assert.assertEquals;

/**
 * Testing pill reminder class.
 */
public class PillReminderTest {

    /**
     * Testing Constructor of Pill Reminder by checking the value return from Getter.
     */
    @Test
    public void TestPillReminderConstructor() {
        Medicine medicine = new Medicine(4321,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder = new PillReminder(1234,"1830",1,0,"",2,"20210321",true,"",medicine);

        assertEquals(1234, pillReminder.getPillReminderId());
        assertEquals("1830",pillReminder.getTime());
        assertEquals(1,pillReminder.getType());
        assertEquals(0,pillReminder.getFrequency());
        assertEquals("",pillReminder.getWeek_bit());
        assertEquals(2,pillReminder.getQuantity());
        LocalDate startDate = LocalDate.of(2021,3,21);
        assertEquals(startDate,pillReminder.getStart_date());
        LocalDate endDate = LocalDate.of(1,1,1);
        assertEquals(endDate,pillReminder.getEnd_date());
        assertEquals(medicine,pillReminder.getMedicine());
    }

    /**
     * Testing stringToLocalDate function
     */
    @Test
    public void TestStringToDateConversion() {
        Medicine medicine = new Medicine(4321,"Medicine Name","Manufacturer Name",200,"","Purpose","Remark.");
        PillReminder pillReminder = new PillReminder(1234,"1830",1,0,"",2,"20210321",true,"",medicine);

        // Test 1
        String date1_string = "20211204";
        LocalDate date1_localdate = LocalDate.of(2021,12,4);
        LocalDate date1_result = pillReminder.stringToLocalDate(date1_string);
        assertEquals(date1_localdate, date1_result);

        // Test 2
        String date2_string = "20000112";
        LocalDate date2_localdate = LocalDate.of(2000,1,12);
        LocalDate date2_result = pillReminder.stringToLocalDate(date2_string);
        assertEquals(date2_localdate, date2_result);
    }

}
