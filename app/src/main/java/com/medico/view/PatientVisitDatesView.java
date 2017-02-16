package com.medico.view;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.adapter.PatientVisitDatesAdapter;
import com.medico.model.DoctorIdPatientId;
import com.medico.model.PatientVisits;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Utils.PARAM;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientVisitDatesView extends ParentFragment
{

    String doctor_email, patientId;
    public SharedPreferences session;
    ProgressDialog progress;
    StickyListHeadersListView allAppointments;
//    TextView show_global_tv;
    String doctorId;
    Global global;
    PatientVisits appointments;
    ImageView addClinic;
    String fragmentCall;
//Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.all_patient_appointment, container, false);
//        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final Bundle bun = getArguments();
        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
//        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
//        global = (Global) getActivity().getApplicationContext();
        // doctor_email = session.getString("patient_doctor_email", null);
        // doctorId = session.getString("doctorId", "");
        // patientId = session.getString("patientEmail", "");
//        show_global_tv.setText("Visit Dates");
        addClinic = (ImageView) view.findViewById(R.id.add_clinic_appointment);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Visit Dates");
//        final Button back = (Button) getActivity().findViewById(R.id.back_button);
//        back.setVisibility(View.VISIBLE);

//        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
//        toolbar.setVisibility(View.VISIBLE);
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.new_visit);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.content_frame);
//                if (f instanceof AllDoctorPatientAppointment){
//                    SharedPreferences.Editor editor = session.edit();
//
//                    editor.putString("appointmentId", "");
//                    editor.putString("patientId", appointments.getPatientVisits().getPatientId());
//                    editor.putString("Summary", "NewSummary");
//
//                    editor.commit();
//                    //appointments
//                    //Fragment fragment = new DoctorAppointmentSummary();
//                    Fragment fragment = new DoctorAppointmentInformation();
//                    FragmentManager fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                }
//
//                return true;
//            }
//        });

//        patientId = session.getString("patientId", "");
//        System.out.println("PatientID from preferances-->" + patientId);
//        doctorId = session.getString("id", "");
//        System.out.println("doctorId from preferances-->" + doctorId);


//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBack();
//            }
//        });
//        if (bun != null) {
//            if (bun.getString("fragment") != null) {
//                fragmentCall = bun.getString("fragment");
//            }
//        }
        //Retrofit Initialization
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .setErrorHandler(new MedicoCustomErrorHandler(getActivity()))
//                .build();
//        api = restAdapter.create(MyApi.class);
        //onList item click item.
        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                SharedPreferences.Editor editor = session.edit();
//
//                editor.putString("appointmentId", appointments.getPatientVisits().getVisits().get(position).getAppointmentId());
//                editor.putString("patientId", appointments.getPatientVisits().getPatientId());
//                editor.putString("Summary", "OldSummary");
//                editor.commit();


                //appointments
                //Fragment fragment = new DoctorAppointmentSummary();
                Bundle bun = getActivity().getIntent().getExtras();
                PatientVisits visits = (PatientVisits)parent.getAdapter().getItem(position);
                bun.putInt(PARAM.APPOINTMENT_ID, visits.getAppointmentId());
                bun.putLong(PARAM.APPOINTMENT_DATETIME, visits.getDateTime());
                bun.putString(PARAM.REFERRED_BY, visits.getReferredBy());
                bun.putString(PARAM.CLINIC_NAME, visits.getClinicName());
                getActivity().getIntent().putExtras(bun);
                ParentFragment fragment = new DoctorAppointmentInformation();
                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });

        //this button is hidden and moved to menu
        addClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = session.edit();
//
//                editor.putString("appointmentId", "");
//                editor.putString("patientId", appointments.getPatientVisits().getPatientId());
//
//                editor.commit();

                //appointments
                //Fragment fragment = new DoctorAppointmentSummary();
                ParentFragment fragment = new DoctorAppointmentInformation();
                ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();



              /*  Bundle bundle = new Bundle();
                bundle.putString("fragment", fragmentCall);
                Fragment fragment = new AddAppointment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
       */     }
        });
//        getAllPatientAppointment();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        inflater.inflate(R.menu.patient_visit_dates, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.ic_note_add_black_24dp);
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

//    public void goToBack() {
//        Fragment fragment;
//        Bundle bundle = getArguments();
//        String key = "";
//        if (bundle != null) {
//            if (bundle.getString("fragment") != null) {
//                key = bundle.getString("fragment");
//                if (key.equalsIgnoreCase("from main page")) {
//                    fragment = new PatientProfileListView();
//                    FragmentManager fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    final Button back = (Button) getActivity().findViewById(R.id.back_button);
//                    back.setVisibility(View.INVISIBLE);
//                } else if (key.equalsIgnoreCase("doctorPatientListAdapter")) {
//                    fragment = new PatientProfileListView();
//                    FragmentManager fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                } else {
//                    fragment = new PatientDetailsFragment();
//                    FragmentManager fragmentManger = getFragmentManager();
//                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    final Button back = (Button) getActivity().findViewById(R.id.back_button);
//                    back.setVisibility(View.INVISIBLE);
//                }
//            }
//        } else {
//            fragment = new PatientProfileListView();
//            FragmentManager fragmentManger = getFragmentManager();
//            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            final Button back = (Button) getActivity().findViewById(R.id.back_button);
//            back.setVisibility(View.INVISIBLE);
//            /*fragment = new PatientDetailsFragment();
//            FragmentManager fragmentManger = getFragmentManager();
//            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            final Button back = (Button) getActivity().findViewById(R.id.back_button);
//            back.setVisibility(View.INVISIBLE);*/
//        }
//    }


    @Override
    public void onStart()// getAllPatientAppointment()
     {
        super.onStart();
         Bundle bundle = getActivity().getIntent().getExtras();
        DoctorIdPatientId doc = new DoctorIdPatientId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)).toString(), new Integer(bundle.getInt(PARAM.PATIENT_ID)).toString());
        api.getPatientVisitDatesByDoctor1(doc, new Callback<List<PatientVisits>>()
        {
            @Override
            public void success(List<PatientVisits> visits, Response response) {
                String json = new String(((TypedByteArray) response.getBody()).getBytes());
                if (UtilSingleInstance.checkForServerErrorsInResponse(json).equalsIgnoreCase("")) {

//                    appointments = visitHistory;
                    //Doctor_AllPatientAppointmentAdapter adapter = new Doctor_AllPatientAppointmentAdapter(getActivity(), clinicAppointmentList);
                    if (visits != null) {
                        PatientVisitDatesAdapter adapter = new PatientVisitDatesAdapter(getActivity().getApplicationContext(), visits);
                        allAppointments.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "No Visits found!", Toast.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                } else {
//                    Toast.makeText(getActivity(), UtilSingleInstance.checkForServerErrorsInResponse(json), Toast.LENGTH_LONG).show();
                    progress.dismiss();

                }

            }
            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }

        });
     }
}
