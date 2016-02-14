package Model;

/**
 * Created by Narendra on 12-02-2016.
 */
public class AllClinicsForDoctorIdAndPatientId {
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

    public AllClinicsForDoctorIdAndPatientId(String doctorId, String patientId) {
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    private String patientId;
}
