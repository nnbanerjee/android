package com.mindnerves.meidcaldiary.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;
import java.util.List;

import Adapter.SelectDoctorAdapter;
import com.medico.application.MyApi;
import Model.Doctor;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 27-02-2015.
 */
public class SelectDoctorAstnt extends WizardStep {

    private ListView listViewManageDoctor;
    private SelectDoctorAdapter listAdapterManageDoctor;
    private List<Doctor> docSearchRes;
    public MyApi api;
    private ArrayList<Doctor> arrayNew;
    ArrayList<Doctor> removeList;
    private ArrayList<Doctor> selectedList;

    @ContextVariable
    public String docid;



    public SelectDoctorAstnt() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_doctor,
                container, false);

        listViewManageDoctor = (ListView)view.findViewById(R.id.list_select_doctor);
        getActivity().getActionBar().hide();
        listViewManageDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor d = (Doctor)listAdapterManageDoctor.getItem(position);
               docid= d.getDoctorId();

                Toast.makeText(getActivity(),"Selected Doc:"+d.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showDoctorList();


        return view;
    }

    public void showDoctorList()
    {
        docSearchRes = new ArrayList<Doctor>();
        api.getAllDoctors(new Callback<List<Doctor>>() {
            @Override
            public void success(List<Doctor> array, Response response) {

                System.out.println("Kb Array " + array.size());
                arrayNew = new ArrayList<Doctor>();
                if (array.size() == 0) {
                    Doctor docSr = new Doctor();

                    docSr.setName("No Doctor Found");
                    docSr.setLocation("None");
                    arrayNew.add(docSr);
                    System.out.println("IF " + arrayNew.size());


                } else {
                    arrayNew = (ArrayList<Doctor>) array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Arrays Value " + array.toString());

                System.out.println("Krb Url" + response.getUrl());
                docSearchRes.addAll(arrayNew);

                listAdapterManageDoctor = new SelectDoctorAdapter(getActivity(), arrayNew);

                listViewManageDoctor.setAdapter(listAdapterManageDoctor);

                //listAdapter.notifyDataSetChanged();
                System.out.println("Adapter list Count " + listAdapterManageDoctor.getCount());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
