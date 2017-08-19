package com.medicohealthcare.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.medicohealthcare.application.MainActivity;

public class ExternalReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            // Instantiate a Builder object.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Bundle extras = intent.getExtras();
            Toast.makeText(context,"I am here", Toast.LENGTH_LONG).show();
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Puts the PendingIntent into the notification builder
            builder.setContentIntent(contentIntent);
            // Notifications are issued by sending them to the
            // NotificationManager system service.
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // Builds an anonymous Notification object from the builder, and
            // passes it to the NotificationManager
            mNotificationManager.notify(1, builder.build());

        }
    }
}

