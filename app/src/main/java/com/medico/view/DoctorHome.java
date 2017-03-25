package com.medico.view;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.MenuAdapter;
import com.medico.model.DoctorId;
import com.medico.model.DoctorProfile;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.application.R;
import com.medico.view.settings.ManagePersonSettings;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.medico.adapter.MenuAdapter;

//import com.mindnerves.meidcaldiary.Fragments.ManageMessageNotification;
//import com.mindnerves.meidcaldiary.Fragments.ShowClinicSpecialities;
//import com.mindnerves.meidcaldiary.Fragments.ShowPatients;

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
        messages = (Button) findViewById(R.id.alarm_notes);
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
            public void onClick(View v) {
//                if (profileRole == DOCTOR) {
//                    fragment = new ShowPatients();
//                    fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
//                }
            }
        });
        fragment = new DoctorMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                fragment = new ManageMessageNotification();
//                fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
            }
        });
        backButton = (Button) findViewById(R.id.back_button);
        backButton.setVisibility(View.INVISIBLE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        dList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        dList.setDividerHeight(1);

        clinicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

//                fragment = new ShowClinicSpecialities();
//                fragmentManger = getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
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
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> selectedItem, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                String switchCaseId = (String) adapter.getItem(position);
                switch (switchCaseId) {
                    case "Manage Profile":
                        manageProfile();
                        break;

                    case "Manage Patients":
                        managePatients();
                        break;
                    case "Manage Clinics":

                        manageClinics();
                        break;

                    case "Manage Assistants":
                        manageAssistants();
                        break;

                    case "Manage Dependents":
                        manageDependents();
                        break;

                    case "Manage Templates":
                        manageTemplates();
                        break;

                    case "Terms & Conditions":
                        termsAndConditions();
                        break;

                    case "Logout":
                        logout();
                        break;
                }
            }
        });
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

    protected void managePatients()
    {
        System.out.println("i am here::::::::::::");
        Bundle bundle = new Bundle();
        setSettingParameters(bundle);
        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.PATIENT_SETTING_VIEW);
        Intent intObj = new Intent(this, ManagePersonSettings.class);
        intObj.putExtras(bundle);
        startActivity(intObj);
        onPause();
        dLayout.closeDrawer(dList);
    }
    protected void manageClinics()
    {

        System.out.println("i am here::::::::::::");
        Bundle bundle = new Bundle();
        setSettingParameters(bundle);
        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.CLINIC_SETTING_VIEW);
        Intent intObj = new Intent(this, ManagePersonSettings.class);
        intObj.putExtras(bundle);
        startActivity(intObj);
        onPause();
        dLayout.closeDrawer(dList);
    }
    protected void manageAssistants()
    {
        System.out.println("i am here::::::::::::");
        Bundle bundle = new Bundle();
        setSettingParameters(bundle);
        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.ASSISTANT_SETTING_VIEW);
        Intent intObj = new Intent(this, ManagePersonSettings.class);
        intObj.putExtras(bundle);
        startActivity(intObj);
        onPause();
        dLayout.closeDrawer(dList);
    }

    protected void manageTemplates()
    {
//        //fragment = new ManageTemplate();
//        fragment = new ManageProcedure();
//        fragmentManger = getFragmentManager();
//        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Template").addToBackStack(null).commit();
//        dList.setSelection(position);
//        dLayout.closeDrawer(dList);
    }

}