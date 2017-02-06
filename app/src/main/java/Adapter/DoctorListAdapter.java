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

import com.mindnerves.meidcaldiary.Fragments.DoctorDetailsFragment;
import com.mindnerves.meidcaldiary.Fragments.FeedbackFragment;
import com.mindnerves.meidcaldiary.Fragments.PatientAllAppointment;
import com.mindnerves.meidcaldiary.Fragments.PatientAppointmentInformation;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.DoctorSearchResponse;


/**
 * Created by MNT on 23-Feb-15.
 */

//Patient Login
public class DoctorListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorSearchResponse>  doctorSearchResponses;

    public DoctorListAdapter(Activity activity, List<DoctorSearchResponse>  doctorSearchResponses) {
        this.activity = activity;
        this.doctorSearchResponses = doctorSearchResponses;
    }
    @Override
    public int getCount() {
        return doctorSearchResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorSearchResponses.get(position);
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

        TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        final TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        final TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);
        TextView totalAppointment = (TextView)convertView.findViewById(R.id.total_appointment);
        final TextView lastAppointmentValue = (TextView) convertView.findViewById(R.id.lastAppointmentValue);
        ImageView downImage = (ImageView)convertView.findViewById(R.id.downImg);
        Button nextBtn = (Button)convertView.findViewById(R.id.nextBtn);
        totalAppointment.setVisibility(View.GONE);

        if(doctorSearchResponses.get(position).lastVisited == null){
            lastVisitedValue.setText("Not Visited ");
        }else{
            lastVisitedValue.setText(doctorSearchResponses.get(position).lastVisited);
        }
        System.out.println("Book Date::::::::::"+doctorSearchResponses.get(position).getBookDate());

        if(doctorSearchResponses.get(position).getBookDate() != null){
            if(doctorSearchResponses.get(position).getBookDate().equals("")){
                appointmentDate.setText("None");
            }
            else {
                appointmentDate.setText(doctorSearchResponses.get(position).getBookDate() + " " + doctorSearchResponses.get(position).getBookTime());
            }
        }
        else{
            appointmentDate.setText("None");
        }

        if(!doctorSearchResponses.get(position).getPreviousDate().equals("")){
            lastAppointmentValue.setText(doctorSearchResponses.get(position).getPreviousDate()+" "+doctorSearchResponses.get(position).previousAppointment);
        }else{
            lastAppointmentValue.setText("None");
        }
        doctorName.setText(doctorSearchResponses.get(position).getName());
        doctorSpeciality.setText(doctorSearchResponses.get(position).getSpeciality());
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_DoctorName", doctorSearchResponses.get(position).getName());
                editor.putString("patient_DoctorId", doctorSearchResponses.get(position).getDoctorId());
                editor.putString("patient_DoctorEmail", doctorSearchResponses.get(position).getEmailID());
                editor.putString("doctorId", doctorSearchResponses.get(position).getDoctorId());
                editor.commit();
                Bundle args = new Bundle();
                args.putString("doctor_email", doctorSearchResponses.get(position).getEmailID());
                args.putString("fragment","from_adapter");
                Fragment fragment = new DoctorDetailsFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","doctor_list");
                Fragment fragment = new PatientAllAppointment();// DoctorProfileDetails();
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_doctor_email", doctorSearchResponses.get(position).getEmailID());
                editor.putString("patient_doctorId", doctorSearchResponses.get(position).getDoctorId());
                editor.putString("doctorId", doctorSearchResponses.get(position).getDoctorId());
                editor.commit();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        lastAppointmentValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastAppointment = lastAppointmentValue.getText().toString();
                if(!lastAppointment.equalsIgnoreCase("none")) {
                    SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("doctor_patient_appointmentDate", doctorSearchResponses.get(position).previousDate);
                    editor.putString("doctor_patient_appointmentTime", doctorSearchResponses.get(position).previousAppointment);
                    editor.putString("patient_DoctorEmail", doctorSearchResponses.get(position).getEmailID());
                    editor.putString("clinicDoctorId", doctorSearchResponses.get(position).getDoctorId());
                    editor.putString("patient_doctorId", doctorSearchResponses.get(position).getDoctorId());
                    editor.putString("patient_clinicId", "" + doctorSearchResponses.get(position).previousClinicId);
                    editor.putString("patient_appointment_date", doctorSearchResponses.get(position).getPreviousDate());
                    editor.putString("patient_appointment_time", doctorSearchResponses.get(position).getPreviousAppointment());
                    editor.commit();
                    Fragment fragment = new FeedbackFragment();
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
            }
        });
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appointmentDateString = appointmentDate.getText().toString();
                if (!appointmentDateString.equalsIgnoreCase("none")) {
                    SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("patient_DoctorName", doctorSearchResponses.get(position).getName());
                    editor.putString("patient_DoctorId", doctorSearchResponses.get(position).getDoctorId());
                    editor.putString("patient_DoctorEmail", doctorSearchResponses.get(position).getEmailID());
                    editor.commit();
                    System.out.println("Condition Shift::::::"+doctorSearchResponses.get(position).getShift() != null);
                    if (doctorSearchResponses.get(position).getShift() != null) {
                        Bundle args = new Bundle();
                        args.putString("doctor_email", doctorSearchResponses.get(position).getEmailID());
                        args.putString("fragment", "appointmentDate");
                        Fragment fragment = new DoctorDetailsFragment();
                        fragment.setArguments(args);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }
            }
        });
        lastVisitedValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastVisitedString = lastVisitedValue.getText().toString();
                if(!lastVisitedString.equalsIgnoreCase("none")){
                    Global global = (Global) activity.getApplicationContext();
                    SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    global.setAppointmentDate(doctorSearchResponses.get(position).lastVisited);
                    global.setAppointmentTime(doctorSearchResponses.get(position).lastVisitedTime);
                    editor.putString("doctor_patient_appointmentDate", doctorSearchResponses.get(position).lastVisited);
                    editor.putString("doctor_patient_appointmentTime", doctorSearchResponses.get(position).lastVisitedTime);
                    editor.putString("patient_DoctorEmail", doctorSearchResponses.get(position).getEmailID());
                    editor.putString("clinicDoctorId", doctorSearchResponses.get(position).getDoctorId());
                    editor.commit();
                    Bundle bun = new Bundle();
                    bun.putString("fragment","doctorList");
                    Fragment fragment = new PatientAppointmentInformation();
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = activity.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patient Consultations").addToBackStack(null).commit();
                }
            }
        });
        return convertView;

    }
}
