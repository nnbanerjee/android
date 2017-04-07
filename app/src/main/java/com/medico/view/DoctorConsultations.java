package com.medico.view;

import android.app.FragmentTransaction;

import com.medico.application.R;

public class DoctorConsultations extends ManagePatientProfile {

    @Override
    protected void attachView()
    {
        ParentFragment fragment = new DoctorProfileListView();
        attachFragment(fragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.service, fragment).addToBackStack(null).commit();
    }

}
