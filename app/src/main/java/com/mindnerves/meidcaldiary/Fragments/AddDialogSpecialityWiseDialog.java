package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoctorAddAdapter;
import Application.MyApi;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 9/24/15.
 */
public class AddDialogSpecialityWiseDialog extends DialogFragment {
    private ListView listView;
    private DoctorAddAdapter listAdapter;
    private ArrayList<DoctorSearchResponse> docSearchRes;
    public SharedPreferences session = null;
    public MyApi api;
    public String searchText = "";
    private RadioButton nameRadio,mobileNumberRadio,emailIdRadio;
    private Button searchButton;
    private EditText searchTv;
    private ArrayList<DoctorSearchResponse> nameList,finalList;
    private String patientId;
    private ArrayList<DoctorSearchResponse> arrayNew;
    private ArrayList<DoctorSearchResponse> list;
    ArrayList<DoctorSearchResponse> removeList;
    private String speciality = "";
    RelativeLayout mapLayout;
    Global go;
    public static AddDialogSpecialityWiseDialog newInstance() {
        return new AddDialogSpecialityWiseDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_doctor,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        listView = (ListView)view.findViewById(R.id.list_add_doctor);
        go = (Global)getActivity().getApplicationContext();
        searchTv = (EditText)view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mapLayout = (RelativeLayout)view.findViewById(R.id.map_layout);
        patientId = session.getString("sessionID",null);
        speciality = go.getSpecialityString();
        System.out.println("Speciality String::::::::"+speciality);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        ArrayList<DoctorSearchResponse> doctorList = go.getDoctorSpeciality();
        if(doctorList == null)
        {
            showListView();
        }
        else
        {

            if(doctorList.size() == 0)
            {
                DoctorSearchResponse docSr = new DoctorSearchResponse();
                docSr.setSelected(false);
                docSr.setName("No Doctor Found");
                docSr.setLocation("None");
                doctorList.add(docSr);
            }
            finalList = doctorList;
            listAdapter = new DoctorAddAdapter(getActivity(),finalList);
            listView.setAdapter(listAdapter);

        }
        nameRadio = (RadioButton)view.findViewById(R.id.search_name);
        mobileNumberRadio = (RadioButton)view.findViewById(R.id.search_phone);
        emailIdRadio = (RadioButton)view.findViewById(R.id.search_email);
        nameRadio.setChecked(true);
        searchButton = (Button)view.findViewById(R.id.button_search);
        searchButton.setVisibility(View.INVISIBLE);
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
                Boolean emailBoolean = emailIdRadio.isChecked();
                System.out.println("Mobile Boolean::::"+mobileBoolean);
                nameList = new ArrayList<DoctorSearchResponse>();

                if(nameBoolean)
                {

                    int len = docSearchRes.size();

                    for(int i=0;i<len;i++)
                    {
                        if(docSearchRes.get(i).getName().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            nameList.add(docSearchRes.get(i));
                        }
                    }

                    showListByName();
                }
                if(searchText.equals(""))
                {
                    showListView();
                }
            }
        });
        mobileNumberRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    nameRadio.setChecked(true);
                    mobileNumberRadio.setChecked(false);
                    go.setDoctorSpeciality(finalList);
                    System.out.println("I ma here in map/////////////////////");
                    MapDialog adf = MapDialog.newInstance();
                    adf.show(getFragmentManager(), "Dialog");
                }

        });
        return view;
    }
    public void showListByName()
    {
        finalList = new ArrayList<DoctorSearchResponse>();
        finalList.clear();
        int lenName = nameList.size();

        if(lenName == 0)
        {
            DoctorSearchResponse docSr = new DoctorSearchResponse();
            docSr.setSelected(false);
            docSr.setName("No Doctor Found");
            docSr.setLocation("None");
            docSr.setEmail(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        }
        else
        {
            finalList.addAll(nameList);
        }

        listAdapter = new DoctorAddAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }
    public void showListView()
    {

        finalList = new ArrayList<DoctorSearchResponse>();

        api.searchDoctors(searchText, new Callback<ArrayList<DoctorSearchResponse>>() {
            @Override
            public void success(ArrayList<DoctorSearchResponse> array, Response response) {

                System.out.println("Arrays Value "+array.toString());
                docSearchRes = new ArrayList<DoctorSearchResponse>();
                if(array.size() == 0)
                {
                    DoctorSearchResponse docSr = new DoctorSearchResponse();
                    docSr.setSelected(false);
                    docSr.setName("No Doctor Found");
                    docSr.setLocation("None");
                    array.add(docSr);
                    System.out.println("IF "+array.size());
                    docSearchRes = array;
                }
                else
                {
                    //docSearchRes = array;
                    for(DoctorSearchResponse doc : array)
                    {
                        if(doc.getSpeciality().equalsIgnoreCase(speciality))
                        {
                            docSearchRes.add(doc);
                        }
                    }
                    removeList = new ArrayList<DoctorSearchResponse>();
                    api.getPatientsDoctors(patientId,new Callback<List<DoctorSearchResponse>>() {
                        @Override
                        public void success(List<DoctorSearchResponse> listArray, Response response) {
                            System.out.println("Kb Array " + listArray.size());
                            arrayNew = new ArrayList<DoctorSearchResponse>();
                            if (listArray.size() == 0) {
                                DoctorSearchResponse docSr = new DoctorSearchResponse();

                                docSr.setSelected(false);
                                docSr.setName("No Doctor Found");
                                docSr.setLocation("None");
                                arrayNew.add(docSr);
                                System.out.println("IF " + arrayNew.size());


                            } else {
                                arrayNew = (ArrayList<DoctorSearchResponse>) listArray;
                                System.out.println("Else " + arrayNew.size());
                            }

                            System.out.println("Arrays Value " + listArray.toString());
                            list = arrayNew;
                            for(int i=0;i<docSearchRes.size();i++)
                            {
                                for(int j=0;j<list.size();j++)
                                {
                                    if(docSearchRes.get(i).getName().equals(list.get(j).getName())) {
                                        removeList.add(docSearchRes.get(i));
                                    }
                                }
                            }
                            docSearchRes.removeAll(removeList);
                            if(docSearchRes.size() == 0)
                            {
                                DoctorSearchResponse docSr = new DoctorSearchResponse();
                                docSr.setSelected(false);
                                docSr.setName("No Doctor Found");
                                docSr.setLocation("None");
                                docSearchRes.add(docSr);
                            }
                            System.out.println("DocsearchResponse::::"+docSearchRes.size());

                            System.out.println("Krb Url"+response.getUrl());

                            finalList.addAll(docSearchRes);

                            listAdapter = new DoctorAddAdapter(getActivity(),finalList);
                            listView.setAdapter(listAdapter);
                            //listAdapter.notifyDataSetChanged();
                            System.out.println("Adapter list Count "+listAdapter.getCount());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
