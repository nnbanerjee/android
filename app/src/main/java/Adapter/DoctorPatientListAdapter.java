package Adapter;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Fragments.AllDoctorPatientAppointment;
import com.mindnerves.meidcaldiary.Fragments.PatientDetailsFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.AllPatients;
import Model.profileId;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorPatientListAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<AllPatients> allPatients;
    Global global;
    MyApi api;
    String doctorId;
    SharedPreferences session;
    private ProgressDialog progress;

    public DoctorPatientListAdapter(Activity activity, List<AllPatients> allPatients) {
        this.activity = activity;
        this.allPatients = allPatients;
    }

    @Override
    public int getCount() {
        return allPatients.size();
    }

    @Override
    public Object getItem(int position) {
        return allPatients.get(position);
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
        global = (Global) activity.getApplicationContext();
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
        System.out.println("picture Url is---->" + activity.getString(R.string.image_base_url) + allPatients.get(position).getImageUrl());
        new ImageLoadTask(activity.getString(R.string.image_base_url) + allPatients.get(position).getImageUrl(), viewImage).execute();

        viewImage.setBackgroundResource(R.drawable.patient);
        totalCount.setText("" + allPatients.get(position).getNumberOfVisits());
       /* if (allPatients.get(position).getAddress() == null || allPatients.get(position).getAddress().equals("")) {
            lastVisitedValue.setText("None");

        } else {
            lastVisitedValue.setText(allPatients.get(position).getLastVisit());

        }*/

        if (allPatients.get(position).getAddress() != null) {
            if (allPatients.get(position).getAddress().equals("")) {
                address.setText("None");

            } else {
                address.setText(allPatients.get(position).getAddress());

            }
        }

        if (allPatients.get(position).getUpcomingVisit() != null) {
            if (allPatients.get(position).getUpcomingVisit().equals("")) {
                appointmentDate.setText("None");

            } else {
                appointmentDate.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(allPatients.get(position).getUpcomingVisit()));

            }
        }
        if (allPatients.get(position).getLastVisit() == null || allPatients.get(position).getLastVisit().equals("")) {
            lastAppointment.setText("None");

        } else {
            lastAppointment.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(allPatients.get(position).getLastVisit()));
        }
        doctorName.setText(allPatients.get(position).getName());
        doctorSpeciality.setText(allPatients.get(position).getProfession());



        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.loading_wait));
                api.getProfile(new profileId(allPatients.get(position).getpatientId()), new Callback<AllPatients>() {
                    @Override
                    public void success(AllPatients patient, Response response) {
                        //global.setClinicDetailVm(patient);

                        //Store Selected Patient profile
                        progress.dismiss();
                        SharedPreferences.Editor editor = session.edit();
                        global.setSelectedPatientsProfile(patient);
                        Gson gson = new Gson();
                        String json = gson.toJson(patient);
                        editor.putString("SelectedPatient", json);
                        editor.commit();

                        Bundle args = new Bundle();


                        editor.putString("patient_Last_Visited", allPatients.get(position).getLastVisit());
                        editor.putString("patient_Upcoming_Appt", allPatients.get(position).getUpcomingVisit());
                        editor.putString("patient_Total_visits", allPatients.get(position).getNumberOfVisits());


                        editor.putString("patientId", allPatients.get(position).getpatientId());
                        System.out.println("doctor_patientEmail = " + allPatients.get(position).getEmail());
                        editor.putString("doctor_patientEmail", allPatients.get(position).getEmail());
                        editor.putString("doctorId", allPatients.get(position).getDoctorId());
                        editor.putString("patient_Name", allPatients.get(position).getName());
                        editor.putString("doctor_patientEmail", allPatients.get(position).getEmail());
                        editor.putString("" +
                                "", allPatients.get(position).getEmail());
                        editor.commit();
                        Fragment fragment = new PatientDetailsFragment();
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                        error.printStackTrace();
                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // patientId = session.getString("patientId", null);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("doctorId", allPatients.get(position).getDoctorId());
                editor.putString("patientId", allPatients.get(position).getpatientId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment", "doctorPatientListAdapter");
                Fragment fragment = new AllDoctorPatientAppointment();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }


}
