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
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
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

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import Adapter.SpecialityAdapter;
import Application.MyApi;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 22-10-2015.
 */
public class ShowPatients extends Fragment {
    private ListView listView;
    private MyApi api;
    private SpecialityAdapter cityAdapter;
    EditText editTextSearch;
    String searchText = "";
    Set<String> cities = new HashSet<>();
    ArrayList<String> cityList = new ArrayList<String>();
    ArrayList<String> finalCityList = new ArrayList<String>();
    List<Patient> patientList = new ArrayList<Patient>();
    Global go;
    Button back,filter,drawar,logout;
    ImageView profilePicture;
    TextView accountName,globalTv,addText;
    RelativeLayout profileLayout;
    LinearLayout layout;
   // ImageView medicoLogo,medicoText;
    Button refresh;
    String type;
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
        filter = (Button) view.findViewById(R.id.filter_home);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        editTextSearch = (EditText) view.findViewById(R.id.searchET);
        addText = (TextView)view.findViewById(R.id.add_text);
     //   medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
     //   medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        manageScreenIcons();
        showListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = (String)cityAdapter.getItem(position);
                System.out.println("City:::::"+city);
                ArrayList<Patient> patientCityWise = new ArrayList<Patient>();
                for(Patient patient : patientList){
                    if(patient.getLocation().equalsIgnoreCase(city)){
                        patientCityWise.add(patient);
                    }
                }
                System.out.println("PatientCitywise= "+patientCityWise.size());
                go.setPatientList(patientCityWise);
                Fragment frag = new ShowPatientCityWise();
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
        globalTv.setText("Patients Cities");
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        filter.setVisibility(View.GONE);
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        String udata="Add Patient";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        addText.setText(content);
    //    medicoLogo.setVisibility(View.GONE);
     //   medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }
    public void showListView(){

        api.searchPatients("",new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> patients, Response response) {
                patientList = patients;
                for(Patient patient : patientList){
                    cities.add(patient.getLocation());
                }
                for(String city : cities){
                    cityList.add(city);
                }
                finalCityList = cityList;
                cityAdapter = new SpecialityAdapter(getActivity(),finalCityList);
                listView.setAdapter(cityAdapter);
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
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.home_jump);
        drawar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
     //   medicoLogo.setVisibility(View.VISIBLE);
     //   medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }

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
    public void showListSort(String search){
        finalCityList = new ArrayList<String>();
        for(String city : cityList){
            if(city.contains(search)){
                finalCityList.add(city);
            }
        }
        cityAdapter = new SpecialityAdapter(getActivity(),finalCityList);
        listView.setAdapter(cityAdapter);
    }
}
