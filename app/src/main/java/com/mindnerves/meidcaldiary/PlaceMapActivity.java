package com.mindnerves.meidcaldiary;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by MNT on 31-Mar-15.
 */
public class PlaceMapActivity extends FragmentActivity {

    GoogleMap googleMap;
    String searchText = "";
    TextView showLocation;
    String stringLocation = "";
    Button save;
    Global go;
    String countryName,region,city;

    @Override
    public void onBackPressed() {

        finish();
        Intent intObj = new Intent(PlaceMapActivity.this, MapActivity.class);

        // Sending user current geo location

        startActivity(intObj);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);
        getActionBar().hide();
        Bundle extras = getIntent().getExtras();
        showLocation = (TextView)findViewById(R.id.search_map);
        if (extras != null) {
            searchText = extras.getString("search_string");

        }
        go = (Global) PlaceMapActivity.this.getApplicationContext();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {

            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map_view)).getMap();

            save = (Button)findViewById(R.id.save_location);
            System.out.println("Button:::::"+save.toString());
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater factory = getLayoutInflater();
                    View regisText = factory.inflate(R.layout.registration, null);
                    EditText user = (EditText) regisText.findViewById(R.id.location);
                    user.setText(stringLocation);
                    go.setLocation(stringLocation);
                    go.countryName = countryName;
                    go.regionName = region;
                    go.city = city;
                    finish();
                }
            });
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            getLatLongFromPlace(searchText);
        }



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
                System.out.println("Url:::::::"+googleMapUrl);

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

                System.out.println("Lat::::"+lat);
                System.out.println("Long::::"+lng);
                System.out.println("Address:::::"+addressString);
                LatLng point = new LatLng(lat, lng);

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(point)
                        .zoom(17)
                        .bearing(90)
                        .tilt(40)
                        .build();
                showLocation.setText(addressString);
                String[] cityArray = addressString.split(",");
                city = cityArray[0];
                region = cityArray[1];
                countryName = cityArray[2];
                System.out.println("City = "+city);
                System.out.println("Country= "+countryName);
                System.out.println("Region= "+region);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MarkerOptions marker = new MarkerOptions().position(point).title(addressString);
                System.out.println("Marker::::::" + marker.toString());
                googleMap.addMarker(marker);
                go.setUserLatitude(lat);
                go.setUserLongitude(lng);
                stringLocation = addressString;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(getApplicationContext());
            List<Address> address;
            fetchLatLongFromService fetch_latlng_from_service_abc = new fetchLatLongFromService(
                    place.replaceAll("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        } catch (Exception e) {
            e.printStackTrace();


        }

    }
}
