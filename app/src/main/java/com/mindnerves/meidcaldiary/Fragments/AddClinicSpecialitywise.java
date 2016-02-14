package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import Application.MyApi;
import Model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 29-10-2015.
 */
public class AddClinicSpecialitywise extends Fragment {
    private ListView listView;
    public MyApi api;
    public String searchText = "";
    private ClinicSpecialityAdapter adapter;
    private Button searchButton, addNewButton, doneButton;
    private EditText searchTv;
    private ArrayList<Clinic> clinicList,finalList;
    private String doctorId = "";
    private String speciality = "";
    Button filter;
    Global go;
    SharedPreferences session;
    RelativeLayout radioLayout;
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
        radioLayout = (RelativeLayout)view.findViewById(R.id.layout2);
        doctorId = session.getString("sessionID", null);
        Bundle bun = getArguments();
        speciality = bun.get("specialization").toString();
        System.out.println("Speciality Fragment::::::" + speciality);
        getActivity().getActionBar().hide();
        radioLayout.setVisibility(View.GONE);
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
        showListView(speciality);
        return view;
    }
    public void goBack(){
        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinics Specialities");
        go.setAllClinicsList(null);
        Fragment fragment = new ShowClinicSpecialities();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();

    }
    public void showListView(final String speciality){
        clinicList = new ArrayList<Clinic>();
        api.getAllClinics(new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinics, Response response) {
                for(Clinic c : clinics){
                    if(c.getSpeciality().equals(speciality)){
                        c.setSelected(false);
                        clinicList.add(c);
                    }
                }
                api.doctorClinics(doctorId,new Callback<List<Clinic>>() {
                    @Override
                    public void success(List<Clinic> clinicsDoctor, Response response) {
                        for(Clinic cl : clinicList){
                            for(Clinic c : clinicsDoctor){
                                if(c.getIdClinic().equals(cl.getIdClinic())){
                                    cl.setSelected(true);
                                    break;
                                }
                            }
                        }
                        for(Clinic cli : clinicList){
                            System.out.println("Clinic Name= "+cli.getClinicName());
                            System.out.println("Clinic status= "+cli.isSelected());
                        }

                        adapter = new ClinicSpecialityAdapter(getActivity(),clinicList);
                        listView.setAdapter(adapter);
                    }
                   @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
