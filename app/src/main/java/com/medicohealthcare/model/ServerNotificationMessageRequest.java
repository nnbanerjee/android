package com.medicohealthcare.model;

/**
 * Created by Narendra on 14-07-2017.
 */

public class ServerNotificationMessageRequest extends ServerResponseStatus
{
    public int profileId;
    public int days;

    public ServerNotificationMessageRequest(int profileId, int days)
    {
        this.profileId = profileId;
        this.days = days;
    }
}
