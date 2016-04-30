package Model;

/**
 * Created by Narendra on 15-04-2016.
 */
public class ResponseAddTemplates {
    //{"treatmentId":13,"status":1,"invoiceId":5}
    private String treatmentId;
    private String status;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String invoiceId;


}
