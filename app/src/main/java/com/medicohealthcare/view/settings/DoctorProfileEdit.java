package com.medicohealthcare.view.settings;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.view.home.ParentFragment;

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
		View viewActionBar = getActivity().getLayoutInflater().inflate(R.layout.toolbar, null);
		TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
		textviewTitle.setText("Doctor Profile");
		setHasOptionsMenu(true);
		doctorGeneral = (Button)view.findViewById(R.id.general_doctor_profile);
		doctorPersonal = (Button)view.findViewById(R.id.personal_doctor_profile);
		doctorGeneral.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doctorGeneral.setBackgroundResource(R.drawable.page_selected);
				doctorPersonal.setBackgroundResource(R.drawable.page_default);
				showDoctorProfileEdit();
			}
		});
		doctorPersonal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doctorPersonal.setBackgroundResource(R.drawable.page_selected);
				doctorGeneral.setBackgroundResource(R.drawable.page_default);
				showDoctorPersonal();
			}
		});
		showDoctorProfileEdit();
		return view;
	}

	public void showDoctorProfileEdit(){

		ParentFragment fragment = new DoctorProfileManageView();
		ManagePersonSettings activity = ((ManagePersonSettings)getActivity());
		selectedfragment = fragment;
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().add(R.id.frame_layout,fragment,"Doctor Consultations").commit();
	}
	public void showDoctorPersonal(){
		ParentFragment fragment = new DoctorDetailedProfileManageView();
		ManagePersonSettings activity = ((ManagePersonSettings)getActivity());
		selectedfragment = fragment;
		FragmentManager fragmentManger = getFragmentManager();
		fragmentManger.beginTransaction().add(R.id.frame_layout,fragment,"Doctor Consultations").commit();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		inflater.inflate(R.menu.settings_profile, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.profile_save: {
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

				return true;
			}

		}
		return false;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if(selectedfragment != null)
			selectedfragment.onStart();
	}

}
