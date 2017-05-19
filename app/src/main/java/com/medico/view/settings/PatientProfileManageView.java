package com.medico.view.settings;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.Person;
import com.medico.model.ProfileId;
import com.medico.model.SearchParameter;
import com.medico.model.ServerResponse;
import com.medico.model.Specialization;
import com.medico.util.GeoUtility;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentFragment;

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
    ProgressDialog progress;

    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button, change_password;
    TextView profileId;
    EditText name, email, dob, country, city;
    Spinner gender_spinner;
    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization;//,location;
    AutoCompleteTextView mAutocompleteView;
    CheckBox auto_login;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.person_profile_edit_view,container,false);
        RelativeLayout autologin = (RelativeLayout)view.findViewById(R.id.layout30);
        autologin.setVisibility(View.VISIBLE);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        profileId = (TextView) view.findViewById(R.id.person_id);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
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
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        country = (EditText) view.findViewById(R.id.country_spinner);
        city = (EditText) view.findViewById(R.id.city);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        specialization.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        specialization.setThreshold(1);
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
        profilePicUploadBtn.setOnClickListener(new View.OnClickListener() {
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
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
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
                    if(person.getDateOfBirth() != null )
                        dob.setText(format.format(new Date(person.getDateOfBirth())));
                    mAutocompleteView.setText(person.getAddress());
                    country.setText(person.getCountry());
                    city.setText(person.getCity());
                    specialization.setText(person.getSpeciality());


                    SharedPreferences sharedPref = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
                    boolean status = sharedPref.getBoolean("USER_STATUS", true);
                    auto_login.setChecked(status);
                    new GeoUtility(getActivity(),mAutocompleteView,country,city,location_delete_button, current_location_button, personModel);

                }
                progress.dismiss();


            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void save(Person person)
    {
        api.updateProfile(person, new Callback<ServerResponse>() {
            @Override
            public void success(ServerResponse s, Response response)
            {
                if(s.status == 1)
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
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


    }

}