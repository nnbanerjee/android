package com.mindnerves.meidcaldiary.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import Adapter.DocumentAdapter;

import Adapter.StickyDocumentAdapter;
import Application.MyApi;
import Model.DoctorNotesVM;
import Model.FileUpload;
import Model.ReminderVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentDocument extends Fragment{

    Button upload,back;
    private static final int FILE_SELECT_CODE = 0;
    View convertView;
    public MyApi api;
    String doctorId = "";
    String patientId = "";
    String appointmentDate = "";
    String appointmentTime = "";
    SharedPreferences session;
    Global global;
    ListView allDocumentDoctor;
    List<FileUpload> uploadFiles;
    ProgressDialog progress;
    String type = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_document, container,false);
        upload = (Button)view.findViewById(R.id.upload);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        allDocumentDoctor = (ListView)view.findViewById(R.id.list_documents);
        back = (Button)getActivity().findViewById(R.id.back_button);
        global = (Global) getActivity().getApplicationContext();
        convertView = view;
        global = (Global) getActivity().getApplicationContext();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploadDialog adf = FileUploadDialog.newInstance();
                adf.show(getFragmentManager(),"Dialog");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });
        doctorId = session.getString("sessionID",null);
        type = session.getString("type",null);
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

        allDocumentDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileUpload fileUpload = uploadFiles.get(position);
                System.out.println("Url:::::::::"+fileUpload.getUrl());
                if(fileUpload.getUrl() != null) {
                    global.setUpload(fileUpload);
                    ShowPreviewDialog adf = ShowPreviewDialog.newInstance();
                    adf.show(getFragmentManager(), "Dialog");
                }

            }
        });
        return view;
    }



    public void loadListData()
    {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        doctorId = session.getString("sessionID",null);
        patientId = session.getString("doctor_patientEmail", null);
        appointmentDate = global.getAppointmentDate();
        appointmentTime = global.getAppointmentTime();

        System.out.println("Url 123:::::::::"+doctorId);
        api.getFiles(doctorId,patientId,appointmentDate,appointmentTime,new Callback<List<FileUpload>>() {
            @Override
            public void success(List<FileUpload> fileUploads, Response response) {

                System.out.println("Url123:::::::::"+fileUploads.size());

                DocumentAdapter adapter = new DocumentAdapter(getActivity(),fileUploads,type);
                uploadFiles = fileUploads;
                System.out.println("Adapter size= "+adapter.getCount());
                allDocumentDoctor.setAdapter(adapter);
                progress.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

    }
    public void goToBack(){
        Fragment fragment;
        fragment = new AllDoctorPatientAppointment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }
}
