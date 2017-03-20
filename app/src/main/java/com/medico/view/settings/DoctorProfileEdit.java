package com.medico.view.settings;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.view.ManageDoctorDetailedProfile;
import com.medico.view.ManageDoctorProfile;
import com.medico.view.ParentFragment;

/**
 * Created by JAY GANESH on 12/14/2015.
 */
public class DoctorProfileEdit extends ParentFragment {
	Button doctorGeneral,doctorPersonal;
	ParentFragment selectedfragment;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.doctor_profile,container,false);
		setHasOptionsMenu(true);
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

		ParentFragment fragment = new ManageDoctorProfile();
		ManagePersonSettings activity = ((ManagePersonSettings)getActivity());
		if(selectedfragment != null)
			activity.fragmentList.remove(selectedfragment);
		selectedfragment = fragment;
		activity.fragmentList.add(fragment);
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().add(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
	}
	public void showDoctorPersonal(){
		ParentFragment fragment = new ManageDoctorDetailedProfile();
		ManagePersonSettings activity = ((ManagePersonSettings)getActivity());
		if(selectedfragment != null)
			activity.fragmentList.remove(selectedfragment);
		selectedfragment = fragment;
		activity.fragmentList.add(fragment);
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().add(R.id.frame_layout,fragment,"Doctor Consultations").addToBackStack(null).commit();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.menu, menu);
		MenuItem menuItem = menu.findItem(R.id.add);
		menuItem.setIcon(R.drawable.save);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.add: {
				selectedfragment.update();
				if (selectedfragment.isChanged()) {
					if (selectedfragment.canBeSaved()) {
						selectedfragment.save();
					} else {
						Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
					}
				} else if (canBeSaved()) {
					Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
				}


			}
			break;
		}
		return true;
	}

}
