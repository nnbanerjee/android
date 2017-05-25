package com.medicohealthcare.view.settings;

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

import com.medicohealthcare.application.R;
import com.medicohealthcare.util.GeoClient;
import com.medicohealthcare.util.LocationService;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

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
                ParentFragment patientListView = new PersonListView();
                fragmentList.add(patientListView);
                FragmentTransaction fft = getFragmentManager().beginTransaction();
                fft.add(R.id.service, patientListView,PersonListView.class.getName()).addToBackStack(PersonListView.class.getName()).commit();
                break;
            case PARAM.ASSISTANT_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, ASSISTANT);
                bundle.putInt(PARAM.PROFILE_ROLE, ASSISTANT);
                getIntent().putExtras(bundle);
                ParentFragment assistantListView = new PersonListView();
                fragmentList.add(assistantListView);
                FragmentTransaction fft1 = getFragmentManager().beginTransaction();
                fft1.add(R.id.service, assistantListView,PersonListView.class.getName()).addToBackStack(PersonListView.class.getName()).commit();
                break;
            case PARAM.CLINIC_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, CLINIC);
                bundle.putInt(PARAM.DOCTOR_ID, bundle.getInt(LOGGED_IN_ID));
                getIntent().putExtras(bundle);
                ParentFragment clinicListView = new ClinicListView();
                fragmentList.add(clinicListView);
                FragmentTransaction fft33 = getFragmentManager().beginTransaction();
                fft33.add(R.id.service, clinicListView,ClinicListView.class.getName()).addToBackStack(ClinicListView.class.getName()).commit();
                break;
            case PARAM.DEPENDENT_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DEPENDENT);
//                bundle.putInt(PROFILE_ID,bundle.getInt(LOGGED_IN_ID));
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment dependentListView = new DependentDelegateListView();
                fragmentList.add(dependentListView);
                FragmentTransaction fft2 = getFragmentManager().beginTransaction();
                fft2.add(R.id.service, dependentListView,DependentDelegateListView.class.getName()).addToBackStack(DependentDelegateListView.class.getName()).commit();
                break;
            case PARAM.DELEGATE_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DELEGATE);
//                bundle.putInt(LOGGED_IN_ID,bundle.getInt(PROFILE_ID));
//                bundle.putInt(PARAM.PROFILE_ROLE, PATIENT);
                getIntent().putExtras(bundle);
                ParentFragment delegateListView = new DependentDelegateListView();
                fragmentList.add(delegateListView);
                FragmentTransaction fft3 = getFragmentManager().beginTransaction();
                fft3.add(R.id.service, delegateListView,DependentDelegateListView.class.getName()).addToBackStack(DependentDelegateListView.class.getName()).commit();
                break;
            case PARAM.DOCTOR_SETTING_VIEW:
                bundle.putInt(PARAM.PROFILE_TYPE, DOCTOR);
//                bundle.putInt(PARAM.PROFILE_ROLE, DOCTOR);
                getIntent().putExtras(bundle);
                ParentFragment doctorListView = new PersonListView();
                fragmentList.add(doctorListView);
                FragmentTransaction fft5 = getFragmentManager().beginTransaction();
                fft5.add(R.id.service, doctorListView,PersonListView.class.getName()).addToBackStack(PersonListView.class.getName()).commit();
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
