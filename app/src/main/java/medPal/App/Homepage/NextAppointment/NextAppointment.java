package medPal.App.Homepage.NextAppointment;

public class NextAppointment {
    private String email;
    private String apptID;
    private String date;
    private String time;
    private String hospital;
    private String docName;

    public NextAppointment(String email, String apptID, String date, String time,
                           String hospital, String docName){
        this.email=email;
        this.apptID=apptID;
        this.date=date;
        this.time=time;
        this.hospital=hospital;
        this.docName=docName;
    }


    public String getEmail(){
        return email;
    }

    public String getApptID(){
        return apptID;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getHospital() {
        return hospital;
    }

    public String getDocName() {
        return docName;
    }

    @Override
    public String toString() {
        return "NextAppointment{" +
                "email='" + email + '\'' +
                ", apptID=" + apptID +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", hospital='" + hospital + '\'' +
                ", docName='" + docName + '\'' +
                '}';
    }
}
