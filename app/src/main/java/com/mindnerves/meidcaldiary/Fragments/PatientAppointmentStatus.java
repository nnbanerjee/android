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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.PatientMenusManage;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;
import com.mindnerves.meidcaldiary.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import Adapter.ClinicAppointmentListAdapter;
import com.medico.application.MyApi;
import com.medico.util.DatabaseHandler;
import Model.AppointmentDB;
import com.medico.model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 09-10-2015.
 */
public class PatientAppointmentStatus extends Fragment {
    public MyApi api;
    SharedPreferences session;
    ListView upcomingList, pastList;
    ProgressDialog progress;
    TextView globalTv,accountName;
    Button drawar,logout,back,refresh;
    ClinicAppointmentListAdapter upcomingAdapter,pastAdapter;
    ArrayList<Clinic> upcomingClinicList,pastClinicList;
    DatabaseHandler databaseHandler;
    ImageView addAppointment,profilePicture;//,medicalLogo,medicalText;
    RelativeLayout profileLayout;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_all_appointments, container, false);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        upcomingList = (ListView) view.findViewById(R.id.upcoming_appointment_list);
        pastList = (ListView) view.findViewById(R.id.past_appointment_list);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        addAppointment = (ImageView)view.findViewById(R.id.add_appointment);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        back = (Button) getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
    //    medicalLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
     //   medicalText = (ImageView)getActivity().findViewById(R.id.home_icon);
        type = session.getString("loginType",null);
        BackStress.staticflag =1;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientAddAppointment();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showClinicsList();
        manageScreenIcons();

        upcomingList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        pastList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }
    public void manageScreenIcons(){
        globalTv.setText("Appointments");
        BackStress.staticflag = 0;
        profileLayout.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        logout.setBackgroundResource(R.drawable.home_jump);
        drawar.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
    //    medicalLogo.setVisibility(View.GONE);
     //   medicalText.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }
    public void showClinicsList(){
        final String patientId = session.getString("sessionID", null);
        api.getPatientAppointment(patientId,new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinics, Response response) {
                upcomingClinicList = new ArrayList<Clinic>();
                pastClinicList = new ArrayList<Clinic>();
                databaseHandler = new DatabaseHandler(getActivity());
                Global global = (Global) getActivity().getApplicationContext();
                global.setAllClinicsList(clinics);
                ArrayList<AppointmentDB> appointments = databaseHandler.getAllPatientAppointment(patientId);
                for(Clinic clinic : clinics){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = null;
                    try {
                        date = formatter.parse(clinic.appointmentDate);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    String[] timeValue;
                    Calendar calOne = Calendar.getInstance();
                    calOne.setTime(date);
                    timeValue = clinic.appointmentTime.split(":");
                    int hour1 = Integer.parseInt(timeValue[0].trim());
                    int min1 = Integer.parseInt(timeValue[1].trim().split(
                            "[a-zA-Z ]+")[0]);
                    calOne.set(Calendar.HOUR, hour1);
                    calOne.set(Calendar.MINUTE, min1);
                    String strAM_PM = timeValue[1].replaceAll("[0-9]", "");
                    if (strAM_PM.equals("AM")) {
                        calOne.set(Calendar.AM_PM, 0);
                    } else {
                        calOne.set(Calendar.AM_PM, 1);
                    }
                    Calendar calTwo = Calendar.getInstance();
                    System.out.println("Condition::::::"+(calOne.getTimeInMillis() < calTwo.getTimeInMillis()));
                    System.out.println("Appoitment Date And Time= "+clinic.appointmentDate+" "+clinic.appointmentTime);
                    System.out.println("Appointment in mili= "+ calOne.getTimeInMillis());
                    System.out.println("Current in mili= "+ calTwo.getTimeInMillis());
                    for(AppointmentDB appointmentDB : appointments){
                        System.out.println("Appointment Status DB= "+appointmentDB.getAppointment_status());
                        System.out.println("Appointment Date= "+appointmentDB.getAppointment_date());
                        System.out.println("Appointment Time= "+appointmentDB.getAppointment_time());


                        System.out.println("Alarm Condition:::::::"+(appointmentDB.getAppointment_time().equals(clinic.appointmentTime)));
                        if((appointmentDB.getAppointment_date().equals(clinic.appointmentDate))){
                               clinic.alarmFlag = Boolean.parseBoolean(appointmentDB.getAppointment_status());
                               break;
                        }
                    }
                    if (calOne.getTimeInMillis() < calTwo.getTimeInMillis()) {
                        clinic.upcomingFlag = false;
                        pastClinicList.add(clinic);
                    }else{
                        clinic.upcomingFlag = true;
                        upcomingClinicList.add(clinic);
                    }
                }
                if(upcomingClinicList.size() == 0){
                    Clinic clinic = new Clinic();
                    clinic.setClinicName("No Appointment Found");
                    upcomingClinicList.add(clinic);
                }
                if(pastClinicList.size() == 0){
                    Clinic clinic = new Clinic();
                    clinic.setClinicName("No Appointment Found");
                    pastClinicList.add(clinic);
                }
                System.out.println("UpcomingList= "+upcomingClinicList.size());
                System.out.println("PastList= "+pastClinicList.size());
                upcomingAdapter = new ClinicAppointmentListAdapter(getActivity(),upcomingClinicList);
                pastAdapter = new ClinicAppointmentListAdapter(getActivity(),pastClinicList);
                upcomingList.setAdapter(upcomingAdapter);
                Utility.setListViewHeightBasedOnChildren(upcomingList);
                pastList.setAdapter(pastAdapter);
                Utility.setListViewHeightBasedOnChildren(pastList);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(getActivity(), retrofitError.toString(), Toast.LENGTH_LONG).show();
                retrofitError.printStackTrace();
            }
        });
    }

    public void goToBack() {
        globalTv.setText(""+type);
        Fragment fragment = new PatientMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        logout.setVisibility(View.GONE);
        drawar.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
     //   medicalLogo.setVisibility(View.VISIBLE);
     //   medicalText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
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
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }
}
