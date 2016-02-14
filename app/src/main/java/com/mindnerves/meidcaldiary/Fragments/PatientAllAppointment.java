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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.AllPatientAppointmentAdapter;
import Adapter.ClinicListItemAdapter;
import Adapter.PatientAllAppointmentAdapter;
import Application.MyApi;
import Model.ClinicAppointment;
import Model.ClinicDetailVm;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class PatientAllAppointment extends Fragment {

    MyApi api;
    String doctor_email,userId;
    public SharedPreferences session;
    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
    TextView show_global_tv;
    Integer doctorId;
    Global global;
    ImageView addAppointment;
    List<ClinicAppointment> appointments;
    String fragmentCall = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_patient_appointment, container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        System.out.println("PatientAllAppointment:::::::::");
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        doctor_email = session.getString("patient_doctor_email", null);
        doctorId = Integer.parseInt(session.getString("patient_doctorId","0"));
        userId =  session.getString("sessionID", "");
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv.setText(session.getString("patient_DoctorName", "Medical Diary"));
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        addAppointment = (ImageView)view.findViewById(R.id.add_clinic_appointment);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked:::::::::");
                goToBack();
            }
        });
        Bundle bun = getArguments();
        if(bun != null) {
            fragmentCall = getArguments().getString("fragment", "");
        }
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClinicAppointment appoint = appointments.get(position);
                if(!appoint.getBookTime().equals("")) {
                    Bundle bun = new Bundle();
                    SharedPreferences.Editor editor = session.edit();
                    global.setAppointmentDate(appointments.get(position).getAppointmentDate());
                    global.setAppointmentTime(appointments.get(position).getBookTime());
                    editor.putString("doctor_patient_appointmentDate", appointments.get(position).getAppointmentDate());
                    editor.putString("doctor_patient_appointmentTime", appointments.get(position).getBookTime());
                    editor.putString("clinicDoctorId", ""+appointments.get(position).getDoctorId());
                    editor.putString("doctor_patientEmail", doctor_email);
                    editor.commit();
                    bun.putString("patientAllAppointment","PatientAllAppointment");
                    Fragment fragment = new PatientAppointmentInformation();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patient Consultations").addToBackStack(null).commit();
                }
            }
        });
        getAllPatientAppointment();
        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun  = new Bundle();
                bun.putString("fragment","PatientAllAppointment");
                Fragment fragment = new AddAppointment();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
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
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void  goToBack(){
        Fragment fragment;
        Bundle args = new Bundle();
        System.out.println("Back in action:::::::::");
        if(fragmentCall.equalsIgnoreCase("doctor_list")){
            fragment = new PatientAllDoctors();
            args.putString("fragment","PatientAllAppointment");
        }else if(fragmentCall.equalsIgnoreCase("doctorAdapter")){
            Bundle specialityBun = new Bundle();
            specialityBun.putString("specialization",global.specialityString);
            fragment = new AddDoctorSpecialityWise();
            fragment.setArguments(specialityBun);
        }else{
            fragment = new DoctorDetailsFragment();
            args.putString("doctorId", doctor_email);
            args.putString("fragment","DoctorDetailFragment");
            fragment.setArguments(args);
        }
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }

    public void getAllPatientAppointment(){
        api.getPatientAppointment(doctorId, userId, new Callback<List<ClinicAppointment>>() {
            @Override
            public void success(List<ClinicAppointment> clinicAppointmentList, Response response) {

                if(clinicAppointmentList.size() == 0){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Calendar calendar = Calendar.getInstance();
                    ClinicAppointment clinicAppointment = new ClinicAppointment();
                    clinicAppointment.setAppointmentDate(formatter.format(calendar.getTime()));
                    clinicAppointment.setBookTime("");
                    clinicAppointmentList.add(clinicAppointment);
                }else{
                    for(ClinicAppointment clinicAppointment : clinicAppointmentList){
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                        long milliSeconds= Long.parseLong(clinicAppointment.getAppointmentDate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        System.out.println(formatter.format(calendar.getTime()));
                        clinicAppointment.setAppointmentDate(formatter.format(calendar.getTime()));
                    }
                }
                appointments = clinicAppointmentList;
                PatientAllAppointmentAdapter adapter = new PatientAllAppointmentAdapter(getActivity().getApplicationContext(),clinicAppointmentList);
                allAppointments.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
