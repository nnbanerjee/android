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
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.AllDoctorClinicAdapter;
import Application.MyApi;
import com.medico.model.Clinic;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ClinicDetailsFragment extends Fragment {

    MyApi api;
    ListView clinicListView;
    ProgressDialog progress;
    public String doctorId = null;
    public String bookDate = null;
    public String bookTime = null;
    public String shift = null;
    String clinicId = "";
    SharedPreferences session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_profile_fragment, container, false);

        Global global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        clinicId = session.getString("patient_clinicId", null);
        clinicListView = (ListView) view.findViewById(R.id.clinicListView);
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");

        List<Clinic> clinics = global.getAllClinicsList();

        for (Clinic clinic : clinics) {
            if (clinic.getShift() != null) {
                doctorId = clinic.getDoctorId();
                bookDate = clinic.bookDate;
                bookTime = clinic.bookTime;
                shift = clinic.shift;
            }
        }

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

    public void goBack() {
        Fragment fragment = new PatientAllClinics();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getAllClinicsDetails() {
        api.getAlldoctorsOfClinicDetail(clinicId, new Callback<List<ClinicDetailVm>>() {
            @Override
            public void success(List<ClinicDetailVm> clinicDetailVm, Response response) {
                //System.out.println("clinicDetailVm size ()" + clinicDetailVm.size());
                Global global = (Global) getActivity().getApplicationContext();
                global.setAllDoctorClinicList(clinicDetailVm);
                //we required doctor id to match
                if (doctorId != null) {
                    for (ClinicDetailVm detailVm : clinicDetailVm) {
                        if (doctorId.equals(detailVm.getDoctorId())) {
                            detailVm.setHasNextAppointment(true);

                            System.out.println("bookDate = " + bookDate);
                            System.out.println("bookTime = " + bookTime);

                            detailVm.setAppointmentDate(bookDate);
                            detailVm.setAppointmentTime(bookTime);
                            detailVm.setAppointmentShift(shift);
                        }
                    }
                }
                // ClinicListItemAdapter
                AllDoctorClinicAdapter clinicListItemAdapter = new AllDoctorClinicAdapter(getActivity(), clinicDetailVm);
                clinicListView.setAdapter(clinicListItemAdapter);
                progress.dismiss();
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }
}
