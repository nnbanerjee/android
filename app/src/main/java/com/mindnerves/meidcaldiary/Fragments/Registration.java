package com.mindnerves.meidcaldiary.Fragments;

/**
 * Created by User on 16-02-2015.
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import Application.MyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by User on 03-02-2015.
 */
//Patient registration
public class Registration extends WizardStep {

    Calendar calendar = Calendar.getInstance();
    ImageButton cal;
    TextView lable;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    Uri selectedImageUri = null;
    private Button mapButton;
    private ImageView personimage;
    private EditText etNormalText, etEmailAddrss, etPhoneNumber, etpassword, etlocation, postalCodeTv, allergic, city;
    RadioGroup genderBtn;
    private RadioButton radioSexButton;
    private CheckBox agreeCondition;
    Global go;
    String buttonText = "";
    List<String> countryList, regionList;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Spinner spinnerBloodGroup, countrySpinner, regionSpinner;
    ArrayAdapter<String> countryAdapter, regionAdapter;
    MyApi api;
    String[] countries, regions;

    private String name;

    private String email;

    private String password;

    private String mobile;

    private String gender;

    private String dob;

    private String location;

    private Uri uri;

    private String postalCode;

    private String country;

    private String region;

    private String latitude;

    private String longitude;

    private String cityContext;

    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {


        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                System.out.println("I am In nExt Button of registration:::::::::::::");
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }


    }

    @Override
    public void onResume() {
        super.onStop();

        System.out.println("i am in resume:::::::");
        if (buttonText.equals("upload")) {
            agreeCondition.setChecked(false);
        }
        Global go = (Global) getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if (locationString.equals("")) {

            notifyIncomplete();
        } else {
            int position = 0;
            etlocation.setText(locationString);
            if (go.countryName != null && countryAdapter != null) {
                System.out.println("");
                position = countryAdapter.getPosition(go.countryName);
                countrySpinner.setSelection(position);
            }
            System.out.println("user Latitude= " + go.userLatitude);
            System.out.println("user Longitude= " + go.userLongitude);

        }

    }

    private void bindDataFields() {
        name = etNormalText.getText().toString();
        email = etEmailAddrss.getText().toString();
        password = etpassword.getText().toString();
        mobile = etPhoneNumber.getText().toString();
        gender = radioSexButton.getText().toString();
        postalCode = postalCodeTv.getText().toString();
        System.out.println("Bind Password Field::::::::" + password);
        location = etlocation.getText().toString();
        uri = go.getUri();
        country = countrySpinner.getSelectedItem().toString();
        // region = regionSpinner.getSelectedItem().toString();
        cityContext = city.getText().toString();
        latitude = "" + go.userLatitude;
        longitude = "" + go.userLongitude;

    }

    public Registration() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration,
                container, false);
        //getActivity().getActionBar().hide();
        System.out.println("I am in Registration::::::::::::::::");
        cal = (ImageButton) view.findViewById(R.id.imageButton_calendar);
        lable = (TextView) view.findViewById(R.id.textView);
        cal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        go = (Global) getActivity().getApplicationContext();
        go.setLocation("");
        updatedate();
        mapButton = (Button) view.findViewById(R.id.location_button);
        agreeCondition = (CheckBox) view.findViewById(R.id.agreeTC);
        personimage = (ImageView) view.findViewById(R.id.personImage);
        etEmailAddrss = (EditText) view.findViewById(R.id.email);
        etNormalText = (EditText) view.findViewById(R.id.name);
        etPhoneNumber = (EditText) view.findViewById(R.id.mobile);
        etpassword = (EditText) view.findViewById(R.id.password);
        etlocation = (EditText) view.findViewById(R.id.location);
        postalCodeTv = (EditText) view.findViewById(R.id.mobile_post_code);
        allergic = (EditText) view.findViewById(R.id.allergic);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
        // regionSpinner = (Spinner)view.findViewById(R.id.region_spinner);
        personimage.setImageURI(uri);
        city = (EditText) view.findViewById(R.id.city);
        genderBtn = (RadioGroup) view.findViewById(R.id.gender);
        int radioId = genderBtn.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) view.findViewById(radioId);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getAllCountry("temp", new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                countryList = new ArrayList<String>();
                for (String country : strings) {
                    if (country != null) {
                        countryList.add(country);
                    }
                }
                countries = new String[countryList.size()];
                int i = 0;
                for (String country : countryList) {
                    countries[i] = country;
                    i = i + 1;
                }
                countryAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, countries);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySpinner.setAdapter(countryAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
        mapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(), MapActivity.class);
                startActivity(intObj);

            }
        });
        agreeCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Boolean Value: " + isChecked);
                if (isChecked) {
                    showNextButton();
                } else {
                    notifyIncomplete();
                }
            }
        });

        genderBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) view.findViewById(radioId);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodGroup = (Spinner) view.findViewById(R.id.bloodGroup);
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBloodGroup.setAdapter(bloodGroupAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        personimage
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        buttonText = "upload";
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    }
                });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = countrySpinner.getSelectedItem().toString();
                api.getRegions(country, new Callback<List<String>>() {
                    @Override
                    public void success(List<String> strings, Response response) {
                        regionList = new ArrayList<String>();
                        for (String region : strings) {
                            if (region != null) {
                                regionList.add(region);
                            }
                        }
                        regions = new String[regionList.size()];
                        int i = 0;
                        for (String region : regionList) {
                            regions[i] = region;
                            i = i + 1;
                        }
                        regionAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, regions);
                        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        regionSpinner.setAdapter(regionAdapter);
                        if (go.regionName != null) {
                            int pos = 0;
                            pos = regionAdapter.getPosition(go.regionName);
                            regionSpinner.setSelection(pos);
                        }
                        if (go.city != null) {
                            System.out.println("Registration City= " + go.city);
                            city.setText("" + go.city);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etEmailAddrss.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                showNextButtonForTextField();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etNormalText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                showNextButtonForTextField();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                showNextButtonForTextField();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                showNextButtonForTextField();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return view;
    }

    public void showNextButton() {
        int validation = showValidation();
        if (validation == 0) {
            String email = etEmailAddrss.getText().toString();
            Boolean emailBoolean = isValid(email, EMAIL_REGEX);
            String mobile = etPhoneNumber.getText().toString();
            int len = mobile.length();
            if (!emailBoolean) {
                Toast.makeText(getActivity(), "Please Enter Email Proper", Toast.LENGTH_SHORT).show();
            }
            if (!(len == 10)) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number 10 digit", Toast.LENGTH_SHORT).show();
            }
            if (emailBoolean && (len == 10)) {
                notifyCompleted();
            } else {
                notifyIncomplete();
            }
            System.out.println("I am here::::::");
        } else {
            notifyIncomplete();
        }
    }

    public void showNextButtonForTextField() {
        int validation = showValidationNextButton();

        if (validation == 0) {
            notifyCompleted();
        } else {
            notifyIncomplete();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && data != null) {
            selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            System.out.println("Image Path : " + selectedImagePath);
            personimage.setImageURI(selectedImageUri);
            go.setUri(selectedImageUri);


        }

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void updatedate() {
        lable.setText(calendar.get(Calendar.YEAR) + "-" + showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

    }

    public void setDate() {

        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatedate();
        }

    };

    public int showMonth(int month) {
        int showMonth = month;
        switch (showMonth) {
            case 0:
                showMonth = showMonth + 1;
                break;
            case 1:
                showMonth = showMonth + 1;
                break;
            case 2:
                showMonth = showMonth + 1;
                break;
            case 3:
                showMonth = showMonth + 1;
                break;
            case 4:
                showMonth = showMonth + 1;
                break;
            case 5:
                showMonth = showMonth + 1;
                break;
            case 6:
                showMonth = showMonth + 1;
                break;
            case 7:
                showMonth = showMonth + 1;
                break;
            case 8:
                showMonth = showMonth + 1;
                break;
            case 9:
                showMonth = showMonth + 1;
                break;
            case 10:
                showMonth = showMonth + 1;
                break;
            case 11:
                showMonth = showMonth + 1;
                break;
        }
        return showMonth;
    }

    public int showValidation() {
        int validData = 0;
        if (etNormalText.getText().toString().equals("")) {
            validData = 1;
        }
        if (etEmailAddrss.getText().toString().equals("")) {
            validData = 1;
        }
        if (etPhoneNumber.getText().toString().equals("")) {
            validData = 1;
        }
        if (etpassword.getText().toString().equals("")) {
            validData = 1;
        }
        if (etlocation.getText().toString().equals("")) {
            validData = 1;
        }
        return validData;
    }

    public int showValidationNextButton() {
        int validData = 0;
        if (etNormalText.getText().toString().equals("")) {
            validData = 1;
        }
        if (etEmailAddrss.getText().toString().equals("")) {
            validData = 1;
        }
        if (etPhoneNumber.getText().toString().equals("")) {
            validData = 1;
        }
        if (etpassword.getText().toString().equals("")) {
            validData = 1;
        }
        if (etlocation.getText().toString().equals("")) {
            validData = 1;
        }
        if (spinnerBloodGroup.getSelectedItem().toString().equals("Select blood group")) {
            validData = 1;
        }
        if (allergic.getText().equals("")) {
            validData = 1;
        }
        return validData;
    }

    public static boolean isValid(String emailText, String regex) {
        return Pattern.matches(regex, emailText);
    }

}