package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import Adapter.DelegateAdapter;
import Adapter.DependencyAdapter;
import Application.MyApi;
import Model.AddDelegate;
import Model.AddDelegateElement;
import Model.AddDependent;
import Model.AddDependentElement;
import Model.Delegation;
import Model.GetDelegate;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Mar-15.
 */
public class AddDelegation extends Fragment {

    private ListView listView;
    private EditText searchTv;
    private String patientId = "";
    private String typeId = "";
    public MyApi api;
    private Button searchButton,backButton;
    private ArrayList<GetDelegate> nameList,mobileNumberList,emailList,finalList;
    private String searchText = "";
    private RadioButton nameRadio,mobileNumberRadio,emailIdRadio;
    private ArrayList<GetDelegate> selectedList;
    private ArrayList<Delegation> list;
    private ArrayList<GetDelegate> docSearchRes;
    private DelegateAdapter listAdapter;
    private String sortBy = "";
    private Button buttonAdd;
    private String patientIds = "";
    private String name,location,mobileNumber;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new ManageDelegationFragment();
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
        View view = inflater.inflate(R.layout.add_delegation,container,false);
        listView = (ListView)view.findViewById(R.id.list_add_patient);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Delegation");
        searchTv = (EditText)view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
        typeId = session.getString("type",null);
        if(typeId.equals("Doctor"))
        {
            typeId = "D";
        }
        BackStress.staticflag = 0;
        getActivity().getActionBar().hide();
        Global go = (Global)getActivity().getApplicationContext();
        list = go.getDelegationList();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageDelegationFragment();
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                searchText = searchTv.getText().toString();

                Boolean nameBoolean = nameRadio.isChecked();
                Boolean mobileBoolean = mobileNumberRadio.isChecked();
                Boolean emailBoolean = emailIdRadio.isChecked();

