package com.medicohealthcare.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import com.medicohealthcare.application.R;
import com.medicohealthcare.util.WakeLocker;

import java.util.ArrayList;

/**
 * Created by Narendra on 14-07-2017.
 */

public final class RemoteNotificationListener //extends GcmListenerService
{
    protected NotificationManager mNotificationManager;

//    @Override
//    public void onMessageReceived(String from, Bundle data) {
//        // perform any necessary verification before displaying
//        sendNotification(new RemoteNotification(this,data));
//    }

    public void sendNotification(RemoteNotification remoteNotification) {
        // handle building and sending a normal notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(remoteNotification.getContext());
        // perform other configuration ...
        builder.setContentTitle(remoteNotification.getActivityText());
        // set the group, this is important for later
        builder.setGroup(remoteNotification.getUserNotificationGroup());
        Notification builtNotification = builder.build();

        // deliver the standard notification
        getNotificationManagerService(remoteNotification.getContext()).notify(remoteNotification.getUserNotificationGroup(), remoteNotification.getUserNotificationId(), builtNotification);

        // pass our remote notification through to deliver a stack notification
        sendStackNotificationIfNeeded(remoteNotification);
    }

    protected NotificationManager getNotificationManagerService(Context context) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    // other methods
    private void sendStackNotificationIfNeeded(RemoteNotification remoteNotification) {
        // only run this code if the device is running 23 or better
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<StatusBarNotification> groupedNotifications = new ArrayList<>();

            // step through all the active StatusBarNotifications and
            for (StatusBarNotification sbn : getNotificationManagerService(remoteNotification.getContext()).getActiveNotifications()) {
                // add any previously sent notifications with a group that matches our RemoteNotification
                // and exclude any previously sent stack notifications
                if (remoteNotification.getUserNotificationGroup() != null &&
                        remoteNotification.getUserNotificationGroup().equals(sbn.getNotification().getGroup()) &&
                        sbn.getId() != RemoteNotification.TYPE_STACK) {
                    groupedNotifications.add(sbn);
                }
            }

            // since we assume the most recent notification was delivered just prior to calling this method,
            // we check that previous notifications in the group include at least 2 notifications
            if (groupedNotifications.size() > 1) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(remoteNotification.getContext());

                // use convenience methods on our RemoteNotification wrapper to create a title
                builder.setContentTitle(String.format("%s: %s", remoteNotification.getAppName(), remoteNotification.getErrorName()))
                        .setContentText(String.format("%d new activities", groupedNotifications.size()));

                // for every previously sent notification that met our above requirements,
                // add a new line containing its title to the inbox style notification extender
                NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();
                {
                    for (StatusBarNotification activeSbn : groupedNotifications) {
                        String stackNotificationLine = (String)activeSbn.getNotification().extras.get(NotificationCompat.EXTRA_TITLE);
                        if (stackNotificationLine != null) {
                            inbox.addLine(stackNotificationLine);
                        }
                    }

                    // the summary text will appear at the bottom of the expanded stack notification
                    // we just display the same thing from above (don't forget to use string
                    // resource formats!)
                    inbox.setSummaryText(String.format("%d new activities", groupedNotifications.size()));
                }
                builder.setStyle(inbox);

                // make sure that our group is set the same as our most recent RemoteNotification
                // and choose to make it the group summary.
                // when this option is set to true, all previously sent/active notifications
                // in the same group will be hidden in favor of the notifcation we are creating
                builder.setGroup(remoteNotification.getUserNotificationGroup())
                        .setGroupSummary(true);

                // if the user taps the notification, it should disappear after firing its content intent
                // and we set the priority to high to avoid Doze from delaying our notifications
                builder.setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                // create a unique PendingIntent using an integer request code.
                final int requestCode = (int)System.currentTimeMillis() / 1000;
//                builder.setContentIntent(PendingIntent.getActivity(remoteNotification.getContext(), requestCode, remoteNotification.getContext().cre, PendingIntent.FLAG_ONE_SHOT));

                Notification stackNotification = builder.build();
                stackNotification.defaults = Notification.DEFAULT_ALL;

                // finally, deliver the notification using the group identifier as the Tag
                // and the TYPE_STACK which will cause any previously sent stack notifications
                // for this group to be updated with the contents of this built summary notification
                getNotificationManagerService(remoteNotification.getContext()).notify(remoteNotification.getUserNotificationGroup(), RemoteNotification.TYPE_STACK, stackNotification);
            }
        }
        else
        {
            WakeLocker.acquire(remoteNotification.getContext());
            Vibrator v = (Vibrator) remoteNotification.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(5000);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(remoteNotification.getContext()).
                    setSmallIcon(R.drawable.logo_icon).setSound(notification).
                    setContentTitle("Reminder").setGroup("Mygroup");
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            String[] events = remoteNotification.getBundle().getStringArray("names");
            inboxStyle.setBigContentTitle(remoteNotification.getBundle().getString("app_name"));
            for (int i=0; i < events.length; i++) {
                inboxStyle.addLine(events[i]);
            }
            mBuilder.setStyle(inboxStyle);
            NotificationManager mNotificationManager =(NotificationManager)remoteNotification.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(101, mBuilder.build());
        }
    }
}
