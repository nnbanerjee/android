package com.medico.view.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.medico.application.MyApi;
import com.medico.util.ServerConnectionAdapter;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity //implements FragmentManager.OnBackStackChangedListener
{

    public MyApi api;
    private ProgressDialog progress;
    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;
        api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
        progress = new ProgressDialog(this);
        progress.setMessage("Loading, please wait....");
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


    public void showBusy()
    {
        progress.show();
    }
    public void hideBusy()
    {
        progress.dismiss();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        { //Back key pressed
            onBackPressed();
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }
}
