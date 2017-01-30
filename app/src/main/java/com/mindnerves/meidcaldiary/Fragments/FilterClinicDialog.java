package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
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
import android.widget.TextView;
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
import java.util.List;

import Model.Clinic;

/**
 * Created by User on 20-10-2015.
 */
public class FilterClinicDialog extends DialogFragment {
    Button button0to1, button2to4, button4to6, button6to8, button8to10, button10to12, button12to14, button14to16, button16to18, button18to20, button20ToPlus;
    GPSTracker gps;
    double currentLatitude = 0;
    double currentLongitude = 0;
    Global go;
    Button male, female, save, cancel;
    int flagButton = -1;
    boolean isGPSEnabled = false;
    ArrayList<Double> clinicsLatitudes = new ArrayList<Double>();
    ArrayList<Double> clinicsLongitude = new ArrayList<Double>();
    ArrayList<Double> clinicsDistance = new ArrayList<Double>();
    List<Clinic> clinics = new ArrayList<Clinic>();
    TextView genderText;
    static FilterClinicDialog newInstance() {
        return new FilterClinicDialog();
    }

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
        genderText = (TextView)view.findViewById(R.id.gender_text);
        female = (Button) view.findViewById(R.id.button_female);
        male.setVisibility(View.GONE);
        female.setVisibility(View.GONE);
        genderText.setVisibility(View.GONE);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listLength = clinicsLatitudes.size();
                clinicsDistance = new ArrayList<Double>();
                for (int i = 0; i < listLength; i++) {
                    System.out.println("Distance in miles::::::::" + distance(currentLatitude, currentLongitude, clinicsLatitudes.get(i),
                            clinicsLongitude.get(i)));
                    double distanceKm = distance(currentLatitude, currentLongitude, clinicsLatitudes.get(i),
                            clinicsLongitude.get(i)) * 1.60394;
                    System.out.println("Distance in KM:::::::::" + distanceKm);
                    clinicsDistance.add(distanceKm);
                }
                if (sortList().size() == 0) {
                    go.setAllClinicsList(clinics);

                } else {
                    go.setAllClinicsList(sortList());
                }
                System.out.println("New List size//////////////" + sortList().size());
                FilterClinicDialog.this.getDialog().cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.setAllClinicsList(clinics);
                FilterClinicDialog.this.getDialog().cancel();

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
        clinics =  go.getAllClinicsList();
        if(clinics != null){
            for(Clinic clinic : clinics){
                getLatLongFromPlace(clinic.getLocation());
            }
        }

        System.out.println("lat:::" + currentLatitude);
        System.out.println("long:::" + currentLongitude);
        return view;
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

    public void getLatLongFromPlace(String place) {
        try {
            fetchLatLongFromService fetch_latlng_from_service_abc = new fetchLatLongFromService(
                    place.replace("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        } catch (Exception e) {
            e.printStackTrace();


        }

    }
    public ArrayList<Clinic> sortList(){
        ArrayList<Clinic> newList = new ArrayList<Clinic>();
        if (flagButton == -1){
            Toast.makeText(getActivity(), "Please Select At Least One Filter", Toast.LENGTH_SHORT).show();
        }else{
            int len = clinicsDistance.size();
            switch (flagButton){
                case 1:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 0.0) && (distance <= 1.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 2.0) && (distance <= 4.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 4:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);

                        if ((distance >= 4.0) && (distance <= 6.0)) {
                             newList.add(clinics.get(i));
                        }
                    }
                    break;
                case 6:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 6.0) && (distance <= 8.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 8:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 8.0) && (distance <= 10.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 10:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 10.0) && (distance <= 12.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 12:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 12.0) && (distance <= 14.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 14:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 14.0) && (distance <= 16.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 16:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 16.0) && (distance <= 18.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 18:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 18.0) && (distance <= 20.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
                case 20:
                    for (int i = 0; i < len; i++) {
                        Double distance = clinicsDistance.get(i);
                        if ((distance >= 20.0)) {

                            newList.add(clinics.get(i));

                        }
                    }
                    break;
            }
        }
        return newList;
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
                clinicsLatitudes.add(lat);
                clinicsLongitude.add(lng);
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
