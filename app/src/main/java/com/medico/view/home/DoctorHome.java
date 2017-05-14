package com.medico.view.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.medico.model.DoctorId;
import com.medico.model.DoctorProfile;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Narendra on 18-01-2017.
 */

public class DoctorHome extends HomeActivity
{
    public int PRACTICE_TYPE = ALLOPATHIC;

    @Override
    protected void createView()
    {

        parent_activity = this;
        setContentView(R.layout.doctor_home);
        //profile layout
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView) findViewById(R.id.account_name);
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        arrow = (View) findViewById(R.id.down_arrow);
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        status = (TextView) findViewById(R.id.status_text);
        status.setText("Doctor Profile");

        //tool bar construction
        LinearLayout layout = (LinearLayout) findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        messages = (Button) findViewById(R.id.chat);
        patients = (Button) findViewById(R.id.search_patient);
        clinicSearch = (Button) findViewById(R.id.search_clinic);

         arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        patients.setOnClickListener(new View.OnClickListener() {
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
                bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.PATIENT_SETTING_VIEW);
                Intent intObj = new Intent(DoctorHome.this, ManageHomeView.class);
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
                Intent intObj = new Intent(DoctorHome.this, ManageHomeView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        fragment = new DoctorMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        messages.setOnClickListener(new View.OnClickListener() {
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
                bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.CHAT_VIEW);
                Intent intObj = new Intent(DoctorHome.this, ManageHomeView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
        });
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        dList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        dList.setDividerHeight(1);

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
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DoctorHome.this);
                alertDialogBuilder.setMessage("R.string.confirm_logout");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Do Nothing");
                    }
                });

                AlertDialog alertDialog2 = alertDialogBuilder.create();
                alertDialog2.show();
            }
        });

        dList.setSelector(android.R.color.holo_blue_dark);
    }
    @Override
    protected void setParameters()
    {
        session = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        profileId = session.getInt(LOGGED_IN_ID,PATIENT); 
        profileRole = session.getInt(LOGGED_IN_USER_ROLE, PATIENT);
        profileStatus = session.getInt(LOGGED_IN_USER_STATUS, UNREGISTERED);
    }

    public void showMenus()
    {

        arrayMenu = mainMenu.getDoctorMenus();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(this, "", getResources().getString(R.string.loading_wait));
        DoctorId param = new DoctorId(String.valueOf(profileId));
        api.getDoctorLandingPageDetails(param, new Callback<DoctorProfile>() {
            @Override
            public void success(DoctorProfile doc, Response response)
            {
                personProfile = doc;
                if (doc != null && doc.getPerson() != null && doc.getPerson().getImageUrl() != null)
                    new ImageLoadTask(doc.getPerson().getImageUrl(), profilePicture).execute();
                accountName.setText(doc.getPerson().getName());
                adapter = new MenuAdapter(DoctorHome.this, arrayMenu, profileRole, doc.getPerson().getImageUrl());//(new MenuAdapter(this,arrayMenu))
                System.out.println("Adapter Values " + adapter.getCount());
                dList.setAdapter(adapter);
                ((DoctorMenusManage) fragment).updateCounts(doc);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                error.printStackTrace();
                Toast.makeText(DoctorHome.this, R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });
    }


}