package com.medico.view.search;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.HomeAdapter;
import com.medico.application.R;
import com.medico.model.Person;
import com.medico.model.SearchParameterRequest;
import com.medico.util.LocationService;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */


public class PersonSearchView extends ParentFragment implements View.OnClickListener {

//    ProgressDialog progress;
    int LOCATION_REFRESH_TIME = 5000;
    int LOCATION_REFRESH_DISTANCE = 0;

    TextView city_list,country_list;
    Spinner search_by_criteria;
    EditText search_parameter;
    ImageView search_icon;
    boolean isGPSEnabled, isNetworkEnabled;
    SearchParameterRequest model = new SearchParameterRequest();

    HomeAdapter adapter;
    Object adapterParameter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_appointment_booking, container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getResources().getString(R.string.patient_search));
        country_list = (TextView) view.findViewById(R.id.country_list);
        city_list = (TextView) view.findViewById(R.id.city_list);
        search_parameter = (EditText) view.findViewById(R.id.search_parameter);
        search_icon = (ImageView) view.findViewById(R.id.search_icon);
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
        search_icon.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getCurrentLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
//        LocationService locationService = LocationService.getLocationManager(getActivity());
//        locationService.addNotifyListeber(this);
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
        inflater.inflate(R.menu.search, menu);

    }

    @Override
    public boolean isChanged()
    {
        return false;
    }
    @Override
    public void update()
    {

        switch (search_by_criteria.getSelectedItemPosition()) {
            case SEARCH_BY_PERSON_ID: {
                model.personId = new Integer(search_parameter.getText().toString());
             }
            break;
            case SEARCH_BY_PERSON_NAME: {
                model.personName = search_parameter.getText().toString();
            }
            break;
            case SEARCH_BY_MOBILE_NUMBER: {
                model.mobileNum = search_parameter.getText().toString();
             }
            break;

            case SEARCH_BY_PERSON_EMAIL_ID: {
                model.emailAddr = search_parameter.getText().toString();
             }
            break;
            case SEARCH_BY_PERSON_SPECIALITY: {
                model.speciality = search_parameter.getText().toString();
             }
            break;
        }

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
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.filter: {
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

                return true;
            }
            default:
                return false;

        }
    }
    public void onClick(View view)
    {
        Activity activity = getActivity();
        hideSoftKeyboard(getActivity());
        Bundle bundle = activity.getIntent().getExtras();
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer profileRole = bundle.getInt(PROFILE_ROLE);
        Integer searchRole = bundle.getInt(SEARCH_ROLE);
        Integer appointmentId = bundle.getInt(APPOINTMENT_ID);
        update();
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
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
//                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_PERSON_NAME: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                showResult(s);
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
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                showResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error) {
//                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    break;

                    case SEARCH_BY_PERSON_EMAIL_ID: {
                        api.searchPerson(model, new Callback<List<Person>>() {
                            @Override
                            public void success(List<Person> s, Response response) {
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                showResult(s);
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
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                showResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error) {
//                                Toast.makeText(getActivity(), "Appointment Create failed!!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

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

    private void getCurrentLocation()
    {
        LocationService service = LocationService.getLocationManager(getActivity());
        model.lattitude = service.latitude;
        model.longitude = service.longitude;
        model.country = service.countryCode;
        model.city = service.city;
        city_list.setText(service.city);
        country_list.setText(service.country);
    }

    private void showResult(List<Person> result)
    {
        SearchPersonListView personListView = new SearchPersonListView();
        personListView.setModel(result);
        personListView.setAdapter(adapter,adapterParameter);
        FragmentTransaction fft = getFragmentManager().beginTransaction();
        fft.add(R.id.service, personListView).addToBackStack(null).commit();
    }
    public void setAdapter(HomeAdapter adapter, Object parameter)
    {
        this.adapter = adapter;
        this.adapterParameter = parameter;
    }
}

