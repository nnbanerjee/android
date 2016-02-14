package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.method.KeyListener;
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

import Adapter.DependencyAdapter;
import Adapter.DoctorAdapter;
import Adapter.DoctorAddAdapter;
import Application.MyApi;
import Model.Doctor;
import Model.DoctorSearchResponse;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Feb-15.
 */
public class AddDoctor extends Fragment{

    private static final String TAG = AddDoctor.class.getName();
    private ListView listView;
    private DoctorAddAdapter listAdapter;
    private ArrayList<DoctorSearchResponse> docSearchRes,selectedId;
    private ArrayList<DoctorSearchResponse> selectedList;
    public SharedPreferences session = null;
    public MyApi api;
    public String searchText = "";
    private RadioButton nameRadio,mobileNumberRadio,emailIdRadio;
    private Button searchButton,addNewButton,doneButton,backButton;
    private EditText searchTv;
    private ArrayList<DoctorSearchResponse> nameList,mobileNumberList,emailList,finalList;
    private ManageDoctorFragment fragment;
    private String patientId;
    private String doctorsIds = "";
    private ArrayList<DoctorSearchResponse> arrayNew;
    private ArrayList<DoctorSearchResponse> list;
    ArrayList<DoctorSearchResponse> removeList;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    Fragment fragment = new PatientMenusManage();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                    return true;

                }
                return false;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.add_doctor,container,false);
        BackStress.staticflag = 0;
        listView = (ListView)view.findViewById(R.id.list_add_doctor);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Doctor");
        searchTv = (EditText)view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID",null);
        getActivity().getActionBar().hide();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment = new PatientMenusManage();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                final Button back = (Button)getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);

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
        nameRadio.setChecked(true);
        searchButton = (Button)view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                searchText = searchTv.getText().toString();
                Boolean nameBoolean = nameRadio.isChecked();
                Boolean mobileBoolean = mobileNumberRadio.isChecked();
                Boolean emailBoolean = emailIdRadio.isChecked();

                System.out.println("Mobile Boolean::::"+mobileBoolean);
                nameList = new ArrayList<DoctorSearchResponse>();
                mobileNumberList = new ArrayList<DoctorSearchResponse>();
                emailList = new ArrayList<DoctorSearchResponse>();

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
                        if(docSearchRes.get(i).getSpeciality().toLowerCase().contains(searchText.toLowerCase()))
                        {
                            mobileNumberList.add(docSearchRes.get(i));
                        }
                    }

                    System.out.println("Size::::::::"+mobileNumberList.size());
                    showListByMobile();
                }

                if(emailBoolean)
                {
                   int len = docSearchRes.size();
                    System.out.println("In Doctor:::::");
                    System.out.println("SelectedID PatientID::::"+selectedId.size());
                    System.out.println("Array PatientID::::"+len);

                    System.out.println("List size::::::::"+list.size());
                   for (int i=0;i<len;i++)
                   {
                       if(docSearchRes.get(i).getLocation().toLowerCase().contains(searchText.toLowerCase()))
                       {
                          emailList.add(docSearchRes.get(i));
                       }
                   }
                   System.out.println("Location Size::::::"+emailList.size());
                   showListByEmail();
                }

                if(searchText.equals(""))
                {
                    Toast.makeText(getActivity(),"Please Enter Text",Toast.LENGTH_SHORT).show();
                    showListView();
                }
         }
        });




        addNewButton = (Button)view.findViewById(R.id.add_new_doctor);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddNewDoctor();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag,"Add_New_Doc");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });



        doneButton = (Button)view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if(result.equals("No doctors"))
                {
                    Toast.makeText(getActivity(),"No Doctors In List",Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                }
                else if(result.equals("No Selected Doctors"))
                {
                    System.out.println("Result Variable Contain  "+result);
                    Toast.makeText(getActivity(),"No Doctors Selected",Toast.LENGTH_LONG).show();


                }
                else if (result.equals("Normal"))
                {
                    int len = selectedList.size();
                    int j = len-1;

                    for(int i=0;i<len;i++)
                    {
                        if(i == j)
                        {
                            doctorsIds = doctorsIds+selectedList.get(i).getDoctorId();
                        }
                        else
                        {
                            doctorsIds = doctorsIds+selectedList.get(i).getDoctorId()+",";
                        }
                    }
                    System.out.println("Doctors IDs "+doctorsIds);

                    api.patientDoctor(patientId,doctorsIds, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();
                            if(status == 200) {
                                System.out.println("Krb Url ::::"+response.getUrl());
                                Toast.makeText(getActivity(),"Doctors Are Added",Toast.LENGTH_LONG).show();
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

                        }
                    });

                }

            }
        });

        backButton = (Button)view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new ManageDoctorFragment();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        return view;
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
                      docSearchRes = array;
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
                              Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
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

    public void showListByName()
    {
        finalList = new ArrayList<DoctorSearchResponse>();
        finalList.clear();
        int lenName = nameList.size();

        if(lenName == 0)
        {
            DoctorSearchResponse docSr = new DoctorSearchResponse();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
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

    public void showListByMobile()
    {
        finalList = new ArrayList<DoctorSearchResponse>();
        finalList.clear();
        System.out.println("Mobile number::::::"+mobileNumberList.size());
        int lenMobile = mobileNumberList.size();

        if(lenMobile == 0)
        {
            DoctorSearchResponse docSr = new DoctorSearchResponse();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmail(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        }
        else
        {
            finalList.addAll(mobileNumberList);
        }

        listAdapter = new DoctorAddAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public void showListByEmail()
    {
        finalList = new ArrayList<DoctorSearchResponse>();
        finalList.clear();
        int lenEmail = emailList.size();

        if(lenEmail == 0)
        {
            DoctorSearchResponse docSr = new DoctorSearchResponse();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmail(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        }
        else
        {
            finalList.addAll(emailList);

        }

        listAdapter = new DoctorAddAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

    public String doValidation()
    {
        int len = docSearchRes.size();

        if(len == 0)
        {

            return "No doctors";
        }
        else {
            selectedList = new ArrayList<DoctorSearchResponse>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    selectedList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getName() + " Value" + docSearchRes.get(i).isSelected());
                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Doctors";
            }
            else
            {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }

        }

    }



}
