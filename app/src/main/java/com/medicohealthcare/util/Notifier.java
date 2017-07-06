package com.medicohealthcare.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Narendra on 05-04-2017.
 */

public class Notifier
{
    private List<NotifyListener> listeners = new ArrayList<>();

    public synchronized void addNotifyListeber(NotifyListener listener)
    {
        listeners.add(listener);
    }
    public synchronized void removeNotifyListeber(NotifyListener listener)
    {
        for(Iterator<NotifyListener> it = listeners.iterator(); it.hasNext();)
        {
            NotifyListener s = it.next();
            if(s == listener)
            {
                it.remove();
            }
        }
        listeners.remove(listener);
    }
    public synchronized void notifyListeners(int id, Notifier source, Object parameter)
    {
        for(NotifyListener listener : listeners)
            listener.notify(id, source, parameter);
    }
}
