package com.medicohealthcare.view.settings;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.medicohealthcare.application.R;
import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.model.Specialization;
import com.medicohealthcare.util.GeoUtility;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 8/7/15.
 */
public class DoctorProfileEditView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    MenuItem menuItem;
    ImageView profilePic;
    Spinner gender_spinner,practice;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView personIdView;
    EditText name, email, country, city,mobile;
    Spinner mobile_country;
    MultiAutoCompleteTextView specialization;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.doctor_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        personIdView = (TextView) view.findViewById(R.id.person_id);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        TextView password_text = (TextView)view.findViewById(R.id.password_text);
        EditText password = (EditText)view.findViewById(R.id.password);
        password_text.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        mAutocompleteView.setBackground(null);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        country = (EditText) view.findViewById(R.id.country);
        city = (EditText) view.findViewById(R.id.city);
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        specialization.setThreshold(1);
        profilePicUploadBtn.setVisibility(View.GONE);
        practice = (Spinner) view.findViewById(R.id.practice);

        ArrayAdapter<String> practiceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,getActivity().getResources().getStringArray(R.array.practice_name));
        TextView practiceType = (TextView)view.findViewById(R.id.practice_text);
        practice.setAdapter(practiceAdapter);
        RelativeLayout practice_layout = (RelativeLayout) view.findViewById(R.id.practice_layout) ;
        practice_layout.setVisibility(View.VISIBLE);
        practice.setVisibility(View.VISIBLE);
        practiceType.setVisibility(View.VISIBLE);
        Bundle bundle = getActivity().getIntent().getExtras();
        int specializationType = DOCTOR_SPECIALIZATION;
        int personId = bundle.getInt(PERSON_ID);
        int profileType = bundle.getInt(PROFILE_TYPE);
        if (personId > 0)
            textviewTitle.setText("Doctor Profile");
        else
            textviewTitle.setText("Create Doctor Profile");
        ((TextView)view.findViewById(R.id.speciality_text)).setText("Specialization");
        final int specializationQueryType = specializationType;
        specialization.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1).trim();
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSpecialization(new SearchParameter(searchText, specializationQueryType, 1, 100, 5), new Callback<List<Specialization>>() {
                        @Override
                        public void success(List<Specialization> specializationList, Response response)
                        {
                            Specialization[] options = new Specialization[specializationList.size()];
                            specializationList.toArray(options);
                            ArrayAdapter<Specialization> diagnosisAdapter = new ArrayAdapter<Specialization>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
                            specialization.setAdapter(diagnosisAdapter);
                            diagnosisAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
//                            error.printStackTrace();
                        }
                    });
                }

            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putInt(PARAM.PROFILE_TYPE, DEPENDENT);
                bundle.putInt(PROFILE_ID,bundle.getInt(LOGGED_IN_ID));
                bundle.putInt(PARAM.FILE_UPLOAD, PROFILE_PICTURE);
                getActivity().getIntent().putExtras(bundle);
                ParentFragment fileFragment = new FileUploadView();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fileFragment,FileUploadView.class.getName()).addToBackStack(FileUploadView.class.getName()).commit();
            }
        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        showBusy();
        final Integer profileId = bundle.getInt(PROFILE_ID);
        final Integer profileRole = bundle.getInt(PROFILE_ROLE);
        final Integer personId = bundle.getInt(PERSON_ID);
        final Integer personRole = bundle.getInt(PERSON_ROLE);
        final SpinnerAdapter countryListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_layout, countriesList);
        mobile_country.setAdapter(countryListAdapter);
        if(personId != null && personId.intValue() > 0 && personRole != null && personRole.intValue() >= 0)
        {
            api.getProfile(new ProfileId(personId), new Callback<Person>() {
                @Override
                public void success(Person person, Response response) { 
                    if (person != null && person.getId() != null)
                    {
                        personModel = person;
                        String url = person.getImageUrl();
                        if(url != null && url.trim().length() > 0)
                            new ImageLoadTask(url, profilePic).execute();
                        personIdView.setText(person.getId().toString());
                        name.setText(person.getName());
                        if(person.getLocation() != null && person.getLocation().length() > 0)
                            mobile.setText(person.getMobile().toString().substring(person.getMobile().toString().
                                    indexOf(person.getLocation())+1));
                        else
                            mobile.setText(person.getMobile().toString());
                        email.setText(person.getEmail());
                        gender_spinner.setSelection(person.gender.intValue());
                        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                        mAutocompleteView.setText(person.getAddress());
                        country.setText(person.getCountry());
                        city.setText(person.getCity());
                        specialization.setText(person.getSpeciality());
                        practice.setSelection(getPracticeIndex(person.practiceName));
                        practice.setEnabled(false);
                        new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
                        if (person.getStatus().intValue() == UNREGISTERED && person.addedBy != null && person.addedBy.intValue() == profileId.intValue())
                        {
                            setEditableAll(true);
                        }
                        else
                        {
                            setEditableAll(false);
                        }
                        mobile_country.setSelection(getCountryIndex(person.getLocation()));

                    }
                    else
                    {

                    }


                    hideBusy();


                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
        else
        {
            personModel = new Person();
            personModel.setRole(new Integer(DOCTOR).byteValue());
            personModel.setStatus(new Integer(2).byteValue());
            personModel.setAddedBy(profileId);
            personModel.setPrime(new Integer(1).byteValue());
            email.setVisibility(View.GONE);
            getView().findViewById(R.id.email_text).setVisibility(View.GONE);

            country.setVisibility(View.GONE);
            getView().findViewById(R.id.country_text).setVisibility(View.GONE);

            city.setVisibility(View.GONE);
            getView().findViewById(R.id.city_text).setVisibility(View.GONE);

            personIdView.setVisibility(View.GONE);

            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
            setEditable(true);
            hideBusy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void save(Person person)
    {
        showBusy();
        if(person.getId() != null)
        {
            api.updateProfile(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status != null && s.status.intValue()== 1)
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
        else
        {
            api.createDoctorProfile(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });

        }
    }

    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                dateField.setText(format.format(date));
                personModel.setDateOfBirth(date.getTime());
                dateField.setText(format.format(date));
            }

        };
        Date date = null;
        if(dateField.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
            try {
                date = format.parse(dateField.getText().toString());
            }
            catch(ParseException e)
            {
                date = new Date();
            }

        }

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .setMode(SlideDateTimePicker.ONLY_CALENDAR)
                .build();
        pickerDialog.show();
    }

    @Override
    public boolean isChanged()
    {
        return personModel.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        if(personModel.getId() != null)
        {

            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setName(name.getText().toString());
            if(mobile.getText().length() > 0 && mobile_country.getSelectedItem().toString().length() > 0)
                personModel.setMobile(new Long(mobile_country.getSelectedItem().toString().concat(mobile.getText().toString())));
            personModel.setEmail(email.getText().toString());
            personModel.setLocation(mobile_country.getSelectedItem().toString());
        }
        else
        {
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setName(name.getText().toString());
            if(mobile.getText().length() > 0 && mobile_country.getSelectedItem().toString().length() > 0)
                personModel.setMobile(new Long(mobile_country.getSelectedItem().toString().concat(mobile.getText().toString())));
            personModel.setLocation(mobile_country.getSelectedItem().toString());
            personModel.practiceName = practice.getSelectedItem().toString();
        }
    }
    @Override
    public boolean save()
    {
        if(personModel.canBeSavedForUnregisteredPatient())
        {
            save(personModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return personModel.canBeSavedForUnregisteredPatient();
    }
    @Override
    public void setEditable(boolean editable)
    {
        name.setEnabled(editable);
        mobile.setEnabled(editable);
        mobile_country.setEnabled(editable);
        email.setEnabled(editable);
    }
    public void setEditableAll(boolean editable)
    {
        mobile.setEnabled(editable);
        email.setEnabled(editable);
        if(menuItem != null)
            menuItem.setEnabled(editable);
        profilePicUploadBtn.setEnabled(editable);
        location_delete_button.setEnabled(editable);
        current_location_button.setEnabled(editable);
        name.setEnabled(editable);
        mobile_country.setEnabled(editable);
        gender_spinner.setEnabled(editable);
        specialization.setEnabled(editable);
        mAutocompleteView.setEnabled(editable);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.save,menu);
        menuItem = menu.findItem(R.id.save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save: {
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
            return true;
        }
        return false;
    }

    private int getPracticeIndex(String practice)
    {
        String[] bloodgroups = getActivity().getResources().getStringArray(R.array.practice_name);
        for(int i = 0; i < bloodgroups.length; i++)
        {
            if(bloodgroups[i].equalsIgnoreCase(practice))
                return i;
        }
        return 0;
    }

}
