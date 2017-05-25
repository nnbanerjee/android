package com.medicohealthcare.model;

/**
 * Created by Narendra on 16-02-2017.
 */

public class Symptom
{
    public Integer idSymptoms;
    public String description;
    public String name;
    public String url;

    public Symptom()
    {

    }
    @Override
    public String toString()
    {
        return name;
    }
}
