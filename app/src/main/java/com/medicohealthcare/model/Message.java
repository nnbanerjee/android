package com.medicohealthcare.model;

import java.util.Date;

/**
 * Created by Narendra on 15-05-2017.
 */

public class Message
{
    public Integer messageId;
    public Long date;
    public Byte isRead;
    public String message;
    public Integer senderId;
    public Integer recipientId;

    public Message(String message, Integer senderId, Integer recipientId)
    {
        this.message = message;
        this.senderId = senderId;
        this.recipientId = recipientId;
        date = new Date().getTime();
        isRead = (byte)(0);
    }
}
