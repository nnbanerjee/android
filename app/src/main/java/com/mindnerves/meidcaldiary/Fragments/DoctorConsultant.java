package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.DoctorConsultantAdapter;
import com.medico.application.MyApi;
import Model.ClinicDetailVm;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 10-10-2015.
 */
public class DoctorConsultant extends Fragment {
    MyApi api;
    List<DoctorSearchResponse> doctorList;
    Global global;
    ArrayAdapter<CharSequence> adapter;
    String[] doctors;
    Spinner doctorSpinner;
    ListView clinicList;
    ProgressDialog progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_consultant, container, false);
        doctorSpinner = (Spinner)view.findViewById(R.id.doctor_list);
        clinicList = (ListView)view.findViewById(R.id.clinic_doctor_consult_list);
        global = (Global)getActivity().getApplicationContext();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        if(global.getDoctorSearchResponses() != null){
            doctorList = global.getDoctorSearchResponses();
            doctors = new String[doctorList.size()];
            int i = 0;
            for(DoctorSearchResponse doctor : doctorList){
                doctors[i] = doctor.getName();
                i++;
            }
            doctorSpinner.setAdapter(new DoctorSpinner(getActivity(), R.layout.customize_spinner,doctors));
        }
        if(doctorList.size() == 0){
            getAllClinicsDetails(doctorList.get(0).getDoctorId());
            saveDoctorData(0);
        }
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DoctorSearchResponse doctor = doctorList.get(position);
                System.out.println("DoctorId= "+doctor.getDoctorId());
                System.out.println("Doctor Name= "+doctor.getName());
                System.out.println("Doctor Id= "+doctor.getEmailID());
                getAllClinicsDetails(doctor.getDoctorId());
                saveDoctorData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
    public void getAllClinicsDetails(String doctorId){
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        api.getAllClinicsDetailsList(doctorId, "1", new Callback<List<ClinicDetailVm>>() {
            @Override
            public void success(List<ClinicDetailVm> clinicDetailVm, Response response) {

                Global global = (Global) getActivity().getApplicationContext();
                global.setClinicDetailVm(clinicDetailVm);

                //ClinicListItemAdapter clinicListItemAdapter = new ClinicListItemAdapter(getActivity(),clinicDetailVm, bookDate, bookTime, shift, clinicId);
                DoctorConsultantAdapter clinicListItemAdapter = new DoctorConsultantAdapter(getActivity(),clinicDetailVm);
                clinicList.setAdapter(clinicListItemAdapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });
    }
    public void saveDoctorData(int position){
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString("patient_DoctorName", doctorList.get(position).getName());
        editor.putString("patient_DoctorId",  doctorList.get(position).getDoctorId());
        editor.putString("patient_DoctorEmail", doctorList.get(position).getEmailID());
        editor.commit();
    }
    public class DoctorSpinner extends ArrayAdapter<String>
    {
        public DoctorSpinner(Context ctx, int txtViewResourceId, String[] objects)
        {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customize_doctor_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(doctors[position]);
            return mySpinner;
        }


    }
}
