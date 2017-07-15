/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.medicohealthcare.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.medicohealthcare.application.AlarmService;
import com.medicohealthcare.application.MyApi;
import com.medicohealthcare.model.AlarmMessages;
import com.medicohealthcare.model.AlarmNotification;
import com.medicohealthcare.model.ChatMessageCounts;
import com.medicohealthcare.model.DoctorClinicQueueStatus;
import com.medicohealthcare.model.ServerNotificationMessageRequest;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.util.ServerConnectionAdapter;
import com.medicohealthcare.view.home.HomeActivity;

import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This service pulls RSS content from a web site URL contained in the incoming Intent (see
 * onHandleIntent()). As it runs, it broadcasts its status using LocalBroadcastManager; any
 * component that wants to see the status should implement a subclass of BroadcastReceiver and
 * register to receive broadcast Intents with category = CATEGORY_DEFAULT and action
 * Constants.BROADCAST_ACTION.
 *
 */
public class AlarmServer extends IntentService implements PARAM
{
    // Used to write to the system log from this class.
    public static final String LOG_TAG = "RSSPullService";

    private MyApi api;

    AlarmMessages messagereceived;

    // Defines and instantiates an object for handling status updates.
//    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);
    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public AlarmServer()
    {

        super("AlarmService");
        ServerConnectionAdapter adapter = ServerConnectionAdapter.getServerAdapter(null);
        api = adapter.getServerAPI();
        setIntentRedelivery(true);
    }

    /**
     * In an IntentService, onHandleIntent is run on a background thread.  As it
     * runs, it broadcasts its current status using the LocalBroadcastManager.
     * @param workIntent The Intent that starts the IntentService. This Intent contains the
     * URL of the web site from which the RSS parser gets data.
     */
    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        Cursor cursor = null;

        /*
         * A block that tries to connect to the Picasa featured picture URL passed as the "data"
         * value in the incoming Intent. The block throws exceptions (see the end of the block).
         */
           // If the connection is an HTTP connection, continue
            if (api != null)
            {

                // Broadcasts an Intent indicating that processing has started.
//                mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_STARTED);

                /*
                 * Queries the content provider to see if this URL was read previously, and when.
                 * The content provider throws an exception if the URI is invalid.
                 */
                Bundle bundle = workIntent.getExtras();
                int profileId = bundle.getInt(PARAM.PROFILE_ID);

                api.getServerNotificationMessage(new ServerNotificationMessageRequest(profileId,30),new Callback<AlarmMessages>()
                {
                    @Override
                    public void success(AlarmMessages message, Response response)
                    {
                        if(message.status.intValue() == 1)
                        {
                            messagereceived = message;
                            if(message.getChatMessageCounts() != null && message.getChatMessageCounts().size() > 0)
                                notifyMessage(message.getChatMessageCounts());
                            if(message.getQueueStatus() != null && message.getQueueStatus().size() > 0)
                                notifyAppointmentStatus(message.getQueueStatus());
                            if(message.getNotifications() != null && message.getNotifications().size() > 0)
                                refreshAlarms(message.getNotifications());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
//                        error.printStackTrace();
                    }
                });
            }
    }

    private void notifyMessage(List<ChatMessageCounts> messagecount)
    {
        int[] ids = new int[messagecount.size()];
        int[] numberOfMessages = new int[messagecount.size()];
        for(int i = 0; i < ids.length; i++ )
        {
            ids[i] = messagecount.get(i).senderId;
            numberOfMessages[i] = messagecount.get(i).noOfNewMessages;
        }
        Intent localIntent = new Intent(Constants.NEW_MESSAGE_ARRIVED);
        Bundle bundle = new Bundle();
        bundle.putIntArray(Constants.NEW_MESSAGE_IDS,ids);
        bundle.putIntArray(Constants.NEW_MESSAGE_NUMBERS,numberOfMessages);
        localIntent.putExtras(bundle);
        localIntent.setAction(Constants.NEW_MESSAGE_ARRIVED);
        LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).sendBroadcast(localIntent);
    }
    private void notifyAppointmentStatus(List<DoctorClinicQueueStatus> queueStatus)
    {
        Gson gson = new Gson();
        String status = gson.toJson(queueStatus);
        Intent localIntent = new Intent(Constants.NEW_MESSAGE_ARRIVED);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CURRENT_APPOINTMENT,status);
        localIntent.putExtras(bundle);
        localIntent.setAction(Constants.APPOINTMENT_QUEUE);
        LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).sendBroadcast(localIntent);

    }
    private void refreshAlarms(List<AlarmNotification> notifications)
    {
//        Intent myIntent = new Intent(HomeActivity.getParentAtivity(), AlarmService.class);
//        List<AlarmNotification> ids = AlarmUtil.getAlarmIds(HomeActivity.getParentAtivity());
//        long current = new Date().getTime();
//        if(ids != null && ids.size() > 0)
//        {
//            for (AlarmNotification id : ids)
//            {
//                if (id.notificationTime < current)
//                    AlarmUtil.cancelAlarm(HomeActivity.getParentAtivity(), myIntent, id.notificationId);
//                else
//                {
//                    boolean found = false;
//                    for (AlarmNotification notification : notifications)
//                    {
//                        if (notification.notificationId == id.notificationId)
//                            found = true;
//                    }
//                    if (found == false)
//                        AlarmUtil.cancelAlarm(HomeActivity.getParentAtivity(), myIntent, id.notificationId);
//                }
//            }
//        }
//        for (AlarmNotification notification: notifications)
//        {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(notification.notificationTime);
//            String time = DateFormat.getDateTimeInstance().format(calendar.getTime());
//            AlarmUtil.addAlarm(HomeActivity.getParentAtivity(),myIntent,notification,calendar);
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2017,Calendar.JULY,14,17,20,00);
//        String time = DateFormat.getDateTimeInstance().format(calendar.getTime());
//        AlarmUtil.addAlarm(HomeActivity.getParentAtivity(),myIntent,333,calendar);
        // Set the alarm to start at approximately 2:00 p.m.
//        AlarmManager alarmMgr = (AlarmManager)HomeActivity.getParentAtivity().getSystemService(HomeActivity.getParentAtivity().ALARM_SERVICE);
//        Intent intent = new Intent(HomeActivity.getParentAtivity(), AlarmService.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(HomeActivity.getParentAtivity(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        setAlarm(calendar);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void setAlarm(Calendar calendar)
    {
        AlarmManager am = (AlarmManager) HomeActivity.getParentAtivity().getSystemService(HomeActivity.getParentAtivity().getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(HomeActivity.getParentAtivity(), AlarmService.class);
        long trigerTime = calendar.getTimeInMillis();
        System.out.println("trigerTime = " + trigerTime);
        System.out.println("trigerTime current = " + Calendar.getInstance().getTimeInMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.getParentAtivity(), (int) trigerTime,
                intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, trigerTime, pendingIntent);
    }
}
