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

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.ClinicSpecialityAdapter;
import com.medico.application.MyApi;
import com.medico.model.Clinic;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 20-10-2015.
 */
public class ShowClinicsSpecialitwise extends Fragment {
    private ListView listView;
    public MyApi api;
    public String searchText = "";
    private ClinicSpecialityAdapter adapter;
    private RadioButton nameRadio, mobileNumberRadio, emailIdRadio;
    private Button searchButton, addNewButton, doneButton;
    private EditText searchTv;
    private ArrayList<Clinic> clinicList,finalList;
    private String patientId;
    private String doctorsIds = "";
    private String speciality = "";
    RelativeLayout mapLayout;
    Button filter;
    Global go;
    SharedPreferences session;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_doctor, container, false);
        listView = (ListView) view.findViewById(R.id.list_add_doctor);
        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Clinic");
        go = (Global) getActivity().getApplicationContext();
        searchTv = (EditText) view.findViewById(R.id.searchET);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mapLayout = (RelativeLayout) view.findViewById(R.id.map_layout);
        patientId = session.getString("sessionID", null);
        Bundle bun = getArguments();
        speciality = bun.get("specialization").toString();
        System.out.println("Speciality Fragment::::::" + speciality);
        getActivity().getActionBar().hide();
        nameRadio = (RadioButton) view.findViewById(R.id.search_name);
        mobileNumberRadio = (RadioButton) view.findViewById(R.id.search_phone);
        emailIdRadio = (RadioButton) view.findViewById(R.id.search_email);
        nameRadio.setChecked(true);
        searchButton = (Button) view.findViewById(R.id.button_search);
        searchButton.setVisibility(View.INVISIBLE);
        filter = (Button) view.findViewById(R.id.filter_home);
        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
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
                if(nameBoolean){
                    clinicList = new ArrayList<Clinic>();
                    for(Clinic clinic : finalList){
                        if(clinic.getClinicName().contains(searchText)){
                            clinicList.add(clinic);
                        }
                    }
                    finalList = clinicList;
                    go.setAllClinicsList(finalList);
                    adapter = new ClinicSpecialityAdapter(getActivity(),finalList);
                    listView.setAdapter(adapter);
                }
                if (searchText.equals("")) {
                    showListView();
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
        mobileNumberRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumberRadio.isChecked()) {
                    Fragment f = getFragmentManager()
                            .findFragmentById(R.id.map_layout);
                    if (f != null)
                        getFragmentManager().beginTransaction().remove(f).commit();
                    Bundle bun = new Bundle();
                    bun.putSerializable("clinics", finalList);
                    bun.putString("specialization", speciality);
                    go.setAllClinicsList(finalList);
                    System.out.println("I ma here in map/////////////////////");
                    listView.setVisibility(View.GONE);
                    mapLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new ClinicMap();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.map_layout, fragment, "Manage_Doctor").addToBackStack(null).commit();

                }
            }
        });
        List<Clinic> clinicList = go.getAllClinicsList();
        if(clinicList == null){
            System.out.println("Null Found::::::::");
            showListView();
        }else{
            System.out.println("Not Null Found::::::::");
            finalList = (ArrayList<Clinic>)clinicList;
            go.setAllClinicsList(finalList);
            adapter = new ClinicSpecialityAdapter(getActivity(),finalList);
            listView.setAdapter(adapter);
        }
        return view;
    }
    public void goBack(){
        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinics Specialities");
        go.setAllClinicsList(null);
        Fragment fragment = new ShowSpecialityClinics();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();

    }
    public void showListView(){
        finalList = new ArrayList<Clinic>();
        api.getAllPatientClinics(patientId,new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinics, Response response) {
                for(Clinic clinic : clinics){
                    if(clinic.getSpeciality().equals(go.getSpecialityString())){
                        finalList.add(clinic);
                    }
                }
                go.setAllClinicsList(finalList);
                adapter = new ClinicSpecialityAdapter(getActivity(),finalList);
                listView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
