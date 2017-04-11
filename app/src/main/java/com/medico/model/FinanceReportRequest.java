package com.medico.model;

/**
 * Created by Narendra on 10-04-2017.
 */

public class FinanceReportRequest
{
    public Integer doctorId;
    public Integer typeOfReport;
    public Long financeDate;
    public int page = 1;
    public int rows = 100;

    public FinanceReportRequest(Integer doctorId,int typeOfReport)
    {
        this.doctorId = doctorId;
        this.typeOfReport = typeOfReport;
    }
    public FinanceReportRequest(Integer doctorId,Long financeDate,int typeOfReport)
    {
        this.doctorId = doctorId;
        this.typeOfReport = typeOfReport;
        this.financeDate = financeDate;
    }
}
