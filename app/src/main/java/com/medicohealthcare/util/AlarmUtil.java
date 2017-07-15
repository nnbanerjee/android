package com.medicohealthcare.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.medicohealthcare.model.AlarmNotification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Narendra on 10-07-2017.
 */

public class AlarmUtil
{
    private static final String sTagAlarms = ":alarms";

    public static void addAlarm(Context context, Intent intent, AlarmNotification notification, Calendar calendar) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)notification.notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        saveAlarmId(context, notification);
    }
    public static void addAlarm(Context context, Intent intent, int notificationId, Calendar calendar) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, Intent intent, int notificationId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        removeAlarmId(context, notificationId);
    }

    public static void cancelAllAlarms(Context context, Intent intent)
    {
        List<AlarmNotification> idAlarms = getAlarmIds(context);
        if(idAlarms != null && idAlarms.size() > 0)
        {
            for (AlarmNotification idAlarm : idAlarms)
            {
                cancelAlarm(context, intent, idAlarm.notificationId);
            }
        }
    }

    public static boolean hasAlarm(Context context, Intent intent, int notificationId) {
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private static void saveAlarmId(Context context, AlarmNotification id) {
        List<AlarmNotification> idsAlarms = getAlarmIds(context);
        boolean found = false;
        if (idsAlarms != null)
        {
            for(AlarmNotification notification: idsAlarms)
            {
                if(notification.notificationId == id.notificationId)
                {
                    notification.merge(id);
                    found = true;
                }
            }
        }
        else
            idsAlarms = new ArrayList<AlarmNotification>();

        if(!found)
            idsAlarms.add(id);

        saveIdsInPreferences(context, idsAlarms);
    }

    private static void removeAlarmId(Context context, int id)
    {
        List<AlarmNotification> idsAlarms = getAlarmIds(context);
        if(idsAlarms != null && idsAlarms.size() > 0)
        {
            for (int i = 0; i < idsAlarms.size(); i++)
            {
                if (idsAlarms.get(i).notificationId == id)
                    idsAlarms.remove(i);
            }

            saveIdsInPreferences(context, idsAlarms);
        }
    }

    public static List<AlarmNotification> getAlarmIds(Context context)
    {
        AlarmNotification[] notification = null;
        try
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String alarms = prefs.getString(context.getPackageName() + sTagAlarms, null);
             notification = new Gson().fromJson(alarms, AlarmNotification[].class);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(notification != null && notification.length > 0)
        {
            List<AlarmNotification> list = new ArrayList<>();
            for (int i = 0; i < notification.length; i++)
                list.add(notification[i]);
            return list;
        }
        return null;
    }

    private static void saveIdsInPreferences(Context context, List <AlarmNotification> notification)
    {
        Gson gson = new Gson();
        String json = gson.toJson(notification);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + sTagAlarms, json);
        editor.apply();
    }
}
