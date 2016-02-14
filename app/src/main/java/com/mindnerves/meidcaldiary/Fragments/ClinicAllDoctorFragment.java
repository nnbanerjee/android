package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import Adapter.ClinicListItemAdapter;
import Adapter.DoctorClinicListItemAdapter;
import Application.MyApi;
import Model.ClinicDetailVm;
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
public class ClinicAllDoctorFragment extends Fragment {

    MyApi api;
    ListView clinicListView;
    String doctorId,doctor_email;
    ProgressDialog progress;
    public String bookDate = null;
    public String bookTime = null;
    public String shift = null;
    public Integer clinicId = null;
    TextView globalTv;
    Button back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_profile_fragment, container,false);
        Bundle args = getArguments();
        doctorId = args.getString("doctorId");
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        back = (Button)getActivity().findViewById(R.id.back_button);
        Global global = (Global) getActivity().getApplicationContext();
        List<DoctorSearchResponse> doctorSearchResponses = global.getDoctorSearchResponses();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String doctorEmail = session.getString("patient_DoctorEmail", null);
        for(int i = 0; i < doctorSearchResponses.size(); i++){
            if(doctorEmail.equals(doctorSearchResponses.get(i).getEmailID())){
                DoctorSearchResponse doctorSearchResponse = doctorSearchResponses.get(i);
                bookDate = doctorSearchResponse.bookDate;
                bookTime = doctorSearchResponse.bookTime;
                shift = doctorSearchResponse.shift;
                clinicId = doctorSearchResponse.clinicId;
            }
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackWindow();
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
        api.getAllClinicsDetailsList(doctorId, "1", new Callback<List<ClinicDetailVm>>() {
            @Override
            public void success(List<ClinicDetailVm> clinicDetailVm, Response response) {

                Global global = (Global) getActivity().getApplicationContext();
                global.setClinicDetailVm(clinicDetailVm);
                if(clinicId != null){
                    for(ClinicDetailVm detailVm : clinicDetailVm ){
                        if(clinicId == Integer.parseInt(detailVm.getClinicId())){
                            detailVm.setHasNextAppointment(true);
                            detailVm.setAppointmentDate(bookDate);
                            detailVm.setAppointmentTime(bookTime);
                            detailVm.setAppointmentShift(shift);
                        }
                    }
                }

                //ClinicListItemAdapter clinicListItemAdapter = new ClinicListItemAdapter(getActivity(),clinicDetailVm, bookDate, bookTime, shift, clinicId);
                DoctorClinicListItemAdapter clinicListItemAdapter = new DoctorClinicListItemAdapter(getActivity(),clinicDetailVm);
                clinicListView.setAdapter(clinicListItemAdapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        manageScreenIcon();
    }

    public void manageScreenIcon(){
        globalTv.setText("Clinics");
    }

    public void goBackWindow(){
        Bundle bun = getArguments();
        System.out.println("Condition Null check= "+(bun.getString("fragment") != null));
        Global go = (Global) getActivity().getApplicationContext();
        if(bun.getString("fragment") != null){
            String backString = bun.getString("fragment");
            System.out.println("Condition String Check = "+backString.equalsIgnoreCase("doctorAdapter"));
            if(backString.equalsIgnoreCase("doctorAdapter")){
                Bundle specialityBun = new Bundle();
                specialityBun.putString("specialization",go.specialityString);
                Fragment fragment = new AddDoctorSpecialityWise();
                fragment.setArguments(specialityBun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
            }
        }else{
            Bundle specialityBun = new Bundle();
            specialityBun.putString("specialization",go.specialityString);
            Fragment fragment = new AddDoctorSpecialityWise();
            fragment.setArguments(specialityBun);
            FragmentManager fragmentManger = getActivity().getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
        }
    }
}
