package com.medico.view.settings;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.PersonDetailProfile;
import com.medico.model.ProfileId;
import com.medico.model.ServerResponse;
import com.medico.application.R;
import com.medico.view.home.ParentFragment;
import com.medico.view.registration.RegistrationFileUpload;

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
public class DoctorDetailedProfileManageView extends ParentFragment {

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    ProgressDialog progress;

    EditText qualification, institution, yearOfExprience, experienceDetails,services,awards,memberOf,registrationNumber,registrationFile, registrationDate;
    Spinner practice;
    ImageButton qualificationDateBtn,registrationDateBtn,registrationFileBtn,registrationDateChooser;

    PersonDetailProfile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_doctor_detailed_profile,container,false);
//        setHasOptionsMenu(true);

        qualification = (EditText) view.findViewById(R.id.qualification);
        institution = (EditText) view.findViewById(R.id.institution);
        yearOfExprience = (EditText) view.findViewById(R.id.years_of_experience);
        experienceDetails = (EditText) view.findViewById(R.id.experience_description);
        services = (EditText) view.findViewById(R.id.services);
        awards = (EditText) view.findViewById(R.id.awards);
        memberOf = (EditText) view.findViewById(R.id.member);
        practice = (Spinner) view.findViewById(R.id.practice1);
        registrationNumber = (EditText) view.findViewById(R.id.registration);
        registrationFile = (EditText) view.findViewById(R.id.regisration_file);
        registrationDate = (EditText) view.findViewById(R.id.regisration_date);
        registrationDateChooser = (ImageButton) view.findViewById(R.id.registration_date_chooser);
        registrationDateChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(registrationDate);
            }
        });
        registrationFileBtn = (ImageButton) view.findViewById(R.id.registration_file_chooser);
        registrationFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getActivity().getIntent().getExtras();
                ParentFragment fragment = new RegistrationFileUpload();
                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, RegistrationFileUpload.class.getName()).addToBackStack(RegistrationFileUpload.class.getName()).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
        api.getDetailedProfile(new ProfileId(bundle.getInt(LOGGED_IN_ID)),new Callback<PersonDetailProfile>() {
            @Override
            public void success(PersonDetailProfile person, Response response) {
                if(person != null && person.personId != null)
                {
                    profile = person;
                    qualification.setText(person.qualification);
                    institution.setText(person.institution);
                    yearOfExprience.setText(person.experience);//person.dateOfDegreeRegistration - new Date()));
                    experienceDetails.setText(person.briefDescription);
                    services.setText(person.services);
                    awards.setText(person.awards);
                    memberOf.setText(person.memberOf);
                    registrationNumber.setText(person.registrationNo);
//                    registrationDate.set);
                    if(person.uploadFileUrl != null && person.uploadFileUrl.length() > 1)
                        registrationFile.setText(person.uploadFileUrl.substring(person.uploadFileUrl.lastIndexOf("/")+1));

                    String practiceName = person.practiceName;
                    String[] practiceNames = getActivity().getResources().getStringArray(R.array.practice_name);
                    for(int i = 0; i < practiceNames.length; i++)
                    {
                        if(practiceName.equalsIgnoreCase(practiceNames[i]))
                        {
                            practice.setSelection(i);
                            break;
                        }
                    }
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
//        new DatePickerDialog(getActivity(),d,calendar_grey.get(Calendar.YEAR),calendar_grey.get(Calendar.MONTH),calendar_grey.get(Calendar.DAY_OF_MONTH)).show();
//    }
//    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
//
//
//
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            calendar_grey.set(Calendar.YEAR,year);
//            calendar_grey.set(Calendar.MONTH,monthOfYear);
//            calendar_grey.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//            updatedate();
//        }
//
//    };
//    public void updatedate()
//    {
//        dob.setText(calendar_grey.get(Calendar.YEAR)+"-"+showMonth(calendar_grey.get(Calendar.MONTH))+"-"+calendar_grey.get(Calendar.DAY_OF_MONTH));
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
    private void save(PersonDetailProfile person)
    {
        api.updateDetailedProfile(person, new Callback<ServerResponse>() {
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
                profile.dateOfDegreeRegistration = date.getTime();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setTitle("SAVE");
    }

    @Override
    public boolean isChanged()
    {
        return profile.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        profile.awards = awards.getText().toString();
        profile.briefDescription = experienceDetails.getText().toString();
        profile.experience = yearOfExprience.getText().toString();
        profile.institution = institution.getText().toString();
        profile.memberOf = memberOf.getText().toString();
        profile.qualification = qualification.getText().toString();
        profile.registrationNo = registrationNumber.getText().toString();
        profile.services = services.getText().toString();
        profile.practiceName = practice.getSelectedItem().toString();
        profile.uploadFileUrl = registrationFile.getText().toString();
    }
    @Override
    public boolean save()
    {
        if(profile.canBeSaved())
        {
            save(profile);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return profile.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
//               if(fragment instanceof DoctorAppointmentSummary || fragment instanceof PatientMedicinReminder || fragment instanceof PatientSummaryFileUpload) {
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
            case R.id.home: {

            }
            break;

        }
        return true;
    }



}
