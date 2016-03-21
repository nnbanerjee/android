package Model;

import java.util.ArrayList;
import java.util.List;

public class AddPatientMedicineSummary {

    private String autoSchedule;
    private String doctorInstruction;
    private String dosesPerSchedule;
    private String durationSchedule;
    private String endDate;
    private String medicinName;
    private String reminder;
    private String schedule;
    private String startDate;
    private List<MedicineSchedule> medicineSchedule = new ArrayList<MedicineSchedule>();
    private String appointmentId;
    private String patientId;
    private String loggedinUserId;
    private String userType;

    /**
     *
     * @return
     * The autoSchedule
     */
    public String getAutoSchedule() {
        return autoSchedule;
    }

    /**
     *
     * @param autoSchedule
     * The autoSchedule
     */
    public void setAutoSchedule(String autoSchedule) {
        this.autoSchedule = autoSchedule;
    }

    /**
     *
     * @return
     * The doctorInstruction
     */
    public String getDoctorInstruction() {
        return doctorInstruction;
    }

    /**
     *
     * @param doctorInstruction
     * The doctorInstruction
     */
    public void setDoctorInstruction(String doctorInstruction) {
        this.doctorInstruction = doctorInstruction;
    }

    /**
     *
     * @return
     * The dosesPerSchedule
     */
    public String getDosesPerSchedule() {
        return dosesPerSchedule;
    }

    /**
     *
     * @param dosesPerSchedule
     * The dosesPerSchedule
     */
    public void setDosesPerSchedule(String dosesPerSchedule) {
        this.dosesPerSchedule = dosesPerSchedule;
    }

    /**
     *
     * @return
     * The durationSchedule
     */
    public String getDurationSchedule() {
        return durationSchedule;
    }

    /**
     *
     * @param durationSchedule
     * The durationSchedule
     */
    public void setDurationSchedule(String durationSchedule) {
        this.durationSchedule = durationSchedule;
    }

    /**
     *
     * @return
     * The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     * The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     * The medicinName
     */
    public String getMedicinName() {
        return medicinName;
    }

    /**
     *
     * @param medicinName
     * The medicinName
     */
    public void setMedicinName(String medicinName) {
        this.medicinName = medicinName;
    }

    /**
     *
     * @return
     * The reminder
     */
    public String getReminder() {
        return reminder;
    }

    /**
     *
     * @param reminder
     * The reminder
     */
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    /**
     *
     * @return
     * The schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     *
     * @param schedule
     * The schedule
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     *
     * @return
     * The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     * The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     * The medicineSchedule
     */
    public List<MedicineSchedule> getMedicineSchedule() {
        return medicineSchedule;
    }

    /**
     *
     * @param medicineSchedule
     * The medicineSchedule
     */
    public void setMedicineSchedule(List<MedicineSchedule> medicineSchedule) {
        this.medicineSchedule = medicineSchedule;
    }

    /**
     *
     * @return
     * The appointmentId
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     *
     * @param appointmentId
     * The appointmentId
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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
     * The loggedinUserId
     */
    public String getLoggedinUserId() {
        return loggedinUserId;
    }

    /**
     *
     * @param loggedinUserId
     * The loggedinUserId
     */
    public void setLoggedinUserId(String loggedinUserId) {
        this.loggedinUserId = loggedinUserId;
    }

    /**
     *
     * @return
     * The userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     *
     * @param userType
     * The userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

}

