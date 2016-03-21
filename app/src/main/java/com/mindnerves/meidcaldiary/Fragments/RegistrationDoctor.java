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

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import Application.AppController;
import Application.MyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by User on 03-02-2015.
 */
public class RegistrationDoctor extends WizardStep {

    private DateFormat formate = DateFormat.getDateInstance();
    private Calendar calendar = Calendar.getInstance();
    private ImageButton cal;
    private TextView lable;
    private Button locationButton;
    private String buttonText = "";
    private String selectedImagePath;
    Uri selectedImageUri = null;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private ImageView personimage;
    private EditText etNormalText,etEmailAddrss,etPhoneNumber,etpassword,etlocation,postalCodeTv,city;
    RadioGroup genderBtn;
    private RadioButton radioSexButton;
    private CheckBox agreeCondition;
    Global go;
    List<String> countryList;
            //regionList;
    Spinner countrySpinner;
            //,regionSpinner;
    ArrayAdapter<String> countryAdapter,regionAdapter;
    MyApi api;
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
    private Uri uri;
    @ContextVariable
    private String postalCode;
    @ContextVariable
    private String country;
    @ContextVariable
    private String region;
    @ContextVariable
    private double latitude;
    @ContextVariable
    private double longitude;
    @ContextVariable
    private String cityContext;

    @ContextVariable
    private long dobInMiliSeconds;

    public RadioButton local_save;
    public RadioButton local_personal;
    public RadioButton local_encrypted;
    Button uploadDocument;
    Uri selectedDocumentUri;
    String selectedDocumentPath = "";
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_DOCUMENT = 2;
    TextView documentPath;
    @ContextVariable
    private String id;
    @ContextVariable
    private String passwordCloud;
    @ContextVariable
    private Uri documentUri;
    private Spinner practiceName;
    /**
     * Called whenever the wizard proceeds to the next step or goes back to the previous step
     */

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                //Do nothing...
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(buttonText.equals("upload"))
        {
            agreeCondition.setChecked(false);
        }
        Global go = (Global)getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if(locationString.equals("")) {

            notifyIncomplete();
        }
        else
        {   int position = 0;
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
        gender = radioSexButton.getText().toString();
        postalCode = postalCodeTv.getText().toString();
        location = etlocation.getText().toString();
        uri = go.getUri();
        country = countrySpinner.getSelectedItem().toString();
        //region = regionSpinner.getSelectedItem().toString();
        cityContext = city.getText().toString();
        latitude = go.userLatitude;
        longitude = go.userLongitude;
        dobInMiliSeconds=calendar.getTimeInMillis();

    }

    public RegistrationDoctor(){


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_doctor,
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

        go = (Global)getActivity().getApplicationContext();
        go.setLocation("");
        updatedate();
        agreeCondition = (CheckBox)view.findViewById(R.id.agreeTC);
        personimage = (ImageView) view.findViewById(R.id.personImage);
        etEmailAddrss = (EditText) view.findViewById(R.id.email);
        etNormalText = (EditText) view.findViewById(R.id.name);
        etPhoneNumber = (EditText) view.findViewById(R.id.mobile);
        etpassword = (EditText) view.findViewById(R.id.password);
        etlocation = (EditText) view.findViewById(R.id.location);
        postalCodeTv = (EditText) view.findViewById(R.id.mobile_post_code);
        genderBtn = (RadioGroup) view.findViewById(R.id.gender);
        int radioId = genderBtn.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) view.findViewById(radioId);
        locationButton = (Button)view.findViewById(R.id.location_button);
        countrySpinner = (Spinner)view.findViewById(R.id.country_spinner);

       // Spinner spinner = (Spinner) view.findViewById(R.id.bloodGroup);
        uploadDocument = (Button)view.findViewById(R.id.upload_document);
        local_save = (RadioButton) view.findViewById(R.id.saveLocal);
        local_personal = (RadioButton) view.findViewById(R.id.personalCloud);
        local_encrypted = (RadioButton) view.findViewById(R.id.encrypted);
        documentPath = (TextView)view.findViewById(R.id.document_name);
        practiceName = (Spinner)view.findViewById(R.id.practice_name);
        ArrayAdapter<CharSequence> practiceNameAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.practice_name, android.R.layout.simple_spinner_item);
        practiceNameAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        practiceName.setAdapter(practiceNameAdapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
       // spinner.setAdapter(adapter);
        Spinner spinnerspl = (Spinner) view.findViewById(R.id.speciality);
        ArrayAdapter<CharSequence> adapterspl = ArrayAdapter.createFromResource(getActivity(),
                R.array.speciality_list, android.R.layout.simple_spinner_item);
        adapterspl.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerspl.setAdapter(adapterspl);
        final Spinner spinner1 = (Spinner) view.findViewById(R.id.cloudSelect);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.drive_list, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spinner1.setAdapter(adapter1);
        /*local_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.INVISIBLE);
                id.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
            }
        });
        local_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
            }
        });
        local_encrypted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.INVISIBLE);
                id.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
            }
        });*/
        uploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_DOCUMENT);
            }
        });
       // regionSpinner = (Spinner)view.findViewById(R.id.region_spinner);
        city = (EditText)view.findViewById(R.id.city);
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
    if(go.getAllCountriesList()==null || go.getAllCountriesList().size()<=0 ) {
        api.getAllCountry("temp",new Callback<List<String>>() {
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
                go.setAllCountriesList(Arrays.asList(countries));

                countryAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, countries);
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                countrySpinner.setAdapter(countryAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });
    }else
    {
        countryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, go.getAllCountriesList());
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);
    }

        locationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(),MapActivity.class);
                startActivity(intObj);


           }
        });
        personimage.setImageURI(uri);
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


        etPhoneNumber.addTextChangedListener(new TextWatcher() {
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


        etlocation.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        personimage.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                buttonText = "upload";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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
            go.setUri(selectedImageUri);
        }else   if (requestCode == SELECT_DOCUMENT) {
            selectedDocumentUri = data.getData();
            selectedDocumentPath = getPath(selectedDocumentUri);
            System.out.println("Image Path : " + selectedDocumentPath);
            File file = new File(selectedDocumentPath);
            documentPath.setText(file.getName());
            documentUri = selectedDocumentUri;
        }

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
            else {
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


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void updatedate()
    {
        dobInMiliSeconds=calendar.getTimeInMillis();
        lable.setText(calendar.get(Calendar.YEAR) + "-" + showMonth(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
        System.out.println("Miliseconds-------------------------------->"+dobInMiliSeconds);


    }
    public void setDate(){

        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        dobInMiliSeconds=calendar.getTimeInMillis();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){




        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendar.get(Calendar.HOUR_OF_DAY);
            calendar.get(Calendar.MINUTE);
            calendar.get(Calendar.SECOND);
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
        if(city.getText().toString().equals(""))
        {
            validData = 1;
        }
        return validData;
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
    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        }
        return true;
    }



}

