package medPal.App.Homepage.UpcomingAppointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class UpcomingAppointment {
    private int appointmentID;
    private LocalDate date;
    private String time;
    private String doctor;
    private String venue;
    private int contact;
    private String email;
    private String purpose;
    private String remark;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UpcomingAppointment(int appointmentID, String date, String time, String doctor, String venue, int contact, String email, String purpose, String remark) {
        this.appointmentID = appointmentID;
        this.date = stringToLocaldate(date);
        this.time = time;
        this.doctor = doctor;
        this.venue = venue;
        this.contact = contact;
        this.email = email;
        this.purpose = purpose;
        this.remark = remark;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate stringToLocaldate(String date) {
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(4,6));
        int day = Integer.parseInt(date.substring(6));
        String result = year + "" + month + "" + day;
        LocalDate localDate = LocalDate.of(year, month, day);
        return localDate;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Override
    public String toString() {
        return "UpcomingAppointment{" +
                "appointmentID=" + appointmentID +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", doctor='" + doctor + '\'' +
                ", venue='" + venue + '\'' +
                ", contact=" + contact +
                ", email='" + email + '\'' +
                ", purpose='" + purpose + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}


