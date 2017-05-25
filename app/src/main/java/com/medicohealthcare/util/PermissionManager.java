package com.medicohealthcare.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 04-05-2017.
 */



public class PermissionManager
{
    private static PermissionManager manager;

    String[] permissions =  {Manifest.permission.WRITE_GSERVICES,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_NETWORK_STATE,
        android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.WAKE_LOCK,
        android.Manifest.permission.VIBRATE,
        android.Manifest.permission.FLASHLIGHT,
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.DISABLE_KEYGUARD,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA};

    public static String[] CAMERA =
            {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.FLASHLIGHT,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA};
    public static String[] LOCATION =
            {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE};

    private Activity activity;
    private int targetSdkVersion;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;

    public static PermissionManager getInstance(Activity activity)
    {
        if(manager == null || manager.activity != activity)
        {
            manager = new PermissionManager(activity);
        }
        return manager;
    }

    private PermissionManager(Activity activity)
    {
        this.activity = activity;
        try
        {
            final PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;

            hasPermission(permissions);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    public boolean hasPermission(String permission)
    {
        String[] permissions = {permission};
        return hasPermission(permissions);
    }

    public boolean hasPermission(String[] permissions)
    {
        final List<String> askPermissionsList = new ArrayList<String>();
        List<String> requestPermission = new ArrayList<String>();
        for(int i = 0; i < permissions.length; i++)
        {
            if (!selfPermissionGranted(permissions[i]))
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS))
                    askPermissionsList.add(permissions[i]);
                else
                    requestPermission.add(permissions[i]);
        }
        if(requestPermission.size() > 0 || askPermissionsList.size() > 0)
        {
            if (requestPermission.size() > 0)
            {
                String perm[] = new String[requestPermission.size()];
                requestPermission.toArray(perm);
                ActivityCompat.requestPermissions(activity,perm, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            if (askPermissionsList.size() > 0)
            {
                final String reqPermission[] = new String[askPermissionsList.size()];
                requestPermission.toArray(reqPermission);
                // Need Rationale
                String message = "Please grant access to " + askPermissionsList.get(0);
                for (int i = 1; i < askPermissionsList.size(); i++)
                    message = message + ", " + askPermissionsList.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                ActivityCompat.requestPermissions(activity, reqPermission, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        });

            }
            return false;
        }
        return true;
    }
    public boolean selfPermissionGranted(String permission)
    {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if (targetSdkVersion >= Build.VERSION_CODES.M)
            {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = activity.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            }
            else
            {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(activity, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
