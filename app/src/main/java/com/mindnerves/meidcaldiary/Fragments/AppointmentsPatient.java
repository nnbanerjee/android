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
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Adapter.PatientAllAppointmentAdapter;
import Application.MyApi;
import Model.ClinicAppointment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by User on 08-10-2015.
 */
public class AppointmentsPatient extends Fragment {
    MyApi api;
    String doctor_email,userId;
    public SharedPreferences session;
    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
    TextView show_global_tv;
    Integer doctorId;
    Global global;
    ImageView add;
    List<ClinicAppointment> appointments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_patient_appointment, container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        progress = ProgressDialog.show(getActivity(), "",getResources().getString(R.string.loading_wait));
        doctor_email = session.getString("patient_DoctorEmail", null);
        doctorId = Integer.parseInt(session.getString("clinicDoctorId","0"));
        userId =  session.getString("sessionID", "");
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv.setText(session.getString("patient_clinicName", "Medical Diary"));
        add = (ImageView)view.findViewById(R.id.add_clinic_appointment);
        add.setVisibility(View.GONE);
        System.out.println("doctorEmail= "+doctor_email);
        System.out.println("doctorId= "+doctorId);
        System.out.println("Patient Email= "+userId);
        Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        getAllPatientAppointment();
        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClinicAppointment appoint = appointments.get(position);
                if(appoint.getVisitType().equalsIgnoreCase("Physical Exam")){
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("patient_doctor_email",doctor_email);
                    editor.commit();
                    Toast.makeText(getActivity(),"Physical Test Found",Toast.LENGTH_SHORT).show();
                    Fragment fragment = new PatientAppointmentDocument();
                    global.setAppointmentDate(appointments.get(position).getAppointmentDate());
                    global.setAppointmentTime(appointments.get(position).getBookTime());
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }else{
                    SharedPreferences.Editor editor = session.edit();
                    Bundle bun = new Bundle();
                    global.setAppointmentDate(appointments.get(position).getAppointmentDate());
                    global.setAppointmentTime(appointments.get(position).getBookTime());
                    editor.putString("patient_doctor_email",doctor_email);
                    editor.putString("patient_DoctorId",""+doctorId);
                    editor.putString("doctor_patient_appointmentDate", appointments.get(position).getAppointmentDate());
                    editor.putString("doctor_patient_appointmentTime", appointments.get(position).getBookTime());
                    editor.commit();
                    bun.putString("appointmnetPatient","1");
                    Fragment fragment = new PatientAppointmentInformation();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patient Consultations").addToBackStack(null).commit();
                }
            }
        });
        return view;
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

    public void goToBack()
    {
        Bundle bun = getArguments();
        if(bun != null)
        {
            if(bun.getString("fragment").equalsIgnoreCase("clinicListAdapter")){
                Fragment fragment = new PatientAllClinics();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        }else {
            Fragment fragment = new ClinicFragment();
            FragmentManager fragmentManger = getActivity().getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
        }
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
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }
}
