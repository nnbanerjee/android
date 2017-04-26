package com.medico.view.profile;

import android.app.Activity;
import android.app.FragmentManager;
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
import com.medico.application.R;
import com.medico.model.AppointmentId1;
import com.medico.model.FileUpload1;
import com.medico.util.FileUploadDialog;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentDocument extends ParentFragment {

    Button upload;
    private static final int FILE_SELECT_CODE = 0;
    View convertView;
    ListView allDocumentDoctor;
    List<FileUpload1> uploadFiles;

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

         allDocumentDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileUpload1 fileUpload = uploadFiles.get(position);
                System.out.println("Url:::::::::"+fileUpload.url);
                if(fileUpload.url != null) {
                     PreviewDialogView adf = PreviewDialogView.newInstance();
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
        showBusy();
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
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
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
        ((ParentActivity)getActivity()).attachFragment(fragment);
        fragment.setArguments(args);
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().add(R.id.service, fragment, PatientSummaryFileUpload.class.getName()).addToBackStack(PatientSummaryFileUpload.class.getName()).commit();
        return true;
    }

    @Override
    public boolean isChanged()
    {
        return true;
    }
    @Override
    public void update()
    {
    }
    @Override
    public boolean canBeSaved()
    {
        return true;
    }
    @Override
    public void setEditable(boolean editable)
    {
    }
}
