package com.medicohealthcare.view.registration;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

public class ProfileRegistrationVerificationActivity extends ParentActivity {
    private static final int CONTENT_VIEW_ID = 10101010;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_profile);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(R.id.service);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.toolbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        viewActionBar.setBackgroundColor(0xFF206799);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getResources().getString(R.string.patients_profiles));
        abar.setCustomView(viewActionBar, params);
        abar.setBackgroundDrawable(getResources().getDrawable(R.color.medico_blue));
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
//        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        attachView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void attachView()
    {
        ParentFragment fragment = new RegistrationVerificationView();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.service, fragment,RegistrationVerificationView.class.getName()).addToBackStack(RegistrationVerificationView.class.getName()).commit();
    }

}
