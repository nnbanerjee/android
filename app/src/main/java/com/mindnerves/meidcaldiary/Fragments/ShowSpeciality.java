package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.SpecialityAdapter;
import Application.MyApi;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 8/18/15.
 */
public class ShowSpeciality extends Fragment {
    private ListView listView;
    private MyApi api;
    private SpecialityAdapter specialityAdapter;
    EditText editTextSearch;
    String searchText = "";
    Button filter,back,drawar,logout;
    List<String> specialities = new ArrayList<String>();
    List<String> sortSpeciality = new ArrayList<String>();
    ArrayList<DoctorSearchResponse> doctorList = new ArrayList<DoctorSearchResponse>();
    Global go;
    TextView globalTv,accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    LinearLayout layout;
   // ImageView medicoLogo,medicoText;
    String type;
    Button refresh;
    SharedPreferences session;
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
        View view = inflater.inflate(R.layout.show_speciality, container, false);
        listView = (ListView) view.findViewById(R.id.list_speciality);
        go = (Global) getActivity().getApplicationContext();
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        back = (Button) getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        BackStress.staticflag =1;
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);
        filter = (Button) view.findViewById(R.id.filter_home);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        editTextSearch = (EditText) view.findViewById(R.id.searchET);
     //   medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
     //   medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = editTextSearch.getText().toString();
                showListSort(searchText);


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
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorList = new ArrayList<DoctorSearchResponse>();
                api.searchDoctors("", new Callback<ArrayList<DoctorSearchResponse>>() {
                    @Override
                    public void success(ArrayList<DoctorSearchResponse> doctorSearchResponses, Response response) {

                        if (sortSpeciality.size() == 0) {
                            sortSpeciality = specialities;
                        }
                        for (DoctorSearchResponse doctor : doctorSearchResponses) {
                            for (String speciality : sortSpeciality) {
                                if (doctor.getSpeciality().equalsIgnoreCase(speciality)) {
                                    doctorList.add(doctor);
                                }
                            }
                        }
                        FilterDialog filter = FilterDialog.newInstance();
                        filter.show(getFragmentManager(), "Dialog");
                        go.setDoctorSpeciality(doctorList);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();

            }
        });
        showListView();
        manageScreenIcons();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String speciality = (String) specialityAdapter.getItem(position);
                Bundle args = new Bundle();
                Global go = (Global) getActivity().getApplicationContext();
                go.setDoctorSpeciality(null);
                args.putString("specialization", speciality);
                Fragment frag = new AddDoctorSpecialityWise();
                frag.setArguments(args);
                go.setSpecialityString(speciality);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_New_Doc");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;
    }
    public void manageScreenIcons(){
        globalTv.setText("Doctor Specialities");
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        drawar.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
      //  medicoLogo.setVisibility(View.GONE);
     //   medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }
    public void showListView() {
        api.getAllSpeciality(new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                specialities = strings;
                specialityAdapter = new SpecialityAdapter(getActivity(), strings);
                listView.setAdapter(specialityAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goBack() {
        globalTv.setText(type);
        Fragment fragment = new PatientMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
     //   medicoLogo.setVisibility(View.VISIBLE);
     //   medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }

    public void showListSort(String search) {
        sortSpeciality = new ArrayList<String>();
        for (String sp : specialities) {
            if (sp.contains(search)) {
                sortSpeciality.add(sp);
            }
        }
        if (sortSpeciality.size() == 0) {
            sortSpeciality.add("No Speciality Found");
        }
        specialityAdapter = new SpecialityAdapter(getActivity(), sortSpeciality);
        listView.setAdapter(specialityAdapter);
    }

}
