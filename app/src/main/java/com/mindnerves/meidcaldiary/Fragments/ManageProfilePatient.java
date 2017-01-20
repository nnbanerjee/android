package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.MapActivity;
import com.mindnerves.meidcaldiary.R;

import java.io.File;
import java.util.Calendar;

import Application.MyApi;
import Model.PersonTemp;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by User on 8/10/15.
 */
public class ManageProfilePatient extends Fragment {

    public MyApi api;
    private EditText etNameText,etpassword,etlocation,eAllergic;
    Spinner spinnerBloodGroup;
    TextView emailTv,mobileTv,dob;
    RadioGroup genderBtn;
    ImageView profilePic;
    ImageButton calButton;
    Button mapButton,upload,save,drawar,logout;
    SharedPreferences session;
    ArrayAdapter<CharSequence> adapterBloodGroup;
    String patientId;
    int position = 0;
    private static final int SELECT_PICTURE = 1;
    Calendar calendar = Calendar.getInstance();
    Uri selectedImageUri = null;
    String path = null;
    ProgressDialog progress;
    Button upload_patient;
    RadioButton male,female,sexButton;
    public static final String IMAGE_URL = "139.162.31.36:9000"+"/getPicture/";
    String name,location,password,gender,dobString,bloddGroup,speciality,allergic;
    Button back;
    TextView globalTv,accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    LinearLayout layout;
   // ImageView medicoLogo,medicoText;
    String type;
    Button refresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_patient_profile,container,false);
        back = (Button)getActivity().findViewById(R.id.back_button);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        logout = (Button)getActivity().findViewById(R.id.logout);
        etNameText = (EditText)view.findViewById(R.id.name);
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
        upload = (Button)view.findViewById(R.id.eupload);
        mapButton = (Button)view.findViewById(R.id.location_button);
        calButton = (ImageButton) view.findViewById(R.id.imageButton_calendar);
        save = (Button)view.findViewById(R.id.save_profile_data);
        male = (RadioButton)view.findViewById(R.id.gender_male);
        female = (RadioButton)view.findViewById(R.id.gender_female);
        int radioId = genderBtn.getCheckedRadioButtonId();
        sexButton = (RadioButton) view.findViewById(radioId);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);
        type = session.getString("loginType",null);
        adapterBloodGroup = ArrayAdapter.createFromResource(getActivity(), R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapterBloodGroup.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerBloodGroup.setAdapter(adapterBloodGroup);
        eAllergic = (EditText)view.findViewById(R.id.eallergic);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
      //  medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
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
                sexButton = (RadioButton) view.findViewById(radioId);
            }
        });
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "set Date", Toast.LENGTH_SHORT).show();
                setDate();
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etNameText.getText().toString();
                location = etlocation.getText().toString();
                password = etpassword.getText().toString();
                gender = sexButton.getText().toString();
                dobString = dob.getText().toString();
                bloddGroup = spinnerBloodGroup.getSelectedItem().toString();
                allergic = eAllergic.getText().toString();
                int flagValidation = 0;
                String validationText = "";
                if(name.equals(""))
                {
                    flagValidation = 1;
                    validationText = "Please Enter Name";
                }
                if(location.equals(""))
                {
                    flagValidation = 1;
                    validationText = validationText+"\nPlease Enter Location";
                }
                if(password.equals(""))
                {
                    flagValidation = 1;
                    validationText = validationText+"\nPlease Enter Password";
                }
                if(bloddGroup.equalsIgnoreCase("Select blood group"))
                {
                    flagValidation = 1;
                    validationText = validationText+"\nPlease Select Blood Group";
                }
                if(allergic.equals(""))
                {
                    flagValidation = 1;
                    validationText = validationText+"\nPlease Enter Allegric";
                }
                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(),validationText,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                    if (!(path == null))
                    {
                        File file = new File(path);
                        TypedFile picture = new TypedFile("application/octet-stream", file);
                        api.updatePatientProfilePicture(picture, patientId, new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                progress.dismiss();
                                PersonTemp person = new PersonTemp();
                                person.name = name;
                                person.location = location;
                                person.password = password;
                                person.bloodGroup = bloddGroup;
                                person.allegricTo = allergic;
                                person.emailID = patientId;
                                person.gender = gender;
                                person.dateOfBirth = dobString;
                                api.updatePatientProfile(person, new Callback<String>() {
                                    @Override
                                    public void success(String status, Response response) {
                                        if(status.equalsIgnoreCase("success"))
                                        {
                                            Toast.makeText(getActivity(),"Profile Saved Successfully",Toast.LENGTH_SHORT).show();
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
                    }
                    else
                    {
                        PersonTemp person = new PersonTemp();
                        person.name = name;
                        person.location = location;
                        person.password = password;
                        person.bloodGroup = bloddGroup;
                        person.allegricTo = allergic;
                        person.emailID = patientId;
                        person.gender = gender;
                        person.dateOfBirth = dobString;
                        api.updatePatientProfile(person,new Callback<String>() {
                            @Override
                            public void success(String status, Response response) {
                                progress.dismiss();
                                if(status.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(getActivity(),"Profile Saved Successfully",Toast.LENGTH_SHORT).show();
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
        profile();
        manageScreenIcons();
        return view;
    }
    public void manageScreenIcons()
    {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Profile");
        drawar.setVisibility(View.GONE);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);
        layout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
      //  medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }

    public void profile()
    {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getProfilePatient(patientId,new Callback<PersonTemp>() {
            @Override
            public void success(PersonTemp person, Response response) {
                etNameText.setText(person.getName());
                etlocation.setText(person.getLocation());
                location = person.getLocation();
                emailTv.setText(person.getEmailID());
                mobileTv.setText(person.getMobileNumber());
                etpassword.setText(person.getPassword());
                dob.setText(person.getDateOfBirth());
                position = adapterBloodGroup.getPosition(person.getBloodGroup());
                spinnerBloodGroup.setSelection(position);
                eAllergic.setText(person.getAllegricTo());
                if(person.getGender().equalsIgnoreCase("male"))
                {
                    male.setChecked(true);
                }
                else
                {
                    female.setChecked(true);
                }
                new ImageLoadTask("http://"+IMAGE_URL+person.getId(), profilePic).execute();
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
    public void goBack()
    {
        globalTv.setText(type);
        Fragment fragment = new PatientMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
       // medicoLogo.setVisibility(View.VISIBLE);
      //  medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
        api.getProfilePatient(patientId,new Callback<PersonTemp>() {
            @Override
            public void success(PersonTemp person, Response response) {
                new ImageLoadTask("http://"+IMAGE_URL+person.getId(), profilePicture).execute();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        final Global go = (Global)getActivity().getApplicationContext();
        String locationString = go.getLocation();
        if(locationString.equals("")) {
            etlocation.setText(location);
        }
        else
        {
            etlocation.setText(locationString);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            profilePic.setImageURI(selectedImageUri);
            path = getPath(selectedImageUri);
        }
    }
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
