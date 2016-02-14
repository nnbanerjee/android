package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.DoctorClinicAdapter;
import Application.MyApi;
import Model.DoctorClinicSchedule;
import Model.Schedule;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by JAY GANESH on 12/9/2015.
 */
public class ShowDoctorClinicSchedule extends Fragment {

	ListView doctorClinicScheduleList;
	ImageView addTimeSchedule;
	String clinicId = null;
	ArrayList<DoctorClinicSchedule> scheduleList;
	public MyApi api;
	SharedPreferences session;
	String doctorId = null;
	DoctorClinicAdapter adapter;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.doctor_clinic_schedule, container, false);
		doctorClinicScheduleList = (ListView)view.findViewById(R.id.doctor_clinic_list);
		addTimeSchedule = (ImageView)view.findViewById(R.id.add_doctor_clinic);
		session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		doctorId = session.getString("id", null);
		Bundle bun = getArguments();
		if(bun!=null){
			if(bun.getString("new_clinicId") != null){
				clinicId = bun.getString("new_clinicId");
			}
		}
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(getResources().getString(R.string.base_url))
				.setClient(new OkClient())
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();
		api = restAdapter.create(MyApi.class);
		addTimeSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				Fragment frag2 = new AddTimeSchedule();
				bundle.putString("clinicId",clinicId);
				frag2.setArguments(bundle);
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.content_frame, frag2, "Add_Clinic");
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		showDoctorClinicSchedules();
		return view;
	}
	public void showDoctorClinicSchedules(){
		scheduleList = new ArrayList<DoctorClinicSchedule>();
		api.getClinicDetails(doctorId,clinicId,new Callback<ArrayList<DoctorClinicSchedule>>() {
			@Override
			public void success(ArrayList<DoctorClinicSchedule> schedules, Response response) {
				scheduleList = schedules;
				if(scheduleList.size() == 0){
					DoctorClinicSchedule clinicSchedule = new DoctorClinicSchedule();
					clinicSchedule.slotName = "None";
					clinicSchedule.daysOfWeek = "None";
					clinicSchedule.startTime = "None";
					clinicSchedule.endTime = "None";
					scheduleList.add(clinicSchedule);
				}
				adapter = new DoctorClinicAdapter(getActivity(),scheduleList);
				doctorClinicScheduleList.setAdapter(adapter);
			}

			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
			}
		});

	}
}
