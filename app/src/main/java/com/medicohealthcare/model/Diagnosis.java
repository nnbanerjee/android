package com.medicohealthcare.model;

/**
 * Created by Narendra on 20-04-2017.
 */

public class Diagnosis
{
    public int idDiagnosis;
    public String description;
    public String name;
    public String url;
    @Override
    public String toString()
    {
        return name;
    }
}
