package Model;

/**
 * Created by Narendra on 18-02-2016.
 */
public class DoctorIdPatientId {

    public DoctorIdPatientId(String doctorId, String patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    private String doctorId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    private String patientId;
    //{"patientId":3,"doctorId":1}
}
