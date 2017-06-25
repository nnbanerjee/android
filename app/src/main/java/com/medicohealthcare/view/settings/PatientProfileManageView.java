package com.medicohealthcare.view.settings;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
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
public class PatientProfileManageView extends ParentFragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button, change_password;
    TextView profileId;
    EditText name, email, dob, country, city,mobile_number,allergic;
    Spinner gender_spinner,mobileCountry,bloodgroup;;
    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization;//,location;
    AutoCompleteTextView mAutocompleteView;
    CheckBox auto_login;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.person_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        RelativeLayout autologin = (RelativeLayout)view.findViewById(R.id.layout30);
        autologin.setVisibility(View.VISIBLE);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePic.setBackground(null);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        profileId = (TextView) view.findViewById(R.id.person_id);
        name = (EditText) view.findViewById(R.id.name);
        name.setEnabled(false);
        email = (EditText) view.findViewById(R.id.email);
        mobile_number = (EditText)view.findViewById(R.id.mobile_number);
        email.setEnabled(false);
        mobile_number.setEnabled(false);
        mobileCountry = (Spinner)view.findViewById(R.id.country_code);
        bloodgroup = (Spinner) view.findViewById(R.id.bloodGroup);
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
        country = (EditText) view.findViewById(R.id.country_spinner);
        city = (EditText) view.findViewById(R.id.city);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        specialization.setThreshold(1);
        allergic = (EditText)view.findViewById(R.id.allergic_to);
        view.findViewById(R.id.password).setVisibility(View.GONE);
        view.findViewById(R.id.password_text).setVisibility(View.GONE);
        view.findViewById(R.id.layout_relation).setVisibility(View.GONE);
        view.findViewById(R.id.relation_text).setVisibility(View.GONE);
        auto_login = (CheckBox) view.findViewById(R.id.auto_login);
        auto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("USER_STATUS",auto_login.isChecked()).commit();

            }
        });
        change_password = (Button) view.findViewById(R.id.change_password);
        change_password.setBackground(null);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setHasOptionsMenu(false);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putInt(PARAM.PROFILE_TYPE, DEPENDENT);
                bundle.putInt(PROFILE_ID,bundle.getInt(LOGGED_IN_ID));
                bundle.putInt(PARAM.FILE_UPLOAD, PROFILE_PICTURE);
                bundle.putString(PARAM.PROFILE_NAME, personModel.getName());
                getActivity().getIntent().putExtras(bundle);
                ParentFragment fileFragment = new ProfilePictureSelectionView();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.service, fileFragment,ProfilePictureSelectionView.class.getName()).addToBackStack(ProfilePictureSelectionView.class.getName()).commit();
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
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        View viewActionBar = getActivity().getLayoutInflater().inflate(R.layout.toolbar, null);
        Bundle bundle = getActivity().getIntent().getExtras();
        setProfile(bundle.getInt(LOGGED_IN_USER_ROLE));
        showBusy();
        api.getProfile(new ProfileId(bundle.getInt(LOGGED_IN_ID)),new Callback<Person>() {
            @Override
            public void success(Person person, Response response) {
                if(person != null && person.getId() != null)
                {
                    personModel = person;
                    if(personModel.getImageUrl() != null && personModel.getImageUrl().trim().length() > 0)
                    new ImageLoadTask(person.imageUrl,profilePic).execute();
                    profileId.setText(person.getId().toString());
                    name.setText(person.getName());
                    mobile_number.setText(person.getMobile().toString().substring(person.location.trim().length()));
                    if(countriesList != null)
                    {
                        SpinnerAdapter countryListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_layout, countriesList);
                        mobileCountry.setAdapter(countryListAdapter);
                        mobileCountry.setEnabled(false);
                        mobileCountry.setSelection(getCountryIndex(person.getLocation()));
                    }
                    email.setText(person.getEmail());
                    gender_spinner.setSelection(person.gender.intValue());
                    DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                    if(person.getDateOfBirth() != null )
                        dob.setText(format.format(new Date(person.getDateOfBirth())));
                    mAutocompleteView.setText(person.getAddress());
                    country.setText(person.getCountry());
                    city.setText(person.getCity());
                    specialization.setText(person.getSpeciality());
                    if(person.getBloodGroup()!=null)
                        bloodgroup.setSelection(getBloodgroupIndex(person.getBloodGroup()));
                    specialization.setText(person.speciality);
                    allergic.setText(person.getAllergicTo());
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
                    boolean status = sharedPref.getBoolean("USER_STATUS", true);
                    auto_login.setChecked(status);
                    new GeoUtility(getActivity(),mAutocompleteView,country,city,location_delete_button, current_location_button, personModel);

                }
                hideBusy();


            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setHasOptionsMenu(true);
        View viewActionBar = getActivity().getLayoutInflater().inflate(R.layout.toolbar, null);
        Bundle bundle = getActivity().getIntent().getExtras();
        setProfile(bundle.getInt(LOGGED_IN_USER_ROLE));
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }

    private void save(Person person)
    {
        showBusy();
        api.updateProfile(person, new Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse s, Response response)
            {
                if(s.status == 1)
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
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
        Bundle bundle1 = getActivity().getIntent().getExtras();
        personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
        personModel.setSpeciality(specialization.getText().toString());
        personModel.setBloodGroup(bloodgroup.getSelectedItem().toString());
        personModel.setAllergicTo(allergic.getText().toString());
    }
    @Override
    public boolean save()
    {
        if(personModel.canBeSaved())
        {
            save(personModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return personModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {


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
    public int getCountryIndex(String isdCode)
    {
        if(countriesList != null && !countriesList.isEmpty() && isdCode != null && !isdCode.isEmpty())
        {
            for (int i = 0; i < countriesList.size(); i++)
            {
                if (countriesList.get(i).getIsdCode().equals(isdCode))
                    return i;
            }
        }
        return 0;
    }

    public void setProfile(int role)
    {
        switch(role)
        {
            case PATIENT:
                setTitle("Patient Profile");
                profilePic.setImageResource(R.drawable.patient_default);
                break;
            case ASSISTANT:
                setTitle("Assistant Profile");
                profilePic.setImageResource(R.drawable.assistant_default);
                getView().findViewById(R.id.bloodGroup_text).setVisibility(View.GONE);
                getView().findViewById(R.id.allergic_to_text).setVisibility(View.GONE);
                getView().findViewById(R.id.allergic_to).setVisibility(View.GONE);
                getView().findViewById(R.id.layout_bloodgroup).setVisibility(View.GONE);
                break;
        }

    }
}
