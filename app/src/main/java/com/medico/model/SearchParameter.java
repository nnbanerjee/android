package com.medico.model;

/**
 * Created by Narendra on 07-02-2017.
 */

public class SearchParameter
{
    String name;
    int requestType;
    int treatmentType;
    int page;
    int row;
    public SearchParameter(String name, int treatmentType, int page, int row, int type)
    {
        this.name = name;
        this.treatmentType = treatmentType;
        this.page = page;
        this.row = row;
        this.requestType = type;
    }
}
