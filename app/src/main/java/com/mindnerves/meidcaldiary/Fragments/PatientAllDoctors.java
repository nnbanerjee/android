package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoctorAdapter;
import Adapter.DoctorListAdapter;
import Application.MyApi;
import Model.Chat;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class PatientAllDoctors extends Fragment {

    public MyApi api;
    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;
    TextView globalTv,accountName;
    ImageView addDoctor,profilePicture,medicalLogo,medicalText;
    Button drawar,logout,back,refresh;
    RelativeLayout profileLayout;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);
        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        back = (Button)getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        addDoctor = (ImageView)view.findViewById(R.id.add_clinic_appointment);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        medicalLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
        medicalText = (ImageView)getActivity().findViewById(R.id.home_icon);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        logout = (Button)getActivity().findViewById(R.id.logout);
        type = session.getString("type",null);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShowSpecialityAppointment();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showDoctorList();
        manageScreenIcons();
        return view;
    }

    public void manageScreenIcons(){
        globalTv.setText("Consultantions");
        profileLayout.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        logout.setVisibility(View.VISIBLE);
        drawar.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.GONE);
        medicalLogo.setVisibility(View.GONE);
        medicalText.setVisibility(View.GONE);
    }

    public void showDoctorList(){
        String  patientId = session.getString("sessionID",null);
        System.out.println("patientId = "+patientId);

        api.getPatientsDoctors(patientId, new Callback<List<DoctorSearchResponse>>() {
            @Override
            public void success(List<DoctorSearchResponse>  doctorSearchResponses, Response response) {

                Global global = (Global) getActivity().getApplicationContext();
                global.setDoctorSearchResponses(doctorSearchResponses);
                DoctorListAdapter doctorListAdapter = new DoctorListAdapter(getActivity(), doctorSearchResponses);
                doctorListView.setAdapter(doctorListAdapter);
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
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goToBack(){
        globalTv.setText(""+type);
        Fragment fragment = new PatientMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        medicalLogo.setVisibility(View.VISIBLE);
        medicalText.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }


}
