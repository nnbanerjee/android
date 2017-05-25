package com.medicohealthcare.model;

import retrofit.mime.TypedFile;

/**
 * Created by User on 7/9/15.
 */
public class RegistrationFile {

    public Integer fileId;
    public String fileName;
    public Byte type;
    public String url;
    public Integer personId;
    public transient TypedFile file;

    public boolean canBeSaved()
    {
        boolean canBeSaved = true;
        if(fileName == null )
            canBeSaved = false;
        if(type == null )
            canBeSaved = false;
        if(personId == null )
            canBeSaved = false;
        if(file == null )
            canBeSaved = false;
        return canBeSaved;
    }

}
