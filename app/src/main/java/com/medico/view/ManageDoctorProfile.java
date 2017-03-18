package com.medico.view;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.medico.model.Person;
import com.medico.model.ProfileId;
import com.medico.model.ServerResponse;
import com.medico.util.GeoUtility;
import com.medico.util.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

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
public class ManageDoctorProfile extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback{

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
//    LocationService locationService =  LocationService.getLocationManager(getActivity());
    Person personModel;
//    public static int MY_PERMISSION_ACCESS_COARSE_LOCATION = 1;
//    int GPSoff = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_doctor_profile,container,false);
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

//        PlaceDetectionApi places = new PlaceDetectionApi() {
//            @Override
//            public PendingResult<PlaceLikelihoodBuffer> getCurrentPlace(GoogleApiClient googleApiClient, @Nullable PlaceFilter placeFilter) {
//                return null;
//            }
//
//            @Override
//            public PendingResult<Status> reportDeviceAtPlace(GoogleApiClient googleApiClient, PlaceReport placeReport) {
//                return null;
//            }
//        };
//        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
////                Log.i(TAG, String.format("Place '%s' has likelihood: %g",
////                        placeLikelihood.getPlace().getName(),
////                        placeLikelihood.getLikelihood()));
//                  }
//                likelyPlaces.release();}});

        specialization = (MultiAutoCompleteTextView) view.findViewById(R.id.specialization);
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


//
//    public void setDate(){
//
//        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
//
//
//
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            calendar.set(Calendar.YEAR,year);
//            calendar.set(Calendar.MONTH,monthOfYear);
//            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//            updatedate();
//        }
//
//    };
//    public void updatedate()
//    {
//        dob.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH));
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SELECT_PICTURE) {
//            selectedImageUri = data.getData();
//            profilePic.setImageURI(selectedImageUri);
//            path = getPath(selectedImageUri);
//        }else if(requestCode == SELECT_DOCUMENT){
//            selectdDocumentUri = data.getData();
//            documentPath = getPath(selectdDocumentUri);
//            File documentFile = new File(documentPath);
//            document.setText(documentFile.getName());
//        }
//    }
//    public String getPath(Uri uri) {
//
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
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
    protected void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        personModel.role = new Integer(bundle1.getInt(LOGGED_IN_USER_ROLE)).byteValue();
        personModel.setAddress(mAutocompleteView.getText().toString());
        personModel.setCity(city.getText().toString());
        personModel.setCountry(country.getText().toString().trim());
//        String countryName = personModel.getCountry();
//        personModel.setCountry(personModel.isoCountry);
//        personModel.setIsoCountry(countryName);
        personModel.setGender(new Integer(gender_spinner.getSelectedItemPosition()).byteValue());
        personModel.setSpeciality(specialization.getText().toString());
    }
    @Override
    protected boolean save()
    {
        if(personModel.canBeSaved())
        {
            save(personModel);
//            String countryName = personModel.getCountry();
//            personModel.setCountry(personModel.isoCountry);
//            personModel.setIsoCountry(countryName);
            return true;
        }
        return false;
    }
    @Override
    protected boolean canBeSaved()
    {
        return personModel.canBeSaved();
    }
    @Override
    protected void setEditable(boolean editable)
    {


    }



}
