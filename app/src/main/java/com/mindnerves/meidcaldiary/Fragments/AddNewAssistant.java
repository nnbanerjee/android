package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.Calendar;

import Application.MyApi;
import Model.Assistant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 13-Mar-15.
 */
public class AddNewAssistant extends Fragment {

    DateFormat formate = DateFormat.getDateInstance();
    Calendar calendar = Calendar.getInstance();
    ImageButton cal;
    TextView lable;
    private RadioButton radioSexButton;
    private ImageView personimage;
    private EditText etNormalText,etEmailAddrss,etPhoneNumber,etlocation,countryCodeTv;
    private Spinner spinner1;
    RadioGroup genderBtn;
    private Uri uri;
    Button btn;
    DatePicker picker;
    ImageButton imageButton_calendar;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String dob;
    private String countryCode;
    private String location;
    private String bloodGroup;
    public String doctorId;
    private Button buttonBack,buttonAdd,buttonUpload,mapButton,back;
    private String validation = "";
    private int flagValidation = 0;
    private TextView dobTv;
    public MyApi api;
    private Boolean isPhoto = false;
    final Integer SELECT_PICTURE = 1;
    private Uri selectedImageUri = null;
    private String selectedImagePath = null;
    TextView globalTv;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        final Global go = (Global)getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if(locationString.equals("")) {
            etlocation.setText("");
        }
        else
        {
            etlocation.setText(locationString);
        }

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }
    public void goBack() {
        final Global go = (Global)getActivity().getApplicationContext();
        go.setLocation("");
        Fragment frag2 = new AddAssistant();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
    public void manageScreenIcon() {
        globalTv.setText("Add New Assistant");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_new_assistant,
                container, false);
        getActivity().getActionBar().hide();
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
        personimage = (ImageView)view.findViewById(R.id.personImage);
        cal = (ImageButton) view.findViewById(R.id.imageButton_calendar);
        back = (Button)getActivity().findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 goBack();
            }
        });
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        lable = (TextView)view.findViewById(R.id.textView);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();
            }
        });
        updatedate();
        spinner1 = (Spinner) view.findViewById(R.id.bloodGroup);
        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);

        adapterBlood.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner

        spinner1.setAdapter(adapterBlood);

        personimage = (ImageView) view.findViewById(R.id.personImage);
        etEmailAddrss = (EditText) view.findViewById(R.id.email);
        etNormalText = (EditText) view.findViewById(R.id.name);
        etPhoneNumber = (EditText) view.findViewById(R.id.mobile);
        etlocation = (EditText) view.findViewById(R.id.location);
        spinner1 = (Spinner) view.findViewById(R.id.bloodGroup);
        genderBtn = (RadioGroup) view.findViewById(R.id.gender);
        int radioId = genderBtn.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) view.findViewById(radioId);
        dobTv = (TextView)view.findViewById(R.id.textView);
        countryCodeTv = (EditText)view.findViewById(R.id.postal_code);
        mapButton = (Button)view.findViewById(R.id.location_button);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intObj = new Intent(getActivity(),MapActivity.class);
                startActivity(intObj);
            }
        });
        genderBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) view.findViewById(radioId);
            }
        });

        personimage.setImageURI(uri);
        view.findViewById(R.id.upload)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                     /*   Intent intent = new Intent();
                        intent.setType("image*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);*/
                    }
                });

        buttonBack = (Button)view.findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Global go = (Global)getActivity().getApplicationContext();
                go.setLocation("");
                Fragment frag2 = new AddAssistant();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        buttonUpload = (Button)view.findViewById(R.id.upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                isPhoto = true;

            }
        });
        buttonAdd = (Button)view.findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagValidation = 0;
                validation = "";

                gender = radioSexButton.getText().toString();
                dob = dobTv.getText().toString();
                bloodGroup = spinner1.getSelectedItem().toString();
                name = etNormalText.getText().toString();
                email = etEmailAddrss.getText().toString();
                mobile = etPhoneNumber.getText().toString();
                countryCode = countryCodeTv.getText().toString();
                gender = radioSexButton.getText().toString();
                location = etlocation.getText().toString();


                if(name.equals(""))
                {
                    validation = "Please Enter Assistant Name";
                    flagValidation = 0;
                }
                if(email.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Email Address";
                    flagValidation = 0;

                }
                if(mobile.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Mobile Number";
                    flagValidation = 0;
                }
                if(location.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Location Number";
                    flagValidation = 0;
                }
                if(gender.equals(""))
                {
                    validation = validation+"\n"+"Please Select Gender";
                    flagValidation = 0;
                }
                if(mobile.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Code Number";
                    flagValidation = 0;
                }


                int len = mobile.length();
                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                }
                else if(len < 10)
                {
                    Toast.makeText(getActivity(),"Please Enter Mobile Number Properly",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mobile = countryCode+mobile;
                    Assistant ats = new Assistant();
                    ats.setName(name);
                    ats.setEmailID(email);
                    ats.setDateOfBirth(dob);
                    ats.setGender(gender);
                    ats.setLocation(location);
                    ats.setBloodGroup(bloodGroup);
                    ats.setMobileNumber(mobile);
                    ats.setDateOfBirth(dob);

                    api.addAssistant(doctorId,ats, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            // int status = response.getStatus();
                            System.out.println("Response " + response.getStatus());

                            int status = response2.getStatus();

                            if (status == 200) {

                                Toast.makeText(getActivity(), "Assistant Added Successfully", Toast.LENGTH_LONG);
                                Fragment fragment = new ManageastantFragment();
                                FragmentManager fragmentManger = getFragmentManager();
                                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Doctor").commit();
                            }


                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();

                        }
                    });

                }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            selectedImageUri.getPath();
            personimage.setImageURI(selectedImageUri);
        }

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void updatedate()
    {
        lable.setText(formate.format(calendar.getTime()));
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
  }
