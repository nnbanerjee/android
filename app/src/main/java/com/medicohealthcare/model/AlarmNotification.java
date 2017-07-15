package com.medicohealthcare.model;

import java.util.List;

/**
 * Created by Narendra on 10-07-2017.
 */

public class AlarmNotification
{
    public Long notificationTime;
    public int notificationId;
    public List<AlarmTopic> topics;

    public class AlarmTopic
    {
        public int type;
        public String name;
        public String description;
        public int id;

        public boolean equals(AlarmTopic topic)
        {
            if(name.equals(topic.name))
                return true;
            else
                return false;
        }
    }

    public boolean merge(AlarmNotification notification)
    {
        boolean mergedSuccessfilly = false;
        if(notificationId == notification.notificationId)
        {
            for(AlarmTopic topic: notification.topics)
            {
                boolean found = false;
                for(AlarmTopic topic1: topics)
                {
                    if(topic.equals(topic1))
                    {
                        found = true;
                        mergedSuccessfilly = true;
                        break;
                    }
                }
                if(!found)
                    topics.add(topic);
            }
        }
        return mergedSuccessfilly;
    }
}
