package com.medicohealthcare.application;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.medicohealthcare.service.RemoteNotification;
import com.medicohealthcare.service.RemoteNotificationListener;

/**
 * Created by Narendra on 14-07-2017.
 */

public class AlarmService extends BroadcastReceiver
{
    private static int counter = 0;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = new Bundle();
        String[] events = {"Crocine 150 mg", "Calpol 200 MG", "Brufen 200mg"};
        bundle.putStringArray("names",events);
        bundle.putString("app_name","Medico Reminder");
        bundle.putString("error_name","NA");
        bundle.putString("activity_text","Crocine 500 MG");
        bundle.putString("url","www.medicohealthcareservices.com");
        new RemoteNotificationListener().sendNotification(new RemoteNotification(context,bundle));
    }
}
