package com.medicohealthcare.util;

import android.app.Activity;
import android.widget.Toast;

import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.view.home.HomeActivity;

import retrofit.RetrofitError;

/**
 * Created by Narendra on 26-02-2016.
 */
public  class MedicoCustomErrorHandler implements retrofit.ErrorHandler
{
    private Activity ctx;
    private boolean messageRequired = true;

    public static String DATA_NOT_FOUND = "201";
    public static String SESSION_EXPIRED = "401";
    public static String UNFORMATTED_REQUEST = "400";

    public MedicoCustomErrorHandler(Activity ctx)
    {
        this.ctx = ctx;
    }
    public MedicoCustomErrorHandler(Activity ctx, boolean messageRequired)
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
                    if(messageRequired)
                        errorDescription = "No data available";
                    else
                        return cause;
                else if(response.errorCode.equalsIgnoreCase(SESSION_EXPIRED))
                {
                    errorDescription = "Session expired";
                    Toast.makeText(ctx, errorDescription, Toast.LENGTH_LONG).show();
                    ctx.finish();
                    if(HomeActivity.getParentAtivity() != null)
                         HomeActivity.getParentAtivity().finish();

                }
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
        if(errorDescription != null)
        {
            Toast.makeText(ctx, errorDescription, Toast.LENGTH_LONG).show();
            return new Exception(errorDescription);
        }
        return cause;
    }
}
