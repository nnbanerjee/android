package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.AllDoctorPatientAppointment;
import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentInformation;
import com.mindnerves.meidcaldiary.Fragments.FeedbackFragment;
import com.mindnerves.meidcaldiary.Fragments.PatientDetailsFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.AllPatients;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class DoctorPatientListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<AllPatients> allPatients;
    Global global;
    MyApi api;
    String doctorId;
    SharedPreferences session;

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
        doctorId = session.getString("sessionID", null);
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_list_item, null);
        global = (Global) activity.getApplicationContext();
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        ImageView downImage = (ImageView) convertView.findViewById(R.id.downImg);
        final TextView lastAppointment = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        TextView totalCount = (TextView) convertView.findViewById(R.id.totalCount);
        Button rightButton = (Button) convertView.findViewById(R.id.nextBtn);
        TextView totalAppointment = (TextView) convertView.findViewById(R.id.total_appointment);
        totalAppointment.setVisibility(View.GONE);

        viewImage.setBackgroundResource(R.drawable.patient);
        totalCount.setText("" + allPatients.get(position).getTotalAppointment());
        if (allPatients.get(position).getAddress() == null || allPatients.get(position).getAddress().equals("")) {
            lastVisitedValue.setText("None");

        } else {
            lastVisitedValue.setText(allPatients.get(position).getAddress());

        }
        if (allPatients.get(position).getBookDate() != null) {
            if (allPatients.get(position).getBookDate().equals("")) {
                appointmentDate.setText("None");

            } else {
                appointmentDate.setText(allPatients.get(position).getBookDate() + " " + allPatients.get(position).getBookTime());

            }
        }
        if (allPatients.get(position).getLastAppointmentDate() == null || allPatients.get(position).getAppointmentDate().equals("")) {
            lastAppointment.setText("None");

        } else {
            lastAppointment.setText(allPatients.get(position).getAppointmentDate() + " " + allPatients.get(position).getAppointmentTime());
        }
        doctorName.setText(allPatients.get(position).getName());
        doctorSpeciality.setText(allPatients.get(position).getSpeciality());

        lastVisitedValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = lastVisitedValue.getText().toString();
                if (!value.equals("None")) {
                    global.setAppointmentDate(allPatients.get(position).getLastAppointmentDate());
                    global.setAppointmentTime(allPatients.get(position).getLastVisitedTime());
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("patient_doctor_email", doctorId);
                    editor.putString("clinicId", allPatients.get(position).lastVisitedClinicId);
                    editor.putString("doctorId", "" + allPatients.get(position).getDoctorId());
                    editor.commit();
                    Fragment fragment = new DoctorAppointmentInformation();
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });

        lastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = lastAppointment.getText().toString();
                if (!value.equals("None")) {
                    if (allPatients.get(position).getStar() == null) {
                        global.setAppointmentDate(allPatients.get(position).getAppointmentDate());
                        global.setAppointmentTime(allPatients.get(position).getAppointmentTime());
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("patient_appointment_date", allPatients.get(position).getAppointmentDate());
                        editor.putString("patient_appointment_time", allPatients.get(position).getAppointmentTime());
                        editor.putString("patient_DoctorEmail", doctorId);
                        editor.putString("clinicId", allPatients.get(position).appointmentClinicId);
                        editor.putString("patient_doctorId", allPatients.get(position).getDoctorId());
                        editor.putString("doctorId", "" + allPatients.get(position).getDoctorId());
                        editor.putString("patient_clinicId", "" + allPatients.get(position).appointmentClinicId);
                        editor.putString("doctor_patient_appointmentDate", allPatients.get(position).getAppointmentDate());
                        editor.putString("doctor_patient_appointmentTime", allPatients.get(position).getAppointmentTime());
                        editor.commit();
                        Fragment fragment = new FeedbackFragment();
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }

                }
            }
        });

        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patientId", allPatients.get(position).getId());
                System.out.println("doctor_patientEmail = " + allPatients.get(position).getEmail());
                editor.putString("doctor_patientEmail", allPatients.get(position).getEmail());
                editor.putString("doctorId", allPatients.get(position).getName());
                editor.putString("patient_Name", allPatients.get(position).getName());
                editor.putString("doctor_patientEmail", allPatients.get(position).getEmail());
                editor.putString("" +
                        "", allPatients.get(position).getEmail());
                editor.commit();
                Fragment fragment = new PatientDetailsFragment();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.getAllDoctorPatientClinics(doctorId, "1", new Callback<List<ClinicDetailVm>>() {
                    @Override
                    public void success(List<ClinicDetailVm> clinicDetailVms, Response response) {
                        global.setClinicDetailVm(clinicDetailVms);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("Doctor_patientEmail", allPatients.get(position).getEmail());
                        editor.commit();
                        Bundle args = new Bundle();
                        args.putString("clinicId", "" + allPatients.get(position).getClinicId());
                        args.putString("clinicShift", allPatients.get(position).getShift());
                        args.putString("appointmentTime", allPatients.get(position).getBookTime());
                        args.putString("appointmentDate", allPatients.get(position).getBookDate());
                        args.putString("fragment", "DoctorPatientAdapter");
                        Fragment fragment = new ClinicAppointmentFragment();
                        fragment.setArguments(args);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_doctor_email", doctorId);
                editor.putString("doctorId", allPatients.get(position).getDoctorId());
                editor.putString("patientEmail", allPatients.get(position).getEmail());
                editor.putString("patient_Name", allPatients.get(position).getName());
                editor.putString("doctor_patientEmail", allPatients.get(position).getEmail());
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
