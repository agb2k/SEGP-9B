package medPal.App.Appointment;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;


public class Appointment implements Serializable {
    private int appointmentID;
    private LocalDate date;
    private String time;
    private String doctor;
    private String venue;
    private int contact;
    private String email;
    private String purpose;
    private String remark;

    /**
     * Constructor of pill reminder object.
     * @param appointmentID Appointment ID.
     * @param date Appointment Date.
     * @param time Appointment Time.
     * @param doctor Doctor Name.
     * @param venue Appointment Venue.
     * @param contact Doctor Contact Number.
     * @param email Doctor Email Address.
     * @param purpose Appointment Purpose.
     * @param remark Appointment Remark.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Appointment(int appointmentID, String date, String time, String doctor, String venue, int contact, String email, String purpose, String remark) {
        this.appointmentID = appointmentID;
        this.time = time;
        this.date = stringToLocalDate(date);
        this.doctor = doctor;
        this.venue =  venue;
        this.contact = contact;
        this.email = email;
        this.purpose = purpose;
        this.remark = remark;
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
        return LocalDate.of(year, month, day);
    }

//    Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public LocalDate getDate() {
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(String date) {
        this.date = stringToLocalDate(date);
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


}
