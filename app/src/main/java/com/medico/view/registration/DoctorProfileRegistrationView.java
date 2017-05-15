package com.medico.view.registration;

import android.app.FragmentManager;
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
import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.Person;
import com.medico.model.PersonDetailProfile;
import com.medico.model.ProfileId;
import com.medico.model.SearchParameter;
import com.medico.model.ServerResponse;
import com.medico.model.Specialization;
import com.medico.util.GeoUtility;
import com.medico.util.ImageLoadTask;
import com.medico.view.home.ParentFragment;
import com.medico.view.settings.FileUploadView;

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
public class DoctorProfileRegistrationView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
//    ProgressDialog progress;
    MenuItem menuItem;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView personId;
    EditText name, email, dob, country, city,registrationNumber, mobile,password;
    Spinner mobile_country,practice;
    Spinner gender_spinner;
    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    CheckBox auto_login;
    Person personModel;
    PersonDetailProfile personDetailProfile;
    FileUploadView fileFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.person_profile_edit_view,container,false);
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
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        dob = (EditText) view.findViewById(R.id.dob);
        TextView dob_text = (TextView)view.findViewById(R.id.dob_text);
        dob.setVisibility(View.GONE);
        dob_text.setVisibility(View.GONE);
        dob_calendar = (ImageButton) view.findViewById(R.id.dob_calendar);
        dob_calendar.setVisibility(View.GONE);
        dob_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(dob);
            }
        });
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
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
        CheckBox tcCheckBox = (CheckBox)view.findViewById(R.id.auto_login);
        Button nextButton = (Button) view.findViewById(R.id.change_password);
        tcCheckBox.setText("Agree with T&C");
        nextButton.setVisibility(View.GONE);
        TextView registration = (TextView)view.findViewById(R.id.allergic_to_text);
        registration.setText("Registration");
        registrationNumber = (EditText)view.findViewById(R.id.allergic_to);
        registrationNumber.setHint("Enter Medical Registration Number");
        practice = (Spinner) view.findViewById(R.id.bloodGroup);
        ArrayAdapter<String> practiceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,getActivity().getResources().getStringArray(R.array.practice_name));
        TextView practiceType = (TextView)view.findViewById(R.id.bloodGroup_text);
        practiceType.setText("Practice");
        practice.setAdapter(practiceAdapter);
        TextView relationText = (TextView) view.findViewById(R.id.relation_text);
        Spinner relation = (Spinner) view.findViewById(R.id.relation);
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.layout_relation);
        relativeLayout.setVisibility(View.GONE);
        relationText.setVisibility(View.GONE);
        relation.setVisibility(View.GONE);
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
                            error.printStackTrace();
                        }
                    });
                }

            }
        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
//        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
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
                public void success(Person person, Response response) { 
                    if (person != null && person.getId() != null) {
                        personModel = person;
                        String url = person.getImageUrl();
                        if(url != null && url.trim().length() > 0)
                            new ImageLoadTask(url, profilePic).execute();
                        personId.setText(person.getId().toString());
                        name.setText(person.getName());
                        mobile.setText(person.getMobile().toString());
                        email.setText(person.getEmail());
                        gender_spinner.setSelection(person.gender.intValue());
                        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                        if (person.getDateOfBirth() != null)
                            dob.setText(format.format(new Date(person.getDateOfBirth())));
                        mAutocompleteView.setText(person.getAddress());
                        country.setText(person.getCountry());
                        city.setText(person.getCity());
                        specialization.setText(person.getSpeciality());
                        new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
                        mobile_country.setSelection(getCountryIndex(person.getLocation()));

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
//                    progress.dismiss();
                }
            });
        }
        else
        {
            personModel = new Person();
            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
            personDetailProfile = new PersonDetailProfile();

        }
        if(fileFragment != null && fileFragment.fileupload != null )
        {
            String url = fileFragment.fileupload.url;
            if(url != null && url.trim().length() > 0)
                new ImageLoadTask(url, profilePic).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void save(final Person person, final PersonDetailProfile detailedProfile)
    {
        showBusy();
        if(person.getId() != null) {
            api.updateProfile(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
                        Toast.makeText(getActivity(), "Profile is Successfully Activated", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), "Could not activate the Profile", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            api.checkMobileEmailAvailability(person, new Callback<ServerResponse>()
            {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 0 && s.errorCode == null )
                    {
                        api.createDoctorProfile(person, new Callback<ServerResponse>() {
                            @Override
                            public void success(ServerResponse s, Response response)
                            {
                                if (s.status == 1 && s.profileId != null)
                                {
                                    Bundle bundle = getActivity().getIntent().getExtras();
                                    bundle.putString("person_name", personModel.getName());
                                    bundle.putInt("gender", personModel.getGender().intValue());
                                    bundle.putInt(PROFILE_ROLE, personModel.getRole().intValue());
                                    bundle.putInt(PROFILE_ID, s.profileId);
                                    getActivity().getIntent().putExtras(bundle);
                                    ParentFragment fragment = new RegistrationSuccessfulView();
                                    FragmentManager manager = getActivity().getFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.add(R.id.service,fragment, RegistrationSuccessfulView.class.getName()).commit();
                                }
                                else
                                {
                                    hideBusy();
                                    Toast.makeText(getActivity(), "Profile Could not be created", Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void failure(RetrofitError error)
                            {
                                hideBusy();
                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                            }
                        });
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
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
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


}
