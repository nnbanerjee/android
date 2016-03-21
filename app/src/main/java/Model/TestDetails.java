package Model;

/**
 * Created by Narendra on 17-03-2016.
 */
public class TestDetails {
    private String doctorInstruction;
    private String reminder;
    private String testName;
    private String datetime;
    private String patientTestId;
    private String appointmentId;
    private Clinic clinic;
    private String referredId;
    private Test test;

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
     * The test
     */
    public Test getTest() {
        return test;
    }

    /**
     *
     * @param test
     * The test
     */
    public void setTest(Test test) {
        this.test = test;
    }
}
