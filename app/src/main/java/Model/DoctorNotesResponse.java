package Model;

/**
 * Created by Narendra on 15-03-2016.
 */
public class DoctorNotesResponse {
    //{"appointmentId":584,"diagnosis":"cold","symptoms":"fever","doctorNotes":"found cold and fever"}
    private String appointmentId;
    private String diagnosis;

    public DoctorNotesResponse(String appointmentId, String diagnosis, String symptoms, String doctorNotes) {
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.doctorNotes = doctorNotes;
    }

    private String symptoms;

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    private String doctorNotes;
}
