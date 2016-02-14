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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Adapter.AllPatientAppointmentAdapter;
import Adapter.DoctorAllPatientAppointmentAdapter;
import Adapter.Doctor_AllPatientAppointmentAdapter;
import Application.MyApi;
import Model.ClinicAppointment;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AllDoctorPatientAppointment extends Fragment {

    MyApi api;
    String doctor_email,patientId;
    public SharedPreferences session;
    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
    TextView show_global_tv;
    Integer doctorId;
    Global global;
    List<ClinicAppointment> appointments;
    ImageView addClinic;
    String fragmentCall;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_patient_appointment, container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final Bundle bun = getArguments();
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        global = (Global) getActivity().getApplicationContext();
        doctor_email = session.getString("patient_doctor_email", null);
        doctorId = Integer.parseInt(session.getString("doctorId","0"));
        patientId =  session.getString("patientEmail", "");
        show_global_tv.setText(session.getString("patient_Name", "Medical Diary"));
        addClinic = (ImageView)view.findViewById(R.id.add_clinic_appointment);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        if(bun != null){
            if(bun.getString("fragment") != null){
                fragmentCall = bun.getString("fragment");
            }
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

                SharedPreferences.Editor editor = session.edit();
                global.setAppointmentDate(appointments.get(position).getAppointmentDate());
                global.setAppointmentTime(appointments.get(position).getBookTime());
                editor.putString("doctor_patient_appointmentDate", appointments.get(position).getAppointmentDate());
                editor.putString("doctor_patient_appointmentTime", appointments.get(position).getBookTime());
                editor.putString("doctorId",""+appointments.get(position).getDoctorId());
                editor.putString("clinicId", ""+appointments.get(position).getClinicId());
                editor.commit();

                //Fragment fragment = new DoctorAppointmentSummary();
                Fragment fragment = new DoctorAppointmentInformation();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        addClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("fragment",fragmentCall);
                Fragment fragment = new AddAppointment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        getAllPatientAppointment();

        return view;
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

    public void  goToBack(){
        Fragment fragment;
        Bundle bundle = getArguments();
        String key = "";
        if(bundle != null){
            if(bundle.getString("fragment") != null){
                key = bundle.getString("fragment");
                if(key.equalsIgnoreCase("from main page")){
                    fragment = new AllDoctorsPatient();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                }
                else if(key.equalsIgnoreCase("doctorPatientListAdapter")){
                    fragment = new AllDoctorsPatient();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
                else
                {
                    fragment = new PatientDetailsFragment();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                }
            }
        }else{
            fragment = new PatientDetailsFragment();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button)getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
    }

    public void getAllPatientAppointment(){
        api.getPatientAppointment(doctorId, patientId, new Callback<List<ClinicAppointment>>() {
            @Override
            public void success(List<ClinicAppointment> clinicAppointmentList, Response response) {

                for(ClinicAppointment clinicAppointment : clinicAppointmentList){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

                    long milliSeconds= Long.parseLong(clinicAppointment.getAppointmentDate());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSeconds);
                    clinicAppointment.setAppointmentDate(formatter.format(calendar.getTime()));
                }
                appointments = clinicAppointmentList;
                //Doctor_AllPatientAppointmentAdapter adapter = new Doctor_AllPatientAppointmentAdapter(getActivity(), clinicAppointmentList);
                DoctorAllPatientAppointmentAdapter adapter = new DoctorAllPatientAppointmentAdapter(getActivity().getApplicationContext(), clinicAppointmentList);
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
