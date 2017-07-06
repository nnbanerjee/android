package com.medicohealthcare.view.profile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.ClinicSpinnerAdapter;
import com.medicohealthcare.adapter.DiagnosticTestSpinnerAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.datepicker.SlideDateTimeListener;
import com.medicohealthcare.datepicker.SlideDateTimePicker;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.DiagnosticTest;
import com.medicohealthcare.model.FileUpload1;
import com.medicohealthcare.model.PersonID;
import com.medicohealthcare.model.ResponseAddDocuments;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.util.ImageUtil;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.util.PermissionManager;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

//import Model.PersonID;
//import Model.ResponseAddDocuments;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class PatientSummaryFileUpload extends ParentFragment {

    TextView startDateEdit = null;
    EditText testValueEdit1 = null;
    Button browse;
    ImageView calenderImg;
    Calendar calendar = Calendar.getInstance();
    ArrayAdapter<String> scheduleAdapter;
    Spinner clinicName = null;
    Spinner referredBy = null;
    Spinner testValueEdit = null;
    Spinner category = null;
    Spinner type = null;
    RelativeLayout layout4,layout5,layout6;
    RadioButton browseDocument,browseImage;
    private static int SELECT_PICTURE = 1;
    private static final int FILE_CHOOSER = 2;
    DiagnosticTestSpinnerAdapter adapter;
    FileUpload1 fileupload = new FileUpload1();
    TypedFile typedFile;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_summary_file_upload, container, false);
        setHasOptionsMenu(true);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Diagnostic Test Details");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        //Load Ui controls
        testValueEdit1 = (EditText)view.findViewById(R.id.testValueEdit1);
        testValueEdit = (Spinner)view.findViewById(R.id.testValueEdit);
        layout4 = (RelativeLayout)view.findViewById(R.id.manage_appointment);
        layout5 = (RelativeLayout)view.findViewById(R.id.layout5);
        layout6 = (RelativeLayout)view.findViewById(R.id.layout6);

        type = (Spinner)view.findViewById(R.id.test_type_value) ;
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        startDateEdit = (TextView) view.findViewById(R.id.start_date);
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
        startDateEdit.setText(format.format(new Date()));
        fileupload.date = new Date().getTime();
        category = (Spinner) view.findViewById(R.id.file_category_value);
        clinicName = (Spinner) view.findViewById(R.id.clinicNames);
        referredBy = (Spinner)view.findViewById(R.id.referredByValue);
        browse = (Button)view.findViewById(R.id.image_previw) ;
        browseDocument = (RadioButton)view.findViewById(R.id.document_browse);

        browseImage = (RadioButton)view.findViewById(R.id.image_browse);
        calenderImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setDate(startDateEdit);
            }
        });
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position == 1)
                {
                    layout4.setVisibility(View.VISIBLE);
                    layout5.setVisibility(View.VISIBLE);
                    layout6.setVisibility(View.VISIBLE);
                    type.setAdapter(adapter);
                }
                else {
                    layout4.setVisibility(View.INVISIBLE);
                    layout5.setVisibility(View.INVISIBLE);
                    layout6.setVisibility(View.INVISIBLE);
                    ArrayAdapter<String> a =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.upload_document_type));
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setAdapter(a);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        browse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                if(browseDocument.isChecked()) {
//                    Intent intent = new Intent(getActivity(), FileChooser.class);
//                    ArrayList<String> extensions = new ArrayList<String>();
//                    extensions.add(".pdf");
//                    extensions.add(".png");
//                    intent.putStringArrayListExtra("filterFileExtension", extensions);
//                    startActivityForResult(intent, FILE_CHOOSER);
//                }
//                else if(browseImage.isChecked()) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//                }
//                else
                {
                    if ( Build.VERSION.SDK_INT >= 23 && !PermissionManager.getInstance((getActivity())).hasPermission(PermissionManager.CAMERA))
                        return  ;

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, 0);
                }
            }
        });
        return view;
    }




    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        setTitle("Document Upload");
    }



    //Declaration
    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        final int doctorId = bundle.getInt(DOCTOR_ID);
        final int patientId = bundle.getInt(PATIENT_ID);
        final Integer testId = bundle.getInt(PARAM.DIAGNOSTIC_TEST_ID);
        final Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);

        api.getAllClinics(new PersonID(new Integer(doctorId)), new Callback<List<Clinic1>>() {
            @Override
            public void success(List<Clinic1> clinicsList, Response response) {

                ClinicSpinnerAdapter adapter = new ClinicSpinnerAdapter(getActivity(), R.layout.customize_spinner, clinicsList);
                clinicName.setAdapter(adapter);
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);

            }
        });
        api.searchAutoFillDiagnostic(new SearchParameter("", 0, 1, 100, 4), new Callback<List<DiagnosticTest>>() {
            @Override
            public void success(List<DiagnosticTest> medicineList, Response response)
            {
                adapter = new DiagnosticTestSpinnerAdapter(getActivity(), R.layout.customize_spinner, medicineList);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });


        setTitle("Document Upload");
    }

    public void saveFile(FileUpload1 fileupload)
    {
        showBusy();
        api.addPatientVisitDocument(fileupload.patientId.toString(), fileupload.appointmentId.toString(),
                fileupload.clinicId.toString(), fileupload.type.toString(), fileupload.personId.toString(),
                fileupload.fileName, fileupload.category, fileupload.subcategory, fileupload.file, new Callback<ResponseAddDocuments>()
        {

            @Override
            public void success(ResponseAddDocuments responseAddDocuments, Response response) {
                {

                    if (responseAddDocuments != null) {
                        // System.out.println("Url::::::::" + responseAddDocuments.getUrl());
                        Toast.makeText(getActivity(), "File Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        hideBusy();

                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "File upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
    }


    public void setDate(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.SHORT);
                dateField.setText(format.format(date));
                fileupload.date = date.getTime();
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
        menuItem.setIcon(null);
        menuItem.setTitle("SAVE");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
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
            case R.id.home: {
                return false;
            }

        }
        return true;
    }
    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    public boolean canBeSaved()
    {
        return fileupload.canBeSaved();
    }

    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        fileupload = new FileUpload1();
        fileupload.category = new Byte((byte)this.category.getSelectedItemPosition());
        fileupload.appointmentId = bundle.getInt(APPOINTMENT_ID);
        fileupload.personId = bundle.getInt(LOGGED_IN_ID);
        fileupload.patientId = bundle.getInt(PATIENT_ID);
        fileupload.file = this.typedFile;
        fileupload.fileName = testValueEdit1.getText().toString();
        fileupload.type = /*browseImage.isChecked()? new Integer(0).byteValue():*/new Integer(1).byteValue();
        if(fileupload.category.intValue() == 1)
        {
            fileupload.subcategory = ((DiagnosticTest)this.type.getSelectedItem()).testId;
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
            try {
                fileupload.date = format.parse(startDateEdit.getText().toString()).getTime();
            }
            catch(ParseException e) {
                fileupload.date = null;
            }
            fileupload.clinicId = ((Clinic1)clinicName.getSelectedItem()).idClinic;
        }
        else
        {
            fileupload.subcategory = this.type.getSelectedItemPosition();
            fileupload.clinicId = bundle.getInt(CLINIC_ID);
            fileupload.date = new Date().getTime();
        }
    }
    @Override
    public boolean save()
    {
        saveFile(fileupload);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult/////////////////////////////////");

        try {
            if (requestCode == ImageUtil.SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String path = getPath(selectedImageUri);
                System.out.println("Path::::::::" + path);
                String selectedImagePath = path;
                File file = new File(path);
                typedFile = new TypedFile("application/octet-stream", file);
                System.out.println("typedFIle::::::::" + typedFile.toString());
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                image.setImageURI(selectedImageUri);
            } else if (requestCode == 0) {
                File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                Uri selectedImageUri = Uri.fromFile(picture);
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                image.setImageURI(selectedImageUri);
                String selectedImagePath = picture.getName();
                typedFile = new TypedFile("application/octet-stream", picture);
                System.out.println("typedFIle::::::::" + typedFile.toString());
            } else if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
                String fileSelected = data.getStringExtra("fileSelected");
                File picture = new File(fileSelected);
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                String selectedImagePath = picture.getName();
                typedFile = new TypedFile("application/octet-stream", picture);
                System.out.println("typedFIle::::::::" + typedFile.toString());
                String documentType = selectedImagePath.substring(selectedImagePath.lastIndexOf("."));
                System.out.println("Document TYpe::::::" + documentType);
                if (documentType.equalsIgnoreCase(".pdf")) {
                    image.setImageResource(R.drawable.document_pdf);
                } else if (documentType.equalsIgnoreCase(".txt")) {
                    image.setImageResource(R.drawable.document_png);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
