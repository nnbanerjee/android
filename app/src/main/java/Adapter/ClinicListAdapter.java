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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.AppointmentsPatient;
import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentBook;
import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentBookFragment;
import com.mindnerves.meidcaldiary.Fragments.ClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorDetailsFragment;
import com.mindnerves.meidcaldiary.Fragments.FeedbackFragment;
import com.mindnerves.meidcaldiary.Fragments.PatientAppointmentInformation;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.Clinic;
import Model.ClinicDetailVm;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<Clinic> clinicList;

    public ClinicListAdapter(Activity activity, List<Clinic> clinicList) {
        this.activity = activity;
        this.clinicList = clinicList;
    }
    @Override
    public int getCount() {
        return clinicList.size();
    }

    @Override
    public Object getItem(int position) {
        return clinicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_list_item, null);

        TextView clinicName = (TextView) convertView.findViewById(R.id.doctorName);
        ImageView doctorImg = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView clinicsSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        final TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        final TextView lastAppointment = (TextView)convertView.findViewById(R.id.lastAppointment);
        TextView totalAppointment = (TextView)convertView.findViewById(R.id.total_appointment);
        TextView lastAppointmentValue = (TextView)convertView.findViewById(R.id.lastAppointmentValue);
        ImageView downImage = (ImageView)convertView.findViewById(R.id.downImg);
        doctorImg.setBackgroundResource(R.drawable.clinic);
        clinicName.setText(clinicList.get(position).getClinicName());
        clinicsSpeciality.setText(clinicList.get(position).getClinicName());

        if(clinicList.get(position).equals("0")){
            totalAppointment.setText("0");
        }else{
            totalAppointment.setText(clinicList.get(position).totalAppointmentCount);
        }
        if(clinicList.get(position).lastVisited == null){
            lastVisitedValue.setText("Not Visited ");
        }else{
            lastVisitedValue.setText(clinicList.get(position).lastVisited);
        }
        if(clinicList.get(position).appointmentDate.equals("")){
            lastAppointmentValue.setText("None");
        }else{
            lastAppointmentValue.setText(clinicList.get(position).appointmentDate+"  "+clinicList.get(position).appointmentTime);
        }
        if(!clinicList.get(position).getBookDate().equalsIgnoreCase("")){
            appointmentDate.setText(clinicList.get(position).getBookDate() + " " +clinicList.get(position).getBookTime());
        }else{
            appointmentDate.setText("None");
        }
        lastAppointmentValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(clinicList.get(position).star == null) {
                   Bundle bundle = new Bundle();
                   SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = session.edit();
                   System.out.println("Clinic ID:::::::::::" + clinicList.get(position).getIdClinic());
                   editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                   editor.putString("patient_appointment_date", clinicList.get(position).appointmentDate);
                   editor.putString("patient_appointment_time", clinicList.get(position).appointmentTime);
                   editor.putString("patient_doctorId", clinicList.get(position).getDoctorId());
                   editor.commit();
                   bundle.putString("fragment", "clinicListAdapter");
                   Fragment fragment = new FeedbackFragment();
                   fragment.setArguments(bundle);
                   FragmentManager fragmentManger = activity.getFragmentManager();
                   fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
               }
            }
        });
        lastVisitedValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                Global global = (Global) activity.getApplicationContext();
                global.setAppointmentDate(clinicList.get(position).lastVisited);
                global.setAppointmentTime(clinicList.get(position).lastVisitedTime);
                editor.putString("doctor_patient_appointmentDate",clinicList.get(position).lastVisited);
                editor.putString("doctor_patient_appointmentTime",clinicList.get(position).lastVisitedTime);
                editor.putString("patient_DoctorEmail", clinicList.get(position).doctorEmail);
                editor.putString("clinicDoctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","clinicListAdapter");
                Fragment fragment = new PatientAppointmentInformation();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patient Consultations").addToBackStack(null).commit();
            }
        });
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                System.out.println("Clinic ID:::::::::::"+clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("doctor_patientEmail",clinicList.get(position).doctorEmail);
                editor.commit();

                Fragment fragment = new ClinicFragment();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        totalAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_DoctorEmail",clinicList.get(position).doctorEmail);
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.commit();
                Fragment fragment = new AppointmentsPatient();
                bundle.putString("fragment","clinicListAdapter");
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        clinicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_DoctorEmail",clinicList.get(position).doctorEmail);
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.commit();
                Fragment fragment = new AppointmentsPatient();
                bundle.putString("fragment","clinicListAdapter");
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appointDate = appointmentDate.getText().toString();
                if(!appointDate.equals("None")) {
                    MyApi api;
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(activity.getString(R.string.base_url))
                            .setClient(new OkClient())
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .build();
                    api = restAdapter.create(MyApi.class);
                    api.getAlldoctorsOfClinicDetail(clinicList.get(position).getIdClinic(), new Callback<List<ClinicDetailVm>>() {
                        @Override
                        public void success(List<ClinicDetailVm> clinicDetailVms, Response response) {
                            Global global = (Global) activity.getApplicationContext();
                            global.setAllDoctorClinicList(clinicDetailVms);

                            Bundle bun = new Bundle();
                            bun.putString("clinicId",clinicList.get(position).getIdClinic());
                            bun.putString("clinicDoctorId",clinicList.get(position).getDoctorId());
                            bun.putString("appointmentTime",clinicList.get(position).getBookTime());
                            bun.putString("appointmentDate", clinicList.get(position).getBookDate());
                            bun.putString("clinicShift", clinicList.get(position).getShift());
                            bun.putString("fragment","clinicListAdapter");
                            Fragment fragment = new ClinicAppointmentBook();
                            fragment.setArguments(bun);
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
            }
        });



        return convertView;

    }
}
