package com.medico.view.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.PatientSearchListAdapter;
import com.medico.application.R;
import com.medico.model.Person;
import com.medico.model.SearchParameterRequest;
import com.medico.util.LocationService;
import com.medico.util.Notifier;
import com.medico.util.NotifyListener;
import com.medico.view.ManagePatientProfile;
import com.medico.view.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */


public class PersonSearchView extends ParentFragment implements View.OnClickListener, NotifyListener {

//    ProgressDialog progress;
    int LOCATION_REFRESH_TIME = 5000;
    int LOCATION_REFRESH_DISTANCE = 0;

    TextView city_list,country_list;
    Spinner search_by_criteria,visitType;
    EditText search_parameter;
    ImageView search_icon;
    ListView search_result;
    boolean isGPSEnabled, isNetworkEnabled;
//    LocationManager lm;
    SearchParameterRequest model = new SearchParameterRequest();
//    private final LocationListener gpsLocationListener =new LocationListener(){
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    Toast.makeText(getActivity(), "GPS Service available again" , Toast.LENGTH_LONG).show();
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    Toast.makeText(getActivity(), "GPS out of service\n" , Toast.LENGTH_LONG).show();
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Toast.makeText(getActivity(), "GPS temporarily unavailable\nn" , Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Toast.makeText(getActivity(), "GPS Provider Enabled\nn" , Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Toast.makeText(getActivity(), "GPS Provider Disabled" , Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            lm.removeUpdates(networkLocationListener);
//            Toast.makeText(getActivity(), "New GPS location:"
//                    + String.format("%9.6f", location.getLatitude()) + ", "
//                    + String.format("%9.6f", location.getLongitude()) + "\n" , Toast.LENGTH_LONG).show();
//        }
//    };
//    private final LocationListener networkLocationListener =
//            new LocationListener(){
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                    switch (status) {
//                        case LocationProvider.AVAILABLE:
//                            Toast.makeText(getActivity(), "NW Service available again" , Toast.LENGTH_LONG).show();
//                            break;
//                        case LocationProvider.OUT_OF_SERVICE:
//                            Toast.makeText(getActivity(), "NW out of service\n" , Toast.LENGTH_LONG).show();
//                            break;
//                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                            Toast.makeText(getActivity(), "NW temporarily unavailable\nn" , Toast.LENGTH_LONG).show();
//                            break;
//                    }
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//                    Toast.makeText(getActivity(), "NW Provider Enabled\nn" , Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//                    Toast.makeText(getActivity(), "NW Provider Disabled" , Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onLocationChanged(Location location) {
//                    lm.removeUpdates(networkLocationListener);
//                    Toast.makeText(getActivity(), "New NW location:"
//                            + String.format("%9.6f", location.getLatitude()) + ", "
//                            + String.format("%9.6f", location.getLongitude()) + "\n" , Toast.LENGTH_LONG).show();
//                    String cityName = null;
//                    Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
//                    List<Address> addresses;
//                    try {
//                        addresses = gcd.getFromLocation(location.getLatitude(),
//                                location.getLongitude(), 1);
//                        if (addresses.size() > 0)
//                            System.out.println(addresses.get(0).getLocality());
//                        cityName = addresses.get(0).getLocality();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    String s =  cityName;
//                    city_list.setText(s);
//
//                }
//            };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_appointment_booking, container,false);
        setHasOptionsMenu(false);
//        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        try {
//            mLocationManager.requestSingleUpdate (LocationManager.GPS_PROVIDER, this, null);
//        }
//        catch(SecurityException e)
//        {
//            e.printStackTrace();
//        }

        country_list = (TextView) view.findViewById(R.id.country_list);
        city_list = (TextView) view.findViewById(R.id.city_list);
        search_parameter = (EditText) view.findViewById(R.id.search_parameter);
        search_icon = (ImageView) view.findViewById(R.id.search_icon);
        search_result = (ListView) view.findViewById(R.id.search_result);
        search_by_criteria = (Spinner) view.findViewById(R.id.search_by_criteria);
        search_by_criteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_parameter.setText("");
                switch(position)
                {
                    case SEARCH_BY_PERSON_ID:
                        search_parameter.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case SEARCH_BY_MOBILE_NUMBER:
                        search_parameter.setInputType(InputType.TYPE_CLASS_PHONE);
                        break;
                    case SEARCH_BY_PERSON_EMAIL_ID:
                        search_parameter.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;
                    case SEARCH_BY_PERSON_NAME:
                    case SEARCH_BY_PERSON_SPECIALITY:
                        search_parameter.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        visitType = (Spinner) view.findViewById(R.id.visit_type_choice);
        search_icon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
//        getLocation();
//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));


//    public void AddLink( DoctorAppointment appointment) {
//
//        if(appointment.appointmentId == null || appointment.appointmentId.intValue() == 0) {
//
//            api.createAppointment(appointment, new Callback<AppointmentResponse>() {
//                @Override
//                public void success(AppointmentResponse s, Response response) {
//                    Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
//                    getActivity().onBackPressed();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//        else
//        {
//            api.updateAppointment(appointment, new Callback<AppointmentResponse>() {
//                @Override
//                public void success(AppointmentResponse s, Response response) {
//                    Toast.makeText(getActivity(), "Appointment update Successful!!", Toast.LENGTH_LONG).show();
//                    getActivity().onBackPressed();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Toast.makeText(getActivity(), "Appointment update failed!!", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//
    }

    @Override
    public void onResume() {
        super.onResume();
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getActivity(), "No Permission Granted" , Toast.LENGTH_LONG).show();
//            return  ;
//        }
//        lm.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 5000, 0,
//                networkLocationListener);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                3000, 0, gpsLocationListener);
        LocationService locationService = LocationService.getLocationManager(getActivity());
        locationService.addNotifyListeber(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        lm.removeUpdates(networkLocationListener);
//        lm.removeUpdates(gpsLocationListener);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.save);
    }

    @Override
    public boolean isChanged()
    {
        return false;
    }
    @Override
    public void update()
    {

    }
    @Override
    public boolean save()
    {
//        if(doctorAppointment.canBeSaved())
//        {
//            saveAppointment(doctorAppointment);
//            return true;
//        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
//        if(((DoctorAppointmentGridViewAdapter)timeTeableList.getAdapter()).getSelectedAppointmentTime() == null)
//            return false;
        return false;
    }
    @Override
    public void setEditable(boolean editable)
    {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }

            }
            break;
            case R.id.home: {

            }
            break;

        }
        return true;
    }
    public void onClick(View view)
    {
        Activity activity = getActivity();
        Bundle bundle = activity.getIntent().getExtras();
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer profileRole = bundle.getInt(PROFILE_ROLE);
        Integer searchRole = bundle.getInt(SEARCH_ROLE);
        Integer appointmentId = bundle.getInt(APPOINTMENT_ID);

        if(isValid(search_by_criteria.getSelectedItemPosition())) {
            if (searchRole == PATIENT || searchRole == ASSISTANT) {


                switch (search_by_criteria.getSelectedItemPosition()) {
                    case SEARCH_BY_PERSON_ID: {
                        model.personId = new Integer(search_parameter.getText().toString());
                        api.searchPersonById(model, new Callback<Person>() {
                            @Override
                            public void success(Person s, Response response) {
                                List<Person> searchList = new ArrayList<Person>();
                                searchList.add(s);
                                search_result.setAdapter(new PatientSearchListAdapter(getActivity(),searchList));
                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_PERSON_NAME: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                search_result.setAdapter(new PatientSearchListAdapter(getActivity(),s));
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_MOBILE_NUMBER: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                search_result.setAdapter(new PatientSearchListAdapter(getActivity(),s));
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;

                    case SEARCH_BY_PERSON_EMAIL_ID: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                search_result.setAdapter(new PatientSearchListAdapter(getActivity(),s));
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_PERSON_SPECIALITY: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                search_result.setAdapter(new PatientSearchListAdapter(getActivity(),s));
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;
                }

            }
            else if (searchRole == DOCTOR)
            {

            }
        }
    }

    private boolean isValid(int searchType)
    {
        if(city_list.getText().length() > 0 && search_parameter.getText().length() > 0)
            return true;
        else
            return false;
    }
//    @Override
//    public void onLocationChanged(Location location)
//    {
//        model.lat = location.getLatitude();
//        model.lat = location.getLongitude();
//        Bundle bundle = location.getExtras();
//        Toast.makeText(getActivity(), "On Location Changed!!" +location.getLatitude()+" "+ location.getLongitude() , Toast.LENGTH_LONG).show();
//    }
//    public void onProviderEnabled(String string)
//    {
//        Toast.makeText(getActivity(), "On Provider Enabled" , Toast.LENGTH_LONG).show();
//
//    }
//    public void onProviderDisabled(String string)
//    {
//        Toast.makeText(getActivity(), "On Provider Disabled" , Toast.LENGTH_LONG).show();
//    }
//    public void onStatusChanged(String provider, int status, Bundle extras)
//    {
//        Toast.makeText(getActivity(), "On Status Changed" , Toast.LENGTH_LONG).show();
//    }
//    private void getLocation() {
//        lm = (LocationManager) getActivity().getSystemService(
//                Context.LOCATION_SERVICE);
//
//        // getting GPS status
//        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        // getting network status
//        isNetworkEnabled = lm
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (!isGPSEnabled && !isNetworkEnabled) {
//            // no network provider is enabled
//            Toast.makeText(getActivity(), "No Provider available" , Toast.LENGTH_LONG).show();
//        } else {
//            if ( Build.VERSION.SDK_INT >= 23 &&
//                    ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getActivity(), "No Permission Granted" , Toast.LENGTH_LONG).show();
//                return  ;
//            }
//
//
//            if (isGPSEnabled) {
//                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                        LOCATION_REFRESH_TIME, 0, gpsLocationListener);
//                Toast.makeText(getActivity(), "GPS Provider available" , Toast.LENGTH_LONG).show();
//            }
//
//            if (isNetworkEnabled) {
//                if (lm == null) {
//                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            LOCATION_REFRESH_TIME, 0, networkLocationListener);
//                    Toast.makeText(getActivity(), "NW Provider available" , Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
    public void notify(int id, Notifier source, Object parameter)
    {
        if(source instanceof LocationService) {

            LocationService service = (LocationService)source;
            model.lat = service.latitude;
            model.lon = service.longitude;
            model.country = service.country;
            model.city = service.city;
            city_list.setText(service.city);
            country_list.setText(service.country);
        }
    }
}

