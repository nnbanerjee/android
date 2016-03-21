package Model;

public class Visit {

    private String appointmentId;
    private String dateTime;
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

    /**
     *
     * @return
     * The visitType
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     *
     * @param visitType
     * The visitType
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

}