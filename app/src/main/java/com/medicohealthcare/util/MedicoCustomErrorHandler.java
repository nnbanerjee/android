package com.medicohealthcare.util;

import android.content.Context;
import android.widget.Toast;

import com.medicohealthcare.model.ServerResponse;

import retrofit.RetrofitError;

/**
 * Created by Narendra on 26-02-2016.
 */
public  class MedicoCustomErrorHandler implements retrofit.ErrorHandler
{
    private Context ctx;
    private boolean messageRequired = true;

    public static String DATA_NOT_FOUND = "201";
    public static String SESSION_EXPIRED = "401";
    public static String UNFORMATTED_REQUEST = "400";

    public MedicoCustomErrorHandler(Context ctx)
    {
        this.ctx = ctx;
    }
    public MedicoCustomErrorHandler(Context ctx, boolean messageRequired)
    {
        this.ctx = ctx;
        this.messageRequired = messageRequired;
    }

    @Override
    public Throwable handleError(RetrofitError cause)
    {
        String errorDescription = null;

        if (cause.getKind() == RetrofitError.Kind.NETWORK)
        {
            errorDescription = "Communication Problem, please check connection";
        }
        else if (cause.getKind() == RetrofitError.Kind.CONVERSION)
        {
            ServerResponse response = ((MedicoConversionException)cause.getCause()).getErrorResponse();
            if( response.errorCode != null)
            {
                if( response.errorCode.equalsIgnoreCase(DATA_NOT_FOUND))
                    errorDescription = "No data available";
                else if(response.errorCode.equalsIgnoreCase(SESSION_EXPIRED))
                    errorDescription = "Session expired, please relogin";
                else if(response.errorCode.equalsIgnoreCase(UNFORMATTED_REQUEST))
                    errorDescription = "Invalid request, kindly check";
                else
                    errorDescription = "Unknown Error occured, please try after sometime";
            }
            else if(response.status == 0)
            {
                errorDescription = "Error occured, please try after sometime";
            }
        }
        else if (cause.getKind() == RetrofitError.Kind.HTTP)
        {
            Toast.makeText(ctx, "Network Problem, please try later", Toast.LENGTH_LONG).show();
        }
        else if (cause.getKind() == RetrofitError.Kind.UNEXPECTED)
        {
            Toast.makeText(ctx, "Unexpected error, please try later", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(ctx, errorDescription, Toast.LENGTH_LONG).show();
        return new Exception(errorDescription);
    }
}
