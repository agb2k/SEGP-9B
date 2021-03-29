package medPal.App.PillReminder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Pill reminder class.
 */
public class PillReminder implements Serializable {
    private int pillReminderId;
    private String time;
    private int type;
    private int frequency;
    private String week_bit;
    private int quantity;
    private LocalDate start_date;
    private LocalDate end_date;
    private boolean noEndDate = false;
    private Medicine medicine;

    /**
     * Constructor of pill reminder object.
     * @param pillReminderId Pill reminder ID.
     * @param time Time to take this pill reminder.
     * @param type Type of this pill reminder: Everyday / Every n days / Every days of week.
     * @param frequency If type = Every n days, frequency = n.
     * @param week_bit If type = Every days of week, week_bit represents the days of week.
     * @param quantity Quantity of pill to be taken.
     * @param start_date Starting date of this pill reminder.
     * @param noEndDate Boolean variables to represents whether this pill reminder has an end date.
     * @param end_date End date of this pill reminder.
     * @param medicine Medicine object.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PillReminder(int pillReminderId, String time, int type, int frequency, String week_bit, int quantity, String start_date, boolean noEndDate, String end_date, Medicine medicine) {
        this.pillReminderId = pillReminderId;
        this.time = time;
        this.type = type;
        this.frequency = frequency;
        this.week_bit = week_bit;
        this.quantity = quantity;
        this.start_date = stringToLocalDate(start_date);
        this.noEndDate = noEndDate;
        if(noEndDate){
            this.end_date = LocalDate.of(1,1,1);
        }else{
            this.end_date = stringToLocalDate(end_date);
        }
        this.medicine = medicine;
    }

    /**
     * Convert string type date to localdate type date.
     * @param date String type date.
     * @return LocalDate type date.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate stringToLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        String result = year + "" + month + "" + day;
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }

    public int getPillReminderId() {
        return pillReminderId;
    }

    public void setPillReminderId(int pillReminderId) {
        this.pillReminderId = pillReminderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWeek_bit() {
        return week_bit;
    }

    public void setWeek_bit(String week_bit) {
        this.week_bit = week_bit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setStart_date(String start_date) {
        this.start_date = stringToLocalDate(start_date);
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public boolean isNoEndDate() {
        return noEndDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setEnd_date(String end_date) {
        this.end_date = stringToLocalDate(end_date);
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    @Override
    public String toString() {
        return "PillReminder\n" +
                "pillReminderId=" + pillReminderId +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", frequency=" + frequency +
                ", week_bit='" + week_bit + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", medicine='" + medicine + '\'' +
                '}';
    }
}
