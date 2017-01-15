package Model;

/**
 * Created by Narendra on 20-02-2016.
 */
public class TreatmentPlanRequest {

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setAppointmentId(String v) {
        this.appointmentId = appointmentId;
    }
    public String getAppintmentId() {
        return appointmentId;
    }


    public TreatmentPlanRequest(String appointmentId, String categoryId) {
        this.categoryId = categoryId;
        this.appointmentId = appointmentId;
    }

    private String categoryId;
    private String appointmentId;

}
