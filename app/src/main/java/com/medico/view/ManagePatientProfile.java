package com.medico.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.mindnerves.meidcaldiary.R;

public class ManagePatientProfile extends AppCompatActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_profile);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.service);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment fragment = new PatientProfileListView();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.service, fragment).commit();
        }
//
//
//        FragmentManager fragmentManger = getFragmentManager();
//        fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
//        onPause();
    }
}
