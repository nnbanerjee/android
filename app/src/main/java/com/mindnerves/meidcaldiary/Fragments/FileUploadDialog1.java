package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.Clinic;
import com.medico.view.FileChooser;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Application.ImageUtil;
import Application.MyApi;
import Model.FileUpload;
import Model.PersonID;
import Model.ResponseAddDocuments;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by MNT on 27-Mar-15.
 */
public class FileUploadDialog1 extends DialogFragment {

    private String doctorId = "0", assistantId = "0", patientId = "0", id = "";
    private String type = "";

    private EditText name;
    private Button capture, browse, upload, document;
    public MyApi api;
    Spinner category, clinicSpinner, medicalReportSpinner,typespinner;
    Global global;
    TextView nameText;
    String appointmentDate, appointmentTime;
    TypedFile typedFile;
    private String selectedImagePath = null;
    private Uri selectedImageUri = null;
    private static int SELECT_PICTURE = 1;
    private static final int FILE_CHOOSER = 2;
    SharedPreferences session;
    List<Clinic> clinicDetailVm;
    String[] clinics;
    String[] categories;
    String[] medicalReports;
    String report;
    ProgressDialog progress;
    String appointMentId;
    TextView doctorValue,dateValue;
    public static FileUploadDialog1 newInstance() {
        return new FileUploadDialog1();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_upload, container, false);
        getDialog().setTitle("Upload File");
        capture = (Button) view.findViewById(R.id.capture_image);
        browse = (Button) view.findViewById(R.id.browse_image);
        upload = (Button) view.findViewById(R.id.upload);
        global = (Global) getActivity().getApplicationContext();
        name = (EditText) view.findViewById(R.id.nameValue);
        category = (Spinner) view.findViewById(R.id.categoryValue);
        clinicSpinner = (Spinner) view.findViewById(R.id.clinic);
        medicalReportSpinner = (Spinner) view.findViewById(R.id.medical_report);
        medicalReports = getResources().getStringArray(R.array.medical_report);


        doctorValue = (TextView) view.findViewById(R.id.doctorvalue);
       // doctorValue.setText();

        dateValue = (TextView) view.findViewById(R.id.datevalues);
        dateValue.setText(UtilSingleInstance.getDateFormattedInStringFormatUsingLongForMedicinDetails(""+Calendar.getInstance().getTimeInMillis()));
        typespinner = (Spinner) view.findViewById(R.id.typespinner);
        typespinner.setAdapter(new MedicalReportSpinner(getActivity(), R.layout.customize_spinner, medicalReports));

        medicalReportSpinner.setAdapter(new MedicalReportSpinner(getActivity(), R.layout.customize_spinner, medicalReports));
        document = (Button) view.findViewById(R.id.browse_document);
        categories = getResources().getStringArray(R.array.category_data_list);
        System.out.println("SIze of categories:::::" + categories.length);
        category.setAdapter(new CategorySpinner(getActivity(), R.layout.customize_spinner, categories));
        appointmentDate = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        name.setText(global.getTestPrescribed());
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id = session.getString("sessionID", null);
        type = session.getString("loginType", null);
        appointMentId = session.getString("appointmentId", "");
        doctorId = session.getString("id", "0");


