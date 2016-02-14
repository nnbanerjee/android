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
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentBookFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorAppointmentFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorClinicAppointmentFragment;
import com.mindnerves.meidcaldiary.R;

import java.util.List;
import Model.ClinicDetailVm;


/**
 * Created by MNT on 23-Feb-15.
 */
public class AllDoctorClinicAdapter extends ArrayAdapter<ClinicDetailVm> {

    private final List<ClinicDetailVm> clinicDetailVm;
    private final Activity context;
    public SharedPreferences session;

   public AllDoctorClinicAdapter(Activity context, List<ClinicDetailVm> clinicDetailVm) {
        super(context, R.layout.all_doctor_clinic_item, clinicDetailVm);
        this.context = context;
        this.clinicDetailVm = clinicDetailVm;
   }

    static class ViewHolder {
        private TextView doctorName;
        private RelativeLayout layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.all_doctor_clinic_item, null);
            mHolder = new ViewHolder();
            mHolder.doctorName = (TextView) convertView.findViewById(R.id.doctorName);
            mHolder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);

            mHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("doctor_patientEmail",clinicDetailVm.get(position).getDoctorEmail());
                    System.out.println("DoctorID::::"+clinicDetailVm.get(position).getDoctorId());
                    editor.putString("clinicDoctorId",clinicDetailVm.get(position).getDoctorId());
                    editor.commit();

                    Fragment fragment = new ClinicAppointmentBookFragment();
                    FragmentManager fragmentManger = context.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            });

            convertView.setTag(mHolder);
            convertView.setTag(R.id.doctorName, mHolder.doctorName);
            convertView.setTag(R.id.layout, mHolder.layout);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.doctorName.setTag(position);
        mHolder.doctorName.setText(clinicDetailVm.get(position).getDoctorName());

        return convertView;
    }
}