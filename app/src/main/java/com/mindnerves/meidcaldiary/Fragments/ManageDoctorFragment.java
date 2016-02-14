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
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import java.util.List;
import Adapter.DoctorAdapter;
import Application.MyApi;
import Model.DoctorSearchResponse;
import Model.MultipleDoctors;
import Model.RemoveDoctors;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Feb-15.
 */
public class ManageDoctorFragment extends Fragment{

    private static final String TAG = AddDoctor.class.getName();
    private ListView listViewManageDoctor;
    private DoctorAdapter listAdapterManageDoctor;
    private ArrayList<DoctorSearchResponse> docSearchRes;
    private Button addDoctor,removeDoc;
    public MyApi api;
    private ArrayList<DoctorSearchResponse> arrayNew;
    ArrayList<DoctorSearchResponse> removeList;
    private String patientId;
    private String doctorsIds = "";
    private ArrayList<DoctorSearchResponse> selectedList;


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
                    Fragment fragment = new PatientMenusManage();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
                    final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
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

        View view = inflater.inflate(R.layout.manage_doctor, container, false);
        listViewManageDoctor = (ListView)view.findViewById(R.id.list_manage_doctor);
        getActivity().getActionBar().hide();
        BackStress.staticflag = 1;
        final Global go = (Global)getActivity().getApplicationContext();
        LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.GONE);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID",null);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Doctor");
        System.out.println("In managedoctor:::::::");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment = new PatientMenusManage();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
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
        addDoctor = (Button) view.findViewById(R.id.add_doc);
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddDoctor();

                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag,"Add_Doctor");
                go.setDoctorList(docSearchRes);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        removeDoc = (Button)view.findViewById(R.id.remove_doctor);
        removeDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();
                if(result.equals("No doctors"))
                {
                    Toast.makeText(getActivity(),"No Doctors In List",Toast.LENGTH_LONG).show();


                }
                else if(result.equals("No Selected Doctors"))
                {
                    System.out.println("Result Variable Contain  "+result);
                    Toast.makeText(getActivity(),"No Doctors Selected",Toast.LENGTH_LONG).show();


                }
                else if (result.equals("Normal"))
                {
                    Toast.makeText(getActivity(),"Doctors Removed",Toast.LENGTH_LONG).show();
                    int len = removeList.size();

                    ArrayList<MultipleDoctors> doctors = new ArrayList<MultipleDoctors>();
                    RemoveDoctors remove = new RemoveDoctors();


                    for(int i=0;i<len;i++)
                    {
                         MultipleDoctors mul = new MultipleDoctors();
                         mul.setId(removeList.get(i).getDoctorId());
                         mul.setType(removeList.get(i).getType());
                         doctors.add(mul);
                    }
                    remove.setPatientId(patientId);
                    remove.setMultipleDoctors(doctors);

                    api.removePatientsDoctor(remove, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();
                            System.out.println("Status "+status);
                            if (status == 200) {

                                Toast.makeText(getActivity(), "Doctors Are Removed", Toast.LENGTH_LONG).show();
                                showDoctorList();

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

    public String doValidation()
    {
        int len = arrayNew.size();

        if(len == 0)
        {

            return "No doctors";
        }
        else
        {
            removeList = new ArrayList<DoctorSearchResponse>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    removeList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getDoctorId() + " Value" + docSearchRes.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Doctors";
            }
            else
            {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }

    }

    public void showDoctorList()
    {
        docSearchRes = new ArrayList<DoctorSearchResponse>();
        api.getPatientsDoctors(patientId, new Callback<List<DoctorSearchResponse>>() {
            @Override
            public void success(List<DoctorSearchResponse> array, Response response) {

                System.out.println("Kb Array " + array.size());
                arrayNew = new ArrayList<DoctorSearchResponse>();
                if (array.size() == 0) {
                    DoctorSearchResponse docSr = new DoctorSearchResponse();

                    docSr.setSelected(false);
                    docSr.setName("No Doctor Found");
                    docSr.setLocation("None");
                    arrayNew.add(docSr);
                    System.out.println("IF " + arrayNew.size());


                } else {
                    arrayNew = (ArrayList<DoctorSearchResponse>) array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Arrays Value " + array.toString());

                System.out.println("Krb Url" + response.getUrl());
                docSearchRes.addAll(arrayNew);

                listAdapterManageDoctor = new DoctorAdapter(getActivity(), arrayNew);

                listViewManageDoctor.setAdapter(listAdapterManageDoctor);

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
