package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.GPSTracker;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.DoctorSearchResponse;

/**
 * Created by User on 9/21/15.
 */
public class FilterDialogMap extends DialogFragment {
    Button button0to1, button2to4, button4to6, button6to8, button8to10, button10to12, button12to14, button14to16, button16to18, button18to20, button20ToPlus;
    Button male, female, save, cancel;
    boolean isGPSEnabled = false;
    GPSTracker gps;
    double currentLatitude = 0;
    double currentLongitude = 0;
    Global go;
    ArrayList<Double> doctorsLatitudes = new ArrayList<Double>();
    ArrayList<Double> doctorsLongitude = new ArrayList<Double>();
    ArrayList<Double> doctorsDistance = new ArrayList<Double>();
    ArrayList<DoctorSearchResponse> doctors = new ArrayList<DoctorSearchResponse>();

    static FilterDialogMap newInstance() {
        return new FilterDialogMap();
    }

    int flagButton = -1;
    String sexString = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.filter_layout, container, false);
        getDialog().setTitle("Filter");
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(HomeActivity.LOCATION_SERVICE);
        System.out.println("LocationManager::::::" + locationManager.toString());
        button0to1 = (Button) view.findViewById(R.id.button_0_1_km);
        button2to4 = (Button) view.findViewById(R.id.button_2_4_km);
        button4to6 = (Button) view.findViewById(R.id.button_4_6_km);
        button6to8 = (Button) view.findViewById(R.id.button_6_8_km);
        button8to10 = (Button) view.findViewById(R.id.button_8_10_km);
        button10to12 = (Button) view.findViewById(R.id.button_10_12_km);
        button12to14 = (Button) view.findViewById(R.id.button_12_14_km);
        button14to16 = (Button) view.findViewById(R.id.button_14_16_km);
        button16to18 = (Button) view.findViewById(R.id.button_16_18_km);
        button18to20 = (Button) view.findViewById(R.id.button_18_20_km);
        button20ToPlus = (Button) view.findViewById(R.id.button_20_above_km);
        male = (Button) view.findViewById(R.id.button_male);
        female = (Button) view.findViewById(R.id.button_female);
        go = (Global) getActivity().getApplicationContext();
        save = (Button) view.findViewById(R.id.save_filter);
        cancel = (Button) view.findViewById(R.id.cancel_filter);
        button0to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button0to1.setBackgroundColor(Color.parseColor("#FF0000"));
                button0to1.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 1;
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button2to4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2to4.setBackgroundColor(Color.parseColor("#FF0000"));
                button2to4.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 2;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button4to6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4to6.setBackgroundColor(Color.parseColor("#FF0000"));
                button4to6.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 4;
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button6to8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6to8.setBackgroundColor(Color.parseColor("#FF0000"));
                button6to8.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 6;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button8to10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button8to10.setBackgroundColor(Color.parseColor("#FF0000"));
                button8to10.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 8;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button10to12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button10to12.setBackgroundColor(Color.parseColor("#FF0000"));
                button10to12.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 10;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button12to14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button12to14.setBackgroundColor(Color.parseColor("#FF0000"));
                button12to14.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 12;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button14to16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button14to16.setBackgroundColor(Color.parseColor("#FF0000"));
                button14to16.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 14;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button16to18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button16to18.setBackgroundColor(Color.parseColor("#FF0000"));
                button16to18.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 16;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button14to16.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button18to20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button18to20.setBackgroundColor(Color.parseColor("#FF0000"));
                button18to20.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 18;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button14to16.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button20ToPlus.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button20ToPlus.setTextColor(Color.parseColor("#000000"));
            }
        });
        button20ToPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button20ToPlus.setBackgroundColor(Color.parseColor("#FF0000"));
                button20ToPlus.setTextColor(Color.parseColor("#ffffff"));
                flagButton = 20;
                button0to1.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button2to4.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button4to6.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button6to8.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button8to10.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button10to12.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button12to14.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button14to16.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button16to18.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button18to20.setBackgroundColor(Color.parseColor("#BBE0E3"));
                button0to1.setTextColor(Color.parseColor("#000000"));
                button2to4.setTextColor(Color.parseColor("#000000"));
                button4to6.setTextColor(Color.parseColor("#000000"));
                button6to8.setTextColor(Color.parseColor("#000000"));
                button8to10.setTextColor(Color.parseColor("#000000"));
                button10to12.setTextColor(Color.parseColor("#000000"));
                button12to14.setTextColor(Color.parseColor("#000000"));
                button14to16.setTextColor(Color.parseColor("#000000"));
                button16to18.setTextColor(Color.parseColor("#000000"));
                button18to20.setTextColor(Color.parseColor("#000000"));
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackgroundColor(Color.parseColor("#FF0000"));
                male.setTextColor(Color.parseColor("#ffffff"));
                sexString = "Male";
                female.setBackgroundColor(Color.parseColor("#BBE0E3"));
                female.setTextColor(Color.parseColor("#000000"));
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackgroundColor(Color.parseColor("#FF0000"));
                female.setTextColor(Color.parseColor("#ffffff"));
                sexString = "Female";
                male.setBackgroundColor(Color.parseColor("#BBE0E3"));
                male.setTextColor(Color.parseColor("#000000"));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listLength = doctorsLatitudes.size();
                doctorsDistance = new ArrayList<Double>();
                for (int i = 0; i < listLength; i++) {
                    System.out.println("Distance in miles::::::::" + distance(currentLatitude, currentLongitude, doctorsLatitudes.get(i),
                            doctorsLongitude.get(i)));
                    double distanceKm = distance(currentLatitude, currentLongitude, doctorsLatitudes.get(i),
                            doctorsLongitude.get(i)) * 1.60394;
                    System.out.println("Distance in KM:::::::::" + distanceKm);
                    doctorsDistance.add(distanceKm);
                }
                if (sortList().size() == 0) {
                    go.setDoctorSpeciality(doctors);

                } else {
                    go.setDoctorSpeciality(sortList());
                }

                System.out.println("New List size//////////////" + sortList().size());
                Bundle bun = new Bundle();
                bun.putSerializable("doctors", sortList());
                bun.putString("specialization", go.getSpecialityString());
                Fragment fragment = new DoctorMap();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.map_layout, fragment, "Manage_Doctor").addToBackStack(null).commit();
                FilterDialogMap.this.getDialog().cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.setDoctorSpeciality(doctors);
                FilterDialogMap.this.getDialog().cancel();

            }
        });
        gps = new GPSTracker(getActivity());
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.println("isGpsEnabled:::::" + isGPSEnabled);

        if (isGPSEnabled) {
            System.out.println("I am in true:::::::");

        } else {
            askUserToOpenGPS();
        }

        if (gps.canGetLocation() == false) {
            gps.showSettingsAlert();
        } else {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }
        doctors = go.getDoctorSpeciality();
        if (doctors != null) {
            for (DoctorSearchResponse doctor : doctors) {
                System.out.println("Doctor sex::::::::::" + doctor.getGender());
                getLatLongFromPlace(doctor.getLocation());
            }
        }
        System.out.println("lat:::" + currentLatitude);
        System.out.println("long:::" + currentLongitude);
        return view;
    }

    public void askUserToOpenGPS() {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        mAlertDialog.setTitle("Location not available, Open GPS?")
                .setMessage("Activate GPS to use use location services?")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void getLatLongFromPlace(String place) {
        try {
            fetchLatLongFromService fetch_latlng_from_service_abc = new fetchLatLongFromService(
                    place.replace("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public ArrayList<DoctorSearchResponse> sortList() {
        ArrayList<DoctorSearchResponse> newList = new ArrayList<DoctorSearchResponse>();

        if ((flagButton == -1) && (sexString.equals(""))) {
            Toast.makeText(getActivity(), "Please Select At Least One Filter", Toast.LENGTH_SHORT).show();
        } else {
            if (sexString.equals("")) {
                int len = doctorsDistance.size();
                switch (flagButton) {
                    case 1:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 0.0) && (distance <= 1.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 2:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 2.0) && (distance <= 4.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);

                            if ((distance >= 4.0) && (distance <= 6.0)) {
                                System.out.println("Condition::::::::::" + ((distance >= 4.0) && (distance <= 6.0)));
                                System.out.println("Sex Condition:::::::::" + doctors.get(i).getGender());
                                System.out.println("Sex Fragment:::::::::" + sexString);

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 6:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 6.0) && (distance <= 8.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 8:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 8.0) && (distance <= 10.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 10:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 10.0) && (distance <= 12.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 12:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 12.0) && (distance <= 14.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 14:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 14.0) && (distance <= 16.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 16:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 16.0) && (distance <= 18.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 18:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 18.0) && (distance <= 20.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                    case 20:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 20.0)) {

                                newList.add(doctors.get(i));

                            }
                        }
                        break;
                }
            }

            if (flagButton == -1) {
                int len = doctorsDistance.size();
                for (int i = 0; i < len; i++) {
                    if (sexString.equalsIgnoreCase(sexString)) {
                        newList.add(doctors.get(i));
                    }

                }
            }

            if ((flagButton != -1) && (!sexString.equalsIgnoreCase(""))) {
                int len = doctorsDistance.size();
                switch (flagButton) {
                    case 1:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 0.0) && (distance <= 1.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 2:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 2.0) && (distance <= 4.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 4:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);

                            if ((distance >= 4.0) && (distance <= 6.0)) {
                                System.out.println("Condition::::::::::" + ((distance >= 4.0) && (distance <= 6.0)));
                                System.out.println("Sex Condition:::::::::" + doctors.get(i).getGender());
                                System.out.println("Sex Fragment:::::::::" + sexString);
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 6:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 6.0) && (distance <= 8.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 8:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 8.0) && (distance <= 10.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 10:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 10.0) && (distance <= 12.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 12:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 12.0) && (distance <= 14.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 14:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 14.0) && (distance <= 16.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 16:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 16.0) && (distance <= 18.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 18:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 18.0) && (distance <= 20.0)) {
                                if (sexString.equalsIgnoreCase(doctors.get(i).getGender())) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                    case 20:
                        for (int i = 0; i < len; i++) {
                            Double distance = doctorsDistance.get(i);
                            if ((distance >= 20.0)) {
                                if (sexString.equalsIgnoreCase(sexString)) {
                                    newList.add(doctors.get(i));
                                }
                            }
                        }
                        break;
                }
            }
        }

        return newList;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public class fetchLatLongFromService extends AsyncTask<Void, Void, StringBuilder> {
        String place;

        public fetchLatLongFromService(String place) {
            super();
            this.place = place;
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?address="
                        + this.place + "&sensor=false";
                System.out.println("Url:::::::" + googleMapUrl);
                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                String a = "";
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");
                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);
                String addressString = before_geometry_jsonObj.getString("formatted_address");
                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry");
                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject("location");
                String lat_helper = location_jsonObj.getString("lat");
                double lat = Double.valueOf(lat_helper);
                String lng_helper = location_jsonObj.getString("lng");
                double lng = Double.valueOf(lng_helper);
                doctorsLatitudes.add(lat);
                doctorsLongitude.add(lng);
                System.out.println("Lat::::" + lat);
                System.out.println("Long::::" + lng);
                System.out.println("Address:::::" + addressString);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }
}