package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.RequestDependentAdapterDoctor;
import Adapter.ShowDependentAdapter;
import com.medico.application.MyApi;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Mar-15.
 */
public class ManageDendencyDoctor extends Fragment {

    private ListView listViewManageDependency,listViewManageRequest;
    private String doctorId = "";
    private String patientIds = "";
    private ShowDependentAdapter adapter;
    private RequestDependentAdapterDoctor requestAdapter;
    public MyApi api;
    private Button buttonAdd,removeDoc,drawar,logout;
    private ArrayList<Patient> requestPatients;
    private ArrayList<Patient> docSearchRes,arrayNew,removeList,populateRemove,confirmList;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    Fragment fragment = new DoctorMenusManage();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
                    final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
                    final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
                    final LinearLayout layoutMenus = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                    drawar = (Button)getActivity().findViewById(R.id.drawar_button);
                    logout = (Button)getActivity().findViewById(R.id.logout);
                    logout.setBackgroundResource(R.drawable.logout);
                    drawar.setVisibility(View.VISIBLE);
                    layoutMenus.setVisibility(View.VISIBLE);
                    profileLayout.setVisibility(View.VISIBLE);
                    back.setVisibility(View.INVISIBLE);
                    profilePicture.setVisibility(View.VISIBLE);
                    accountName.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_dependent, container, false);
        listViewManageDependency = (ListView)view.findViewById(R.id.list_manage_dependent);
        listViewManageRequest = (ListView)view.findViewById(R.id.list_manage_dependent_wcd);
        getActivity().getActionBar().hide();
        BackStress.staticflag = 1;
        LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.GONE);
        final Global go = (Global)getActivity().getApplicationContext();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Dependency");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        final LinearLayout layoutMenus = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        drawar.setVisibility(View.GONE);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);
        layoutMenus.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment = new DoctorMenusManage();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
                drawar = (Button)getActivity().findViewById(R.id.drawar_button);
                logout = (Button)getActivity().findViewById(R.id.logout);
                logout.setBackgroundResource(R.drawable.logout);
                drawar.setVisibility(View.VISIBLE);
                profileLayout.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
                layoutMenus.setVisibility(View.VISIBLE);
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showDependencyConfirmList();
        showDependencyWaitingList();
        buttonAdd = (Button) view.findViewById(R.id.add_dependent);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddDependencyDoctor();
                go.setDependencyPatient(confirmList);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag,"Add_Doctor");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });


        removeDoc = (Button)view.findViewById(R.id.remove_dependent);
        removeDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = doValidation();
                patientIds = "";
                if(result.equals("No Patients"))
                {
                    Toast.makeText(getActivity(),"No Patients In List",Toast.LENGTH_LONG).show();

                }
                else if(result.equals("No Selected Patients"))
                {
                    System.out.println("Result Variable Contain  "+result);
                    Toast.makeText(getActivity(),"No Patients Selected",Toast.LENGTH_LONG).show();
                }
                else if (result.equals("Normal")) {
                    Toast.makeText(getActivity(), "Patients Removed", Toast.LENGTH_LONG).show();
                    int len = removeList.size();
                    int j = len - 1;

                    for (int i = 0; i < len; i++) {
                        System.out.println("Selected ids " + removeList.get(i).getPatientId());
                        if (i == j) {
                            patientIds = patientIds + removeList.get(i).getPatientId();
                        } else {
                            System.out.println("add Item " + removeList.get(i).getPatientId());
                            patientIds = patientIds + removeList.get(i).getPatientId() + ",";
                        }
                    }

                    api.removePatientDependent(doctorId,patientIds,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();
                            System.out.println("URL passed " + response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Doctors Are Added", Toast.LENGTH_LONG).show();
                                showDependencyConfirmList();
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



    public void showDependencyConfirmList()
    {
       docSearchRes = new ArrayList<Patient>();

       api.getAllDoctorDependents(doctorId,new Callback<ArrayList<Patient>>() {
           @Override
           public void success(ArrayList<Patient> patients, Response response) {
               System.out.println("Kb Array " + patients.size());
               arrayNew = new ArrayList<Patient>();
               if (patients.size() == 0) {
                   Patient docSr = new Patient();

                   docSr.setSelected(false);
                   docSr.setName("No Patient Found");
                   docSr.setLocation(" ");
                   arrayNew.add(docSr);
               } else {
                   ArrayList<Patient> newArray = patients;
                   arrayNew = patients;

               }
               confirmList = arrayNew;
               populateRemove = arrayNew;
               adapter = new ShowDependentAdapter(getActivity(), arrayNew);
               listViewManageDependency.setAdapter(adapter);

               System.out.println("Adapter list Count " + adapter.getCount());
           }

           @Override
           public void failure(RetrofitError error) {
               Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
               error.printStackTrace();
           }
       });
       /* requestPatients = new ArrayList<Patient>();
        Patient p = new Patient();
        p.setName("No Patient Found");
        p.setMobile("");
        p.setEmail("");
        p.setLocation("");

        requestPatients.add(p);

        requestAdapter = new RequestDependentAdapter(getActivity(),requestPatients);
        listViewManageRequest.setAdapter(requestAdapter);
        System.out.println("Request Adapter::::: "+requestAdapter.getCount());*/

    }

    public void showDependencyWaitingList()
    {

        docSearchRes = new ArrayList<Patient>();

        api.getAllParentsDoctor(doctorId,new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> patients, Response response) {
                int flagDependency = 0;
                System.out.println("Kb Array " + patients.size());
                arrayNew = new ArrayList<Patient>();
                if (patients.size() == 0) {
                    Patient docSr = new Patient();

                    docSr.setSelected(false);
                    docSr.setName("No Patient Found");
                    docSr.setLocation(" ");
                    docSr.setAccessLevel("");
                    docSr.setStatus("");
                    arrayNew.add(docSr);
                }
                else
                {
                    ArrayList<Patient> newArray = patients;
                    for(Patient pat : newArray)
                    {
                        if((pat.getStatus()).equals("WC"))
                        {
                            arrayNew.add(pat);
                            flagDependency = 1;
                        }

                    }

                    if(flagDependency == 0)
                    {
                        Patient docSr = new Patient();

                        docSr.setSelected(false);
                        docSr.setName("No Patient Found");
                        docSr.setLocation(" ");
                        docSr.setAccessLevel("");
                        docSr.setStatus("");
                        arrayNew.add(docSr);
                    }

                    System.out.println("Waited::::::::::::");

                }

                docSearchRes.addAll(arrayNew);

                requestAdapter = new RequestDependentAdapterDoctor(getActivity(),arrayNew);
                listViewManageRequest.setAdapter(requestAdapter);

                System.out.println("Request Adapter::::: "+requestAdapter.getCount());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });


    }

    public String doValidation()
    {
        int len = populateRemove.size();

        if(len == 0)
        {

            return "No Patients";
        }
        else
        {
            removeList = new ArrayList<Patient>();
            for (int i = 0; i < len; i++) {

                if ((populateRemove.get(i)).isSelected() == true) {
                    removeList.add(populateRemove.get(i));
                    System.out.println("Array Name " + populateRemove.get(i).getPatientId() + " Value" + populateRemove.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Patients";
            }
            else
            {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }
    }
}
