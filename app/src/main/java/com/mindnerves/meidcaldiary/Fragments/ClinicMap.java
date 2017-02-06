package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.List;
import java.util.Locale;

import com.medico.model.Clinic;

/**
 * Created by User on 21-10-2015.
 */
public class ClinicMap extends Fragment {
    public static View view;
    GoogleMap googleMap;
    GPSTracker gps;
    boolean isGPSEnabled = false;
    String fullAddress = "";
    EditText search;
    String searchText = "";
    String speciality = "";
    double currentLatitude = 0;
    double currentlongitude = 0;
    Button filter;
    Global go;
    int flag = 0;
    List<Clinic> clinicList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.doctor_map_show, container, false);
        } catch (InflateException e) {
            e.printStackTrace();

            refresh();

        }
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getActivity(), "Google Map Service is not Available", Toast.LENGTH_SHORT).show();
        }
        go = (Global) getActivity().getApplicationContext();
        clinicList = go.getAllClinicsList();
        System.out.println("List Size///////////" + clinicList.size());
        search = (EditText) getActivity().findViewById(R.id.searchET);
        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        filter = (Button) getActivity().findViewById(R.id.filter_home);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flag == 1) {
                    for (Clinic clinic : clinicList) {

                        if (clinic.getClinicName().contains(search.getText().toString())) {
                            googleMap.clear();
                            LatLng latLng = new LatLng(currentLatitude, currentlongitude);
                            MarkerOptions marker = new MarkerOptions().position(latLng).title(fullAddress);
                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            System.out.println("Marker::::::" + marker.toString());
                            googleMap.addMarker(marker);
                            getLatLongFromPlaceSearch(clinic.getLocation());
                        }

                    }
                } else {
                    flag = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().toString().equals("")) {
                    googleMap.clear();
                    showClinicLocation();
                }
            }
        });
        gps = new GPSTracker(getActivity());
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map_view)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(HomeActivity.LOCATION_SERVICE);
        System.out.println("LocationManager::::::" + locationManager.toString());
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.println("isGpsEnabled:::::" + isGPSEnabled);
        double latitude = 0;
        double longitude = 0;
        if (isGPSEnabled) {
            System.out.println("I am in true:::::::");

        } else {
            askUserToOpenGPS();
        }

        if (gps.canGetLocation() == false) {
            gps.showSettingsAlert();
        } else {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        System.out.println("lat:::" + latitude);
        System.out.println("long:::" + longitude);

        System.out.println("Gps condition;:::::" + (gps != null));
        System.out.println("Gps location;:::::" + (gps.location != null));
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        currentLatitude = latitude;
        currentlongitude = longitude;
        System.out.println("Gps location;:::::" + (location != null));
        System.out.println("Current Value:::::" + latitude);
        System.out.println("Current Value:::::" + longitude);
        if (gps != null && latitude != 0.0) {
            List<Address> addresses = null;
            try {
                Geocoder gcd = new Geocoder(getActivity(), Locale.ENGLISH);
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                System.out.println("Address condition:::::" + (addresses == null));
                if (addresses != null && !addresses.isEmpty()) {
                    fullAddress = addresses.get(0).getAddressLine(1);
                    fullAddress = fullAddress + addresses.get(0).getAddressLine(2);
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(1));
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(2));
                } else {
                    System.out.println("I am here:::::::::::::::");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            System.out.println("i am in gps code::::::::");
            if (!gps.isGPSEnabled) {
                isGPSEnabled = true;
                gps.showSettingsAlert();
            }
        }
        System.out.println("Fulladdress:::::" + fullAddress);
        LatLng latLng = new LatLng(latitude, longitude);
        gps.setLocation(location);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(9)
                .bearing(90)
                .tilt(40)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions marker = new MarkerOptions().position(latLng).title(fullAddress);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        System.out.println("Marker::::::" + marker.toString());
        googleMap.addMarker(marker);
        showClinicLocation();
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClinicFilterDialog filter = ClinicFilterDialog.newInstance();
                filter.show(getFragmentManager(), "Dialog");
                go.setAllClinicsList(clinicList);
            }
        });
        return view;
    }
    public void getLatLongFromPlaceSearch(String place) {
        try {
            fetchLatLongFromServiceFromSearch fetch_latlng_from_service_abc = new fetchLatLongFromServiceFromSearch(
                    place.replace("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void showClinicLocation() {
        for (Clinic clinic : clinicList) {
            getLatLongFromPlace(clinic.getLocation());
        }
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
    public void refresh(){
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getActivity(), "Google Map Service is not Available", Toast.LENGTH_SHORT).show();
        }
        go = (Global) getActivity().getApplicationContext();
        clinicList = go.getAllClinicsList();
        System.out.println("List Size///////////" + clinicList.size());
        search = (EditText) getActivity().findViewById(R.id.searchET);
        gps = new GPSTracker(getActivity());
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map_view)).getMap();
        googleMap.clear();
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(HomeActivity.LOCATION_SERVICE);
        System.out.println("LocationManager::::::" + locationManager.toString());
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.println("isGpsEnabled:::::" + isGPSEnabled);
        double latitude = 0;
        double longitude = 0;
        if (isGPSEnabled) {
            System.out.println("I am in true:::::::");

        } else {
            askUserToOpenGPS();
        }

        if (gps.canGetLocation() == false) {
            gps.showSettingsAlert();
        } else {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        System.out.println("lat:::" + latitude);
        System.out.println("long:::" + longitude);
        System.out.println("Gps condition;:::::" + (gps != null));
        System.out.println("Gps location;:::::" + (gps.location != null));
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        System.out.println("Gps location;:::::" + (location != null));
        System.out.println("Current Value:::::" + latitude);
        System.out.println("Current Value:::::" + longitude);
        currentLatitude = latitude;
        currentlongitude = longitude;
        if (gps != null && latitude != 0.0) {
            List<Address> addresses = null;
            try {
                Geocoder gcd = new Geocoder(getActivity(), Locale.ENGLISH);
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                System.out.println("Address condition:::::" + (addresses == null));
                if (addresses != null && !addresses.isEmpty()) {
                    fullAddress = addresses.get(0).getAddressLine(1);
                    fullAddress = fullAddress + addresses.get(0).getAddressLine(2);
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(1));
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(2));
                } else {
                    System.out.println("I am here:::::::::::::::");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            System.out.println("i am in gps code::::::::");
            if (!gps.isGPSEnabled) {
                isGPSEnabled = true;
                gps.showSettingsAlert();
            }
        }
        showClinicLocation();
        System.out.println("Fulladdress:::::" + fullAddress);
        LatLng latLng = new LatLng(latitude, longitude);
        gps.setLocation(location);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(9)
                .bearing(90)
                .tilt(40)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions marker = new MarkerOptions().position(latLng).title(fullAddress);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        System.out.println("Marker::::::" + marker.toString());
        googleMap.addMarker(marker);
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
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

    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {
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

                System.out.println("Lat::::" + lat);
                System.out.println("Long::::" + lng);
                System.out.println("Address:::::" + addressString);
                LatLng point = new LatLng(lat, lng);


                MarkerOptions marker = new MarkerOptions().position(point).title(addressString);
                System.out.println("Marker::::::" + marker.toString());
                googleMap.addMarker(marker);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }

    }
    public class fetchLatLongFromServiceFromSearch extends
            AsyncTask<Void, Void, StringBuilder> {
        String place;


        public fetchLatLongFromServiceFromSearch(String place) {
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

                System.out.println("Lat::::" + lat);
                System.out.println("Long::::" + lng);
                System.out.println("Address:::::" + addressString);
                LatLng point = new LatLng(lat, lng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(point)
                        .zoom(9)
                        .bearing(90)
                        .tilt(40)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MarkerOptions marker = new MarkerOptions().position(point).title(addressString);
                System.out.println("Marker::::::" + marker.toString());
                googleMap.addMarker(marker);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }

    }
}
