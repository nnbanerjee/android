package com.medico.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Narendra on 10-04-2017.
 */

public class FinanceDetails
{
    public Long date;
    public List<ProcedureSummary> procedureSummary;
    public Double totalAdvance = new Double("0");
    public Double discount;
    public Double tax;
    public Double other;


    public static class ProcedureSummary
    {
        public String procedureName;
        public BigDecimal totalCost = new BigDecimal("0");
        public BigDecimal totalDiscount = new BigDecimal("0");
        public BigDecimal totalTax = new BigDecimal("0");

    }
}