        nameText = (TextView) view.findViewById(R.id.name);
        OkHttpClient client =new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.MINUTES);
        client.setReadTimeout(5, TimeUnit.MINUTES);
        client.setWriteTimeout(5, TimeUnit.MINUTES);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient(client))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        api.getAllClinics(new PersonID(doctorId),new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> clinicsList, Response response) {
                clinicDetailVm = clinicsList;
                clinics = new String[clinicsList.size()];
                int count = 0;
                for (Clinic vm : clinicDetailVm) {
                    clinics[count] = vm.getClinicName();
                    count = count + 1;
                }
                clinicSpinner.setAdapter(new ClinicSpinner(getActivity(), R.layout.customize_spinner, clinics));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                report = (String) category.getSelectedItem();
                if (report.equalsIgnoreCase("Medical Report")) {
                    name.setVisibility(View.GONE);
                    nameText.setVisibility(View.GONE);
                    medicalReportSpinner.setVisibility(View.VISIBLE);

                } else {
                    name.setVisibility(View.VISIBLE);
                    nameText.setVisibility(View.VISIBLE);
                    medicalReportSpinner.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, 0);
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FileChooser.class);
                ArrayList<String> extensions = new ArrayList<String>();
                extensions.add(".pdf");
                extensions.add(".xls");
                extensions.add(".xlsx");
                extensions.add(".txt");
                extensions.add(".doc");
                extensions.add(".docx");
                extensions.add(".jpg");
                intent.putStringArrayListExtra("filterFileExtension", extensions);
                startActivityForResult(intent, FILE_CHOOSER);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
                String clinicName = (String) clinicSpinner.getSelectedItem();
                System.out.println("Clinic Name:::::::" + clinicName);
                 String clinicId = clinicDetailVm.get(clinicSpinner.getSelectedItemPosition()).getIdClinic();
                 System.out.println("Clinic Id:::::::"+clinicId);
               // String clinicId = "105";
                String nameData;
                /*
                httpPost.addHeader("x-patientId", "102");
                 httpPost.addHeader("x-appointmentId", "584");
                 httpPost.addHeader("x-clinicId", "101");
                 httpPost.addHeader("x-type", "1");
                 httpPost.addHeader("x-loggedinUserId", "102");
                 httpPost.addHeader("x-fileName", "ANkLE BoNe X-ray.xlsx");
                 */

                if (report.equalsIgnoreCase("Medical Report")) {
                    nameData = (String) medicalReportSpinner.getSelectedItem();
                } else {
                    nameData = name.getText().toString();
                }

                if (type.equalsIgnoreCase("doctor")) {
                    doctorId = id;
                    patientId = session.getString("patientId", null);
                    System.out.println("PatientId::::::" + patientId);
                    String categoryData = category.getSelectedItem().toString();
                    String documentType = "";
                    if (selectedImagePath != null) {
                        documentType = selectedImagePath.substring(selectedImagePath.lastIndexOf("."));
                    } else {
                        documentType = "None";
                        typedFile = null;
                    }
                    System.out.println("Document TYpe::::::" + documentType);

                    if(documentType.contains("pdf") )
                    {
                        documentType="1";
                    }else{
                        documentType="0";
                    }


                    //restAdapter..setHea

                    // api.uploadFile(typedFile, type, doctorId, patientId, assistantId, documentType, nameData, categoryData, appointmentDate, appointmentTime,clinicId,clinicName,new Callback<FileUpload>() {
                    api.addPatientVisitDocument(patientId, appointMentId, clinicId, documentType, doctorId, nameData, typedFile, new Callback<ResponseAddDocuments >() {

                        @Override
                        public void success(ResponseAddDocuments responseAddDocuments, Response response) {
                            {

                                if (responseAddDocuments != null) {
                                   // System.out.println("Url::::::::" + responseAddDocuments.getUrl());
                                    Toast.makeText(getActivity(), "File Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();

                                    FileUploadDialog1.this.getDialog().cancel();
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


                } else if (type.equalsIgnoreCase("patient")) {
                    patientId = id;
                    doctorId = session.getString("patient_doctor_email", null);
                    String categoryData = category.getSelectedItem().toString();
                    String documentType = "";
                    if (selectedImagePath != null) {
                        documentType = selectedImagePath.substring(selectedImagePath.lastIndexOf("."));
                    } else {
                        documentType = "None";
                        typedFile = null;
                    }
                    System.out.println("Document TYpe::::::" + documentType);
                    api.uploadFile(typedFile, type, doctorId, patientId, assistantId, documentType, nameData, categoryData, appointmentDate, appointmentTime, clinicId, clinicName, new Callback<FileUpload>() {
                        @Override
                        public void success(FileUpload uploadFile, Response response2) {
                            if (uploadFile != null) {
                                Toast.makeText(getActivity(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();

                                progress.dismiss();
                                FileUploadDialog1.this.getDialog().cancel();
                            } else {
                                Toast.makeText(getActivity(), "Data is not saved", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });

                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        System.out.println("in onActivityResult/////////////////////////////////");

        try {
            if (requestCode == ImageUtil.SELECT_PICTURE) {
                selectedImageUri = data.getData();
                String path = getPath(selectedImageUri);
                System.out.println("Path::::::::" + path);
                selectedImagePath = path;
                File file = new File(path);
                typedFile = new TypedFile("application/octet-stream", file);
                System.out.println("typedFIle::::::::" + typedFile.toString());
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                image.setImageURI(selectedImageUri);
            } else if (requestCode == 0) {
                File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                selectedImageUri = Uri.fromFile(picture);
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                image.setImageURI(selectedImageUri);
                selectedImagePath = picture.getName();
                typedFile = new TypedFile("application/octet-stream", picture);
                System.out.println("typedFIle::::::::" + typedFile.toString());
            } else if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
                String fileSelected = data.getStringExtra("fileSelected");
                File picture = new File(fileSelected);
                ImageView image = (ImageView) getView().findViewById(R.id.preview_image);
                selectedImagePath = picture.getName();
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


    public class ClinicSpinner extends ArrayAdapter<String> {
        public ClinicSpinner(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customize_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(clinics[position]);
            return mySpinner;
        }


    }

    public class CategorySpinner extends ArrayAdapter<String> {
        public CategorySpinner(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customize_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(categories[position]);
            return mySpinner;
        }


    }

    public class MedicalReportSpinner extends ArrayAdapter<String> {
        public MedicalReportSpinner(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customize_spinner, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);
            main_text.setText(medicalReports[position]);
            return mySpinner;
        }


    }

}
