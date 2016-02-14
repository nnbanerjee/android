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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Feb-15.
 */
public class AddNewDoctor extends Fragment {

    private static final String TAG = AddDoctor.class.getName();
    private ListView listView;
    private EditText nameTv,emailTv,mobileTv,locationTv,specialityTv,countryCodeTv;
    private String name,speciality,email,mobile,location,countryCode;
    private Button doneButton,backButton,mapButton;
    public MyApi api;
    private String patientId;
    private int flagValidaltion = 0;
    private String validation = "";


    @Override
    public void onResume() {
       super.onStop();
       getView().setFocusableInTouchMode(true);
       getView().requestFocus();

        final Global go = (Global)getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if(locationString.equals("")) {
            locationTv.setText("");
        }
        else
        {
            locationTv.setText(locationString);
        }

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    go.setLocation("");
                    Fragment frag2 = new AddDoctor();
                    System.out.println("I am in Resume Method::::");
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

        View view = inflater.inflate(R.layout.new_doctor,
                container, false);
        getActivity().getActionBar().hide();
        BackStress.staticflag = 0;
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add New Doctor");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Global go = (Global)getActivity().getApplicationContext();
                go.setLocation("");
                Fragment frag2 = new AddDoctor();
                System.out.println("I am in Resume Method::::");
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //Retrofit Initializtion Code
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID",null);
        nameTv = (EditText)view.findViewById(R.id.name);
        specialityTv = (EditText)view.findViewById(R.id.speciality);
        emailTv = (EditText)view.findViewById(R.id.email);
        mobileTv = (EditText)view.findViewById(R.id.mobile);
        locationTv = (EditText)view.findViewById(R.id.location);
        countryCodeTv = (EditText)view.findViewById(R.id.country_code);
        mapButton = (Button)view.findViewById(R.id.location_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(),MapActivity.class);
                startActivity(intObj);

            }
        });
        doneButton = (Button)view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameTv.getText().toString();
                speciality = specialityTv.getText().toString();
                email = emailTv.getText().toString();
                mobile = mobileTv.getText().toString();
                location = locationTv.getText().toString();
                countryCode = countryCodeTv.getText().toString();
                flagValidaltion = 0;
                validation = "";

                if(name.equals(""))
                {
                    validation = "Please Enter Name";
                    flagValidaltion = 1;
                }
                else if(speciality.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Speciality";
                    flagValidaltion = 1;
                }
                else if(email.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Email";
                    flagValidaltion = 1;
                }
                else if(mobile.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Mobile Number";
                    flagValidaltion = 1;
                }
                else if(location.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Location";
                    flagValidaltion = 1;
                }
                else if(countryCode.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Country Code";
                    flagValidaltion = 1;
                }

                int len = mobile.length();
                if(flagValidaltion == 1)
                {
                    Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
                }
                else if(len < 10)
                {
                    Toast.makeText(getActivity(),"Please Enter Mobile Number Properly",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mobile = countryCode+mobile;
                    DoctorSearchResponse res = new DoctorSearchResponse();

                    res.setName(name);
                    res.setSpeciality(speciality);
                    res.setEmail(email);
                    res.setMobileNumber(mobile);
                    res.setLocation(location);
                   // res.setIdPerson("");


                    api.registerAddDoctor(patientId, res, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();

                            if (status == 200) {
                                Toast.makeText(getActivity(), "Doctor Added Successfully", Toast.LENGTH_LONG);
                                Fragment fragment = new ManageDoctorFragment();

                                FragmentManager fragmentManger = getFragmentManager();
                                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Doctor").commit();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            error.printStackTrace();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        backButton = (Button)view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Global go = (Global)getActivity().getApplicationContext();
                go.setLocation("");
                Fragment frag2 = new AddDoctor();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        nameTv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(nameTv);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        emailTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(emailTv, true);

            }
        });

        mobileTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Validation.isPhoneNumber(mobileTv, false);

            }
        });

        locationTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Validation.hasText(locationTv);
            }
        });

        specialityTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Validation.hasText(specialityTv);
            }
        });


        return view;
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(nameTv)) ret = false;
        if (!Validation.hasText(locationTv)) ret = false;
        if (!Validation.hasText(specialityTv)) ret = false;
        if (!Validation.isEmailAddress(emailTv, true)) ret = false;
        if (!Validation.isPhoneNumber(mobileTv, false)) ret = false;

        return ret;
    }



}
