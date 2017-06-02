package com.medicohealthcare.view.search;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.HomeAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.DoctorSearch;
import com.medicohealthcare.model.DoctorSearchResult;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.SearchParameterRequest;
import com.medicohealthcare.util.GeoUtility;
import com.medicohealthcare.util.LocationService;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.Notifier;
import com.medicohealthcare.util.NotifyListener;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */


public class PersonSearchView extends ParentFragment implements View.OnClickListener, NotifyListener
{
    AutoCompleteTextView location;
    Spinner search_by_criteria;
    EditText city_list,country_list;
    EditText search_parameter;
    ImageView search_icon;
    boolean isGPSEnabled, isNetworkEnabled;
    SearchParameterRequest model = new SearchParameterRequest();
    Button location_delete_button, current_location_button;
    HomeAdapter adapter;
    Object adapterParameter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.patient_appointment_booking, container,false);
        Bundle bundle = getActivity().getIntent().getExtras();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        if(bundle.getInt(SETTING_VIEW_ID)== PATIENT_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.patient_search));
        else if( bundle.getInt(SETTING_VIEW_ID)== CLINIC_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.clinic_search));
        country_list = (EditText) view.findViewById(R.id.country_list);
        city_list = (EditText) view.findViewById(R.id.city_list);
        location = (AutoCompleteTextView) view.findViewById(R.id.location);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
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
        LocationService.getLocationManager(getActivity()).addNotifyListeber(this);
        new GeoUtility(getActivity(), location, country_list, city_list, location_delete_button, current_location_button, model);
        getCurrentLocation();
        Bundle bundle = getActivity().getIntent().getExtras();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        if(bundle.getInt(SETTING_VIEW_ID)== PATIENT_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.patient_search));
        else if( bundle.getInt(SETTING_VIEW_ID)==DOCTOR_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.clinic_search));
        else if( bundle.getInt(SETTING_VIEW_ID)== ASSISTANT_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.clinic_search));
        else if( bundle.getInt(SETTING_VIEW_ID)== CLINIC_SETTING_VIEW)
            textviewTitle.setText(getActivity().getResources().getString(R.string.clinic_search));
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
        Bundle bundle = getActivity().getIntent().getExtras();
        model.loginUserId = bundle.getInt(PROFILE_ID);
        model.loggedinUserId = bundle.getInt(PROFILE_ID);
        switch (search_by_criteria.getSelectedItemPosition()) {
            case SEARCH_BY_PERSON_ID: {
                model.personId = new Integer(search_parameter.getText().toString());
             }
            break;
            case SEARCH_BY_PERSON_NAME: {
                model.personName = search_parameter.getText().toString();
                model.clinicName = search_parameter.getText().toString();
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

        return false;
    }
    @Override
    public boolean canBeSaved()
    {
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
        Integer search_type = bundle.getInt(SETTING_VIEW_ID);
        update();
        if(isValid(search_by_criteria.getSelectedItemPosition()))
        {
            if ((search_type == PATIENT_SETTING_VIEW ||search_type == ASSISTANT_SETTING_VIEW ) && (searchRole == PATIENT || searchRole == ASSISTANT))
            {


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
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
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
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }

                }


            }
            else if (search_type == DOCTOR_SETTING_VIEW && searchRole == DOCTOR)
            {
                model.role = 1;
                switch (search_by_criteria.getSelectedItemPosition())
                {
                    case SEARCH_BY_PERSON_ID:
                    case SEARCH_BY_PERSON_NAME:
                    case SEARCH_BY_PERSON_EMAIL_ID:
                    case SEARCH_BY_MOBILE_NUMBER:
                    case SEARCH_BY_PERSON_SPECIALITY:
                    {
                        api.searchDoctor(model, new Callback<List<DoctorSearchResult>>() {
                            @Override
                            public void success(List<DoctorSearchResult> s, Response response) {
//                                Toast.makeText(getActivity(), "Appointment Create Successful!!", Toast.LENGTH_LONG).show();
                                showDoctorResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                    break;
                    default:
                    //do nothing
                }
            }
            else if (search_type == CLINIC_SETTING_VIEW && searchRole == CLINIC)
            {
                switch (search_by_criteria.getSelectedItemPosition()) {
                    case SEARCH_BY_PERSON_ID: {
                        model.clinicId = new Integer(search_parameter.getText().toString());
                        api.searchClinicById(model, new Callback<Clinic1>() {
                            @Override
                            public void success(Clinic1 s, Response response) {
                                List<Clinic1> searchList = new ArrayList<Clinic1>();
                                searchList.add(s);
                                showClinicResult(searchList);
                            }

                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_PERSON_NAME: {
                        api.searchClinic(model, new Callback<List<Clinic1>>() {
                            @Override
                            public void success(List<Clinic1> s, Response response) {
                                showClinicResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_MOBILE_NUMBER: {
                        api.searchClinic(model, new Callback<List<Clinic1>>() {
                            @Override
                            public void success(List<Clinic1> s, Response response) {
                                showClinicResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                    break;

                    case SEARCH_BY_PERSON_EMAIL_ID: {
                        api.searchClinic(model, new Callback<List<Clinic1>>() {
                            @Override
                            public void success(List<Clinic1> s, Response response) {
                                showClinicResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }
                    break;
                    case SEARCH_BY_PERSON_SPECIALITY: {
                        api.searchClinic(model, new Callback<List<Clinic1>>() {
                            @Override
                            public void success(List<Clinic1> s, Response response) {
                                showClinicResult(s);
                            }
                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                new MedicoCustomErrorHandler(getActivity()).handleError(error);
                            }
                        });
                    }

                }
            }
        }
    }

    private boolean isValid(int searchType)
    {
        if(location.getText().length() > 0 && search_parameter.getText().length() > 0)
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
//        location.setText(service.partialAddress);
    }

    private void showResult(List<Person> result)
    {
        SearchPersonListView personListView = new SearchPersonListView();
        personListView.setModel(result);
        personListView.setAdapter(adapter,adapterParameter);
        FragmentTransaction fft = getFragmentManager().beginTransaction();
        fft.add(R.id.service, personListView).addToBackStack(null).commit();
    }
    private void showDoctorResult(List<DoctorSearchResult> result)
    {
        List<DoctorSearch> doctorSearches = new ArrayList<>();
        for(DoctorSearchResult re : result)
        {
            for(DoctorSearchResult.PersonSlot slot: re.personSlotList)
            {
                doctorSearches.add(new DoctorSearch(slot.person,re.clinic,slot.slots));
            }
        }
        SearchDoctorListView personListView = new SearchDoctorListView();
        personListView.setModel(doctorSearches);
        FragmentTransaction fft = getFragmentManager().beginTransaction();
        fft.add(R.id.service, personListView).addToBackStack(null).commit();
    }
    private void showClinicResult(List<Clinic1> result)
    {
        SearchClinicListView personListView = new SearchClinicListView();
        personListView.setModel(result);
        FragmentTransaction fft = getFragmentManager().beginTransaction();
        fft.add(R.id.service, personListView).addToBackStack(null).commit();
    }
    public void setAdapter(HomeAdapter adapter, Object parameter)
    {
        this.adapter = adapter;
        this.adapterParameter = parameter;
    }

    public void notify(int id, Notifier source, Object parameter)
    {
        if(id == PARAM.LOCATION_UPDATED)
        {
            LocationService manager = LocationService.getLocationManager(getActivity());
            getCurrentLocation();
            manager.removeNotifyListeber(this);
        }
    }
}

