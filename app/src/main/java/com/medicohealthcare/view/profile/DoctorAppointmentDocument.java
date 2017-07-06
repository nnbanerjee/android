package com.medicohealthcare.view.profile;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.medicohealthcare.adapter.DocumentAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.FileUpload1;
import com.medicohealthcare.util.FileUploadDialog;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

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
        setHasOptionsMenu(true);
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
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//                    return true;
//                }
//                return false;
//            }
//        });
//        setTitle("Visit Details");
//    }
    @Override
    public boolean save()
    {
        Bundle args = getActivity().getIntent().getExtras();
        ParentFragment fragment = new PatientSummaryFileUpload();
//        ((ParentActivity)getActivity()).attachFragment(fragment);
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
    @Override
    public void onResume() {
        super.onResume();
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {   menu.clear();
        inflater.inflate(R.menu.patient_visist_summary, menu);
        MenuItem menuItem = menu.findItem(R.id.save_summary);
        menuItem.setIcon(R.drawable.ic_add_white_24dp);
        super.onCreateOptionsMenu(menu,inflater);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.save_summary:
            {
                update();
                if (isChanged()) {
                    if (canBeSaved())
                    {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            case R.id.add_invoice:
            {
                ((DoctorAppointmentInvoices)fragment).addInvoice();
                return true;
            }
            case R.id.add_payment:
            {
                ((DoctorAppointmentInvoices)fragment).addPayment();
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return false;
            }
            default:
            {
                return false;
            }

        }
    }
}
