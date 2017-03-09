package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import Adapter.DoctorShowAdapter;
import com.medico.application.MyApi;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 30-10-2015.
 */
public class ShowDoctorSpecialitywise extends Fragment {
    private static final String TAG = AddDoctorSpecialityWise.class.getName();
    private ListView listView;
    private DoctorShowAdapter listAdapter;
    private ArrayList<DoctorSearchResponse> docSearchRes, selectedId;
    private ArrayList<DoctorSearchResponse> selectedList;
    public SharedPreferences session = null;
    public MyApi api;
    public String searchText = "";
    private RadioButton nameRadio, mobileNumberRadio, emailIdRadio;
    private Button searchButton, addNewButton, doneButton;
    private EditText searchTv;
    private ArrayList<DoctorSearchResponse> nameList, mobileNumberList, emailList, finalList;
    private ManageDoctorFragment fragment;
    private String patientId;
    private String doctorsIds = "";
    private ArrayList<DoctorSearchResponse> arrayNew;
    private ArrayList<DoctorSearchResponse> list;
    ArrayList<DoctorSearchResponse> removeList;
    private String speciality = "";
    RelativeLayout mapLayout;
    Button filter;
    Global go;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_doctor, container, false);
        BackStress.staticflag = 0;
        listView = (ListView) view.findViewById(R.id.list_add_doctor);
        TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Doctor");
        go = (Global) getActivity().getApplicationContext();
        searchTv = (EditText) view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mapLayout = (RelativeLayout) view.findViewById(R.id.map_layout);
        patientId = session.getString("sessionID", null);
        Bundle bun = getArguments();
        speciality = bun.get("specialization").toString();
        System.out.println("Speciality Fragment::::::" + speciality);
        getActivity().getActionBar().hide();
        filter = (Button) view.findViewById(R.id.filter_home);
        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Doctor Specialities");
                Fragment fragment = new ShowSpecialityDoctor();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
                final Button back = (Button) getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);

            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        ArrayList<DoctorSearchResponse> doctorList = go.getDoctorSpeciality();
        if (doctorList == null) {
            showListView();
        } else {
            if (doctorList.size() == 0) {
                DoctorSearchResponse docSr = new DoctorSearchResponse();
                docSr.setSelected(false);
                docSr.setName("No Doctor Found");
                docSr.setLocation("None");
                doctorList.add(docSr);
            }
            finalList = doctorList;
            for(DoctorSearchResponse doctor : finalList){
                doctor.setSelected(true);
            }
            listAdapter = new DoctorShowAdapter(getActivity(), finalList);
            listView.setAdapter(listAdapter);
        }
        nameRadio = (RadioButton) view.findViewById(R.id.search_name);
        mobileNumberRadio = (RadioButton) view.findViewById(R.id.search_phone);
        emailIdRadio = (RadioButton) view.findViewById(R.id.search_email);
        nameRadio.setChecked(true);
        searchButton = (Button) view.findViewById(R.id.button_search);
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
                System.out.println("Mobile Boolean::::" + mobileBoolean);
                nameList = new ArrayList<DoctorSearchResponse>();
                mobileNumberList = new ArrayList<DoctorSearchResponse>();
                emailList = new ArrayList<DoctorSearchResponse>();
                if (nameBoolean) {

                    int len = docSearchRes.size();

                    for (int i = 0; i < len; i++) {
                        if (docSearchRes.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                            nameList.add(docSearchRes.get(i));
                        }
                    }

                    showListByName();
                }
                if (searchText.equals("")) {
                    showListView();
                }
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filter = FilterDialog.newInstance();
                filter.show(getFragmentManager(), "Dialog");
                go.setDoctorSpeciality(docSearchRes);
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
                    bun.putSerializable("doctors", finalList);
                    bun.putString("specialization", speciality);
                    go.setDoctorSpeciality(finalList);
                    System.out.println("I ma here in map/////////////////////");
                    listView.setVisibility(View.GONE);
                    mapLayout.setVisibility(View.VISIBLE);
                    Fragment fragment = new DoctorMap();
                    fragment.setArguments(bun);
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
        addNewButton = (Button) view.findViewById(R.id.add_new_doctor);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddNewDoctor();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_New_Doc");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        doneButton = (Button) view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if (result.equals("No doctors")) {
                    Toast.makeText(getActivity(), "No Doctors In List", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                } else if (result.equals("No Selected Doctors")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Doctors Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {
                    int len = selectedList.size();
                    int j = len - 1;

                    for (int i = 0; i < len; i++) {
                        if (i == j) {
                            doctorsIds = doctorsIds + selectedList.get(i).getDoctorId();
                        } else {
                            doctorsIds = doctorsIds + selectedList.get(i).getDoctorId() + ",";
                        }
                    }
                    System.out.println("Doctors IDs " + doctorsIds);

                    api.patientDoctor(patientId, doctorsIds, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();
                            if (status == 200) {
                                System.out.println("Krb Url ::::" + response.getUrl());
                                Toast.makeText(getActivity(), "Doctors Are Added", Toast.LENGTH_LONG).show();
                                Fragment frag2 = new ManageDoctorFragment();
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, frag2, null);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();
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
        });
        return view;
    }
    public void showListView() {

        finalList = new ArrayList<DoctorSearchResponse>();
        api.searchDoctors(searchText, new Callback<ArrayList<DoctorSearchResponse>>() {
            @Override
            public void success(ArrayList<DoctorSearchResponse> array, Response response) {

                System.out.println("Arrays Value " + array.toString());
                docSearchRes = new ArrayList<DoctorSearchResponse>();
                if (array.size() == 0) {
                    DoctorSearchResponse docSr = new DoctorSearchResponse();
                    docSr.setSelected(false);
                    docSr.setName("No Doctor Found");
                    docSr.setLocation("None");
                    array.add(docSr);
                    System.out.println("IF " + array.size());
                    docSearchRes = array;
                } else {
                    for (DoctorSearchResponse doc : array) {
                        if (doc.getSpeciality().equalsIgnoreCase(speciality)) {
                            docSearchRes.add(doc);
                        }
                    }
                    for(DoctorSearchResponse doctor : docSearchRes){
                        doctor.setSelected(true);
                    }
                    finalList.addAll(docSearchRes);

                    listAdapter = new DoctorShowAdapter(getActivity(), finalList);
                    listView.setAdapter(listAdapter);
                    System.out.println("Adapter list Count " + listAdapter.getCount());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showListByName() {
        finalList = new ArrayList<DoctorSearchResponse>();
        finalList.clear();
        int lenName = nameList.size();

        if (lenName == 0) {
            DoctorSearchResponse docSr = new DoctorSearchResponse();
            docSr.setSelected(false);
            docSr.setName("No Doctor Found");
            docSr.setLocation("None");
            docSr.setEmail(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        } else {
            finalList.addAll(nameList);
        }
        for(DoctorSearchResponse doctor : finalList){
            doctor.setSelected(true);
        }
        listAdapter = new DoctorShowAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

   

    public String doValidation() {
        int len = docSearchRes.size();

        if (len == 0) {

            return "No doctors";
        } else {
            selectedList = new ArrayList<DoctorSearchResponse>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    selectedList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getName() + " Value" + docSearchRes.get(i).isSelected());
                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Doctors";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }

        }

    }
}
