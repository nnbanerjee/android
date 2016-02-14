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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.DependencyAdapter;
import Adapter.DependencyAdapterDoctor;
import Application.MyApi;
import Model.AddDependent;
import Model.AddDependentElement;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Mar-15.
 */
public class AddDependencyDoctor extends Fragment {

    private ListView listView;
    private EditText searchTv;
    private String patientId = "";
    public MyApi api;
    private Button searchButton,backButton;
    private ArrayList<Patient> nameList,mobileNumberList,emailList,finalList;
    private String searchText = "";
    private RadioButton nameRadio,mobileNumberRadio,emailIdRadio;
    private ArrayList<Patient> selectedList;
    private List<Patient> docSearchRes,list;
    private DependencyAdapterDoctor listAdapter;
    private Button buttonAdd;


    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new ManageDendencyDoctor();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2, null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dependent,container,false);
        listView = (ListView)view.findViewById(R.id.list_add_patient);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Dependency");
        searchTv = (EditText)view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
        BackStress.staticflag = 0;
        getActivity().getActionBar().hide();
        Global go = (Global)getActivity().getApplicationContext();
        list = go.getDependencyPatient();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageDendencyDoctor();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showListView();

        nameRadio = (RadioButton)view.findViewById(R.id.search_name);
        mobileNumberRadio = (RadioButton)view.findViewById(R.id.search_phone);
        emailIdRadio = (RadioButton)view.findViewById(R.id.search_email);
        searchButton = (Button) view.findViewById(R.id.button_search);
        nameRadio.setChecked(true);
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

                nameList = new ArrayList<Patient>();
                mobileNumberList = new ArrayList<Patient>();
                emailList = new ArrayList<Patient>();

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

                if(mobileBoolean)
                {
                    int len = docSearchRes.size();

                    for(int i=0;i<len;i++)
                    {
                        System.out.println("Mobile Number::::::::"+docSearchRes.get(i).getMobileNumber());
                    }

                    for(int i=0;i<len;i++)
                    {
                        if(docSearchRes.get(i).getMobileNumber().contains(searchText))
                        {
                            mobileNumberList.add(docSearchRes.get(i));
                        }
                    }
                    showListByMobile();
                }

                if(emailBoolean) {
                    int len = docSearchRes.size();

                    for (int i = 0; i < len; i++) {
                        if (docSearchRes.get(i).getEmailID().toLowerCase().contains(searchText.toLowerCase())) {
                            emailList.add(docSearchRes.get(i));
                        }
                    }

                    showListByEmail();
                }

            }
        });
        searchButton.setVisibility(View.INVISIBLE);
        backButton = (Button)view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new ManageDendencyDoctor();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });



        buttonAdd = (Button)view.findViewById(R.id.add_new);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if(result.equals("Normal")) {
                    System.out.println("Access Level Code ");

                    for (Patient p : selectedList) {
                        System.out.println("Access Code:::::" + p.getAccessLevel());
                    }
                }


               if (result.equals("No Patient")) {
                    Toast.makeText(getActivity(), "No Patient In List", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                } else if (result.equals("No Selected Patient")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Patient Selected", Toast.LENGTH_LONG).show();
                }
                else if (result.equals("Normal")) {
                    int len = selectedList.size();
                    ArrayList<AddDependentElement> selectedElements = new ArrayList<AddDependentElement>();

                    for(Patient pat : selectedList)
                    {
                        AddDependentElement element = new AddDependentElement();
                        element.setId(pat.getPatientId());

                        if(pat.getAccessLevel().equals("Read Only"))
                        {
                            element.setAccessLevel("R");
                        }

                        if(pat.getAccessLevel().equals("Full Access"))
                        {
                            element.setAccessLevel("F");
                        }

                        selectedElements.add(element);
                    }

                    System.out.println("Selected:::::::::::");

                    for(AddDependentElement a : selectedElements)
                    {
                        System.out.println("ID::::::::"+a.getId());
                        System.out.println("Access Level:::::::::::"+a.getAccessLevel());
                    }

                    AddDependent add = new AddDependent();
                    add.setPatientId(patientId);
                    add.setDependents(selectedElements);



                    api.addPatientDependent(add,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();

                            System.out.println("URl "+response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Dependency  Added", Toast.LENGTH_LONG).show();
                                Fragment frag = new ManageDendencyFragment();
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, frag,null);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    });
                }



             }
        });
        return view;
    }

    public void showListView()
    {

       System.out.println("I am showListView Function");
       finalList = new ArrayList<Patient>();

       api.searchPatients(searchText, new Callback<ArrayList<Patient>>() {
           @Override
           public void success(ArrayList<Patient> array, Response response) {
               System.out.println("Arrays Value " + array.toString());
               docSearchRes = new ArrayList<Patient>();
               if (array.size() == 0) {
                   Patient docSr = new Patient();
                   docSr.setSelected(false);
                   docSr.setName("No Patient Found");
                   docSr.setLocation("None");
                   array.add(docSr);
                   System.out.println("IF " + array.size());
                   docSearchRes = array;

               } else {

                   Patient removePatient = new Patient();
                   ArrayList<Patient> newArray = array;
                   ArrayList<Patient> removeList = new ArrayList<Patient>();
                   docSearchRes = array;

                   for(Patient patient : newArray)
                   {
                       if(patient.getEmailID().equals(patientId))
                       {
                           removePatient = patient;
                           System.out.println("id::::::::::::::"+patient.getPatientId());
                           System.out.println("id::::::::::::::"+patient.getName());

                       }
                   }


                   docSearchRes.remove(removePatient);

                   System.out.println("Else " + docSearchRes.size());
                   System.out.println("After Removing::::::::::::::");

                   for(int i=0;i<docSearchRes.size();i++)
                   {
                       for(int j=0;j<list.size();j++)
                       {
                           if(docSearchRes.get(i).getPatientId().equals(list.get(j).getPatientId()))
                           {
                                removeList.add(docSearchRes.get(i));
                           }
                       }
                   }

                   docSearchRes.removeAll(removeList);
                   if(docSearchRes.size() == 0)
                   {
                       Patient docSr = new Patient();
                       docSr.setSelected(false);
                       docSr.setName("No Patient Found");
                       docSr.setLocation("None");
                       docSearchRes.add(docSr);
                   }
               }

               System.out.println("Krb Url" + response.getUrl());

               finalList.addAll(docSearchRes);

              listAdapter = new DependencyAdapterDoctor(getActivity(), finalList);

               listView.setAdapter(listAdapter);
               //listAdapter.notifyDataSetChanged();

           }

           @Override
           public void failure(RetrofitError error) {

               Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
               error.printStackTrace();

           }
       });
    }

   public void showListByName()
   {
       finalList = new ArrayList<Patient>();

       int lenName = nameList.size();

       if(lenName == 0)
       {
           Patient docSr = new Patient();
           docSr.setSelected(false);
           docSr.setName("No Patient Found");
           docSr.setLocation("None");
           docSr.setEmailID(" ");
           docSr.setMobileNumber(" ");
           finalList.add(docSr);
       }
       else
       {
           finalList.addAll(nameList);
       }

       listAdapter = new DependencyAdapterDoctor(getActivity(), finalList);

       listView.setAdapter(listAdapter);
       //listAdapter.notifyDataSetChanged();
       System.out.println("Adapter list Count " + listAdapter.getCount());


   }

    public void showListByMobile()
    {
        finalList = new ArrayList<Patient>();

        int lenMobile = mobileNumberList.size();

        if(lenMobile == 0)
        {
            Patient docSr = new Patient();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmailID(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        }
        else
        {
            finalList.addAll(mobileNumberList);
        }

        listAdapter = new DependencyAdapterDoctor(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public void showListByEmail()
    {
        finalList = new ArrayList<Patient>();

        int lenEmail = emailList.size();

        if(lenEmail == 0)
        {
            Patient docSr = new Patient();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmailID(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        }
        else
        {
            finalList.addAll(emailList);

        }

        listAdapter = new DependencyAdapterDoctor(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public String doValidation() {
        int len = finalList.size();

        if (len == 0) {

            return "No Patient";
        } else {
            selectedList = new ArrayList<Patient>();
            for (int i = 0; i < len; i++) {

                if ((finalList.get(i)).isSelected() == true) {
                    selectedList.add(finalList.get(i));
                    System.out.println("Array Name " + finalList.get(i).getPatientId() + " Value" + finalList.get(i).isSelected());

                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Patient";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }



        }
    }



}
