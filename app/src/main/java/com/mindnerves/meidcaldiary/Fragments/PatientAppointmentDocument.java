package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Created by MNT on 07-Apr-15.
 */
public class PatientAppointmentDocument extends Fragment {

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
    Button back;
    List<FileUpload> uploadFiles;
    ProgressDialog progress;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_document, container, false);
        upload = (Button) view.findViewById(R.id.upload);
        list = (ListView) view.findViewById(R.id.list_documents);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        convertView = view;
        back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        global = (Global) getActivity().getApplicationContext();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportUpload adf = ReportUpload.newInstance();
                adf.show(getFragmentManager(), "Dialog");
            }
        });
        doctorId = session.getString("sessionID", null);
        type = session.getString("loginType",null);
        patientId = session.getString("doctor_patientEmail", null);
        appointmentDate = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        loadListData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileUpload fileUpload = uploadFiles.get(position);
                System.out.println("Url:::::::::" + fileUpload.getUrl());
                global.setUpload(fileUpload);
                ShowPreviewDialog1 adf = ShowPreviewDialog1.newInstance();
                adf.show(getFragmentManager(), "Dialog");
            }
        });
        return view;
    }


    public void loadListData() {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        doctorId = session.getString("clinicDoctorId", null);
        patientId = session.getString("sessionID", null);
        appointmentDate = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();
        System.out.println("Appointment Date:::::::" + appointmentDate);
        System.out.println("Appointment Time:::::::" + appointmentTime);
        System.out.println("DoctorId:::::::" + doctorId);
        api.getFilesPatient(doctorId, patientId, appointmentDate, appointmentTime, new Callback<List<FileUpload>>() {
            @Override
            public void success(List<FileUpload> fileUploads, Response response) {
                System.out.println("Url123:::::::::" + fileUploads);
                DocumentAdapter adapter = new DocumentAdapter(getActivity(), fileUploads,type);
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

    public void goBack(){
        Bundle bun = getArguments();
        if(bun != null) {
            if(bun.getString("fragment").equalsIgnoreCase("clinicAppointmentListAdapter")) {
                Fragment fragment = new PatientAppointmentStatus();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        }
    }

}
