package com.medico.view;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medico.util.PARAM;
import com.medico.view.settings.ManagePatientListView;
import com.medico.application.R;

import java.util.ArrayList;
import java.util.List;

public class ManagePersonSettings extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    public List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_profile);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.service);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {

            attachView();

        }


        final ActionBar abar = getSupportActionBar();
//        abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.toolbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        viewActionBar.setBackgroundColor(0xFF206799);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Patient Profile");
        abar.setBackgroundDrawable((new ColorDrawable(Color.parseColor("#FF206799"))));
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
//        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
    @Override
    public void onBackPressed() {

        if(fragmentList.size() > 1)
        {

            ParentFragment fragment = fragmentList.get(fragmentList.size() - 1);
            if(fragment instanceof ManageDoctorProfile || fragment instanceof ManageDoctorDetailedProfile )
            {
//                fragmentList.remove(fragment);
//                fragment = fragmentList.get(fragmentList.size() - 1);
//                fragmentList.remove(fragment);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(fragment).commit();
                super.onBackPressed();
            }
            else
            {
                fragment = fragmentList.get(fragmentList.size() - 1);
                fragmentList.remove(fragment);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fragment).commit();
            }

            fragment.setHasOptionsMenu(false);
            fragmentList.get(fragmentList.size() - 1).setHasOptionsMenu(true);
            fragmentList.get(fragmentList.size()-1).onStart();

        }
        if(fragmentList.size() > 1 == false)
            super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
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

    protected void attachView()
    {
        Bundle bundle = getIntent().getExtras();
        int viewId = bundle.getInt(PARAM.SETTING_VIEW_ID);
        switch (viewId) {
            case PARAM.MANAGE_DOCTOR_PROFILE_VIEW:
                ParentFragment fragment = new DoctorProfileEdit();
                fragmentList.add(fragment);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fragment).addToBackStack(null).commit();
                break;
            case PARAM.PATIENT_SETTING_VIEW:
                ParentFragment patientListView = new ManagePatientListView();
                fragmentList.add(patientListView);
                FragmentTransaction fft = getFragmentManager().beginTransaction();
                fft.add(R.id.service, patientListView).addToBackStack(null).commit();
                break;

        }
    }

}
