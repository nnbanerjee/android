
package com.medicohealthcare.model;


public class InvoiceDetails1 {

    public Integer invoiceId;
    public Integer doctorId;
    public Integer patientId;
    public Long invoiceDate;
    public Double discount;
    public Double otherCharges;
    public Double tax;
    public Double advance;
    public Double total;
    public Double grandTotal;
    public Byte type;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount)
    {
        this.discount = discount;
        this.grandTotal = calculateGrandTotal();
    }
    public void setOtherCharges(Double otherCharges)
    {
        this.otherCharges = otherCharges;
        this.grandTotal = calculateGrandTotal();
    }
    public void setTax(Double tax)
    {
        this.tax = tax;
        this.grandTotal = calculateGrandTotal();
    }
    public Double getOtherCharges()
    {
        return otherCharges;
    }

    public Double getTax() {
        return tax;
    }



    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Double getTotal() {
        return total;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }




    public Double calculateTotalPlusOtherCharges()
    {
        return new Double(total + otherCharges);
    }

    public Double calculateDiscountValue()
    {
        return new Double((total + otherCharges)*discount/100);
    }
    public Double calculateTotalPlusOtherChargesAfterDiscount()
    {
        return new Double(calculateTotalPlusOtherCharges() - calculateDiscountValue());
    }
    public Double calculateTaxValue()
    {
        return new Double(calculateTotalPlusOtherChargesAfterDiscount()  * tax/100);
    }
    public Double calculateGrandTotal()
    {
        return new Double(calculateTotalPlusOtherChargesAfterDiscount()+calculateTaxValue());
    }
    public Double calculateDues()
    {
        return new Double(calculateGrandTotal() + advance);
    }

}