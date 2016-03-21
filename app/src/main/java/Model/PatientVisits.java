package Model;

import java.util.ArrayList;
import java.util.List;

public class PatientVisits {

    private String doctorId;
    private String patientId;
    private String name;
    private List<Visit> visits = new ArrayList<Visit>();

    /**
     *
     * @return
     * The doctorId
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     *
     * @param doctorId
     * The doctorId
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     *
     * @return
     * The patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     * The patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The visits
     */
    public List<Visit> getVisits() {
        return visits;
    }

    /**
     *
     * @param visits
     * The visits
     */
    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

}