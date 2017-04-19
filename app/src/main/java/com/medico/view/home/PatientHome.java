package com.medico.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
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
        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                    fragment = new ShowSpeciality();
//                    fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
            }
        });
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
//        backButton = (Button) findViewById(R.id.back_button);
//        backButton.setVisibility(View.INVISIBLE);
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
//        logout = (Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PatientHome.this);
//                alertDialogBuilder.setMessage("R.string.confirm_logout");
//                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        finish();
//
//                    }
//                });
//
//                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        System.out.println("Do Nothing");
//                    }
//                });
//
//                AlertDialog alertDialog2 = alertDialogBuilder.create();
//                alertDialog2.show();
//            }
//        });
        fragment = new PatientMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();

//        dList.setSelector(android.R.color.holo_blue_dark);
//        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> selectedItem, View v, int position, long id) {
//                dLayout.closeDrawers();
//                Bundle args = new Bundle();
//                String switchCaseId = (String) adapter.getItem(position);
//                switch (switchCaseId) {
//                    case "Manage Profile":
//                        manageProfile();
//                        break;
//                    case "Manage Doctors":
//                        manageDoctors();
//                        break;
//                    case "Manage Dependents":
//                        manageDependents();
//                        break;
//                    case "Manage Delegations":
//                        manageDelegations();
//                        break;
//                    case "Terms & Conditions":
//                        termsAndConditions();
//                        break;
//
//                    case "Logout":
//                        logout();
//                        break;
//                }
//            }
//        });
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


//    protected void manageDoctors()
//    {
//        System.out.println("i am here::::::::::::");
//        Bundle bundle = new Bundle();
//        bundle.putInt(PARAM.PATIENT_ID, profileId);
//        setSettingParameters(bundle);
//        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.PATIENT_SETTING_VIEW);
//        Intent intObj = new Intent(this, ManagePersonSettings.class);
//        intObj.putExtras(bundle);
//        startActivity(intObj);
//        onPause();
//        dLayout.closeDrawer(dList);
//    }
//    protected void manageDelegations()
//    {
//        Bundle bundle = new Bundle();
//        setSettingParameters(bundle);
//        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.DELEGATE_SETTING_VIEW);
//        Intent intObj = new Intent(this, ManagePersonSettings.class);
//        intObj.putExtras(bundle);
//        startActivity(intObj);
//        onPause();
//        dLayout.closeDrawer(dList);
//    }

    @Override
    protected void showPopup(final Activity context, Point p) {

        super.showPopup(context, p);

    }
}