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
import android.widget.ImageButton;
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
public class PersonProfileRegistrationView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback, NotifyListener
{

    public static int CALLBACK_REQUEST = 400;
    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    MenuItem menuItem;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView personId;
    EditText name, email, dob, country, city,allergicTo, mobile,password;
    Spinner mobile_country,bloodGroup;
    Spinner gender_spinner;
    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;
    FileUploadView fileFragment;
    CheckBox tcCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.person_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
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
        dob = (EditText) view.findViewById(R.id.dob);
        dob_calendar = (ImageButton) view.findViewById(R.id.dob_calendar);
        dob_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(dob);
            }
        });
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        mAutocompleteView.setBackground(null);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        TextView country_text = (TextView) view.findViewById(R.id.city_text);
        country = (EditText) view.findViewById(R.id.country_spinner);
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
        tcCheckBox = (CheckBox)view.findViewById(R.id.auto_login);
        Button nextButton = (Button) view.findViewById(R.id.change_password);
        tcCheckBox.setText("Agree with T&C");
        nextButton.setVisibility(View.GONE);
        allergicTo = (EditText)view.findViewById(R.id.allergic_to);
        bloodGroup = (Spinner) view.findViewById(R.id.bloodGroup);
        TextView relationText = (TextView) view.findViewById(R.id.relation_text);
        Spinner relation = (Spinner) view.findViewById(R.id.relation);
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.layout_relation);
        relativeLayout.setVisibility(View.GONE);
        relationText.setVisibility(View.GONE);
        relation.setVisibility(View.GONE);

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
                    api.searchAutoFillSpecialization(new SearchParameter(searchText, 11, 1, 100, 5), new Callback<List<Specialization>>() {
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
        showBusy();
        final Integer profileId = bundle.getInt(PROFILE_ID);
        final Integer profileRole = bundle.getInt(PROFILE_ROLE);
//        final Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        if(countriesList != null)
        {
            SpinnerAdapter countryListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_layout, countriesList);
            mobile_country.setAdapter(countryListAdapter);
        }
        if(profileId != null && profileId.intValue() > 0 && profileRole != null && profileRole.intValue() >= 0) {
            api.getProfile(new ProfileId(profileId), new Callback<Person>() {
                @Override
                public void success(Person person, Response response)
                {
                    if(person.errorCode == -1 )
                    {
                        if (person != null && person.getId() != null && person.getStatus().intValue() == 2)
                        {
                            personModel = person;
                            String url = person.getImageUrl();
                            if (url != null && url.trim().length() > 0)
                                new ImageLoadTask(url, profilePic).execute();
                            personId.setText(person.getId().toString());
                            personId.setVisibility(View.VISIBLE);
                            name.setText(person.getName());
                            mobile.setText(person.getMobile().toString().substring(person.location.trim().length()));
                            email.setText(person.getEmail());
                            gender_spinner.setSelection(person.gender.intValue());
                            DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                            if (person.getDateOfBirth() != null)
                                dob.setText(format.format(new Date(person.getDateOfBirth())));
                            mAutocompleteView.setText(person.getAddress());
                            country.setText(person.getCountry());
                            city.setText(person.getCity());
                            specialization.setText(person.getSpeciality());
                            bloodGroup.setSelection(getBloodgroupIndex(person.getBloodGroup()));
                            allergicTo.setText(person.getAllergicTo());
                            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
                            mobile_country.setSelection(getCountryIndex(person.getLocation()));

                        }
                        else
                            Toast.makeText(getActivity(), "Profile already registered", Toast.LENGTH_LONG).show();
                    }
                    else if(person.errorCode == 201)
                        Toast.makeText(getActivity(), "Profile does not exist", Toast.LENGTH_LONG).show();
                    setEditable(false);
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
        else if(personModel == null)
        {
            personModel = new Person();
            personModel.setRole(profileRole.byteValue());
            personModel.setStatus(new Integer(0).byteValue());
            personModel.setAddedBy(profileId);
            personModel.setPrime(new Integer(0).byteValue());
            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
            setEditable(true);
            hideBusy();
        }
        else
            hideBusy();
        if(fileFragment != null && fileFragment.fileupload != null )
        {
            String url = fileFragment.fileupload.url;
            if(url != null && url.trim().length() > 0)
                new ImageLoadTask(url, profilePic).execute();
        }
        setHasOptionsMenu(true);
        setProfile();

    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
        setProfile();
    }
    @Override
    public void onPause() {
        super.onPause();
        LocationService.getLocationManager(getActivity()).removeNotifyListeber(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        LocationService.getLocationManager(getActivity()).removeNotifyListeber(this);
    }

    private void save(final Person person)
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
                public void success(ServerResponse s, Response response)
                {
                    if (s.status == 0 && s.errorCode == null )
                    {

                        boolean mobilerequired = personModel.location.equals("91")?true:false;
                        sendVerificationCode(true,mobilerequired);
                    }
                    else
                    {
                        hideBusy();
                        Toast.makeText(getActivity(), "Email or Mobile number already exisit", Toast.LENGTH_LONG).show();
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

            personModel.role = new Integer(bundle.getInt(PROFILE_ROLE)).byteValue();
            personModel.status = 0;
            personModel.prime = 1;
            personModel.setPassword(password.getText().toString());
            personModel.setBloodGroup(bloodGroup.getSelectedItem().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
        }
        else
        {
            personModel.setBloodGroup(bloodGroup.getSelectedItem().toString());
            personModel.role = new Integer(bundle.getInt(PROFILE_ROLE)).byteValue();
            personModel.status = 0;
            personModel.prime = 1;
            personModel.setPassword(password.getText().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setBloodGroup(bloodGroup.getSelectedItem().toString());
            personModel.setName(name.getText().toString());
            if(mobile.getText().length() > 0 && mobile_country.getSelectedItem().toString().length() > 0)
                personModel.setMobile(new Long(mobile_country.getSelectedItem().toString().concat(mobile.getText().toString())));
            personModel.setEmail(email.getText().toString());
            personModel.setLocation(mobile_country.getSelectedItem().toString());

        }
    }
    @Override
    public boolean save()
    {
        if(canBeSaved())
        {
            save(personModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        if(tcCheckBox.isChecked()==false)
            return false;
        switch (bundle.getInt(PROFILE_ROLE))
        {
            case PATIENT:
                return personModel.canBeSaved();
            case ASSISTANT:
                return personModel.canBeSavedForAssistantRegistration();
        }
        return personModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {

    }
    public void setEditableAll(boolean editable)
    {

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
                if (isChanged())
                {
                    if (canBeSaved())
                    {
                        save();
                    }
                    else
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
    public void notify(int id, Notifier source, Object parameter)
    {
        if(id == PARAM.LOCATION_UPDATED)
        {
            LocationService manager = LocationService.getLocationManager(getActivity());
            if(personModel != null)
            {
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


    public void createProfile()
    {
        showBusy();
        if(personModel.getId() != null && personModel.getId().intValue() > 0)
        {
            api.updateProfile(personModel, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
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
                        Toast.makeText(getActivity(), "Profile Could not be updated", Toast.LENGTH_LONG).show();
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
            api.createProfile(personModel, new Callback<ServerResponse>()
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
                        bundle.putInt(PROFILE_ID, s.profileId);
                        getActivity().getIntent().putExtras(bundle);
                        ParentFragment fragment = new RegistrationSuccessfulView();
                        FragmentManager manager = getActivity().getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.service, fragment, RegistrationSuccessfulView.class.getName()).commit();
                    } else
                    {

                        Toast.makeText(getActivity(), "Profile Could not be created", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email and Mobile", Toast.LENGTH_LONG).show();
                            showVerification();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG).show();
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Email", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(), "Verification Code could not be sent, try later", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            Toast.makeText(getActivity(), "Verification Code has been sent successfully to your Mobile", Toast.LENGTH_LONG).show();
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
        hideBusy();
        onPause();
    }

    private void setProfile()
    {
        profilePic.setBackground(null);
        Bundle bundle = getActivity().getIntent().getExtras();
        switch (bundle.getInt(PROFILE_ROLE)) {
            case PATIENT:
                setTitle("Patient Registration");
                profilePic.setImageResource(R.drawable.patient_default);
                break;
            case ASSISTANT:
                setTitle("Assistant Registration");
                profilePic.setImageResource(R.drawable.assistant_default);

                getView().findViewById(R.id.layout_bloodgroup).setVisibility(View.GONE);
                bloodGroup.setVisibility(View.GONE);
                getView().findViewById(R.id.bloodGroup_text).setVisibility(View.GONE);
                allergicTo.setVisibility(View.GONE);
                getView().findViewById(R.id.allergic_to_text).setVisibility(View.GONE);
                break;
            case DOCTOR:
                setTitle("Doctor Registration");
                profilePic.setImageResource(R.drawable.doctor_default);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Check which request we're responding to
        if (requestCode == CALLBACK_REQUEST && resultCode == 1)
        {
            createProfile();
        }
    }

}
