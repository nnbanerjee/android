package com.medicohealthcare.service;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Narendra on 14-07-2017.
 */

public class RemoteNotification
{
    public static final int TYPE_STACK = -1000;

    public Context getContext()
    {
        return context;
    }

    private Context context;
    protected Bundle mExtrasBundle;
    protected int mUserNotificationId = -1;

    protected RemoteNotification(Context context)
    {
        this.context = context;
    }

    public RemoteNotification(Context context, Bundle bundle)
    {
        this.context = context;
        mExtrasBundle = bundle;
        mUserNotificationId = (int)(System.currentTimeMillis() / 1000);
    }

    public Bundle getBundle() {
        if (mExtrasBundle == null) {
            mExtrasBundle = new Bundle();
        }
        return mExtrasBundle;
    }

    public String getAppName() {
        return getBundle().getString("app_name");
    }

    public String getErrorName() {
        return getBundle().getString("error_name");
    }

    public String getActivityText() {
        return getBundle().getString("activity_text");
    }

    public String getUrl() {
        return getBundle().getString("url");
    }

    public String getUserNotificationGroup() {
        // error URLs are unique, and can be used to group
        // related activities in the notification drawer
        return getUrl();
    }

    public int getUserNotificationId() {
        return mUserNotificationId;
    }
}
