package com.medicohealthcare.service;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by Narendra on 04-08-2017.
 */

public class ExternalTopicReceiver extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(com.google.firebase.messaging.RemoteMessage remoteMessage)
    {
        // perform any necessary verification before displaying
//        sendNotification(new RemoteNotification(this,data));
        Toast.makeText(this,"I am here", Toast.LENGTH_LONG).show();
    }
}
