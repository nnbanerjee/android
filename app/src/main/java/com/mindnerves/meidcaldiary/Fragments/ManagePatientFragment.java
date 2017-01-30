package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.List;

import Adapter.PatientAdapter;
import Application.MyApi;
import Model.MultiplePatient;
import Model.Patient;
import Model.RemovePatients;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Feb-15.
 */
public class ManagePatientFragment extends Fragment {

    private static final String TAG = AddDoctor.class.getName();
    private ListView listViewManageDoctor;
    private PatientAdapter listAdapterManagePatient;
    private ArrayList<Patient> docSearchRes;
    private Button addDoctor, removeDoc, drawar, logout, back;
    public MyApi api;
    private ArrayList<Patient> arrayNew;
    private ArrayList<Patient> removeList;
    private String doctorId;
    FragmentManager fragmentManger;
    LinearLayout layout;
    TextView globalTv, accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
   // ImageView medicoLogo,medicoText;
    Button refresh;
    String type;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_patient, container, false);
        listViewManageDoctor = (ListView) view.findViewById(R.id.list_manage_patient);
        BackStress.staticflag = 1;
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);
        doctorId = session.getString("id", null);
        final Global go = (Global) getActivity().getApplicationContext();
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        back = (Button) getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
      //  medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();
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
        System.out.println("In managepatient................");
        getActivity().getActionBar().hide();
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showpatientList();

        addDoctor = (Button) view.findViewById(R.id.add_patient);
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new AddPatient();

                go.setPatientList(docSearchRes);

                for (Patient p : docSearchRes) {
                    System.out.println("Patient name in add:::" + p.getName());
                }
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_Patient");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        removeDoc = (Button) view.findViewById(R.id.remove_patient);
        removeDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();
                if (result.equals("No Patients")) {
                    Toast.makeText(getActivity(), "No Patients In List", Toast.LENGTH_LONG).show();


                } else if (result.equals("No Selected Patients")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Patients Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {

                    int len = removeList.size();
                    ArrayList<MultiplePatient> patients = new ArrayList<MultiplePatient>();
                    RemovePatients remove = new RemovePatients();

                    for (int i = 0; i < len; i++) {
                        MultiplePatient mul = new MultiplePatient();
                        mul.setId(removeList.get(i).getPatientId());
                        mul.setType(removeList.get(i).getType());
                        patients.add(mul);
                    }
                    remove.setDoctorId(doctorId);
                    remove.setPatients(patients);

                    api.removeDoctorsPatient(remove, new Callback<String>() {

                        @Override
                        public void success(String response, Response response2) {
                            if (response.equalsIgnoreCase("Success")) {
                                Toast.makeText(getActivity(), "Patients Are Removed", Toast.LENGTH_LONG).show();
                                showpatientList();
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
        manageScreenIcon();
        return view;
    }

    public String doValidation() {
        int len = arrayNew.size();

        if (len == 0) {

            return "No Patients";
        } else {
            removeList = new ArrayList<Patient>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    removeList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getPatientId() + " Value" + docSearchRes.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Patients";
            } else {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }
    }


    public void showpatientList() {
        docSearchRes = new ArrayList<Patient>();
        api.getDoctorsPatients(doctorId, new Callback<List<Patient>>() {
            @Override
            public void success(List<Patient> array, Response response) {

                System.out.println("Kb Array " + array.size());
                arrayNew = new ArrayList<Patient>();
                if (array.size() == 0) {
                    Patient docSr = new Patient();

                    docSr.setSelected(false);
                    docSr.setName("No Patient Found");
                    docSr.setLocation("None");
                    arrayNew.add(docSr);
                    System.out.println("IF " + arrayNew.size());


                } else {
                    arrayNew = (ArrayList<Patient>) array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Arrays Value " + array.toString());

                System.out.println("Krb Url" + response.getUrl());
                docSearchRes.addAll(arrayNew);

                listAdapterManagePatient = new PatientAdapter(getActivity(), arrayNew);
                listViewManageDoctor.setAdapter(listAdapterManagePatient);
                System.out.println("Adapter list Count " + listAdapterManagePatient.getCount());

            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void manageScreenIcon() {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Patient");
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        BackStress.staticflag = 1;
      //  medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.INVISIBLE);
    }

    public void goBack() {
        globalTv.setText(type);
        DoctorMenusManage fragment = new DoctorMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
     //   medicoLogo.setVisibility(View.VISIBLE);
     //   medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }
}
