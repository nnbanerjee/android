package com.medico.view.profile;

import android.app.FragmentTransaction;

import com.medico.application.R;
import com.medico.view.home.ParentFragment;

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
