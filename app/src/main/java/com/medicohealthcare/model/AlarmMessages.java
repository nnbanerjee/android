package com.medicohealthcare.model;

import java.util.List;

/**
 * Created by Narendra on 10-07-2017.
 */

public class AlarmMessages extends ServerResponseStatus
{
    List<AlarmNotification> notifications;
    List<DoctorClinicQueueStatus> queueStatus;
    List<ChatMessageCounts> chatMessageCounts;

    public List<DoctorClinicQueueStatus> getQueueStatus()
    {
        return queueStatus;
    }

    public void setQueueStatus(List<DoctorClinicQueueStatus> queueStatus)
    {
        this.queueStatus = queueStatus;
    }

    public List<AlarmNotification> getNotifications()
    {
        return notifications;
    }

    public void setPatientDiagnostics(List<AlarmNotification> notifications)
    {
        this.notifications = notifications;
    }

    public List<ChatMessageCounts> getChatMessageCounts()
    {
        return chatMessageCounts;
    }

    public void setChatMessageCounts(List<ChatMessageCounts> chatMessageCounts)
    {
        this.chatMessageCounts = chatMessageCounts;
    }


}
