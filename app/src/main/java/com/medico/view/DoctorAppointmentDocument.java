package com.medico.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.medico.adapter.DocumentAdapter;
import com.medico.model.AppointmentId1;
import com.medico.model.FileUpload1;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentDocument extends ParentFragment{

    Button upload;
    private static final int FILE_SELECT_CODE = 0;
    View convertView;
    ListView allDocumentDoctor;
    List<FileUpload1> uploadFiles;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_document, container,false);
        upload = (Button)view.findViewById(R.id.upload);
        allDocumentDoctor = (ListView)view.findViewById(R.id.list_documents);
        convertView = view;
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploadDialog adf = FileUploadDialog.newInstance();
                adf.show(getFragmentManager(),"Dialog");
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

         allDocumentDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileUpload1 fileUpload = uploadFiles.get(position);
                System.out.println("Url:::::::::"+fileUpload.url);
                if(fileUpload.url != null) {
                     ShowPreviewDialog adf = ShowPreviewDialog.newInstance();
                    adf.setFile(fileUpload);
                    adf.show(getFragmentManager(), "Dialog");
                }

            }
        });
        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        final Activity activity = getActivity();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        Integer patientId = bundle.getInt(PATIENT_ID);
        api.getPatientVisitDocuments1(new AppointmentId1(appointMentId), new Callback<List<FileUpload1>>() {
            @Override
            public void success(List<FileUpload1> fileUploads, Response response) {

                System.out.println("Url123:::::::::" + fileUploads.size());

                DocumentAdapter adapter = new DocumentAdapter(activity, fileUploads, "");
                uploadFiles = fileUploads;
                System.out.println("Adapter size= " + adapter.getCount());
                allDocumentDoctor.setAdapter(adapter);
                progress.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
//                error.printStackTrace();
//                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean save()
    {
        Bundle args = getActivity().getIntent().getExtras();
        ParentFragment fragment = new PatientSummaryFileUpload();
        ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
        fragment.setArguments(args);
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
        return true;
    }

    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    protected void update()
    {
    }
    @Override
    protected boolean canBeSaved()
    {
        return true;
    }
    @Override
    protected void setEditable(boolean editable)
    {
    }
}
