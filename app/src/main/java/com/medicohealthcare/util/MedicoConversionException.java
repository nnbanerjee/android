package com.medicohealthcare.util;

import com.medicohealthcare.model.ServerResponse;

import retrofit.converter.ConversionException;

/**
 * Created by Narendra on 31-05-2017.
 */

public class MedicoConversionException extends ConversionException
{
    private ServerResponse response;

    public MedicoConversionException(ServerResponse response, ConversionException conversionException)
    {
        super(conversionException);
        this.response = response;
    }
    public ServerResponse getErrorResponse()
    {
        return response;
    }
}
