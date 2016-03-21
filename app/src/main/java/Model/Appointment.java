package Model;

/**
 * Created by User on 7/1/15.
 */
public class Appointment {

    String slot = "";
    int appointmentCount = 0;
    String appointmentTime = "";
    String startTime = "";
    String appointmentType = "";
    String endTime = "";
    String appointmentStatus = "";
    PersonVM personInfo ;
    private String appointmentId;
    private String dateTime;

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    private String visitType;

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
     * The dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     *
     * @param dateTime
     * The dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public PersonVM getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonVM personInfo) {
        this.personInfo = personInfo;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
