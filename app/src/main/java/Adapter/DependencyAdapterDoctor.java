package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.AddDependencyDialog;
import com.mindnerves.meidcaldiary.Fragments.AddDependencyDoctor;
import com.mindnerves.meidcaldiary.Fragments.AddDependencyDoctorDialog;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.Patient;

/**
 * Created by MNT on 16-Mar-15.
 */
public class DependencyAdapterDoctor extends BaseAdapter {

    private Activity activity;
    private ArrayList<Patient> patients;
    private LayoutInflater inflater;
    private TextView patientName,locationTv;
    private ImageView addDependency;
    Global global;
    String doctorId;

    public DependencyAdapterDoctor(Activity activity, ArrayList<Patient> patients)
    {
        this.activity = activity;
        this.patients = patients;
    }
    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
        convertView = inflater.inflate(R.layout.patient_dependent, null);
        final int pos = position;
        global = (Global) activity.getApplicationContext();
        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
        addDependency = (ImageView)convertView.findViewById(R.id.add_dependency);
        patientName = (TextView)convertView.findViewById(R.id.patient_first_name);
        locationTv = (TextView)convertView.findViewById(R.id.patient_location);
        final Patient doc = patients.get(position);
        convertView.setTag(patients);
        addDependency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global.setPatient(doc);
                global.setDoctorId(doctorId);
                AddDependencyDoctorDialog adf1 = AddDependencyDoctorDialog.newInstance();
                adf1.show(activity.getFragmentManager(), "Dialog");

            }
        });
        if((doc.getName()).equals("No Patient Found"))
        {
            locationTv.setVisibility(View.INVISIBLE);
            addDependency.setVisibility(View.INVISIBLE);

        }
        patientName.setText(doc.getName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(doc.getLocation());


        return convertView;
    }


}
