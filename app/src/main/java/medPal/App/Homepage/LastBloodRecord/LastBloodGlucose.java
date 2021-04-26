package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

/***
 * Last Blood Glucose class
 */
public class LastBloodGlucose implements Serializable {
    private String date;
    private String time;
    private int level;

    /***
     * Constructor of last blood glucose level record
     * @param date Date of the record
     * @param time Time of the record
     * @param level last blood glucose Level
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LastBloodGlucose(String date, String time, int level) {
        this.date = date;
        this.time = time;
        this.level = level;
    }

    /***
     * Get date of record
     * @return date of record
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
     * Get time of record
     * @return Time of record
     */
    public String getTime() {
        return time;
    }

    /***
     * Set time of record
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /***
     * Get Blood Glucose Level
     * @return
     */
    public int getLevel() {
        return level;
    }

    /***
     * Set blood glucose level
     * @param level Bloo Glucose level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /***
     * Generate String of Blood Glucose record
     * @return String of Blood Glucose record
     */
    @Override
    public String toString() {
        return "LastBloodGlucose{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", level=" + level +
                '}';
    }
}
