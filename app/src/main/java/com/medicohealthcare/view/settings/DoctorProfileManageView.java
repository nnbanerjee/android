package com.medicohealthcare.view.settings;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.medicohealthcare.application.R;
import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.Country;
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
public class DoctorProfileManageView extends ParentFragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button, change_password;
    TextView profileId;
    EditText name, email, country, city,mobile_number;
    Spinner gender_spinner,practice,mobileCountry;
    MultiAutoCompleteTextView specialization;//,location;
    AutoCompleteTextView mAutocompleteView;
    CheckBox auto_login;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.doctor_profile_edit_view,container,false);
        RelativeLayout autologin = (RelativeLayout)view.findViewById(R.id.layout30);
        autologin.setVisibility(View.VISIBLE);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        profilePicUploadBtn.setVisibility(View.GONE);
        profileId = (TextView) view.findViewById(R.id.person_id);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        mobileCountry = (Spinner)view.findViewById(R.id.country_code);
        mobile_number = (EditText)view.findViewById(R.id.mobile_number);
        gender_spinner = (Spinner) view.findViewById(R.id.gender_spinner);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        mAutocompleteView.setBackground(null);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        country = (EditText) view.findViewById(R.id.country);
        city = (EditText) view.findViewById(R.id.city);
        setEditable(false);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        specialization.setThreshold(1);
        practice = (Spinner)view.findViewById(R.id.practice);
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
        Bundle bundle = getActivity().getIntent().getExtras();
        showBusy();
        api.getProfile(new ProfileId(bundle.getInt(LOGGED_IN_ID)),new Callback<Person>() {
            @Override
            public void success(Person person, Response response) {
                if(person != null && person.getId() != null)
                {
                    personModel = person;
                    new ImageLoadTask(person.imageUrl,profilePic).execute();
                    profileId.setText(person.getId().toString());
                    name.setText(person.getName());
                    email.setText(person.getEmail());
                    gender_spinner.setSelection(person.gender.intValue());
                    DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
                    mAutocompleteView.setText(person.getAddress());
                    country.setText(person.getCountry());
                    city.setText(person.getCity());
                    mobile_number.setText(person.getMobile().toString());
                    ArrayAdapter<Country> mobCountry = new ArrayAdapter<Country>(getActivity(), android.R.layout.simple_dropdown_item_1line, getSupportedCountries());
                    mobileCountry.setAdapter(mobCountry);
                    mobileCountry.setSelection(getCountryIndex(person.location));
                    specialization.setText(person.getSpeciality());
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
    }

    @Override
    public void onResume() {
        super.onResume();

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
            public void failure(RetrofitError error)
            {
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
        personModel.role = new Integer(bundle1.getInt(LOGGED_IN_USER_ROLE)).byteValue();
        personModel.setAddress(mAutocompleteView.getText().toString());
        personModel.setCity(city.getText().toString());
        personModel.setCountry(country.getText().toString().trim());
        personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
        personModel.setSpeciality(specialization.getText().toString());
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
        name.setEnabled(editable);
        email.setEnabled(editable);
        mobileCountry.setEnabled(editable);
        mobile_number.setEnabled(editable);
    }

}
