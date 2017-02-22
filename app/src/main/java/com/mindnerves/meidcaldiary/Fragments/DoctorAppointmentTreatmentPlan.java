package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medico.view.ParentFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentTreatmentPlan extends ParentFragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    String patientId,doctorId,appointmentDate;
    private Toolbar toolbar;
    TextView show_global_tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_treatment_plan, container,false);

        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        show_global_tv.setText("<  4 / 5  >");

        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();

        getAllTreatentPlans();

        return view;
    }

    public void getAllTreatentPlans(){
        Fragment fragment = new DoctorAppointmentAllTreatmentPlan();
        FragmentManager fragmentManger = getActivity().getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.replacementTreament,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                goToBack();
                return true;
            }
            return false;
            }
        });
    }

    public void  goToBack() {
        //Fragment fragment = new PatientAllAppointment();
        /*Fragment fragment = new AllDoctorPatientAppointment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);*/
    }
}
