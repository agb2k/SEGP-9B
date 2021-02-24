package medPal.App.Homepage.NextAppointment;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

/***
 * Next Appoinment Class
 */
public class NextAppointment implements Serializable{

    private int apptID;
    private String date;
    private String time;
    private String hospital;
    private String docName;

    @RequiresApi(api= Build.VERSION_CODES.O)
    public NextAppointment(int apptID, String date, String time, String hospital, String docName) {
        this.apptID = apptID;
        this.date = date;
        this.time = time;
        this.hospital = hospital;
        this.docName = docName;
    }

    public int getApptID() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Override
    public String toString() {
        return "NextAppointment{" +
                "apptID=" + apptID +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", hospital='" + hospital + '\'' +
                ", docName='" + docName + '\'' +
                '}';
    }
}
