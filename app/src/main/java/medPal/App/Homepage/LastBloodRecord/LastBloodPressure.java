package medPal.App.Homepage.LastBloodRecord;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class LastBloodPressure implements Serializable {
    private String date;
    private String time;
    private int SYS;
    private int DIA;

    @RequiresApi(api= Build.VERSION_CODES.O)
    public LastBloodPressure(String date, String time, int SYS, int DIA) {
        this.date = date;
        this.time = time;
        this.SYS = SYS;
        this.DIA = DIA;
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

    public int getSYS() {
        return SYS;
    }

    public void setSYS(int SYS) {
        this.SYS = SYS;
    }

    public int getDIA() {
        return DIA;
    }

    public void setDIA(int DIA) {
        this.DIA = DIA;
    }

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
