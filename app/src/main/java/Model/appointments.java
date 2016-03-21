package Model;


public class appointments{

    private String appointmentId;
    private String dateTime;

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

}