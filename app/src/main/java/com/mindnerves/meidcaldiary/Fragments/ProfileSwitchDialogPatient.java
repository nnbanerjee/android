package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.HomeActivityRevision;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.ProfileDelegationAdapter;
import Adapter.ProfileDependencyAdapter;
import com.medico.application.MyApi;
import Model.Patient;
import Model.PersonTemp;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class ProfileSwitchDialogPatient extends DialogFragment {
    SharedPreferences session;
    String patientId;
    ListView dependentList,delegationList;
    TextView profileName;
    public MyApi api;
    ProfileDependencyAdapter dependentAdapter;
    ProfileDelegationAdapter delegationAdapter;
    String typeId;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static ProfileSwitchDialogPatient newInstance() {
        return new ProfileSwitchDialogPatient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_dialog,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID",null);
        typeId = session.getString("loginType",null);
        typeId = "P";
        dependentList = (ListView)view.findViewById(R.id.profile_dependent_list);
        delegationList = (ListView)view.findViewById(R.id.profile_delegation_list);
        profileName = (TextView)view.findViewById(R.id.profile_name);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getProfilePatient(patientId,new Callback<PersonTemp>() {
            @Override
            public void success(PersonTemp person, Response response) {
                profileName.setText(person.getName());

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
            }
        });
        api.getAllPatientDependents(patientId, new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> patients, Response response) {
                int flagDependency = 0;
                ArrayList<Patient> arrayNew = new ArrayList<Patient>();
                if (patients.size() == 0) {
                    Patient docSr = new Patient();
                    docSr.setSelected(false);
                    docSr.setName("No Dependent Found");
                    docSr.setLocation(" ");
                    docSr.setAccessLevel("");
                    docSr.setStatus("");
                    arrayNew.add(docSr);
                } else {
                    for (Patient pat : patients) {
                        if ((pat.getStatus()).equals("C")) {
                            arrayNew.add(pat);
                            flagDependency = 1;
                        }

                    }

                    if (flagDependency == 0) {
                        Patient docSr = new Patient();
                        docSr.setSelected(false);
                        docSr.setName("No Dependent Found");
                        docSr.setLocation(" ");
                        docSr.setAccessLevel("");
                        docSr.setStatus("");
                        arrayNew.add(docSr);
                    }
                }
               /* dependentAdapter = new ProfileDependencyAdapter(getActivity(), arrayNew);
                dependentList.setAdapter(dependentAdapter);*/
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });
        dependentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Patient pat = (Patient)dependentAdapter.getItem(position);
                System.out.println("Patient Name::::::"+pat.getName());
                if(!(pat.getName().equalsIgnoreCase("No Dependent Found"))) {
                    String emailId = pat.getEmailID();
                    saveToSession(emailId,"Patient");
                    Intent intObj = new Intent(getActivity(), HomeActivityRevision.class);
                    startActivity(intObj);
                }

            }
        });
        /*api.getAllDelegatesForParent(patientId,typeId,new Callback<ArrayList<Delegation>>() {
            @Override
            public void success(ArrayList<Delegation> delegations, Response response) {
                System.out.println("Kb Array " + delegations.size());
                int flagDelegation = 0;
                ArrayList<Delegation> delegatesAdapter = new ArrayList<Delegation>();
                if (delegations.size() == 0) {
                    Delegation del = new Delegation();
                    del.setName("No Delegation Found");
                    del.setAccessLevel("");
                    del.setLocation("");
                    del.setMobileNumber("");
                    del.setStatus("");
                    del.setEmailID("");
                    del.setAccessLevel("");
                    del.setType("");
                    delegatesAdapter.add(del);
                }
                else
                {
                    for(Delegation dele : delegations)
                    {
                        if(dele.getStatus().equalsIgnoreCase("C"))
                        {
                            flagDelegation = 1;
                            delegatesAdapter.add(dele);
                        }
                    }

                    if(flagDelegation == 0)
                    {
                        Delegation del = new Delegation();
                        del.setName("No Delegation Found");
                        del.setEmailID("");
                        del.setStatus("");
                        del.setMobileNumber("");
                        del.setType("");
                        del.setLocation("");
                        del.setAccessLevel("");
                        delegatesAdapter.add(del);
                    }
                }

                delegationAdapter = new ProfileDelegationAdapter(getActivity(), delegatesAdapter);
                delegationList.setAdapter(delegationAdapter);
                System.out.println("Adapter list Count " + delegationAdapter.getCount());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
            }
        });
        delegationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Delegation del = (Delegation)delegationAdapter.getItem(position);
                System.out.println("doctor email::::::"+del.getEmailID());
                if(!(del.getName().equalsIgnoreCase("No Delegation Found"))) {
                    String emailId = del.getEmailID();
                    String type = del.getType();
                    if(type.equalsIgnoreCase("D"))
                    {
                        type = "Doctor";
                    }
                    else if(type.equalsIgnoreCase("P"))
                    {
                        type = "Patient";
                    }
                    else if(type.equalsIgnoreCase("A"))
                    {
                        type = "Assistant";
                    }
                    saveToSession(emailId,type);
                    Intent intObj = new Intent(getActivity(), HomeActivityRevision.class);
                    startActivity(intObj);
                }
            }
        });*/
        return view;
    }
    public void saveToSession(String email,String typeId) {

        String userId = email;
        String type = typeId;
        session = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        session.edit().putString("sessionID_Revision", userId).apply();
        session.edit().putString("type_Revision",type).apply();

    }
   
}
