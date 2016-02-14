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
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.PatientAppointmentSummary;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ClinicAppointment;


/**
 * Created by MNT on 23-Feb-15.
 */
//Check Login User
public class AllPatientAppointmentAdapter extends ArrayAdapter<ClinicAppointment> {

    private final List<ClinicAppointment> clinicAppointment;
    private final Activity context;
    public String shift = null;
    public SharedPreferences session;
    public Integer clinicId = null;


   public AllPatientAppointmentAdapter(Activity context, List<ClinicAppointment> clinicAppointment) {
        super(context, R.layout.all_patient_appo_item, clinicAppointment);
        this.context = context;
        this.clinicAppointment = clinicAppointment;
        session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
   }

    static class ViewHolder {
        private TextView date,time,visitType;
        private RelativeLayout layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.all_patient_appo_item, null);
            mHolder = new ViewHolder();

            mHolder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            mHolder.date = (TextView) convertView.findViewById(R.id.date);
            mHolder.time = (TextView) convertView.findViewById(R.id.time);

            mHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_doctor_appointmentDate", clinicAppointment.get(position).getAppointmentDate());
                editor.putString("patient_doctor_appointmentTime", clinicAppointment.get(position).getBookTime());
                editor.commit();

                Fragment fragment = new PatientAppointmentSummary();
                FragmentManager fragmentManger = context.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            });

            convertView.setTag(mHolder);
            convertView.setTag(R.id.date, mHolder.date);
            convertView.setTag(R.id.time, mHolder.time);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.date.setTag(position);
        mHolder.time.setTag(position);

        mHolder.date.setText(clinicAppointment.get(position).getAppointmentDate());
        mHolder.time.setText(clinicAppointment.get(position).getBookTime());

        return convertView;
    }
}