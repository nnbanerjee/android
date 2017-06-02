package com.medicohealthcare.view.registration;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.FileUpload1;
import com.medicohealthcare.util.FileChooser;
import com.medicohealthcare.util.ImageUtil;
import com.medicohealthcare.view.home.ParentFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit.mime.TypedFile;

//import Model.ResponseAddDocuments;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class RegistrationFileUpload extends ParentFragment {

    RadioButton browseDocument,browseImage;
    private static int SELECT_PICTURE = 1;
    private static final int FILE_CHOOSER = 2;
    Button document;
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
        document = (Button)view.findViewById(R.id.image_previw) ;
        browseDocument = (RadioButton)view.findViewById(R.id.document_browse);

        browseImage = (RadioButton)view.findViewById(R.id.image_browse);

        document.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(browseDocument.isChecked()) {
                    Intent intent = new Intent(getActivity(), FileChooser.class);
                    ArrayList<String> extensions = new ArrayList<String>();
                    extensions.add(".pdf");
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
//        Bundle bundle = getActivity().getIntent().getExtras();
//        final int doctorId = bundle.getInt(DOCTOR_ID);
//        final int patientId = bundle.getInt(PATIENT_ID);
//        final Integer testId = bundle.getInt(PARAM.DIAGNOSTIC_TEST_ID);
//        final Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
//        final Integer clinicId = bundle.getInt(CLINIC_ID);
//        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);

    }

    public void saveFile(FileUpload1 fileupload) {

            // api.uploadFile(typedFile, type, doctorId, patientId, assistantId, documentType, nameData, categoryData, appointmentDate, appointmentTime,clinicId,clinicName,new Callback<FileUpload>() {
//        api.addPatientVisitDocument(fileupload.patientId.toString(), fileupload.appointmentId.toString(), fileupload.clinicId.toString(), fileupload.type.toString(), fileupload.personId.toString(), fileupload.fileName, fileupload.file, new Callback<ResponseAddDocuments>() {
//
//            @Override
//            public void success(ResponseAddDocuments responseAddDocuments, Response response) {
//                {
//
//                    if (responseAddDocuments != null) {
//                        // System.out.println("Url::::::::" + responseAddDocuments.getUrl());
//                        Toast.makeText(getActivity(), "File Uploaded Successfully!", Toast.LENGTH_SHORT).show();
////                        progress.dismiss();
//
//                        getActivity().onBackPressed();
//                    } else {
//                        Toast.makeText(getActivity(), "File upload Failed!", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.printStackTrace();
//            }
//        });
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
        fileupload.appointmentId = bundle.getInt(APPOINTMENT_ID);
        fileupload.personId = bundle.getInt(LOGGED_IN_ID);
        fileupload.patientId = bundle.getInt(PATIENT_ID);
        fileupload.file = this.typedFile;;
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
                } else if (documentType.equalsIgnoreCase(".png")) {
                    image.setImageResource((R.drawable.document_png));
                }



            }
        } catch (Exception e)
            {
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
