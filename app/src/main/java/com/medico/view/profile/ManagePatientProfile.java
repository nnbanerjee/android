package com.medico.view.profile;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.view.ParentActivity;
import com.medico.view.ParentFragment;

public class ManagePatientProfile extends ParentActivity {
    private static final int CONTENT_VIEW_ID = 10101010;

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
//    public void onBackPressed() {
//
//        if(fragmentList.size() > 1)
//        {
//
//            ParentFragment fragment = fragmentList.get(fragmentList.size() - 1);
//            if(fragment instanceof PatientMedicinReminder)
//            {
//                fragmentList.remove(fragmentList.size()-1);
//                FragmentManager fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().detach(fragment).commit();
//
//            }
//            else if(fragment instanceof DoctorAppointmentSummary)
//            {
//                fragmentList.remove(fragmentList.size()-1);
//                fragment = fragmentList.get(fragmentList.size() - 1);
//                fragmentList.remove(fragment);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(fragment).commit();
//            }
//            else
//            {
//                fragment = fragmentList.get(fragmentList.size() - 1);
//                fragmentList.remove(fragment);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(fragment).commit();
//            }
//
//            fragment.setHasOptionsMenu(false);
//            fragmentList.get(fragmentList.size() - 1).setHasOptionsMenu(true);
//            fragmentList.get(fragmentList.size()-1).onStart();
//
//        }
//    }


    protected void attachView()
    {
        ParentFragment fragment = new PatientProfileListView();
        attachFragment(fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.service, fragment).addToBackStack(null).commit();
    }

}