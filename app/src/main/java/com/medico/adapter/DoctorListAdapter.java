package com.medico.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.model.DoctorProfileList;
import com.medico.util.ImageLoadTask;
import com.medico.util.PARAM;
import com.medico.view.DoctorDetailsFragment;
import com.medico.view.ManagePatientProfile;
import com.medico.view.ParentFragment;
import com.medico.view.PatientVisitDatesView;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Utils.UtilSingleInstance;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorListAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    DoctorProfileList allDoctors;
    Global global;
    MyApi api;
    String doctorId;
    SharedPreferences session;
    private ProgressDialog progress;

    public DoctorListAdapter(Activity activity, DoctorProfileList allDoctors) {
        this.activity = activity;
        this.allDoctors = allDoctors;
    }

    @Override
    public int getCount() {

        if(allDoctors.getDoctorList()==null)
            return 0;
        else
            return allDoctors.getDoctorList().size();
    }

    @Override
    public Object getItem(int position) {
        return allDoctors.getDoctorList().get(position);
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
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        doctorId = session.getString("id", null);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_list_item, null);
//        global = (Global) activity.getApplicationContext();
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctorImg);

        TextView address = (TextView) convertView.findViewById(R.id.lastAppointmentDate);
        TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);
        TextView totalAppointment = (TextView) convertView.findViewById(R.id.total_appointment);
        totalAppointment.setVisibility(View.GONE);

        new ImageLoadTask(activity.getString(R.string.image_base_url) + allDoctors.getDoctorList().get(position).getImageUrl(), viewImage).execute();

        viewImage.setBackgroundResource(R.drawable.patient);
        totalCount.setText("" + allDoctors.getDoctorList().get(position).getNumberOfVisits());
       /* if (allDoctors.get(position).getAddress() == null || allDoctors.get(position).getAddress().equals("")) {
            lastVisitedValue.setText("None");

        } else {
            lastVisitedValue.setText(allDoctors.get(position).getLastVisit());

        }*/

        if (allDoctors.getDoctorList().get(position).getAddress() != null) {
            if (allDoctors.getDoctorList().get(position).getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(allDoctors.getDoctorList().get(position).getAddress());

            }
        }

        if (allDoctors.getDoctorList().get(position).getUpcomingVisit() != null) {
            if (allDoctors.getDoctorList().get(position).getUpcomingVisit().equals("")) {
                appointmentDate.setText("None");

            } else {
                appointmentDate.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(allDoctors.getDoctorList().get(position).getUpcomingVisit()));

            }
        }
        if (allDoctors.getDoctorList().get(position).getLastVisit() == null || allDoctors.getDoctorList().get(position).getLastVisit().equals("")) {
            lastAppointment.setText("None");

        } else {
            lastAppointment.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(allDoctors.getDoctorList().get(position).getLastVisit()));
        }
        doctorName.setText(allDoctors.getDoctorList().get(position).getName());
        doctorSpeciality.setText(allDoctors.getDoctorList().get(position).getProfession());



        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
                ManagePatientProfile parentactivity = (ManagePatientProfile)activity;
                Bundle bundle = parentactivity.getIntent().getExtras();
                bundle.putInt(PARAM.DOCTOR_ID, allDoctors.getDoctorList().get(position).getDoctorId());
                parentactivity.getIntent().putExtras(bundle);
                ParentFragment fragment = new DoctorDetailsFragment();
                parentactivity.fragmentList.add(fragment);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();


//                api.getProfile(new ProfileId(allDoctors.getDoctorList().get(position).getDoctorId()), new Callback<AllPatients>() {
//                    @Override
//                    public void success(AllPatients patient, Response response) {
//                        //global.setClinicDetailVm(patient);
//
//                        //Store Selected Patient profile
//                        progress.dismiss();
//                        SharedPreferences.Editor editor = session.edit();
//                        global.setSelectedPatientsProfile(patient);
//                        Gson gson = new Gson();
//                        String json = gson.toJson(patient);
//                        editor.putString("SelectedPatient", json);
//                        editor.commit();
//
//                        Bundle args = new Bundle();
//
//
//                        editor.putString("patient_Last_Visited", allDoctors.getDoctorList().get(position).getLastVisit().toString());
//                        editor.putString("patient_Upcoming_Appt", allDoctors.getDoctorList().get(position).getUpcomingVisit().toString());
//                        editor.putString("patient_Total_visits", allDoctors.getDoctorList().get(position).getNumberOfVisits().toString());
//
//
//                        editor.putString("patientId", allDoctors.getDoctorList().get(position).getDoctorId().toString());
////                        editor.putString("doctor_patientEmail", allDoctors.get(position).get);
////                        editor.putString("doctorId", allDoctors.get(position).getDoctorId());
//                        editor.putString("patient_Name", allDoctors.getDoctorList().get(position).getName());
////                        editor.putString("doctor_patientEmail", allDoctors.get(position).getEmail());
////                        editor.putString("" +
////                                "", allDoctors.get(position).getEmail());
//                        editor.commit();
//                        Fragment fragment = new PatientDetailsFragment();
//                        FragmentManager fragmentManger = activity.getFragmentManager();
//                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        progress.dismiss();
//                        error.printStackTrace();
//                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // patientId = session.getString("patientId", null);
                SharedPreferences.Editor editor = session.edit();
//                editor.putString("doctorId", allDoctors.get(position).getDoctorId());
                editor.putString("patientId", allDoctors.getDoctorList().get(position).getDoctorId().toString());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment", "doctorPatientListAdapter");
                Fragment fragment = new PatientVisitDatesView();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }


}
