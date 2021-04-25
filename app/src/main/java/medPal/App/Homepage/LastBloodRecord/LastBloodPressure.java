package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

/***
 * Last Blood Pressure class
 */
public class LastBloodPressure implements Serializable {
    private String date;
    private String time;
    private int SYS;
    private int DIA;

    /***
     * Constructor for LastBloodPressure
     * @param date date of the record
     * @param time time of the record
     * @param SYS systolic
     * @param DIA diastolic
     */
    @RequiresApi(api= Build.VERSION_CODES.O)
    public LastBloodPressure(String date, String time, int SYS, int DIA) {
        this.date = date;
        this.time = time;
        this.SYS = SYS;
        this.DIA = DIA;
    }

    /***
     * Date of Record
     * @return Date of Record
     */
    public String getDate() {
        return date;
    }

    /***
     * Set date of record
     * @param date Date of record
     */
    public void setDate(String date) {
        this.date = date;
    }

    /***
     * Get time of Record
     * @return Time of Record
     */
    public String getTime() {
        return time;
    }

    /***
     * Set time of record
     * @param time Time of Record
     */
    public void setTime(String time) {
        this.time = time;
    }

    /***
     * Get systolic
     * @return systolic
     */
    public int getSYS() {
        return SYS;
    }

    /***
     * Set systolic
     * @param SYS systolic
     */
    public void setSYS(int SYS) {
        this.SYS = SYS;
    }

    /***
     * Get diastolic
     * @return diastolic
     */
    public int getDIA() {
        return DIA;
    }

    /***
     * Set diastolic
     * @param DIA diastolic
     */
    public void setDIA(int DIA) {
        this.DIA = DIA;
    }

    /***
     * Generate string of blood pressure record
     * @return string of blood pressure record
     */
    @Override
    public String toString() {
        return "LastBloodPressure{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", SYS=" + SYS +
                ", DIA=" + DIA +
                '}';
    }
}
