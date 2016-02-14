package Model;

/**
 * Created by mindnerves on 5/11/15.
 */
public class ManageFinance {

    private String appointmentDate;
    private String totalInvoice;
    private Integer totalCount;
    private String grandTotal;
    private String totalDue;

    public ManageFinance(){

    }

    public ManageFinance(String appointmentDate, String totalInvoice, Integer totalCount){
        this.appointmentDate = appointmentDate;
        this.totalInvoice = totalInvoice;
        this.totalCount = totalCount;
    }

    public ManageFinance(String appointmentDate, String totalInvoice, Integer totalCount, String grandTotal, String totalDue){
        this.appointmentDate = appointmentDate;
        this.totalInvoice = totalInvoice;
        this.totalCount = totalCount;
        this.grandTotal = grandTotal;
        this.totalDue = totalDue;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate){
        this.appointmentDate = appointmentDate;
    }

    public String getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(String totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String totalDue) {
        this.totalDue = totalDue;
    }
}
