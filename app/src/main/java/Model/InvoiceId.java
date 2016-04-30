package Model;

/**
 * Created by Narendra on 02-04-2016.
 */
public class InvoiceId {
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    private String invoiceId;
}
