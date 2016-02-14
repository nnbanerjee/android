package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Patient;


/**
 * Created by MNT on 16-Feb-15.
 */
public class PatientAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Patient> patientList;
    private int count = 0;
    private TextView firstNameTv;
    private TextView locationTv;
    private CheckBox checkBox;




    public PatientAdapter(Activity activity, List<Patient> patientList) {
        this.activity = activity;
        this.patientList = patientList;
    }



    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
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
            convertView = inflater.inflate(R.layout.patient_element, null);


        final int pos = position;

        firstNameTv = (TextView) convertView.findViewById(R.id.patient_first_name);

        locationTv = (TextView) convertView.findViewById(R.id.patient_location);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_patient);




       checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                System.out.println("I am here " + cb.isChecked());

                Patient doc = (Patient) cb.getTag();

                System.out.println("Doc " + doc.toString());

                if (cb.isChecked()) {
                    doc.setSelected(true);
                    System.out.println("Doctor Object Value " + doc.isSelected());
                } else {
                    doc.setSelected(false);
                }


            }
        });

        convertView.setTag(patientList);


        final Patient doc = patientList.get(position);

        System.out.println("Value of Doc "+doc.isSelected());

        if((doc.getName()).equals("No Patient Found"))
        {
            checkBox.setVisibility(View.INVISIBLE);

            locationTv.setVisibility(View.INVISIBLE);

        }
        else
        {
            if(doc.isSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(doc);
        }

        firstNameTv.setText(doc.getName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(doc.getLocation());
        return convertView;

    }



}
