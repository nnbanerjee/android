package com.mindnerves.meidcaldiary.Fragments;

/**
 * Created by User on 16-02-2015.
 */

import android.app.DatePickerDialog;
import android.app.Fragment;
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
public class RegistrationAssistant extends WizardStep {
    Calendar calendar = Calendar.getInstance();
    ImageButton cal;
    TextView lable;
    Global go;
    String buttonText = "";
    Spinner countrySpinner,regionSpinner,professionSpinner,specialitySpinner;
    ArrayAdapter<String> countryAdapter,regionAdapter,professionAdapter,specialityAdapter;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    Uri selectedImageUri = null;
    private Button mapButton;
    RadioGroup genderBtn;
    private RadioButton radioSexButton;
    private ImageView personimage;
    private EditText etNormalText,etEmailAddrss,etPhoneNumber,etpassword,etlocation,postalCodeTv,city;
    private CheckBox agreeCondition;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Spinner spinner1;
    List<String> countryList,regionList;
    String[] countries,regions;
    @ContextVariable
    private String name;
    @ContextVariable
    private String email;
    @ContextVariable
    private String password;
    @ContextVariable
    private String mobile;
    @ContextVariable
    private String gender;
    @ContextVariable
    private String dob;
    @ContextVariable
    private String location;
    @ContextVariable
    private String bloodGroup;
    @ContextVariable
    private Uri uri;
    @ContextVariable
    private String postalCode;
    @ContextVariable
    private String country;

