package com.medicohealthcare.view.registration;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.CheckBox;
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
import com.medicohealthcare.model.PersonDetailProfile;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.model.RegistrationVerificationRequest;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.model.ServerResponseStatus;
import com.medicohealthcare.model.Specialization;
import com.medicohealthcare.util.GeoUtility;
import com.medicohealthcare.util.ImageLoadTask;
import com.medicohealthcare.util.LocationService;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.Notifier;
import com.medicohealthcare.util.NotifyListener;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.util.PermissionManager;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.settings.FileUploadView;

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
public class DoctorProfileRegistrationView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback, NotifyListener
{
    public static int CALLBACK_REQUEST = 400;
    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
//    ProgressDialog progress;
    MenuItem menuItem;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView personId;
    EditText name, email, country, city,registrationNumber, mobile,password;
    Spinner mobile_country,practice;
    Spinner gender_spinner;
//    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    CheckBox auto_login;
    Person personModel;
    PersonDetailProfile personDetailProfile;
    FileUploadView fileFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.doctor_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Doctor Registration");
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        auto_login = (CheckBox)view.findViewById(R.id.auto_login);
        profilePic.setBackground(null);
        profilePic.setImageResource(R.drawable.doctor_default);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        profilePicUploadBtn.setVisibility(View.GONE);
        personId = (TextView) view.findViewById(R.id.person_id);
        personId.setVisibility(View.GONE);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText)view.findViewById(R.id.password);
        password.setVisibility(View.VISIBLE);
        view.findViewById(R.id.password_text).setVisibility(View.VISIBLE);
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        mAutocompleteView.setBackground(null);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        TextView country_text = (TextView) view.findViewById(R.id.city_text);
        country = (EditText) view.findViewById(R.id.country);
        TextView city_text = (TextView) view.findViewById(R.id.country_text);
        city = (EditText) view.findViewById(R.id.city);
        country_text.setVisibility(View.GONE);
        country.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        city_text.setVisibility(View.GONE);
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        specialization.setThreshold(1);
        RelativeLayout tc = (RelativeLayout)view.findViewById(R.id.layout30);
        tc.setVisibility(View.VISIBLE);
        CheckBox tcCheckBox = (CheckBox)view.findViewById(R.id.auto_login);
        Button nextButton = (Button) view.findViewById(R.id.change_password);
        tcCheckBox.setText("Agree with T&C");
        nextButton.setVisibility(View.GONE);
        TextView registration = (TextView)view.findViewById(R.id.registration_text);
        registration.setText("Registration");
        registrationNumber = (EditText)view.findViewById(R.id.registration);
        registrationNumber.setHint("Enter Medical Registration Number");
        registration.setVisibility(View.VISIBLE);
        registrationNumber.setVisibility(View.VISIBLE);
        RelativeLayout practice_layout = (RelativeLayout)view.findViewById(R.id.practice_layout);
        practice = (Spinner) view.findViewById(R.id.practice);
        ArrayAdapter<String> practiceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,getActivity().getResources().getStringArray(R.array.practice_name));
        TextView practiceType = (TextView)view.findViewById(R.id.practice_text);
        practice.setAdapter(practiceAdapter);
        practice_layout.setVisibility(View.VISIBLE);
        practice.setVisibility(View.VISIBLE);
        practiceType.setVisibility(View.VISIBLE);
        Bundle bundle = getActivity().getIntent().getExtras();

        profilePic.setImageResource(R.drawable.doctor_default);

        profilePicUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putInt(PROFILE_TYPE, DEPENDENT);
                bundle.putInt(PROFILE_ID,bundle.getInt(LOGGED_IN_ID));
                bundle.putInt(FILE_UPLOAD, PROFILE_PICTURE);
                getActivity().getIntent().putExtras(bundle);
                fileFragment = new FileUploadView();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fileFragment,FileUploadView.class.getName()).addToBackStack(FileUploadView.class.getName()).commit();
            }
        });
        specialization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1).trim();
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSpecialization(new SearchParameter(searchText, 0, 1, 100, 5), new Callback<List<Specialization>>() {
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
        PermissionManager.getInstance(getActivity()).hasPermission(PermissionManager.LOCATION);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        LocationService.getLocationManager(getActivity()).addNotifyListeber(this);
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer profileId = bundle.getInt(PROFILE_ID);
        final Integer profileRole = bundle.getInt(PROFILE_ROLE);
        if(countriesList != null)
        {
            SpinnerAdapter countryListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_layout, countriesList);
            mobile_country.setAdapter(countryListAdapter);
        }
        if(profileId != null && profileId.intValue() > 0 && profileRole != null && profileRole.intValue() >= 0) {
            api.getProfile(new ProfileId(profileId), new Callback<Person>() {
                @Override
                public void success(Person person, Response response) { 
                    if (person != null && person.getId() != null) {
                        personModel = person;
                        String url = person.getImageUrl();
                        if(url != null && url.trim().length() > 0)
                            new ImageLoadTask(url, profilePic).execute();
                        personId.setText(person.getId().toString());
                        personId.setVisibility(View.VISIBLE);
                        name.setText(person.getName());
                        mobile.setText(person.getMobile().toString().substring(person.location.trim().length()));
                        email.setText(person.getEmail());
                        gender_spinner.setSelection(person.gender.intValue());
                        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                        mAutocompleteView.setText(person.getAddress());
                        country.setText(person.getCountry());
                        city.setText(person.getCity());
                        specialization.setText(person.getSpeciality());
                        new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
                        mobile_country.setSelection(getCountryIndex(person.getLocation()));
                        hideBusy();
                    }
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
            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
            personDetailProfile = new PersonDetailProfile();
            hideBusy();
        }
        if(fileFragment != null && fileFragment.fileupload != null )
        {
            String url = fileFragment.fileupload.url;
            if(url != null && url.trim().length() > 0)
                new ImageLoadTask(url, profilePic).execute();
        }
        setTitle("Doctor Registration");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitle("Doctor Registration");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        LocationService.getLocationManager(getActivity()).removeNotifyListeber(this);

    }
    @Override
    public void onStop()
    {
        super.onStop();
        LocationService.getLocationManager(getActivity()).removeNotifyListeber(this);

    }
    private void save(final Person person, final PersonDetailProfile detailedProfile)
    {
        showBusy();
        if(person.getId() != null)
        {
            boolean mobilerequired = personModel.location.equals("91")?true:false;
            sendVerificationCode(true,mobilerequired);
        }
        else
        {
            api.checkMobileEmailAvailability(person, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 0 && s.errorCode == null )
                    {
                        boolean mobilerequired = personModel.location.equals("91")?true:false;
                        sendVerificationCode(true,mobilerequired);
                    }
                    else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Email Id or Mobile Number Already Exisits", Toast.LENGTH_LONG).show();
                    }
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
                .setMaxDate(new Date())
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

            personModel.role = DOCTOR;
            personModel.status = 1;
            personModel.prime = 1;
            personModel.setPassword(password.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.registrationNo = registrationNumber.getText().toString();
            personModel.practiceName = (String)practice.getSelectedItem();
        }
        else
        {
            personModel.role = DOCTOR;
            personModel.status = 1;
            personModel.prime = 1;
            personModel.setPassword(password.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setName(name.getText().toString());
            if(mobile.getText().length() > 0 && mobile_country.getSelectedItem().toString().length() > 0)
                personModel.setMobile(new Long(mobile_country.getSelectedItem().toString().concat(mobile.getText().toString())));
            personModel.setEmail(email.getText().toString());
            personModel.setLocation(mobile_country.getSelectedItem().toString());
            personModel.registrationNo = registrationNumber.getText().toString();
            personModel.practiceName = (String)practice.getSelectedItem();
        }
    }
    @Override
    public boolean save()
    {
        if(canBeSaved())
        {
            save(personModel, personDetailProfile);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        if(auto_login.isChecked()==false)
            return false;
        return personModel.canBeSavedForDoctor();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.save,menu);
        MenuItem menuitem = menu.findItem(R.id.save);
        menuitem.setTitle("NEXT");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save:
            {

                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else
                    {
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

    private int getBloodgroupIndex(String bloodgroup)
    {
        String[] bloodgroups = getActivity().getResources().getStringArray(R.array.bloodgroup_list);
        for(int i = 0; i < bloodgroups.length; i++)
        {
            if(bloodgroups[i].equalsIgnoreCase(bloodgroup))
                return i;
        }
        return 0;
    }
    private void sendVerificationCode(boolean email, boolean mobile)
    {
        if(email && mobile)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(personModel.getEmail(), personModel.getMobile()), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email and Mobile", Toast.LENGTH_LONG);
                            showVerification();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG);
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG);
                            break;
                    }
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
        else if(email)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(personModel.getEmail()), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG);
                            break;
                    }
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
        else if(mobile)
        {
            api.getVerificationCodeForNewRegistration(new RegistrationVerificationRequest(personModel.getMobile()), new Callback<ServerResponseStatus>()
            {
                @Override
                public void success(ServerResponseStatus s, Response response)
                {
                    switch (s.status)
                    {
                        case 0:
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG);
                            break;
                    }
                    hideBusy();
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

    private void showVerification()
    {
        setHasOptionsMenu(false);
        Intent intent = new Intent(getActivity(), ProfileRegistrationVerificationActivity.class);
        intent.putExtra(PARAM.PERSON_EMAIL, personModel.email);
        intent.putExtra(PARAM.PERSON_MOBILE, personModel.mobile);
        intent.putExtra("MOBILE_VERIFICATION_REQUIRED",(personModel.location.equals("91")?true:false));
        startActivityForResult(intent, CALLBACK_REQUEST);
        onPause();
    }
    public void notify(int id, Notifier source, Object parameter)
    {
        if(id == PARAM.LOCATION_UPDATED)
        {
            if(personModel != null)
            {
                LocationService manager = LocationService.getLocationManager(getActivity());
                mAutocompleteView.setText(manager.partialAddress);
                personModel.setAddress(manager.completeAddress);
                personModel.setRegion(manager.region);
                personModel.setLocationLat(manager.latitude);
                personModel.setLocationLong(manager.longitude);
                personModel.setCity(manager.city);
                personModel.setCountry(manager.country);
                personModel.setIsoCountry(manager.countryCode);
                manager.removeNotifyListeber(this);
            }
        }
    }
    public void createProfile(final Person person)
    {
        showBusy();
        if (personModel.getId() != null && personModel.getId().intValue() > 0)
        {
            api.updateProfile(personModel, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response)
                {
                    if (s.status == 1)
                    {
                        setHasOptionsMenu(false);
                        Bundle bundle = getActivity().getIntent().getExtras();
                        bundle.putString("person_name", personModel.getName());
                        bundle.putInt("gender", personModel.getGender().intValue());
                        bundle.putInt(PROFILE_ROLE, personModel.getRole().intValue());
                        bundle.putInt(PROFILE_ID, personModel.getId());
                        getActivity().getIntent().putExtras(bundle);
                        ParentFragment fragment = new RegistrationSuccessfulView();
                        FragmentManager manager = getActivity().getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.service, fragment, RegistrationSuccessfulView.class.getName()).commit();
                    }
                    else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Profile Could not be updated", Toast.LENGTH_LONG).show();
                    }
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }
        else
        {
            api.createDoctorProfile(person, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response)
                {
                    if (s.status == 1)
                    {
                        setHasOptionsMenu(false);
                        Bundle bundle = getActivity().getIntent().getExtras();
                        bundle.putString("person_name", person.getName());
                        bundle.putInt("gender", person.getGender().intValue());
                        bundle.putInt(PROFILE_ROLE, person.getRole().intValue());
                        bundle.putInt(PROFILE_ID, s.profileId);
                        getActivity().getIntent().putExtras(bundle);
                        ParentFragment fragment = new RegistrationSuccessfulView();
                        FragmentManager manager = getActivity().getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.service, fragment, RegistrationSuccessfulView.class.getName()).commit();
                    } else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Profile Could not be created", Toast.LENGTH_LONG).show();
                    }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == CALLBACK_REQUEST && resultCode == 1)
        {
            createProfile(personModel);
        }
    }
}
