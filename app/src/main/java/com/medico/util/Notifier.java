package com.medico.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 05-04-2017.
 */

public class Notifier
{
    private List<NotifyListener> listeners = new ArrayList<>();

    public void addNotifyListeber(NotifyListener listener)
    {
        listeners.add(listener);
    }
    public void removeNotifyListeber(NotifyListener listener)
    {
        listeners.remove(listener);
    }
    public void notifyListeners(int id, Notifier source, Object parameter)
    {
        for(NotifyListener listener : listeners)
            listener.notify(id, source, parameter);
    }
}
