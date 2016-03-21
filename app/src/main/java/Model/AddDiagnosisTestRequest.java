package Model;

/**
 * Created by Narendra on 17-03-2016.
 */
public class AddDiagnosisTestRequest {

    private String patientTestId;
    private String doctorInstruction;
    private String reminder;
    private String testName;
    private String datetime;
    private String appointmentId;
    private String clinicId;
    private String referredId;
    private String testId;
    private String userType;
    private String loggedinUserId;

    /**
     *
     * @return
     * The patientTestId
     */
    public String getPatientTestId() {
        return patientTestId;
    }

    /**
     *
     * @param patientTestId
     * The patientTestId
     */
    public void setPatientTestId(String patientTestId) {
        this.patientTestId = patientTestId;
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
     * The testName
     */
    public String getTestName() {
        return testName;
    }

    /**
     *
     * @param testName
     * The testName
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     *
     * @return
     * The datetime
     */
    public String getDatetime() {
        return datetime;
    }

    /**
     *
     * @param datetime
     * The datetime
     */
    public void setDatetime(String datetime) {
        this.datetime = datetime;
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
     * The clinicId
     */
    public String getClinicId() {
        return clinicId;
    }

    /**
     *
     * @param clinicId
     * The clinicId
     */
    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    /**
     *
     * @return
     * The referredId
     */
    public String getReferredId() {
        return referredId;
    }

    /**
     *
     * @param referredId
     * The referredId
     */
    public void setReferredId(String referredId) {
        this.referredId = referredId;
    }

    /**
     *
     * @return
     * The testId
     */
    public String getTestId() {
        return testId;
    }

    /**
     *
     * @param testId
     * The testId
     */
    public void setTestId(String testId) {
        this.testId = testId;
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

}