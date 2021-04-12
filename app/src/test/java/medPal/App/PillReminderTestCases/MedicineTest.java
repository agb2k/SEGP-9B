package medPal.App.PillReminderTestCases;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import medPal.App.PillReminder.Medicine;

public class MedicineTest {

    /**
     * Testing Constructor of Medicine by checking the result returned from Getter.
     */
    @Test
    public void TestMedicineConstructor() {
        // Test 1: No medicine image
        Medicine medicine1 = new Medicine(4321,"Medicine Name","Manufacturer Name",200,"","Purpose.","Remark.");
        assertEquals(4321, medicine1.getMedicineId());
        assertEquals("Medicine Name", medicine1.getMedicineName());
        assertEquals("Manufacturer Name", medicine1.getManufacturer());
        assertEquals(200, medicine1.getDosage());
        assertEquals("",medicine1.getImagePath());
        assertEquals("Purpose.",medicine1.getPurpose());
        assertEquals("Remark.",medicine1.getMedicineRemarks());

        // Test 2: With medicine image
        Medicine medicine2 = new Medicine(4321,"Medicine Name","Manufacturer Name",200,"image.jpg","Purpose.","Remark.");
        assertEquals(4321, medicine2.getMedicineId());
        assertEquals("Medicine Name", medicine2.getMedicineName());
        assertEquals("Manufacturer Name", medicine2.getManufacturer());
        assertEquals(200, medicine2.getDosage());
        assertEquals("https://bulacke.xyz/medpal-img/image.jpg",medicine2.getImagePath());
        assertEquals("Purpose.",medicine2.getPurpose());
        assertEquals("Remark.",medicine2.getMedicineRemarks());
    }

}
