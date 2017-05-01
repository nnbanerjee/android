package com.medico.view.home;

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
import com.medico.util.LocationService;
import com.medico.util.PARAM;
import com.medico.view.search.PersonSearchView;

public class ManageHomeView extends ParentActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
//    public List<ParentFragment> fragmentList = new ArrayList<ParentFragment>();
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
        LocationService locationService = LocationService.getLocationManager(this);
    }


    protected void attachView()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle.getInt(PARAM.SETTING_VIEW_ID)== PARAM.PATIENT_SETTING_VIEW)
        {
            bundle.putInt(PARAM.SEARCH_ROLE, PARAM.PATIENT);
            bundle.putInt(PARAM.SEARCH_TYPE, PARAM.SEARCH_GLOBAL);
            getIntent().putExtras(bundle);
            ParentFragment fragment = new PersonSearchView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment).addToBackStack(null).commit();
        }
        else if(bundle.getInt(PARAM.SETTING_VIEW_ID)== PARAM.CLINIC_SETTING_VIEW)
        {
            bundle.putInt(PARAM.SEARCH_ROLE, PARAM.CLINIC);
            bundle.putInt(PARAM.SEARCH_TYPE, PARAM.SEARCH_GLOBAL);
            getIntent().putExtras(bundle);
            ParentFragment fragment = new PersonSearchView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment).addToBackStack(null).commit();
        }
        else if(bundle.getInt(PARAM.SETTING_VIEW_ID)== PARAM.DOCTOR_SETTING_VIEW)
        {
            bundle.putInt(PARAM.SEARCH_ROLE, PARAM.DOCTOR);
            bundle.putInt(PARAM.SEARCH_TYPE, PARAM.SEARCH_GLOBAL);
            getIntent().putExtras(bundle);
            ParentFragment fragment = new PersonSearchView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment).addToBackStack(null).commit();
        }
    }
}
