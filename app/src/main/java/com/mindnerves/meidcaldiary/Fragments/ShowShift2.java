package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mindnerves.meidcaldiary.R;

import com.medico.application.MyApi;

/**
 * Created by User on 9/7/15.
 */
public class ShowShift2 extends Fragment {
    MyApi api;
    Button day,week,month,add;
    ProgressDialog progress;
    String backString = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_doctor, container, false);
        day = (Button)view.findViewById(R.id.dayBtn);
        week = (Button)view.findViewById(R.id.weekBtn);
        month = (Button)view.findViewById(R.id.monthBtn);
        add = (Button)view.findViewById(R.id.add);
        backString = getArguments().getString("fragment");
        Bundle bun = new Bundle();
        bun.putString("fragment",backString);
        Fragment frag2 = new AppointmentDayShift2();
        frag2.setArguments(bun);
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setBackgroundResource(R.drawable.square_blue_color);
                day.setTextColor(Color.parseColor("#ffffff"));
                week.setBackgroundResource(R.drawable.square_grey_color);
                week.setTextColor(Color.parseColor("#000000"));
                month.setBackgroundResource(R.drawable.square_grey_color);
                month.setTextColor(Color.parseColor("#000000"));
                Bundle bun = new Bundle();
                bun.putString("fragment",backString);
                Fragment frag2 = new AppointmentDayShift2();
                frag2.setArguments(bun);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week.setBackgroundResource(R.drawable.square_blue_color);
                week.setTextColor(Color.parseColor("#ffffff"));
                day.setBackgroundResource(R.drawable.square_grey_color);
                day.setTextColor(Color.parseColor("#000000"));
                month.setBackgroundResource(R.drawable.square_grey_color);
                month.setTextColor(Color.parseColor("#000000"));
                Fragment frag2 = new AppointmentWeekShift2();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month.setBackgroundResource(R.drawable.square_blue_color);
                month.setTextColor(Color.parseColor("#ffffff"));
                day.setBackgroundResource(R.drawable.square_grey_color);
                day.setTextColor(Color.parseColor("#000000"));
                week.setBackgroundResource(R.drawable.square_grey_color);
                week.setTextColor(Color.parseColor("#000000"));
                Fragment frag2 = new AppointmentMonthShift2();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new AddAppointmentManual();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;
    }
    public void goToBack()
    {
        Fragment fragment;
        if(backString.equalsIgnoreCase("from_summary")) {
            fragment = new DoctorAllClinics();
        }else{
            fragment = new DoctorClinicFragment();
        }
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
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
}
