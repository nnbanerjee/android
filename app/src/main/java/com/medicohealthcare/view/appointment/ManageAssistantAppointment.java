package com.medicohealthcare.view.appointment;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.util.GeoClient;
import com.medicohealthcare.util.LocationService;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

public class ManageAssistantAppointment extends ParentActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    GeoClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_profile);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.service);
        setContentView(frame, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

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
        client = GeoClient.getInstance(this);
        LocationService locationService = LocationService.getLocationManager(this);
    }



    protected void attachView()
    {
        Bundle bundle = getIntent().getExtras();
        int viewId = bundle.getInt(PARAM.SETTING_VIEW_ID);
        if(viewId == PARAM.ASSISTANT_APPOINTMENT_VIEW)
        {
            ParentFragment fragment = new DoctorListView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment, ClinicProfileListView.class.getName()).addToBackStack(ClinicProfileListView.class.getName()).commit();
        }
        else if(viewId == PARAM.ASSISTANT_QUEUE_VIEW)
        {
            ParentFragment fragment = new DoctorQueueListView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment, ClinicProfileListView.class.getName()).addToBackStack(ClinicProfileListView.class.getName()).commit();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        client.getGeoApiClient().connect();
    }

    @Override
    public void onStop() {
        client.getGeoApiClient().disconnect();
        super.onStop();
    }

}
