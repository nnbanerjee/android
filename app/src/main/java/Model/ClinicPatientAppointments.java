package Model;

import java.util.ArrayList;
import java.util.List;

public class ClinicPatientAppointments {

    private String doctorId;
    private String patientId;
    private String name;
    private List<ClinicList> clinicList = new ArrayList<ClinicList>();
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
     * The clinicList
     */
    public List<ClinicList> getClinicList() {
        return clinicList;
    }

    /**
     *
     * @param clinicList
     * The clinicList
     */
    public void setClinicList(List<ClinicList> clinicList) {
        this.clinicList = clinicList;
    }

}