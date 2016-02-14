package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.ClinicPatientAdapter;
import Application.MyApi;
import Model.AllClinicsByDoctorPatientId;
import Model.AllClinicsForDoctorIdAndPatientId;
import Model.AllPatients;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicAllPatientFragment extends Fragment {

    MyApi api;
    ListView clinicListView;
    String doctorId,patientId;
    ProgressDialog progress;
    public String bookDate = null;
    public String bookTime = null;
    public String shift = null;
    public Integer clinicId = null;
    Button back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_profile_fragment, container,false);

        Global global = (Global) getActivity().getApplicationContext();
        List<AllPatients> allPatients = global.getAllPatients();
        back = (Button)getActivity().findViewById(R.id.back_button);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", null);
        patientId = session.getString("patientId", null);

        for(int i = 0; i < allPatients.size(); i++){
            if(patientId.equals(allPatients.get(i).getId())){
                AllPatients patients = allPatients.get(i);
                bookDate = patients.getBookDate();
                bookTime = patients.getBookTime();
                shift = patients.getShift();
                clinicId = patients.getClinicId();
                SharedPreferences.Editor edit = session.edit();
                edit.putString("patient_email", patients.getEmail());
                edit.commit();
                //System.out.println("doctorSearchResponse.getEmail() = "+patients.getEmail());
                //System.out.println("doctorSearchResponse.getDoctorId() = "+doctorSearchResponse.);
            }
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        clinicListView = (ListView) view.findViewById(R.id.clinicListView);
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        getAllClinicsDetails();

        return view;
    }

    public void getAllClinicsDetails(){
        api.getPatientAppointmentsByDoctor(new AllClinicsForDoctorIdAndPatientId(doctorId, "1") , new Callback<AllClinicsByDoctorPatientId>() {
            @Override
            public void success(AllClinicsByDoctorPatientId clinicDetailVm, Response response) {


              /*  Global global = (Global) getActivity().getApplicationContext();
                global.setClinicDetailVm(clinicDetailVm);
                if (clinicId != null) {
                    for (ClinicDetailVm detailVm : clinicDetailVm) {
                        if (clinicId == Integer.parseInt(detailVm.getClinicId())) {
                            detailVm.setHasNextAppointment(true);
                            detailVm.setAppointmentDate(bookDate);
                            detailVm.setAppointmentTime(bookTime);
                            detailVm.setAppointmentShift(shift);
                        }
                    }
                }

                ClinicPatientAdapter clinicListItemAdapter = new ClinicPatientAdapter(getActivity(), clinicDetailVm);
                clinicListView.setAdapter(clinicListItemAdapter);
                progress.dismiss();*/
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Patients");
        Fragment fragment = new AllDoctorsPatient();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }

}
