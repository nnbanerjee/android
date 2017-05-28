package com.medicohealthcare.view.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.model.DoctorProfile;
import com.medicohealthcare.service.Constants;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.PARAM;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Narendra on 18-01-2017.
 */

public class DoctorHome extends HomeActivity
{
    public int PRACTICE_TYPE = ALLOPATHIC;
    MessageStateReceiver mDownloadStateReceiver;
    @Override
    protected void createView()
    {

        parent_activity = this;
        setContentView(R.layout.doctor_home);
        //profile layout
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView) findViewById(R.id.account_name);
        status = (TextView) findViewById(R.id.status_text);
        status.setText("Doctor Profile");
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

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(DoctorHome.this, p);
            }
        });
        //tool bar construction
        LinearLayout layout = (LinearLayout) findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        messages = (Button) findViewById(R.id.chat);
        patients = (Button) findViewById(R.id.search_patient);
        clinicSearch = (Button) findViewById(R.id.search_clinic);

        profilePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dList.getChildAt(0).callOnClick();
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
        registerChatMessage();
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