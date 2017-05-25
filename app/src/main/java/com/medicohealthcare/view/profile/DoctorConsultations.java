package com.medicohealthcare.view.profile;

import android.app.FragmentTransaction;

import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentFragment;

public class DoctorConsultations extends ManagePatientProfile {

    @Override
    protected void attachView()
    {
        ParentFragment fragment = new DoctorProfileListView();
//        attachFragment(fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.service, fragment,DoctorProfileListView.class.getName()).addToBackStack(DoctorProfileListView.class.getName()).commit();
    }

}
