package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.DocumentAdapter;
import Application.MyApi;
import Model.FileUpload;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 08-10-2015.
 */
public class AppointmentPatientDocument extends Fragment {
    Button upload;
    private static final int FILE_SELECT_CODE = 0;
    View convertView;
    ListView list;
    public MyApi api;
    String doctorId = "";
    String patientId = "";
    String appointmentDate = "";
    String appointmentTime = "";
    SharedPreferences session;
    Global global;
    List<FileUpload> uploadFiles;
    ProgressDialog progress;
    String type = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_appointment_document, container,false);
        upload = (Button)view.findViewById(R.id.upload);
        list = (ListView)view.findViewById(R.id.list_documents);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        convertView = view;
        global = (Global) getActivity().getApplicationContext();
        patientId = session.getString("sessionID",null);
        doctorId = session.getString("patient_DoctorId", null);
        type = session.getString("type",null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploadDialog adf = FileUploadDialog.newInstance();
                adf.show(getFragmentManager(),"Dialog");
            }
        });
        loadListData();
        return view;
    }
    public void loadListData()
    {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        System.out.println("Url 123:::::::::"+doctorId);
        api.getFilesPatient(doctorId,patientId,appointmentDate,appointmentTime,new Callback<List<FileUpload>>() {
            @Override
            public void success(List<FileUpload> fileUploads, Response response) {
                System.out.println("Url123:::::::::"+fileUploads);
                DocumentAdapter adapter = new DocumentAdapter(getActivity(),fileUploads,type);

                uploadFiles = fileUploads;
                list.setAdapter(adapter);
                progress.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
