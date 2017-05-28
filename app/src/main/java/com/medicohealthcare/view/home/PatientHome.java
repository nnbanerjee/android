package com.medicohealthcare.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.MenuAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.PatientId;
import com.medicohealthcare.model.PatientProfile;
import com.medicohealthcare.service.Constants;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Narendra on 18-01-2017.
 */

public class PatientHome extends HomeActivity
{
    MessageStateReceiver mDownloadStateReceiver;
    protected void createView()
    {
        parent_activity = this;
        setContentView(R.layout.patient_home);

        //profile layout
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView) findViewById(R.id.account_name);
        status = (TextView) findViewById(R.id.status_text);

        if(parent != null)
            status.setText("Dependent Profile");
        else
            status.setText("Patient Profile");
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
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(PatientHome.this, p);
            }
        });
        //tool bar construction
        LinearLayout layout = (LinearLayout) findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        notes = (Button) findViewById(R.id.alarm_notes);
        messages = (Button) findViewById(R.id.chat);
//        patients = (Button) findViewById(R.id.patient_ping);
        clinicSearch = (Button) findViewById(R.id.search_clinic);
        doctorSearch = (Button) findViewById(R.id.search_doctor);
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
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.PATIENT_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                bundle.putInt(PARAM.SETTING_VIEW_ID,PARAM.CHAT_VIEW);
                Intent intObj = new Intent(PatientHome.this, ManageHomeView.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();            }
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
        registerChatMessage();
    }

    @Override
    protected void showPopup(final Activity context, Point p) {

        super.showPopup(context, p);

    }
    @Override
    public void onResume()
    {
        super.onResume();
        registerChatMessage();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        deregisterChatMessage();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        deregisterChatMessage();
    }
    public void registerChatMessage()
    {
        if(mDownloadStateReceiver == null)
        {
            IntentFilter statusIntentFilter = new IntentFilter();
            statusIntentFilter.addAction(Constants.NEW_MESSAGE_ARRIVED);
            // Instantiates a new DownloadStateReceiver
            mDownloadStateReceiver = new MessageStateReceiver();
            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
        }
    }
    public void deregisterChatMessage()
    {
        if(mDownloadStateReceiver != null)
        {
            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).unregisterReceiver(mDownloadStateReceiver);
            mDownloadStateReceiver = null;
        }

    }
    public class MessageStateReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        public MessageStateReceiver()
        {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            int[] numberOfMessages = bundle.getIntArray(Constants.NEW_MESSAGE_NUMBERS);
            Button numberButton = (Button)findViewById(R.id.numberOfMessages);
            if(numberOfMessages != null && numberOfMessages.length > 0)
            {
                numberButton.setVisibility(View.VISIBLE);
                int numbers = 0;
                for(int i = 0; i < numberOfMessages.length; i++)
                    numbers = numbers + numberOfMessages[i];

                numberButton.setText(new Integer(numbers).toString());
            }
            else
                numberButton.setVisibility(View.GONE);

        }

    }

}
