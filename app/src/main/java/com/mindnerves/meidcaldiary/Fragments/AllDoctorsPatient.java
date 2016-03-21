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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.LoggingInterceptor;
import com.mindnerves.meidcaldiary.R;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import Adapter.DoctorListAdapter;
import Adapter.DoctorPatientListAdapter;
import Adapter.PatientListAdapter;
import Application.MyApi;
import Model.AllPatients;
import Model.DoctorId;
import Model.DoctorSearchResponse;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AllDoctorsPatient extends Fragment {

    public MyApi api;
    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;
    TextView globalTv,accountName;
    Button drawar,logout,back;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    public  String  doctorId="";
    //ImageView medicoLogo,medicoText;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);
        setHasOptionsMenu(true);
        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        back = (Button)getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setVisibility(View.GONE);
       // medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
       // medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        type = session.getString("loginType",null);
        doctorId = session.getString("id",null);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new LoggingInterceptor());

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)

                .build();

        api = restAdapter.create(MyApi.class);
        manageScreenIcons();
        showDoctorList();
        return view;
    }

    //This will show all patients list for logged in doctor
    public void showDoctorList(){


        DoctorId doc= new DoctorId(doctorId);
        api.getPatientProfileList(doc, new Callback<List<AllPatients>>() {
            @Override
            public void success(final List<AllPatients> allPatientsProfiles, Response response) {
                Global global = (Global) getActivity().getApplicationContext();
                global.setAllPatients(allPatientsProfiles);
                DoctorPatientListAdapter adapter = new DoctorPatientListAdapter(getActivity(), allPatientsProfiles);
                doctorListView.setAdapter(adapter);
                doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("doctorId", doctorId);
                        editor.putString("patientId", allPatientsProfiles.get(i).getpatientId());
                        editor.commit();
                        Bundle bun = new Bundle();
                        bun.putString("fragment", "doctorPatientListAdapter");
                        Fragment fragment = new AllDoctorPatientAppointment();
                        fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                });
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void manageScreenIcons()
    {
        drawar.setVisibility(View.GONE);
       // medicoLogo.setVisibility(View.GONE);
       // medicoText.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        globalTv.setText("Patient Profiles");
        back.setVisibility(View.VISIBLE);
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
       // medicoLogo.setVisibility(View.VISIBLE);
       // medicoText.setVisibility(View.VISIBLE);
    }
}
