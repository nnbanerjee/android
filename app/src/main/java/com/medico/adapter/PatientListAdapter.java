package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.PatientProfileList;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.PatientDetailsFragment;
import com.medico.view.profile.PatientVisitDatesView;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    PatientProfileList allPatients;
//    MyApi api;
//    String doctorId;
//    SharedPreferences session;
    private ProgressDialog progress;

    public PatientListAdapter(Activity activity, PatientProfileList allPatients)
    {
        super(activity);
        this.activity = activity;
        this.allPatients = allPatients;
    }

    @Override
    public int getCount() {
        return allPatients.getPatientlist().size();
    }

    @Override
    public Object getItem(int position) {
        return allPatients.getPatientlist().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
//        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(activity.getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
//        doctorId = session.getString("id", null);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_list_item, null);
//        global = (Global) activity.getApplicationContext();
        TextView doctorName = (TextView) convertView.findViewById(R.id.clinic_name);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.clinicSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.clinic_image);

        TextView address = (TextView) convertView.findViewById(R.id.lastAppointmentDate);
        TextView appointmentDate = (TextView) convertView.findViewById(R.id.review_value);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        TextView totalAppointment = (TextView) convertView.findViewById(R.id.total_appointment);
        totalAppointment.setVisibility(View.GONE);

        new ImageLoadTask(activity.getString(R.string.image_base_url) + allPatients.getPatientlist().get(position).getImageUrl(), viewImage).execute();

        viewImage.setBackgroundResource(R.drawable.patient);
        totalCount.setText("" + allPatients.getPatientlist().get(position).getNumberOfVisits());
       /* if (allPatients.get(position).getAddress() == null || allPatients.get(position).getAddress().equals("")) {
            lastVisitedValue.setText("None");

        } else {
            lastVisitedValue.setText(allPatients.get(position).getLastVisit());

        }*/

        if (allPatients.getPatientlist().get(position).getAddress() != null) {
            if (allPatients.getPatientlist().get(position).getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(allPatients.getPatientlist().get(position).getAddress());

            }
        }

        if (allPatients.getPatientlist().get(position).getUpcomingVisit() != null) {
            if (allPatients.getPatientlist().get(position).getUpcomingVisit().equals("")) {
                appointmentDate.setText("None");

            } else {
                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
                appointmentDate.setText(format.format(new Date(allPatients.getPatientlist().get(position).getUpcomingVisit())));

            }
        }
        if (allPatients.getPatientlist().get(position).getLastVisit() == null || allPatients.getPatientlist().get(position).getLastVisit().equals("")) {
            lastAppointment.setText("None");

        } else {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            lastAppointment.setText(format.format(new Date(allPatients.getPatientlist().get(position).getLastVisit())));
        }
        doctorName.setText(allPatients.getPatientlist().get(position).getName());
        doctorSpeciality.setText(allPatients.getPatientlist().get(position).getProfession());



        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParentActivity parentactivity = (ParentActivity)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.PATIENT_ID, allPatients.getPatientlist().get(position).getPatientId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new PatientDetailsFragment();
                parentactivity.attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
//
//                progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
//                api.getProfile1(new ProfileId(allPatients.getPatientlist().get(position).getPatientId()), new Callback<Person>() {
//                    @Override
//                    public void success(Person patient, Response response)
//                    {
//
//                        Bundle args = new Bundle();
//                        //Store Selected Patient profile
//                        progress.dismiss();
//                        SharedPreferences.Editor editor = session.edit();
////                        global.setSelectedPatientsProfile(patient);
//                        Gson gson = new Gson();
//                        String json = gson.toJson(patient);
//                        editor.putString("SelectedPatient", json);
//                        editor.commit();
//                        editor.putString("patient_Last_Visited", allPatients.getPatientlist().get(position).getLastVisit().toString());
//                        editor.putString("patient_Upcoming_Appt", allPatients.getPatientlist().get(position).getUpcomingVisit().toString());
//                        editor.putString("patient_Total_visits", allPatients.getPatientlist().get(position).getNumberOfVisits().toString());
//                        editor.putString("patientId", allPatients.getPatientlist().get(position).getPatientId().toString());
//                        editor.putString("patient_Name", allPatients.getPatientlist().get(position).getName());
//                        editor.commit();

//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        progress.dismiss();
//                        error.printStackTrace();
//                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // patientId = session.getString("patientId", null);
//                SharedPreferences.Editor editor = session.edit();
//                editor.putString("doctorId", allPatients.get(position).getDoctorId());
//                editor.putString("patientId", allPatients.getPatientlist().get(position).getPatientId().toString());
//                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment", "doctorPatientListAdapter");
                ParentFragment fragment = new PatientVisitDatesView();
                fragment.setArguments(bun);
                ((ParentActivity)activity).attachFragment(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }


}