    @ContextVariable
    private String latitude;
    @ContextVariable
    private String longitude;
    @ContextVariable
    private String cityContext;
    @ContextVariable
    private String speciality;
    @ContextVariable
    private String profession;
    MyApi api;

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Global go = (Global)getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if(buttonText.equals("upload"))
        {
            agreeCondition.setChecked(false);
        }
        if(locationString.equals("")) {

            notifyIncomplete();
        }
        else
        {
            int position = 0;
            etlocation.setText(locationString);
            if(go.countryName != null){
                position = countryAdapter.getPosition(go.countryName);
                countrySpinner.setSelection(position);
            }
            System.out.println("user Latitude= "+go.userLatitude);
            System.out.println("user Longitude= "+go.userLongitude);
        }
    }

    private void bindDataFields() {

        name = etNormalText.getText().toString();
        email = etEmailAddrss.getText().toString();
        password = etpassword.getText().toString();
        mobile = etPhoneNumber.getText().toString();
        postalCode = postalCodeTv.getText().toString();
        gender = radioSexButton.getText().toString();
        location = etlocation.getText().toString();
        bloodGroup = spinner1.getSelectedItem().toString();
        uri=selectedImageUri;
        country = countrySpinner.getSelectedItem().toString();
        //region = regionSpinner.getSelectedItem().toString();
        cityContext = city.getText().toString();
        latitude = ""+go.userLatitude;
        longitude = ""+go.userLongitude;
        speciality = specialitySpinner.getSelectedItem().toString();
        profession = professionSpinner.getSelectedItem().toString();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.registration_assistant,
                container, false);
        getActivity().getActionBar().hide();
        cal = (ImageButton) view.findViewById(R.id.imageButton_calendar);

        lable = (TextView)view.findViewById(R.id.textView);
        //cal.setOnClickListener(this);
        cal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();
            }
        });


        updatedate();
        agreeCondition = (CheckBox)view.findViewById(R.id.agreeTC);
        personimage = (ImageView) view.findViewById(R.id.personImage);
        etEmailAddrss = (EditText) view.findViewById(R.id.email);
        etNormalText = (EditText) view.findViewById(R.id.name);
        etPhoneNumber = (EditText) view.findViewById(R.id.mobile);
        etpassword = (EditText) view.findViewById(R.id.password);
        etlocation = (EditText) view.findViewById(R.id.location);
        spinner1 = (Spinner) view.findViewById(R.id.bloodGroup);
        postalCodeTv = (EditText) view.findViewById(R.id.postal_code);
        mapButton = (Button)view.findViewById(R.id.location_button);
        countrySpinner = (Spinner)view.findViewById(R.id.country_spinner);
       // regionSpinner = (Spinner)view.findViewById(R.id.region_spinner);
        professionSpinner = (Spinner)view.findViewById(R.id.profession_spinner);
        specialitySpinner = (Spinner)view.findViewById(R.id.speciality_spinner);
        city = (EditText)view.findViewById(R.id.city);
        mapButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(),MapActivity.class);
                startActivity(intObj);


            }
        });
        go = (Global)getActivity().getApplicationContext();
        go.setLocation("");
        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapterBlood);
        ArrayAdapter<CharSequence> professionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.assistan_profession, android.R.layout.simple_spinner_item);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        professionSpinner.setAdapter(professionAdapter);
        ArrayAdapter<CharSequence> specialityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.speciality_list, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        specialitySpinner.setAdapter(specialityAdapter);
        genderBtn = (RadioGroup) view.findViewById(R.id.gender);
        int radioId = genderBtn.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) view.findViewById(radioId);
        genderBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) view.findViewById(radioId);
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        api.getAllCountry("temp",new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                countryList = new ArrayList<String>();
                for(String country : strings){
                    if(country != null){
                        countryList.add(country);
                    }
                }
                countries = new String[countryList.size()];
                int i =0;
                for(String country : countryList){
                    countries[i] = country;
                    i = i+1;
                }
                countryAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, countries);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySpinner.setAdapter(countryAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = countrySpinner.getSelectedItem().toString();



                api.getRegions(country,new Callback<List<String>>() {
                    @Override
                    public void success(List<String> strings, Response response) {
                        regionList = new ArrayList<String>();
                        for(String region : strings){
                            if(region != null){
                                regionList.add(region);
                            }
                        }
                        regions = new String[regionList.size()];
                        int i =0;
                        for(String region : regionList){
                            regions[i] = region;
                            i = i+1;
                        }
                        regionAdapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, regions);
                        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        regionSpinner.setAdapter(regionAdapter);
                        if(go.regionName != null){
                            int pos = 0;
                            pos = regionAdapter.getPosition(go.regionName);
                            regionSpinner.setSelection(pos);
                        }
                        if(go.city != null){
                            System.out.println("Registration City= "+go.city);
                            city.setText(""+go.city);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        personimage.setImageURI(uri);
        view.findViewById(R.id.upload)
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        buttonText = "upload";
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    }
                });

        agreeCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                System.out.println("Boolean Value: "+isChecked);
                System.out.println("Boolean Value: "+isChecked);
                if (isChecked) {
                    showNextButton();
                }
                else {
                    //Notify that the step is incomplete
                    notifyIncomplete();
                }
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


        etEmailAddrss.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
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


        etlocation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && data!=null) {
            selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            System.out.println("Image Path : " + selectedImagePath);
            personimage.setImageURI(selectedImageUri);
        }

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }





    public void updatedate()
    {
        lable.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));

    }
    public void setDate(){

        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){




        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updatedate();
        }

    };
    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(etNormalText)) ret = false;
        if (!Validation.isEmailAddress(etEmailAddrss, true)) ret = false;
        if (!Validation.isPhoneNumber(etPhoneNumber, false)) ret = false;
        if (!Validation.hasText(etpassword)) ret = false;

        return ret;
    }

    public int showMonth(int month)
    {
        int showMonth = month;
        switch(showMonth)
        {
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
    public void showNextButton()
    {
        int validation = showValidation();

        if(validation == 0) {
            String email = etEmailAddrss.getText().toString();
            Boolean emailBoolean = isValid(email,EMAIL_REGEX);
            String mobile = etPhoneNumber.getText().toString();
            int len = mobile.length();
            if(!emailBoolean)
            {
                Toast.makeText(getActivity(), "Please Enter Email Proper", Toast.LENGTH_SHORT).show();
            }
            if(!(len == 10))
            {
                Toast.makeText(getActivity(),"Please Enter Mobile Number 10 digit",Toast.LENGTH_SHORT).show();
            }
            if(emailBoolean && (len == 10))
            {
                notifyCompleted();
            }
            else
            {
                notifyIncomplete();
            }


            System.out.println("I am here::::::");
        }
        else
        {
            notifyIncomplete();
        }

    }
    public void showNextButtonForTextField()
    {
        int validation = showValidationNextButton();

        if(validation == 0)
        {
            notifyCompleted();
        }
        else
        {
            notifyIncomplete();
        }
    }

    public int showValidationNextButton()
    {
        int validData = 0;
        if(etNormalText.getText().toString().equals(""))
        {

            validData = 1;
        }
        if(etEmailAddrss .getText().toString().equals(""))
        {

            validData = 1;
        }
        if(etPhoneNumber.getText().toString().equals(""))
        {

            validData = 1;
        }
        if(etpassword.getText().toString().equals(""))
        {

            validData = 1;
        }
        if(etlocation.getText().toString().equals(""))
        {

            validData = 1;
        }

        return validData;
    }


    public int showValidation()
    {
        int validData = 0;
        if(etNormalText.getText().toString().equals(""))
        {
            validData = 1;
        }
        if(etEmailAddrss .getText().toString().equals(""))
        {
            validData = 1;
        }
        if(etPhoneNumber.getText().toString().equals(""))
        {
            validData = 1;
        }
        if(etpassword.getText().toString().equals(""))
        {
            validData = 1;
        }
        if(etlocation.getText().toString().equals(""))
        {
            validData = 1;
        }

        return validData;
    }
    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        }
        return true;
    }


}

