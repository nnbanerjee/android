package com.medico.model;

import java.math.BigDecimal;

/**
 * Created by Narendra on 24-04-2017.
 */

public class Invoice
{
    public Integer invoiceId;
    public Long invoiceDate;
    public Double discount;
    public Double otherCharges;
    public Double tax;
    public Byte type;
    public Double advance;
    public Integer doctorId;
    public Integer patientId;
    public BigDecimal total;
    public BigDecimal grandTotal;
}
