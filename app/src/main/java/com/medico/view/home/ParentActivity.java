package com.medico.view.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.medico.application.MyApi;
import com.medico.util.ServerConnectionAdapter;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener
{

    private List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
    private Document document;
    private int backStakeCount = 0;
    public MyApi api;
    private ProgressDialog progress;
//    public int identifier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        document = getDocument();
        getFragmentManager().addOnBackStackChangedListener(this);
        api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
        progress = new ProgressDialog(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
//                if (fragmentList.size() > 0) {
                    onBackPressed();
                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//

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

    public void attachFragment(ParentFragment fragment)
    {
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.add(fragment);
    }
    public void detachFragment(ParentFragment fragment)
    {
//        if(fragmentList.size()>0)
//            fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
//        fragmentList.remove(fragment);
    }
    public Fragment getParentFragment()
    {
        if(fragmentList.size()>0)
            return fragmentList.get(fragmentList.size()-1);
        else
            return null;
    }


    public void onBackStackChanged()
    {
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
    }

    public void showBusy()
    {
        progress.show();
    }
    public void hideBusy()
    {
        progress.dismiss();
    }
}
