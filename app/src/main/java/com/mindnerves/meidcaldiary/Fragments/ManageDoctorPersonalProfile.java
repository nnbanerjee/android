package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import com.medico.application.MyApi;
import Model.DoctorPersonalVM;
import Model.PersonTemp;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by JAY GANESH on 12/14/2015.
 */
public class ManageDoctorPersonalProfile extends Fragment {

	EditText award,briefDescription,experience,institution,member,qualification,service;
	String awardString,briefDescriptionString,experienceString,institutionString,memberString,
		   qualificationString,serviceString,doctorId;
	MyApi api;
	SharedPreferences session;
	Button save;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.doctor_personal_profile,container,false);
		award = (EditText)view.findViewById(R.id.award);
		briefDescription = (EditText)view.findViewById(R.id.brief_description);
		experience = (EditText)view.findViewById(R.id.exprience);
		institution = (EditText)view.findViewById(R.id.institution);
		member = (EditText)view.findViewById(R.id.member);
		qualification = (EditText)view.findViewById(R.id.qualification);
		service = (EditText)view.findViewById(R.id.service);
		save = (Button)view.findViewById(R.id.save_personal);
		session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		doctorId = session.getString("id",null);
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(getResources().getString(R.string.base_url))
				.setClient(new OkClient())
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();
		api = restAdapter.create(MyApi.class);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				awardString = award.getText().toString();
				briefDescriptionString = briefDescription.getText().toString();
				experienceString = experience.getText().toString();
				institutionString = institution.getText().toString();
				memberString = member.getText().toString();
				qualificationString = qualification.getText().toString();
				serviceString = service.getText().toString();
				DoctorPersonalVM doctorPersonalVM = new DoctorPersonalVM();
				doctorPersonalVM.awards = awardString;
				doctorPersonalVM.briefDescription = briefDescriptionString;
				doctorPersonalVM.experience = experienceString;
				doctorPersonalVM.institution = institutionString;
				doctorPersonalVM.memeber = memberString;
				doctorPersonalVM.qualification = qualificationString;
				doctorPersonalVM.service = serviceString;
				doctorPersonalVM.doctorId = doctorId;
				api.saveDoctorPersonDetail(doctorPersonalVM,new Callback<String>() {
					@Override
					public void success(String s, Response response) {
						if(s.equals("Success")){
							Toast.makeText(getActivity(),"Doctor Profile Saved",Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
						Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
		getPersonalProfileData();
		return view;
	}

	public void getPersonalProfileData(){
		api.getProfileDoctor(doctorId,new Callback<PersonTemp>() {
			@Override
			public void success(PersonTemp person, Response response) {
				awardString = person.awards;
				briefDescriptionString = person.briefDescription;
				experienceString = person.experience;
				institutionString = person.institution;
				qualificationString = person.qualification;
				serviceString = person.service;
				memberString = person.memeber;

				if(awardString == null){
					award.setText("");
				}else{
					award.setText(awardString);
				}
				if(briefDescriptionString == null){
					briefDescription.setText("");
				}else{
					briefDescription.setText(briefDescriptionString);
				}
				if(experienceString == null){
					experience.setText("");
				}else{
					experience.setText(experienceString);
				}
				if(institutionString == null){
					institution.setText("");
				}else{
					institution.setText(institutionString);
				}
				if(qualificationString == null){
					qualification.setText("");
				}else{
					qualification.setText(qualificationString);
				}
				if(serviceString == null){
					service.setText("");
				}else{
					service.setText(serviceString);
				}
				if(memberString == null){
					member.setText("");
				}else{
					member.setText(memberString);
				}

			}

			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
			}
		});
	}
}
