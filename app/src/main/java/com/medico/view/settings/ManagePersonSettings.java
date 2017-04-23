package com.medico.view.settings;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.util.GeoClient;
import com.medico.util.LocationService;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

public class ManagePersonSettings extends ParentActivity implements PARAM{
    private static final int CONTENT_VIEW_ID = 10101010;
    public List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
    GeoClient client;
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
        client = GeoClient.getInstance(this);
        LocationService locationService = LocationService.getLocationManager(this);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//    @Override
//    public void onBackPressed() {
//
//        if(fragmentList.size() > 1)
//        {
//
//            ParentFragment fragment = fragmentList.get(fragmentList.size() - 1);
//            if(fragment instanceof ManageDoctorProfile || fragment instanceof ManageDoctorDetailedProfile)
//            {
//
//                super.onBackPressed();
//            }
//            else
//            {
//                fragmentList.remove(fragment);
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(fragment).commit();
//            }
//
//            fragment.setHasOptionsMenu(false);
//            fragment = fragmentList.get(fragmentList.size() - 1);
//            fragment.setHasOptionsMenu(true);
//            fragment.onStart();
//
//        }
//        else
//            super.onBackPressed();
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                    onBackPressed();
                    return true;
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
            case PARAM.MANAGE_PROFILE_VIEW:
                if(bundle.getInt(LOGGED_IN_USER_ROLE) == DOCTOR)
                {
                    ParentFragment fragment = new DoctorProfileEdit();
                    fragmentList.add(fragment);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.service, fragment,DoctorProfileEdit.class.getName()).addToBackStack(DoctorProfileEdit.class.getName()).commit();
                }
                else if(bundle.getInt(LOGGED_IN_USER_ROLE) == PATIENT)
                {
                    //open patient profile
                }
                break;
            case PARAM.PATIENT_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, PATIENT); 
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment patientListView = new ManagePersonListView();
                fragmentList.add(patientListView);
                FragmentTransaction fft = getFragmentManager().beginTransaction();
                fft.add(R.id.service, patientListView,ManagePersonListView.class.getName()).addToBackStack(ManagePersonListView.class.getName()).commit();
                break;
            case PARAM.ASSISTANT_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, ASSISTANT);
                bundle.putInt(PARAM.PROFILE_ROLE, ASSISTANT);
                getIntent().putExtras(bundle);
                ParentFragment assistantListView = new ManagePersonListView();
                fragmentList.add(assistantListView);
                FragmentTransaction fft1 = getFragmentManager().beginTransaction();
                fft1.add(R.id.service, assistantListView,ManagePersonListView.class.getName()).addToBackStack(ManagePersonListView.class.getName()).commit();
                break;
            case PARAM.CLINIC_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DELEGATE);
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment clinicListView = new ManageClinicListView();
                fragmentList.add(clinicListView);
                FragmentTransaction fft33 = getFragmentManager().beginTransaction();
                fft33.add(R.id.service, clinicListView,ManageClinicListView.class.getName()).addToBackStack(ManageClinicListView.class.getName()).commit();
                break;
            case PARAM.DEPENDENT_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DEPENDENT);
//                bundle.putInt(PROFILE_ID,bundle.getInt(LOGGED_IN_ID));
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment dependentListView = new ManageDependentDelegateListView();
                fragmentList.add(dependentListView);
                FragmentTransaction fft2 = getFragmentManager().beginTransaction();
                fft2.add(R.id.service, dependentListView,ManageDependentDelegateListView.class.getName()).addToBackStack(ManageDependentDelegateListView.class.getName()).commit();
                break;
            case PARAM.DELEGATE_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DELEGATE);
//                bundle.putInt(LOGGED_IN_ID,bundle.getInt(PROFILE_ID));
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment delegateListView = new ManageDependentDelegateListView();
                fragmentList.add(delegateListView);
                FragmentTransaction fft3 = getFragmentManager().beginTransaction();
                fft3.add(R.id.service, delegateListView,ManageDependentDelegateListView.class.getName()).addToBackStack(ManageDependentDelegateListView.class.getName()).commit();
                break;
            case PARAM.DOCTOR_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DOCTOR);
//                bundle.putInt(PARAM.PROFILE_ROLE, DOCTOR);
                getIntent().putExtras(bundle);
                ParentFragment doctorListView = new ManagePersonListView();
                fragmentList.add(doctorListView);
                FragmentTransaction fft5 = getFragmentManager().beginTransaction();
                fft5.add(R.id.service, doctorListView,ManagePersonListView.class.getName()).addToBackStack(ManagePersonListView.class.getName()).commit();
                break;
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
