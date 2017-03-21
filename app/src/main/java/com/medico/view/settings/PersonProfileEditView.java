package com.medico.view.settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.Person;
import com.medico.model.ProfileId;
import com.medico.model.ServerResponse;
import com.medico.util.GeoUtility;
import com.medico.util.ImageLoadTask;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 8/7/15.
 */
public class PersonProfileEditView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    ProgressDialog progress;
    MenuItem menuItem;
    ImageView profilePic;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView personId;
    EditText name, email, dob, country, city,allergicTo, mobile;
    Spinner mobile_country;
    Spinner gender_spinner;
    ImageButton dob_calendar;
    MultiAutoCompleteTextView specialization,bloodGroup;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    Person personModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.patient_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        personId = (TextView) view.findViewById(R.id.person_id);
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
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
        allergicTo = (EditText)view.findViewById(R.id.allergic_to);
        bloodGroup = (MultiAutoCompleteTextView)view.findViewById(R.id.bloodGroup);
        Bundle bundle = getActivity().getIntent().getExtras();
        switch (bundle.getInt(PROFILE_TYPE))
        {
            case PATIENT:
                textviewTitle.setText("Patient Profile");
                profilePic.setImageResource(R.drawable.patient);
                break;
            case DOCTOR:
                textviewTitle.setText("Doctor Profile");
                profilePic.setImageResource(R.drawable.doctor);
                break;
            case ASSISTANT:
                textviewTitle.setText("Assistant Profile");
                profilePic.setImageResource(R.drawable.assistant);
                break;
//            case DEPENDENT:
//                textviewTitle.setText("Dependent Profile");
//                profilePic.setImageResource(R.drawable.patient);
//                break;
//            case DELEGATE:
//                textviewTitle.setText("Delagated Profile");
//                switch (bundle.getInt(PROFILE_ROLE))
//                {
//                    case PATIENT:
//                        profilePic.setImageResource(R.drawable.patient);
//                        break;
//                    case DOCTOR:
//                        profilePic.setImageResource(R.drawable.doctor);
//                        break;
//                    case ASSISTANT:
//                        profilePic.setImageResource(R.drawable.assistant);
//                        break;
//                }
//
//                break;
        }
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer profileRole = bundle.getInt(PROFILE_ROLE);
        final Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
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
                        bloodGroup.setText(person.getBloodGroup());
                        allergicTo.setText(person.getAllergicTo());
                        new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
                        if (person.getStatus() == UNREGISTERED && person.addedBy != null && person.addedBy.intValue() == loggedinUserId) {
                            menuItem.setEnabled(true);
                        } else
                            menuItem.setEnabled(false);
                    }
                    setEditable(false);
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
        else
        {
            personModel = new Person();
            personModel.setRole(profileRole.byteValue());
            personModel.setStatus(new Integer(2).byteValue());
            personModel.setAddedBy(loggedinUserId);
            personModel.setPrime(new Integer(0).byteValue());
            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, personModel);
            setEditable(true);
            progress.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void save(Person person)
    {
        if(person.getId() != null) {
            api.updateProfile(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
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
        else
        {
            api.createProfile(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
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
        if(personModel.getId() != null) {
            Bundle bundle1 = getActivity().getIntent().getExtras();
            personModel.setAddress(mAutocompleteView.getText().toString());
            personModel.setCity(city.getText().toString());
            personModel.setCountry(country.getText().toString().trim());
            personModel.setBloodGroup(bloodGroup.getText().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setBloodGroup(bloodGroup.getText().toString());
        }
        else
        {
            personModel.setAddress(mAutocompleteView.getText().toString());
            personModel.setCity(city.getText().toString());
            personModel.setCountry(country.getText().toString().trim());
            personModel.setRegion(" ");
            personModel.setBloodGroup(bloodGroup.getText().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
            personModel.setSpeciality(specialization.getText().toString());
            personModel.setAllergicTo(allergicTo.getText().toString());
            personModel.setBloodGroup(bloodGroup.getText().toString());
            personModel.setName(name.getText().toString());
            personModel.setMobile(new Long(mobile.getText().toString()));
            personModel.setEmail(email.getText().toString());

        }
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
        mobile.setEnabled(editable);
        email.setEnabled(editable);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        }
        return true;
    }


}
