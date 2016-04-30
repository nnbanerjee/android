package Model;

/**
 * Created by Narendra on 31-03-2016.
 */
public class AppointmentPatientIds {

    private String appointmentId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentPatientIds(String appointmentId, String patientId) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
    }

    private String patientId;
}
