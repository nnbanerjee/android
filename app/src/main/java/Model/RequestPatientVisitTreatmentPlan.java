package Model;

/**
 * Created by Narendra on 01-04-2016.
 */
public class RequestPatientVisitTreatmentPlan {
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public RequestPatientVisitTreatmentPlan(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    private String appointmentId;
    private String PatientId;
    private String DoctorId;
}
