package com.medico.model;

import retrofit.mime.TypedFile;

/**
 * Created by User on 7/9/15.
 */
public class FileUpload1 {

    public Integer fileId;
    public Byte category;
    public Integer subcategory;
    public Long date;
    public String fileName;
    public Byte type;
    public String url;
    public Integer appointmentId;
    public Integer patientId;
    public Integer personId;
    public String personName;
    public Integer clinicId;
    public String clinicName;
    public transient TypedFile file;

    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(category == null )
            canBeSaved = false;
        if(subcategory == null )
            canBeSaved = false;
        if(date == null )
            canBeSaved = false;
        if(fileName == null )
            canBeSaved = false;
        if(type == null )
            canBeSaved = false;
        if(appointmentId == null )
            canBeSaved = false;
        if(patientId == null )
            canBeSaved = false;
        if(personId == null )
            canBeSaved = false;
        if(clinicId == null )
            canBeSaved = false;
        if(file == null )
            canBeSaved = false;
        return canBeSaved;
    }

}
