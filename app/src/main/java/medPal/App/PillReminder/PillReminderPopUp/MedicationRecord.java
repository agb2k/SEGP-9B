package medPal.App.PillReminder.PillReminderPopUp;

public class MedicationRecord {

    private String date;
    private String time;
    private String medicine;
    private int dose;
    private int status;

    MedicationRecord(String date, String time, String medicine, int dose, int status) {
        this.date = date;
        this.time = time;
        this.medicine = medicine;
        this.dose = dose;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMedicine() {
        return medicine;
    }

    public int getDose() {
        return dose;
    }

    public int getStatus() {
        return status;
    }

}
