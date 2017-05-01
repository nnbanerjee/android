package com.medico.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.MenuAdapter;
import com.medico.application.R;
import com.medico.model.PatientId;
import com.medico.model.PatientProfile;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Narendra on 18-01-2017.
 */

public class PatientHome extends HomeActivity
{



    protected void createView()
    {
        parent_activity = this;
        setContentView(R.layout.patient_home);

        //profile layout
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView) findViewById(R.id.account_name);
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(PatientHome.this, p);
            }
        });
        arrow = (View) findViewById(R.id.down_arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(PatientHome.this, p);
            }
        });
        status = (TextView) findViewById(R.id.status_text);

        if(parent != null)
            status.setText("Dependent Profile");
        else
            status.setText("Patient Profile");

        //tool bar construction
        LinearLayout layout = (LinearLayout) findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        notes = (Button) findViewById(R.id.alarm_notes);
        messages = (Button) findViewById(R.id.chat);
//        patients = (Button) findViewById(R.id.patient_ping);
        clinicSearch = (Button) findViewById(R.id.search_clinic);
        doctorSearch = (Button) findViewById(R.id.search_doctor);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(PatientHome.this, p);
            }
        });
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(PatientHome.this, p);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (profileRole == DOCTOR) {
//                    fragment = new ManageMessageNotification();
//                    fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
//                } else if (profileRole == PATIENT) {
//                    fragment = new ManageMessageNotification();
//                    fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
//                }
            }
        });

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        dList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        dList.setDividerHeight(1);
        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.DOCTOR_SETTING_VIEW);
                Intent intObj = new Intent(PatientHome.this, ManageHomeView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        clinicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.DOCTOR_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.CLINIC_SETTING_VIEW);
                Intent intObj = new Intent(PatientHome.this, ManageHomeView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        drawerButton = (Button) findViewById(R.id.drawar_button);
        drawerButton.setVisibility(View.VISIBLE);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("I am here");
                System.out.println("FLag button value " + flagActionButton);
                if (flagActionButton == 0) {
                    System.out.println("open");
                    dLayout.openDrawer(dList);
                    flagActionButton = 1;
                } else {
                    System.out.println("closed");
                    dLayout.closeDrawer(Gravity.LEFT);
                    flagActionButton = 0;

                }
            }
        });
        fragment = new PatientMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
    }

    public void showMenus()
    {

        arrayMenu = mainMenu.getPatientMenus();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(this, "", getResources().getString(R.string.loading_wait));
        PatientId param = new PatientId(profileId);
        api.getPatientLandingPageDetails(param, new Callback<PatientProfile>() {
            @Override
            public void success(PatientProfile patient, Response response)
            {
                personProfile = patient;
                if(parent != null ) personProfile.setDependentProfile(true);
                if (patient != null && patient.getPerson() != null && patient.getPerson().getImageUrl() != null)
                    new ImageLoadTask(patient.getPerson().getImageUrl(), profilePicture).execute();
                accountName.setText(patient.getPerson().getName());
                adapter = new MenuAdapter(PatientHome.this, arrayMenu, profileRole, patient.getPerson().getImageUrl());//(new MenuAdapter(this,arrayMenu))
                System.out.println("Adapter Values " + adapter.getCount());
                dList.setAdapter(adapter);
                ((PatientMenusManage) fragment).updateCounts(patient);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                error.printStackTrace();
                Toast.makeText(PatientHome.this, R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void showPopup(final Activity context, Point p) {

        super.showPopup(context, p);

    }
}
