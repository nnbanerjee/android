package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.DoctorClinicDetails;
import com.medico.util.ImageLoadTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class AppointmentClinicListAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorClinicDetails> clinicDetails;
    MyApi api;
    String doctorId;
    SharedPreferences session;
    private ProgressDialog progress;

    public AppointmentClinicListAdapter(Activity activity, List<DoctorClinicDetails> clinicDetails) {
        this.activity = activity;
        this.clinicDetails = clinicDetails;
    }

    @Override
    public int getCount() {
        return clinicDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicDetails.get(position);
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
            convertView = inflater.inflate(R.layout.appointment_clinic_list, null);
//        global = (Global) activity.getApplicationContext();
        TextView clinicName = (TextView) convertView.findViewById(R.id.clinic_name);
        TextView speciality = (TextView) convertView.findViewById(R.id.clinicSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.clinic_image);

        TextView address = (TextView) convertView.findViewById(R.id.address);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.down_arrow);
        TextView totalCount = (TextView) convertView.findViewById(R.id.total_count);
        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        TableLayout tableLayout = (TableLayout)convertView.findViewById(R.id.upcoming_appointment);
        TableRow dateRow = (TableRow)convertView.findViewById(R.id.date_row);
        TableRow appointRow = (TableRow)convertView.findViewById(R.id.nr_row);

        if(clinicDetails.get(position).clinic.imageUrl != null)
            new ImageLoadTask(clinicDetails.get(position).clinic.imageUrl, viewImage).execute();
//        totalCount.setText( clinicDetails.get(position).);
        address.setText(clinicDetails.get(position).clinic.address);
        clinicName.setText(clinicDetails.get(position).clinic.clinicName);
        speciality.setText(clinicDetails.get(position).clinic.speciality);
        if(clinicDetails.get(position).datecounts != null)
            totalCount.setText(new Integer(clinicDetails.get(position).datecounts.size()).toString());
        else
            totalCount.setText(new Integer(0).toString());
        setAppointmentDates(convertView, clinicDetails.get(position));


        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ManagePatientProfile parentactivity = (ManagePatientProfile)activity;
//                Bundle bundle = parentactivity.getIntent().getExtras();
//                bundle.putInt(PARAM.PATIENT_ID, clinicDetails.get(position).getPatientId());
//                parentactivity.getIntent().putExtras(bundle);
//                ParentFragment fragment = new PatientDetailsFragment();
//                parentactivity.fragmentList.add(fragment);
//                FragmentManager fragmentManger = activity.getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
//
//                progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
//                api.getProfile1(new ProfileId(clinicDetails.get(position).getPatientId()), new Callback<Person>() {
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
//                        editor.putString("patient_Last_Visited", clinicDetails.get(position).getLastVisit().toString());
//                        editor.putString("patient_Upcoming_Appt", clinicDetails.get(position).getUpcomingVisit().toString());
//                        editor.putString("patient_Total_visits", clinicDetails.get(position).getNumberOfVisits().toString());
//                        editor.putString("patientId", clinicDetails.get(position).getPatientId().toString());
//                        editor.putString("patient_Name", clinicDetails.get(position).getName());
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
////                editor.putString("doctorId", allPatients.get(position).getDoctorId());
//                editor.putString("patientId", clinicDetails.get(position).getPatientId().toString());
//                editor.commit();
//                Bundle bun = new Bundle();
//                bun.putString("fragment", "doctorPatientListAdapter");
//                Fragment fragment = new PatientVisitDatesView();
//                fragment.setArguments(bun);
//                FragmentManager fragmentManger = activity.getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }

    private void setAppointmentDates(View convertView, DoctorClinicDetails details)
    {
        TableLayout tableLayout = (TableLayout)convertView.findViewById(R.id.upcoming_appointment);
        tableLayout.setStretchAllColumns(true);
        TableRow dateRow = (TableRow)convertView.findViewById(R.id.date_row);
        TableRow appointRow = (TableRow)convertView.findViewById(R.id.nr_row);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        dateRow.setLayoutParams(lp);
        dateRow.removeAllViews();
        appointRow.removeAllViews();
        List<DoctorClinicDetails.AppointmentCounts> counts = details.datecounts;
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        int i = 0;
        for(DoctorClinicDetails.AppointmentCounts count:counts)
        {
            TextView dateView = new TextView(activity);
            TextView countView = new TextView(activity);
            dateView.setText(format.format(new Date(count.date)));
            dateView.setBackgroundResource(R.drawable.medicine_schedule);
            dateView.setLeft(10);
            dateView.setTop(10);
            dateView.setRight(10);
            dateView.setBottom(10);
            dateRow.addView(dateView,i,lp);
            countView.setText(new Integer(count.counts).toString());
            countView.setBackgroundResource(R.drawable.medicine_schedule);
            countView.setLeft(10);
            countView.setTop(10);
            countView.setRight(10);
            countView.setBottom(10);
            appointRow.addView(countView,i,lp);

        }
        tableLayout.requestLayout();
    }
}
