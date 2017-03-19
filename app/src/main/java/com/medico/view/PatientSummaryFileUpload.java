package com.medico.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.adapter.ClinicSpinnerAdapter;
import com.medico.adapter.DiagnosticTestSpinnerAdapter;
import com.medico.model.Clinic1;
import com.medico.model.DiagnosticTest;
import com.medico.model.FileUpload1;
import com.medico.model.PersonID;
import com.medico.model.ResponseAddDocuments;
import com.medico.model.SearchParameter;
import com.medico.util.ImageUtil;
import com.medico.util.PARAM;
import com.medico.application.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
    Button document;
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
    FileUpload1 fileupload;
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
        layout4 = (RelativeLayout)view.findViewById(R.id.layout4);
        layout5 = (RelativeLayout)view.findViewById(R.id.layout5);
        layout6 = (RelativeLayout)view.findViewById(R.id.layout6);

        type = (Spinner)view.findViewById(R.id.test_type_value) ;
        calenderImg = (ImageView) view.findViewById(R.id.calenderImg);
        startDateEdit = (TextView) view.findViewById(R.id.start_date);
        category = (Spinner) view.findViewById(R.id.file_category_value);
        clinicName = (Spinner) view.findViewById(R.id.clinicNames);
        referredBy = (Spinner)view.findViewById(R.id.referredByValue);
        document = (Button)view.findViewById(R.id.image_previw) ;
        browseDocument = (RadioButton)view.findViewById(R.id.document_browse);

        browseImage = (RadioButton)view.findViewById(R.id.image_browse);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position == 1)
                {
                    ArrayAdapter<String> a =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.diagnostic_test_type_list));
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    layout4.setVisibility(View.VISIBLE);
                    layout5.setVisibility(View.VISIBLE);
                    layout6.setVisibility(View.VISIBLE);
                    type.setAdapter(a);
                    testValueEdit.setVisibility(View.VISIBLE);
                    testValueEdit1.setVisibility(View.INVISIBLE);
                }
                else {
                    layout4.setVisibility(View.INVISIBLE);
                    layout5.setVisibility(View.INVISIBLE);
                    layout6.setVisibility(View.INVISIBLE);
                    ArrayAdapter<String> a =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.upload_document_type));
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setAdapter(a);
                    testValueEdit.setVisibility(View.INVISIBLE);

                    testValueEdit1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        document.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(browseDocument.isChecked()) {
                    Intent intent = new Intent(getActivity(), FileChooser.class);
                    ArrayList<String> extensions = new ArrayList<String>();
                    extensions.add(".pdf");
//                    extensions.add(".xls");
//                    extensions.add(".xlsx");
//                    extensions.add(".txt");
//                    extensions.add(".doc");
//                    extensions.add(".docx");
                    intent.putStringArrayListExtra("filterFileExtension", extensions);
                    startActivityForResult(intent, FILE_CHOOSER);
                }
                else if(browseImage.isChecked()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                }
                else
                {
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
//                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }



    //Declaration
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final int doctorId = bundle.getInt(DOCTOR_ID);
        final int patientId = bundle.getInt(PATIENT_ID);
        final Integer testId = bundle.getInt(PARAM.DIAGNOSTIC_TEST_ID);
        final Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);

        api.getAllClinics1(new PersonID(new Integer(doctorId).toString()), new Callback<List<Clinic1>>() {
            @Override
            public void success(List<Clinic1> clinicsList, Response response) {

                ClinicSpinnerAdapter adapter = new ClinicSpinnerAdapter(getActivity(), R.layout.customize_spinner, clinicsList);
                clinicName.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });
        api.searchAutoFillDiagnostic(new SearchParameter("", 0, 1, 100, 4), new Callback<List<DiagnosticTest>>() {
            @Override
            public void success(List<DiagnosticTest> medicineList, Response response)
            {
                DiagnosticTestSpinnerAdapter adapter = new DiagnosticTestSpinnerAdapter(getActivity(), R.layout.customize_spinner, medicineList);
                testValueEdit.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });



    }

    public void saveFile(FileUpload1 fileupload) {

            // api.uploadFile(typedFile, type, doctorId, patientId, assistantId, documentType, nameData, categoryData, appointmentDate, appointmentTime,clinicId,clinicName,new Callback<FileUpload>() {
        api.addPatientVisitDocument(fileupload.patientId.toString(), fileupload.appointmentId.toString(), fileupload.clinicId.toString(), fileupload.type.toString(), fileupload.personId.toString(), fileupload.fileName, fileupload.file, new Callback<ResponseAddDocuments>() {

            @Override
            public void success(ResponseAddDocuments responseAddDocuments, Response response) {
                {

                    if (responseAddDocuments != null) {
                        // System.out.println("Url::::::::" + responseAddDocuments.getUrl());
                        Toast.makeText(getActivity(), "File Uploaded Successfully!", Toast.LENGTH_SHORT).show();
//                        progress.dismiss();

                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "File upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                }

            }


            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
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
        menuItem.setIcon(R.drawable.save);
    }
    @Override
    protected boolean isChanged()
    {
        return true;
    }
    @Override
    protected boolean canBeSaved()
    {
        return fileupload.canBeSaved();
    }

    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        fileupload = new FileUpload1();
        ProgressDialog progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        fileupload.category = new Byte((byte)this.category.getSelectedItemPosition());
        fileupload.appointmentId = bundle.getInt(APPOINTMENT_ID);
        fileupload.personId = bundle.getInt(LOGGED_IN_ID);
        fileupload.patientId = bundle.getInt(PATIENT_ID);
        fileupload.file = this.typedFile;
        fileupload.subcategory = this.type.getSelectedItemPosition();
        fileupload.type = browseImage.isChecked()? new Integer(0).byteValue():new Integer(1).byteValue();
        if(fileupload.category.intValue() == 1)
        {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
            try {
                fileupload.date = format.parse(startDateEdit.getText().toString()).getTime();
            }
            catch(ParseException e) {
                fileupload.date = null;
            }
            fileupload.fileName = ((DiagnosticTest)testValueEdit.getSelectedItem()).name;
            fileupload.clinicId = ((Clinic1)clinicName.getSelectedItem()).idClinic;
        }
        else
        {
            fileupload.clinicId = bundle.getInt(CLINIC_ID);
            fileupload.date = new Date().getTime();
            fileupload.fileName = testValueEdit1.getText().toString();
        }
    }
    @Override
    protected boolean save()
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
                    image.setImageResource(R.drawable.pdf_preview);
                } else if (documentType.equalsIgnoreCase(".txt")) {
                    image.setImageResource((R.drawable.text));
                } else if (documentType.equalsIgnoreCase(".doc") || (documentType.equalsIgnoreCase(".docx"))) {
                    image.setImageResource(R.drawable.doc_preview);
                } else if (documentType.equalsIgnoreCase(".xls") || (documentType.equalsIgnoreCase(".xlsx"))) {
                    image.setImageResource(R.drawable.xlsx);
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
