package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class LastBloodGlucose implements Serializable {
    private String date;
    private String time;
    private int level;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public LastBloodGlucose(String date, String time, int level) {
        this.date = date;
        this.time = time;
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "LastBloodGlucose{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", level=" + level +
                '}';
    }
}
