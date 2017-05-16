package com.medico.view.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.medico.application.MyApi;
import com.medico.util.ServerConnectionAdapter;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity //implements FragmentManager.OnBackStackChangedListener
{

//    private List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
//    private Document document;
//    private int backStakeCount = 0;
    public MyApi api;
    private ProgressDialog progress;
    public static Activity activity;
//    private int targetSdkVersion;
//    private static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
//    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
//    public int identifier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;
//        document = getDocument();
//        getFragmentManager().addOnBackStackChangedListener(this);
        api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
        progress = new ProgressDialog(this);
        progress.setMessage("Loading, please wait....");
//        loadTargetSdkVersion();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                    onBackPressed();
                    return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public void goHome()
    {
        super.onBackPressed();
    }
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 1 )
        {
            FragmentManager manager = getFragmentManager();
            manager.popBackStackImmediate();
            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount()-1);
            String name = entry.getName();
            if(name != null)
            {
                Fragment fragment = manager.findFragmentByTag(name);
                fragment.onStart();
            }
        }
        else
            super.onBackPressed();
    }
    public void onBackPressed(String tag)
    {
        if(getFragmentManager().getBackStackEntryCount() > 1 )
        {
            FragmentManager manager = getFragmentManager();
            manager.popBackStackImmediate(tag,0);
            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount()-1);
            String name = entry.getName();
            if(name != null)
            {
                Fragment fragment = manager.findFragmentByTag(name);
                fragment.onStart();
            }
        }
        else
            super.onBackPressed();
    }




//    public void attachFragment(ParentFragment fragment)
//    {
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.add(fragment);
//    }
//    public void detachFragment(ParentFragment fragment)
//    {
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.remove(fragment);
//    }
//    public Fragment getParentFragment()
//    {
//        if(fragmentList.size()>0)
//            return fragmentList.get(fragmentList.size()-1);
//        else
//            return null;
//    }


//    public void onBackStackChanged()
//    {
//        FragmentManager manager = getFragmentManager();
//        if(manager.getBackStackEntryCount() > 1 && manager.getBackStackEntryCount() > backStakeCount)
//        {
//            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 2);
//            String name = entry.getName();
//            if (name != null)
//            {
//                Fragment fragment = manager.findFragmentByTag(name);
//                fragment.onPause();
//            }
//        }
//        if(manager.getBackStackEntryCount() > 0 && manager.getBackStackEntryCount() < backStakeCount)
//        {
//            FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
//            String name = entry.getName();
//            if (name != null)
//            {
//                Fragment fragment = manager.findFragmentByTag(name);
//                fragment.onStart();
//            }
//        }
//        backStakeCount = manager.getBackStackEntryCount();
//    }

    public void showBusy()
    {
        progress.show();
    }
    public void hideBusy()
    {
        progress.dismiss();
    }



//    private void loadTargetSdkVersion()
//    {
//        try
//        {
//            final PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
//            targetSdkVersion = info.applicationInfo.targetSdkVersion;
//        }
//        catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//    }
//    public boolean hasPermission(String permission)
//    {
//        String[] permissions = {permission};
//        return hasPermission(permissions);
//    }
//
//    public boolean hasPermission(String[] permissions)
//    {
//        final List<String> askPermissionsList = new ArrayList<String>();
//        List<String> requestPermission = new ArrayList<String>();
//        for(int i = 0; i < permissions.length; i++)
//        {
//            if (!selfPermissionGranted(permissions[i]))
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS))
//                    askPermissionsList.add(permissions[i]);
//                else
//                    requestPermission.add(permissions[i]);
//        }
//        if(requestPermission.size() > 0 || askPermissionsList.size() > 0)
//        {
//            if (requestPermission.size() > 0)
//                ActivityCompat.requestPermissions(this, (String[]) requestPermission.toArray(), MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            if (askPermissionsList.size() > 0)
//            {
//                final String reqPermission[] = (String[]) requestPermission.toArray();
//                // Need Rationale
//                String message = "Please grant access to " + askPermissionsList.get(0);
//                for (int i = 1; i < askPermissionsList.size(); i++)
//                    message = message + ", " + askPermissionsList.get(i);
//                showMessageOKCancel(message,
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                ActivityCompat.requestPermissions(activity, reqPermission, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//                            }
//                        });
//
//            }
//            return false;
//        }
//        return true;
//    }
//    public boolean selfPermissionGranted(String permission)
//    {
//        // For Android < Android M, self permissions are always granted.
//        boolean result = true;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//
//            if (targetSdkVersion >= Build.VERSION_CODES.M)
//            {
//                // targetSdkVersion >= Android M, we can
//                // use Context#checkSelfPermission
//                result = checkSelfPermission(permission)
//                        == PackageManager.PERMISSION_GRANTED;
//            }
//            else
//            {
//                // targetSdkVersion < Android M, we have to use PermissionChecker
//                result = PermissionChecker.checkSelfPermission(this, permission)
//                        == PermissionChecker.PERMISSION_GRANTED;
//            }
//        }
//
//        return result;
//    }
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
//    {
//        switch (requestCode)
//        {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
//            {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                }
//                else
//                {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }

            // other 'case' lines to check for other
            // permissions this app might request
//        }
//    }
}
