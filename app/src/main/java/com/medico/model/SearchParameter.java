package com.medico.model;

/**
 * Created by Narendra on 07-02-2017.
 */

public class SearchParameter
{
    String name;
    int requestType;
    int treatmentType;
    int practiceType;
    int page;
    int rows;
    public SearchParameter(String name, int treatmentType, int page, int rows, int type)
    {
        this.name = name;
        this.treatmentType = treatmentType;
        practiceType = treatmentType;
        this.page = page;
        this.rows = rows;
        this.requestType = type;
    }
}
