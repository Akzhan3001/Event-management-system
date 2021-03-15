import java.util.Objects;

public class Appointment {
    private String doctorId;
    private String patientId;
    private String timeslot;
    private String userId;

    public Appointment(String doctorId, String patientId,String timeslot){
        this.doctorId=doctorId;
        this.patientId = patientId;
        this.timeslot=timeslot;
    }

    public Appointment(String userId, String timeslot){
        this.userId = userId;
        this.timeslot = timeslot;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public boolean equals(Appointment appointment) {
        return this.timeslot == appointment.getTimeslot();
    }
public boolean equals(String doctorId,String patientId,String timeslot){
        return this.doctorId.toLowerCase() == doctorId.toLowerCase() && this.patientId.toLowerCase()==patientId.toLowerCase() && this.timeslot.toLowerCase()==timeslot.toLowerCase();
}
    @Override
    public String toString() {
        return  doctorId  + "\t" + patientId + "\t" + timeslot + "\n";
    }
}
