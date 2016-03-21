package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.MainActivity;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Application.MyApi;
import Model.Person;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.mime.TypedFile;

/**
 * Created by User on 8/7/15.
 */
public class ManageDoctorProfile extends Fragment {
    public MyApi api;
    private EditText etNameText,etpassword,etlocation;
    Spinner spinnerSpeciality,spinnerBloodGroup,countrySpinner,regionSpinner,practiceName;
    TextView emailTv,mobileTv,dob;
    RadioGroup genderBtn;
    ImageView profilePic;
    ImageButton calButton;
    Button mapButton,upload,save,drawar;
    SharedPreferences session;
    ArrayAdapter<CharSequence> adapterBloodGroup;
    ArrayAdapter<CharSequence> adapterSpeciality,practiceNameAdapter;
    ArrayAdapter<String> countryAdapter,regionAdapter;
    String doctorId;
    String[] countries,regions;
    List<String> countryList,regionList;
    int position = 0;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_DOCUMENT = 2;
    public static final String IMAGE_URL = "139.162.31.36:9000"+"/getPicture/";
    Calendar calendar = Calendar.getInstance();
    Uri selectedImageUri = null;
    Uri selectdDocumentUri = null;
    String path = null;
    ProgressDialog progress;
    RadioButton male,female,sexButton;
    Button back,logout;
    TextView globalTv,accountName,document;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    String name,location,password,gender,dobString,bloddGroup,speciality,address,id;
    LinearLayout layout;
    //ImageView medicoLogo,medicoText;
    String type;
    Button refresh,uploadDocument;
    EditText city,registration;
	TextView doctorUniqueId;
    String cityString,regionString,countryString,registrationNumber,latitude,longitude,practiceNameString,documentPath;
    Global go;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_doctor_profile,container,false);
        etNameText = (EditText)view.findViewById(R.id.name);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        etlocation = (EditText) view.findViewById(R.id.location);
        etlocation.setText("");
        etpassword = (EditText)view.findViewById(R.id.password);
        genderBtn = (RadioGroup) view.findViewById(R.id.gender);
        mapButton = (Button)view.findViewById(R.id.location_button);
        emailTv = (TextView)view.findViewById(R.id.email);
        mobileTv = (TextView)view.findViewById(R.id.mobile);
        dob = (TextView)view.findViewById(R.id.textView);
        spinnerBloodGroup = (Spinner)view.findViewById(R.id.bloodGroup);
        spinnerSpeciality = (Spinner)view.findViewById(R.id.speciality);
        upload = (Button)view.findViewById(R.id.upload);
        mapButton = (Button)view.findViewById(R.id.location_button);
        calButton = (ImageButton) view.findViewById(R.id.imageButton_calendar);
        save = (Button)view.findViewById(R.id.save_profile_data);
        male = (RadioButton)view.findViewById(R.id.gender_male);
        female = (RadioButton)view.findViewById(R.id.gender_female);
        layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        int radioId = genderBtn.getCheckedRadioButtonId();
        go = (Global)getActivity().getApplicationContext();
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        back = (Button)getActivity().findViewById(R.id.back_button);
        sexButton = (RadioButton) view.findViewById(radioId);
        logout = (Button)getActivity().findViewById(R.id.logout);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
      //  medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        countrySpinner = (Spinner)view.findViewById(R.id.country_spinner);
        regionSpinner = (Spinner)view.findViewById(R.id.region_spinner);
        practiceName = (Spinner)view.findViewById(R.id.practice_name);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        city = (EditText)view.findViewById(R.id.city);
        registration = (EditText)view.findViewById(R.id.registration_number);
        practiceName = (Spinner)view.findViewById(R.id.practice_name);
        document = (TextView)view.findViewById(R.id.document_name);
        uploadDocument = (Button)view.findViewById(R.id.upload_document);
		doctorUniqueId = (TextView)view.findViewById(R.id.doctor_id);
        practiceNameAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.practice_name, android.R.layout.simple_spinner_item);
        practiceNameAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        practiceName.setAdapter(practiceNameAdapter);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        uploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_DOCUMENT);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(), MapActivity.class);
                startActivity(intObj);
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
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etNameText.getText().toString();
                location = etlocation.getText().toString();
                address = location;
                password = etpassword.getText().toString();
                gender = sexButton.getText().toString();
                dobString = dob.getText().toString();
                bloddGroup = spinnerBloodGroup.getSelectedItem().toString();
                speciality = spinnerSpeciality.getSelectedItem().toString();
                countryString = countrySpinner.getSelectedItem().toString();
                regionString = regionSpinner.getSelectedItem().toString();
                cityString = city.getText().toString();
                practiceNameString = practiceName.getSelectedItem().toString();
                registrationNumber = registration.getText().toString();
                latitude = "" + go.userLatitude;
                longitude = "" + go.userLongitude;
                int flagValidation = 0;
                String validationText = "";
                if (name.equals("")) {
                    flagValidation = 1;
                    validationText = "Please Enter Name";
                }
                if (location.equals("")) {
                    flagValidation = 1;
                    validationText = validationText + "\nPlease Enter Location";
                }
                if (password.equals("")) {
                    flagValidation = 1;
                    validationText = validationText + "\nPlease Enter Password";
                }
                if (bloddGroup.equalsIgnoreCase("Select blood group")) {
                    flagValidation = 1;
                    validationText = validationText + "\nPlease Select Blood Group";
                }
                if (registrationNumber.equals("")) {
                    flagValidation = 1;
                    validationText = validationText + "\nPlease Enter Registration Number";
                }
                if (cityString.equals("")) {
                    flagValidation = 1;
                    validationText = validationText + "\nPlease Enter City";
                }
                if (flagValidation == 1) {
                    Toast.makeText(getActivity(), validationText, Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println("Date of Birth::::" + dobString);
                    System.out.println("Gender::::::::" + gender);
                    progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                    if (!(path == null)) {
                        File file = new File(path);
                        TypedFile picture = new TypedFile("application/octet-stream", file);
                        api.picutreUpdateDoctor(picture, doctorId, new Callback<String>() {
                            @Override
                            public void success(String status, Response response) {
                                progress.dismiss();
                                Person person = new Person();
                                person.id = id;
                                person.name = name;
                                person.location = location;
                                person.password = password;
                                person.bloodGroup = bloddGroup;
                                person.speciality = speciality;
                                person.emailID = doctorId;
                                person.gender = gender;
                                person.dateOfBirth = dobString;
                                person.address = location;
                                person.city = cityString;
                                person.country = countryString;
                                person.region = regionString;
                                person.practiceName = practiceNameString;
                                person.registrationNumber = registrationNumber;
                                if (go.userLatitude != null) {
                                    person.latitude = "" + go.userLatitude;
                                }
                                if (go.userLongitude != null) {
                                    person.longitude = "" + go.userLongitude;
                                }
                                api.updateDoctorProfile(person, new Callback<String>() {
                                    @Override
                                    public void success(String status, Response response) {

                                        if (status.equalsIgnoreCase("success")) {
                                            Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                                            if (documentPath != null) {
                                                File documentFile = new File(documentPath);
                                                TypedFile document = new TypedFile("application/octet-stream", documentFile);
                                                api.updateDocumentDoctor(doctorId, document, new Callback<String>() {
                                                    @Override
                                                    public void success(String s, Response response) {
                                                        if (s.equalsIgnoreCase("Success")) {
                                                            Toast.makeText(getActivity(), "Document Updated Successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        error.printStackTrace();
                                                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
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
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });
                    } else {
                        Person person = new Person();
                        person.id = id;
                        person.name = name;
                        person.location = location;
                        person.password = password;
                        person.bloodGroup = bloddGroup;
                        person.speciality = speciality;
                        person.emailID = doctorId;
                        person.gender = gender;
                        person.dateOfBirth = dobString;
                        person.address = location;
                        person.city = cityString;
                        person.country = countryString;
                        person.region = regionString;
                        person.practiceName = practiceNameString;
                        person.registrationNumber = registrationNumber;
                        if (go.userLatitude != null) {
                            person.latitude = "" + go.userLatitude;
                        }
                        if (go.userLongitude != null) {
                            person.longitude = "" + go.userLongitude;
                        }
                        api.updateDoctorProfile(person, new Callback<String>() {
                            @Override
                            public void success(String status, Response response) {
                                progress.dismiss();
                                if (status.equalsIgnoreCase("success")) {
                                    Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                                    if (documentPath != null) {
                                        File documentFile = new File(documentPath);
                                        TypedFile document = new TypedFile("application/octet-stream", documentFile);
                                        api.updateDocumentDoctor(doctorId, document, new Callback<String>() {
                                            @Override
                                            public void success(String s, Response response) {
                                                if (s.equalsIgnoreCase("Success")) {
                                                    Toast.makeText(getActivity(), "Document Updated Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                error.printStackTrace();
                                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });
                    }

                }
            }
        });
        genderBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                sexButton = (RadioButton)view.findViewById(radioId);
            }
        });
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", null);
        type = session.getString("loginType", null);
        adapterBloodGroup = ArrayAdapter.createFromResource(getActivity(),R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapterBloodGroup.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBloodGroup.setAdapter(adapterBloodGroup);
        adapterSpeciality = ArrayAdapter.createFromResource(getActivity(),R.array.speciality_list, android.R.layout.simple_spinner_item);
        adapterSpeciality.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        if(adapterSpeciality!=null)
        spinnerSpeciality.setAdapter(adapterSpeciality);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showProfileData();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        manageScreenIcon();
        return view;
    }

    public void goBack()
    {
        globalTv.setText(type);
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
      //  medicoLogo.setVisibility(View.VISIBLE);
      //  medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
        api.getProfileDoctor(doctorId,new Callback<Person>() {
            @Override
            public void success(Person person, Response response) {
                new ImageLoadTask("http://"+IMAGE_URL+person.getId(), profilePicture).execute();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showProfileData()
    {
        progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
        api.getProfileDoctor(doctorId,new Callback<Person>() {
            @Override
            public void success(Person person, Response response) {
                if(person.getId() != null) {
                    System.out.println("Person Name::::::" + person.getName());
                    id = ""+person.getId();
                    etNameText.setText(person.getName());
                    etlocation.setText(person.getAddress());
                    location = person.getLocation();
                    emailTv.setText(person.getEmailID());
                    mobileTv.setText(person.getMobileNumber());
                    etpassword.setText(person.getPassword());
                    dob.setText(person.getDateOfBirth());
                    position = adapterBloodGroup.getPosition(person.getBloodGroup());
                    spinnerBloodGroup.setSelection(position);
                    position = adapterSpeciality.getPosition(person.getSpeciality());
                    spinnerSpeciality.setSelection(position);
                    regionString = person.getRegion();
                    countryString = person.getCountry();
                    cityString = person.getCity();
                    city.setText(person.getCity());
					doctorUniqueId.setText(person.id);
                    int pos = 0;
                    pos = practiceNameAdapter.getPosition(person.getPracticeName());
                    practiceName.setSelection(pos);
                    document.setText(person.getDocumentName());
                    registration.setText(person.getRegistrationNumber());
                    if (person.getGender().equalsIgnoreCase("Male")) {
                        male.setChecked(true);
                    } else {
                        female.setChecked(true);
                    }
                    System.out.println("Country= "+countryString);
                    System.out.println("REgion= "+regionString);
                    System.out.println("City= "+cityString);
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
                            int position = 0;
                            position = countryAdapter.getPosition(countryString);
                            countrySpinner.setSelection(position);
                            api.getRegions(countryString,new Callback<List<String>>() {
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
                                    int position = 0;
                                    position = regionAdapter.getPosition(regionString);
                                    regionSpinner.setSelection(position);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    error.printStackTrace();
                                    Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                    System.out.println("image Url:::::::::" + "http://" + IMAGE_URL + person.getId());
                    progress.dismiss();
                    new ImageLoadTask("http://" + IMAGE_URL + person.getId(), profilePic).execute();

                }
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
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        String locationString = go.getLocation();
        if(locationString.equals("")) {
            etlocation.setText(location);
        }else{
            etlocation.setText(locationString);
            if(go.countryName != null){
                position = countryAdapter.getPosition(go.countryName);
                countrySpinner.setSelection(position);
            }
            System.out.println("user Latitude= "+go.userLatitude);
            System.out.println("user Longitude= "+go.userLongitude);
        }
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    goBack();
                    return true;
                }
                return false;
            }
        });
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
    public void updatedate()
    {
        dob.setText(calendar.get(Calendar.YEAR)+"-"+showMonth(calendar.get(Calendar.MONTH))+"-"+calendar.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            profilePic.setImageURI(selectedImageUri);
            path = getPath(selectedImageUri);
        }else if(requestCode == SELECT_DOCUMENT){
            selectdDocumentUri = data.getData();
            documentPath = getPath(selectdDocumentUri);
            File documentFile = new File(documentPath);
            document.setText(documentFile.getName());
        }
    }
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void manageScreenIcon()
    {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Profile");
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        BackStress.staticflag = 1;
       // medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
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
}
