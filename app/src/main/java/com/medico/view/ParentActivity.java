package com.medico.view;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 06-04-2017.
 */

public class ParentActivity extends AppCompatActivity
{

    public List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                if (fragmentList.size() > 1) {
                    onBackPressed();
                    return true;
                }
                else
                {
                    return false;
                }


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public void attachFragment(ParentFragment fragment)
    {
        fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(false);
        fragmentList.add(fragment);
    }
    public void deattachFragment(ParentFragment fragment)
    {
//        fragment.setHasOptionsMenu(false);
        fragmentList.remove(fragment);
//        fragmentList.get(fragmentList.size()-1).setHasOptionsMenu(true);
    }
}
