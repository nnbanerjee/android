package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.Date;
import java.util.List;

import Adapter.ClinicDoctorListAdapter;
import com.medico.application.MyApi;
import Model.AppointmentSlotsByDoctor;
import com.medico.model.DoctorId;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class DoctorAllClinics extends Fragment {

    public MyApi api;
    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;
    TextView globalTv,accountName;
    ImageView addClinic;
    Button back,drawar,logout;
    ImageView profilePicture;//,medicoLogo,medicoText;
    RelativeLayout profileLayout;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);
        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        addClinic = (ImageView)view.findViewById(R.id.add_clinic_appointment);
        back = (Button)getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        logout = (Button)getActivity().findViewById(R.id.logout);
       // medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        type = session.getString("loginType",null);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        addClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageClinicFragment();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,frag2,"Add_Clinic");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        showClinicsList();
        manageScreenIcon();
        return view;
    }
    public void manageScreenIcon()
    {
        globalTv.setText("Manage Appointments");
        drawar.setVisibility(View.GONE);
      //  medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }
    public void showClinicsList(){

        String doctorId = session.getString("sessionID", null);
        Date date = new Date();


        api.getClinicsByDoctor(new DoctorId(doctorId), new Callback<List<AppointmentSlotsByDoctor>>() {
            @Override
            public void success(List<AppointmentSlotsByDoctor> appointmentSlotsByDoctor, Response response) {

                //[{"clinicId":2,"clinicName":"demo2","slots":[{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5},{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,4,5,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5},{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,4,5,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5}],"upcomingAppointment":null,"lastAppointmentl":null}]
                Global global = (Global) getActivity().getApplicationContext();
                // global.setClinicDetailVm(clinicDetailVm);
                global.setAppointmentSlotsByDoctorObj(appointmentSlotsByDoctor);
                //ClinicPatientAdapter clinicListItemAdapter = new ClinicPatientAdapter(getActivity(), clinicPatientAppointmentsObj.getClinicList(), appointmentSlotsByDoctor);
                //clinicListView.setAdapter(clinicListItemAdapter);
             //   progress.dismiss();


                // global.setAllClinicsList(clinicList);
                ClinicDoctorListAdapter adapter = new ClinicDoctorListAdapter(getActivity(), appointmentSlotsByDoctor);
                doctorListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
      /*  api.getAllDoctorClinics(doctorId,date,new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinicList, Response response) {

                Global global = (Global) getActivity().getApplicationContext();
                global.setAllClinicsList(clinicList);
                ClinicDoctorListAdapter adapter = new ClinicDoctorListAdapter(getActivity(), clinicList);
                doctorListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });*/
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
        globalTv.setText(type);
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        logout.setVisibility(View.GONE);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
      //  medicoLogo.setVisibility(View.VISIBLE);
       // medicoText.setVisibility(View.VISIBLE);
    }


}
