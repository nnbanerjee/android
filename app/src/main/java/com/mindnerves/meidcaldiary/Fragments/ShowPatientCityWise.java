package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import java.util.List;
import Adapter.PatientAddAdapter;
import com.medico.application.MyApi;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 22-10-2015.
 */
public class ShowPatientCityWise extends Fragment {
    private ListView listView;
    public SharedPreferences session = null;
    public MyApi api;
    public String searchText = "";
    private RadioButton nameRadio, mobileNumberRadio, emailIdRadio;
    private Button searchButton,back;
    private EditText searchTv;
    RelativeLayout mapLayout;
    Button filter;
    TextView globalTv;
    PatientAddAdapter patientAdapter;
    Global go;
    ArrayList<Patient> finalPatientList = new ArrayList<Patient>();
    String doctorId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_doctor, container, false);
        BackStress.staticflag = 0;
        listView = (ListView) view.findViewById(R.id.list_add_doctor);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        go = (Global) getActivity().getApplicationContext();
        searchTv = (EditText) view.findViewById(R.id.searchET);
        mapLayout = (RelativeLayout) view.findViewById(R.id.map_layout);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        filter = (Button) view.findViewById(R.id.filter_home);
        back = (Button) getActivity().findViewById(R.id.back_button);
        nameRadio = (RadioButton) view.findViewById(R.id.search_name);
        mobileNumberRadio = (RadioButton) view.findViewById(R.id.search_phone);
        emailIdRadio = (RadioButton) view.findViewById(R.id.search_email);
        searchButton = (Button) view.findViewById(R.id.button_search);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        final ArrayList<Patient> patientList = go.getPatientList();
        if(patientList != null){
            finalPatientList = new ArrayList<Patient>();
            api.getDoctorsPatients(doctorId,new Callback<List<Patient>>() {
                @Override
                public void success(List<Patient> patients, Response response) {
                    patientAdapter = new PatientAddAdapter(getActivity(),finalPatientList);
                    listView.setAdapter(patientAdapter);
                    for(Patient pat : patientList){
                        int flag = 0;
                        for(Patient patient : patients){
                            if(patient.getPatientId().equals(pat.getPatientId())){
                                pat.setSelected(true);
                                finalPatientList.add(pat);
                                flag = 1;
                                break;
                            }
                        }
                        if(flag == 0){
                            finalPatientList.add(pat);
                        }

                    }
                    patientAdapter = new PatientAddAdapter(getActivity(),finalPatientList);
                    listView.setAdapter(patientAdapter);

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            });

        }
        searchTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = searchTv.getText().toString();
                Boolean nameBoolean = nameRadio.isChecked();
                Boolean mobileBoolean = mobileNumberRadio.isChecked();
                finalPatientList = new ArrayList<Patient>();
                System.out.println("Size of finalPatientList= "+finalPatientList.size());
                if(nameBoolean){
                    for(Patient patient : patientList){
                        if(patient.getName().contains(searchText)){
                            finalPatientList.add(patient);
                        }
                    }

                    patientAdapter = new PatientAddAdapter(getActivity(),finalPatientList);
                    listView.setAdapter(patientAdapter);
                }else if(searchText.equals("")){
                    finalPatientList = patientList;
                    patientAdapter = new PatientAddAdapter(getActivity(),finalPatientList);
                    listView.setAdapter(patientAdapter);
                }
            }
        });
        mobileNumberRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumberRadio.isChecked()) {
                    Fragment f = getFragmentManager()
                            .findFragmentById(R.id.map_layout);
                    if (f != null)
                        getFragmentManager().beginTransaction().remove(f).commit();
                    go.setPatientList(finalPatientList);
                    System.out.println("I ma here in map/////////////////////");
                    listView.setVisibility(View.GONE);
                    mapLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new PatientMap();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.map_layout, fragment, "Manage_Doctor").addToBackStack(null).commit();
                }
                
            }
        });
        nameRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameRadio.isChecked()) {
                    listView.setVisibility(View.VISIBLE);
                    mapLayout.setVisibility(View.GONE);
                    mobileNumberRadio.setChecked(false);
                    nameRadio.setChecked(true);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        manageScreenIcons();
        return view;
    }
    public void manageScreenIcons(){
        globalTv.setText("Add Patient");
        back.setVisibility(View.VISIBLE);
        nameRadio.setChecked(true);
        searchButton.setVisibility(View.INVISIBLE);
    }
    public void goBack(){
        Fragment fragment;
        fragment = new ShowPatients();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
    }
}
