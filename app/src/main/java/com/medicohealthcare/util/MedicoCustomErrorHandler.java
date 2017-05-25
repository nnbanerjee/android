package com.medicohealthcare.util;

import android.content.Context;
import android.util.Log;

import com.medicohealthcare.model.ErrorResponse;
import com.medicohealthcare.application.R;

import retrofit.RetrofitError;

/**
 * Created by Narendra on 26-02-2016.
 */
public  class MedicoCustomErrorHandler implements retrofit.ErrorHandler {
    private final Context ctx;

    public MedicoCustomErrorHandler(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        String errorDescription;

        if (cause.isNetworkError()) {
            errorDescription = ctx.getString(R.string.error_network);
        } else {
            if (cause.getResponse() == null) {
                errorDescription = ctx.getString(R.string.error_no_response);
            } else {

                // Error message handling - return a simple error to Retrofit handlers..
                try {
                    ErrorResponse errorResponse = (ErrorResponse) cause.getBodyAs(ErrorResponse.class);
                    errorDescription = errorResponse.getErrorCode();
                    if(errorDescription.equalsIgnoreCase("233")){
                        errorDescription=ctx.getString(R.string.error_no_data_found);
                    }

                } catch (Exception ex) {
                    try {
                        errorDescription = ctx.getString(R.string.error_network_http_error, cause.getResponse().getStatus());
                    } catch (Exception ex2) {
                        Log.e("MedicoCustomErrorHandle", "handleError: " + ex2.getLocalizedMessage());
                        errorDescription = ctx.getString(R.string.error_unknown);
                    }
                }
            }
        }

        return new Exception(errorDescription);
    }
}
