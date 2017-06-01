package com.medicohealthcare.model;

import com.medicohealthcare.util.PARAM;

/**
 * Created by Narendra on 16-03-2017.
 */

public class ServerResponse
{
    public Integer status;
    public String errorCode;
    public Integer profileId;
    public Integer messageId;


    public String getErrorMessage()
    {
        String errorDescription = "";

        if( errorCode != null)
        {
            if( errorCode.equalsIgnoreCase(PARAM.DATA_NOT_FOUND))
                errorDescription = "No data available";
            else if(errorCode.equalsIgnoreCase(PARAM.SESSION_EXPIRED))
                errorDescription = "Session expired, please relogin";
            else if(errorCode.equalsIgnoreCase(PARAM.UNFORMATTED_REQUEST))
                errorDescription = "Invalid request, kindly check";
            else
                errorDescription = "Unknown Error occured, please try after sometime";
        }
        else if(status == 0)
        {
            errorDescription = "Error occured, please try after sometime";
        }

        return errorDescription;
    }
}
