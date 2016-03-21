package Model;

/**
 * Created by Narendra on 14-03-2016.
 */
public class AppointmentStatus {
    //{"appointmentId":587,"appointmentStatus":"1"}

    private String appointmentId;

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentStatus(String appointmentId, String appointmentStatus) {
        this.appointmentId = appointmentId;
        this.appointmentStatus = appointmentStatus;
    }

    private String appointmentStatus;
}