                nameList = new ArrayList<GetDelegate>();
                mobileNumberList = new ArrayList<GetDelegate>();
                emailList = new ArrayList<GetDelegate>();

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
                        if(docSearchRes.get(i).getMobileNumber().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            mobileNumberList.add(docSearchRes.get(i));
                        }
                    }
                    showListByMobile();
                }

                if(emailBoolean)
                {
                    int len = docSearchRes.size();

                    for(int i=0;i<len;i++)
                    {
                        if(docSearchRes.get(i).getEmailID().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            emailList.add(docSearchRes.get(i));
                        }
                    }

                    showListByEmail();
                }
                if(searchText.equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter Text",Toast.LENGTH_SHORT).show();
                    showListView();
                }


            }
        });

        backButton = (Button)view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new ManageDelegationFragment();
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

                    for (GetDelegate p : selectedList) {
                        System.out.println("Access Code:::::" + p.getAccessLevel());
                    }
                }


                if (result.equals("No Entry")) {
                    Toast.makeText(getActivity(), "No Entry In List", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                } else if (result.equals("No Selected Entry")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Entry Selected", Toast.LENGTH_LONG).show();
                }
                else if (result.equals("Normal")) {
                    int len = selectedList.size();
                    ArrayList<AddDelegateElement> selectedElements = new ArrayList<AddDelegateElement>();

                    for(GetDelegate del : selectedList)
                    {
                        AddDelegateElement element = new AddDelegateElement();
                        element.setId(del.getId());

                        if(del.getAccessLevel().equals("Read Only"))
                        {
                            element.setAccessLevel("R");
                        }

                        if(del.getAccessLevel().equals("Full Access"))
                        {
                            element.setAccessLevel("F");
                        }
                        element.setType(del.getType());
                        selectedElements.add(element);
                    }

                    System.out.println("Selected:::::::::::");

                    for(AddDelegateElement a : selectedElements)
                    {
                        System.out.println("ID::::::::"+a.getId());
                        System.out.println("Access Level:::::::::::"+a.getAccessLevel());
                    }

                    AddDelegate add = new AddDelegate();
                    add.setId(patientId);
                    add.setDelegates(selectedElements);
                    add.setType(typeId);


                    api.addDeleagatesForParent(add,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();

                            System.out.println("URl "+response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Delegation  Added", Toast.LENGTH_LONG).show();
                                Fragment frag = new ManageDelegationFragment();
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
       finalList = new ArrayList<GetDelegate>();

       api.getAllDoctorsAndAssistants(new Callback<ArrayList<GetDelegate>>() {
           @Override
           public void success(ArrayList<GetDelegate> array, Response response) {
               System.out.println("Arrays Value " + array.toString());
               docSearchRes = new ArrayList<GetDelegate>();
               if (array.size() == 0) {
                   GetDelegate del = new GetDelegate();
                   del.setName("No Doctor And Assistant Found");
                   del.setAccessLevel("");
                   del.setType("");
                   del.setEmailID("");
                   del.setLocation("");
                   del.setMobileNumber("");
                   System.out.println("IF " + array.size());
                   docSearchRes = array;

               } else {

                   GetDelegate removePatient = new GetDelegate();

                   ArrayList<GetDelegate> newArray = array;
                   docSearchRes = array;

                   for (GetDelegate del : newArray) {
                       if (del.getEmailID().equals(patientId)) {
                           removePatient = del;
                           System.out.println("id::::::::::::::" + del.getId());
                           System.out.println("id::::::::::::::" + del.getName());

                       }
                   }

                   docSearchRes.remove(removePatient);

                   System.out.println("Else " + docSearchRes.size());
                   System.out.println("After Removing::::::::::::::");

                   for (GetDelegate pat : docSearchRes) {
                       System.out.println("id::::::::::::::" + pat.getId());
                       System.out.println("id::::::::::::::" + pat.getName());
                   }
               }

               System.out.println("Krb Url" + response.getUrl());
               ArrayList<GetDelegate> removeList = new ArrayList<GetDelegate>();

               for(int i=0;i<docSearchRes.size();i++)
               {
                   for(int j=0;j<list.size();j++)
                   {
                       if(docSearchRes.get(i).getId().equals(list.get(j).getId()))
                       {
                           GetDelegate g = new GetDelegate();
                           g.setId(docSearchRes.get(i).getId());
                           g.setFullAccess(false);
                           g.setReadOnly(false);
                           g.setType(docSearchRes.get(i).getType());
                           g.setMobileNumber(docSearchRes.get(i).getMobileNumber());
                           g.setAccessLevel(docSearchRes.get(i).getAccessLevel());
                           g.setEmailID(docSearchRes.get(i).getEmailID());
                           g.setLocation(docSearchRes.get(i).getLocation());
                           g.setSelected(docSearchRes.get(i).getSelected());
                           removeList.add(g);
                           docSearchRes.remove(i);
                       }
                   }
               }

               System.out.println("RemovelIst::::"+removeList.size());

               System.out.println("docSearchRes size::::"+docSearchRes.size());
               finalList.addAll(docSearchRes);



               listAdapter = new DelegateAdapter(getActivity(), finalList);

               listView.setAdapter(listAdapter);
               //listAdapter.notifyDataSetChanged();
               System.out.println("Adapter list Count " + listAdapter.getCount());
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
       finalList = new ArrayList<GetDelegate>();

       int lenName = nameList.size();

       if(lenName == 0)
       {
           GetDelegate del = new GetDelegate();
           del.setName("No Doctor And Assistant Found");
           del.setMobileNumber("");
           del.setLocation("");
           del.setAccessLevel("");
           del.setEmailID("");
           del.setSelected(false);
           finalList.add(del);
       }
       else
       {
           finalList.addAll(nameList);
       }

       listAdapter = new DelegateAdapter(getActivity(), finalList);

       listView.setAdapter(listAdapter);
       //listAdapter.notifyDataSetChanged();
       System.out.println("Adapter list Count " + listAdapter.getCount());


   }

    public void showListByMobile()
    {
        finalList = new ArrayList<GetDelegate>();

        int lenMobile = mobileNumberList.size();

        if(lenMobile == 0)
        {
            GetDelegate del = new GetDelegate();
            del.setName("No Doctor And Assistant Found");
            del.setMobileNumber("");
            del.setLocation("");
            del.setAccessLevel("");
            del.setEmailID("");
            del.setSelected(false);
            finalList.add(del);
        }
        else
        {
            finalList.addAll(mobileNumberList);
        }

        listAdapter = new DelegateAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public void showListByEmail()
    {
        finalList = new ArrayList<GetDelegate>();

        int lenEmail = emailList.size();

        if(lenEmail == 0)
        {
            GetDelegate del = new GetDelegate();
            del.setName("No Doctor And Assistant Found");
            del.setMobileNumber("");
            del.setLocation("");
            del.setAccessLevel("");
            del.setEmailID("");
            del.setSelected(false);
            finalList.add(del);
        }
        else
        {
            finalList.addAll(emailList);

        }

        listAdapter = new DelegateAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public String doValidation() {
        int len = finalList.size();

        if (len == 0) {

            return "No Entry";
        } else {
            selectedList = new ArrayList<GetDelegate>();
            for (int i = 0; i < len; i++) {

                if ((finalList.get(i)).getSelected() == true) {
                    selectedList.add(finalList.get(i));
                    System.out.println("Array Name " + finalList.get(i).getId() + " Value" + finalList.get(i).getSelected());

                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Entry";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }



        }
    }



}
