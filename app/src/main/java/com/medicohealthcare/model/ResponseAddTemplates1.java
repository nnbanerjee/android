package com.medicohealthcare.model;

/**
 * Created by Narendra on 15-04-2016.
 */
public class ResponseAddTemplates1 {
    //{"treatmentId":13,"status":1,"invoiceId":5}
    private Integer treatmentId;
    private Integer status;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer invoiceId;


}
