package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mindnerves.meidcaldiary.R;

/**
 * Created by JAY GANESH on 12/14/2015.
 */
public class DoctorProfileEdit extends Fragment {
	Button doctorGeneral,doctorPersonal;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.doctor_profile,container,false);
		doctorGeneral = (Button)view.findViewById(R.id.general_doctor_profile);
		doctorPersonal = (Button)view.findViewById(R.id.personal_doctor_profile);
		doctorGeneral.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doctorGeneral.setBackgroundResource(R.drawable.square_blue_color_appointment);
				doctorPersonal.setBackgroundResource(R.drawable.square_grey_color_appointment);
				doctorGeneral.setTextColor(Color.parseColor("#ffffff"));
				doctorPersonal.setTextColor(Color.parseColor("#000000"));
				showDoctorProfileEdit();
			}
		});
		doctorPersonal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doctorPersonal.setBackgroundResource(R.drawable.square_blue_color_appointment);
				doctorGeneral.setBackgroundResource(R.drawable.square_grey_color_appointment);
				doctorPersonal.setTextColor(Color.parseColor("#ffffff"));
				doctorGeneral.setTextColor(Color.parseColor("#000000"));
				showDoctorPersonal();
			}
		});
		showDoctorProfileEdit();
		return view;
	}

	public void showDoctorProfileEdit(){



		Fragment fragment = new ManageDoctorProfile();
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().replace(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
	}
	public void showDoctorPersonal(){
		Fragment fragment = new ManageDoctorPersonalProfile();
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().replace(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
	}
}
