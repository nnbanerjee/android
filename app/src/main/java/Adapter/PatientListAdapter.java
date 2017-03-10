package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.view.PatientDetailsFragment;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.DoctorSearchResponse;


/**
 * Created by MNT on 23-Feb-15.
 */
public class PatientListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<DoctorSearchResponse>  doctorSearchResponses;

    public PatientListAdapter(Activity activity, List<DoctorSearchResponse> doctorSearchResponses) {
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
        TextView appointmentDate = (TextView) convertView.findViewById(R.id.appointmentDate);
        TextView lastVisitedValue = (TextView) convertView.findViewById(R.id.lastVisitedValue);

        if(doctorSearchResponses.get(position).lastVisited == null){
            lastVisitedValue.setText("Not Visited ");
        }else{
            lastVisitedValue.setText(doctorSearchResponses.get(position).lastVisited);
        }

        if(doctorSearchResponses.get(position).getBookDate() != null){
            appointmentDate.setText(doctorSearchResponses.get(position).getBookDate() + " " +doctorSearchResponses.get(position).getBookTime());
        }
        doctorName.setText(doctorSearchResponses.get(position).getName());
        doctorSpeciality.setText(doctorSearchResponses.get(position).getSpeciality());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patientId", doctorSearchResponses.get(position).getDoctorId());
                editor.putString("patient_Name", doctorSearchResponses.get(position).getName());
                editor.commit();
                Fragment fragment = new PatientDetailsFragment();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        return convertView;

    }
}
