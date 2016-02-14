package com.mindnerves.meidcaldiary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import java.util.List;
import java.util.Locale;
import Model.PlacesList;

/**
 * Created by MNT on 30-Mar-15.
 */
public class MapActivity extends FragmentActivity implements LocationListener {
    GoogleMap googleMap;
    boolean isGPSEnabled = false;
    GPSTracker gps;
    Button searchButton, currentLocation;
    AlertDialogManager alert = new AlertDialogManager();
    EditText text;
    String countryName, city, region;
    String types;
    String fullAddress = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        getActionBar().hide();
        gps = new GPSTracker(this);
        setContentView(R.layout.map);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.googleMap)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        text = (EditText) findViewById(R.id.search_map);
        currentLocation = (Button) findViewById(R.id.current_location);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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

        if (gps != null && latitude != 0.0) {
            List<Address> addresses = null;
            try {
                Geocoder gcd = new Geocoder(MapActivity.this, Locale.ENGLISH);
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                System.out.println("Address condition:::::" + (addresses == null));
                if (addresses != null && !addresses.isEmpty()) {
                    fullAddress = addresses.get(0).getAddressLine(1) + ",";
                    fullAddress = fullAddress + addresses.get(0).getAddressLine(2);
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(1));
                    System.out.println("address1:::::" + addresses.get(0).getAddressLine(2));
                    System.out.println("Country Name= " + addresses.get(0).getCountryName());
                    countryName = addresses.get(0).getCountryName();
                    String[] cityArray = addresses.get(0).getAddressLine(2).split(",");
                    city = cityArray[0];
                    String state = cityArray[1].replaceAll("\\d", "");
                    state = state.replaceAll("\\s+", "");
                    region = state;
                    System.out.println("City = " + city);
                    System.out.println("state= " + state);

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
                .zoom(17)
                .bearing(90)
                .tilt(40)
                .build();
        // new LoadPlace().execute();

        searchButton = (Button) findViewById(R.id.search_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                types = text.getText().toString();
                System.out.println("Types::::" + types);
                text.setText("");
                finish();
                Intent intObj = new Intent(MapActivity.this, PlaceMapActivity.class);
                intObj.putExtra("search_string", types);
                // Sending user current geo location
                startActivity(intObj);


            }

        });
        final Double userLatitude = latitude;
        final Double userLongitude = longitude;
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringLocation = text.getText().toString();
                LayoutInflater factory = getLayoutInflater();
                View regisText = factory.inflate(R.layout.registration, null);
                EditText user = (EditText) regisText.findViewById(R.id.location);
                user.setText(stringLocation);
                Global go = (Global) MapActivity.this.getApplicationContext();
                go.setUserLatitude(userLatitude);
                go.setUserLongitude(userLongitude);
                go.setLocation(stringLocation);
                go.countryName = countryName;
                go.regionName = region;
                go.city = city;
                finish();
            }
        });
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions marker = new MarkerOptions().position(latLng).title(fullAddress);

        System.out.println("Marker::::::" + marker.toString());
        googleMap.addMarker(marker);
        text.setText(fullAddress);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    public void askUserToOpenGPS() {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this);

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

    public static HttpRequestFactory createRequestFactory(
            final HttpTransport transport) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                GoogleHeaders headers = new GoogleHeaders();
                headers.setApplicationName("AndroidHive-Places-Test");
                request.setHeaders(headers);
                JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
                request.addParser(parser);
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mindnerves.meidcaldiary/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mindnerves.meidcaldiary/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


