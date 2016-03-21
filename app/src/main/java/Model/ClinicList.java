package Model;

import java.util.ArrayList;
import java.util.List;

public class ClinicList {

    private Clinic clinic;
    private Integer slotNumber;
    private List<Appointment> appointments = new ArrayList<Appointment>();

    public String getIdClinic() {
        return idClinic;
    }

    public void setIdClinic(String idClinic) {
        this.idClinic = idClinic;
    }

    private String idClinic;

    public String getDoctorClinicId() {
        return doctorClinicId;
    }

    public void setDoctorClinicId(String doctorClinicId) {
        this.doctorClinicId = doctorClinicId;
    }

    private String doctorClinicId;

    /**
     *
     * @return
     * The clinic
     */
    public Clinic getClinic() {
        return clinic;
    }

    /**
     *
     * @param clinic
     * The clinic
     */
    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    /**
     *
     * @return
     * The slotNumber
     */
    public Integer getSlotNumber() {
        return slotNumber;
    }

    /**
     *
     * @param slotNumber
     * The slotNumber
     */
    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    /**
     *
     * @return
     * The appointments
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     *
     * @param appointments
     * The appointments
     */
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

}