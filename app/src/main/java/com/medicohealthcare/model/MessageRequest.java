package com.medicohealthcare.model;

/**
 * Created by Narendra on 15-05-2017.
 */

public class MessageRequest
{
    public Integer recipientId;
    public Integer senderId;
    public Integer latestNoOfMessages;
    public Integer endMessageId;
    public Integer pageId;

    public MessageRequest(Integer receipientId, Integer senderId, Integer pageId, Integer numberOfMessages)
    {
        this.recipientId = receipientId;
        this.senderId = senderId;
        this.pageId = pageId;
        this.latestNoOfMessages = numberOfMessages;
    }

    public MessageRequest(Integer receipientId)
    {
        this.recipientId = receipientId;
    }
    public MessageRequest(Integer receipientId, Integer senderId, Integer endMessageId)
    {
        this.recipientId = receipientId;
        this.senderId = senderId;
        this.endMessageId = endMessageId;
    }
}
