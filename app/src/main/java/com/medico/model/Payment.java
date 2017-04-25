package com.medico.model;

import java.math.BigDecimal;

/**
 * Created by Narendra on 25-04-2017.
 */

public class Payment
{
    public Integer paymentId;
    public BigDecimal amount;
    public Long date;
    public Byte modeOfPayment;
    public Byte paymentType;
    public String transactionDetails;
    public String country;
    public Integer invoiceId;
    public Integer paidBy;
    public Integer doctorId;
    public Integer patientId;

    public Payment(BigDecimal amount,Long date, Byte modeOfPayment,
                   Byte paymentType, String transactionDetails, String country,
                   Integer invoiceId, Integer paidBy, Integer doctorId, Integer patientId)
    {
        this.amount = amount;
        this.date = date;
        this.modeOfPayment = modeOfPayment;
        this.paymentType = paymentType;
        this.transactionDetails = transactionDetails;
        this.country = country;
        this.invoiceId = invoiceId;
        this.paidBy = paidBy;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }
}
