package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.mindnerves.meidcaldiary.Fragments.ShowClinic;
import com.mindnerves.meidcaldiary.R;

import java.util.List;


import Model.Clinic;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicAdapter extends BaseAdapter {

    private Activity activity;
    private List<Clinic> clinicList;
    private LayoutInflater inflater;
    private TextView firstNameTv,locationTv;
    private CheckBox checkBox;

    public ClinicAdapter(Activity activity, List<Clinic> clinicList) {
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
    public View getView(int position, View cv, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.basic_clinic_element, null);

        final int pos = position;

        firstNameTv = (TextView) convertView.findViewById(R.id.clinic_name);

        locationTv = (TextView) convertView.findViewById(R.id.clinic_location);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_clinic);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                System.out.println("I am here " + cb.isChecked());

                Clinic clinic = (Clinic) cb.getTag();

                System.out.println("Doc " + clinic.toString());

                if (cb.isChecked()) {
                    clinic.setSelected(true);
                    System.out.println("Doctor Object Value " + clinic.isSelected());
                } else {
                    clinic.setSelected(false);
                }


            }
        });

        convertView.setTag(clinicList);
        final Clinic cl = clinicList.get(position);

        System.out.println("Value of Doc "+cl.isSelected());

        if((cl.getClinicName()).equals("No Clinic Found"))
        {
            checkBox.setVisibility(View.INVISIBLE);

            locationTv.setVisibility(View.INVISIBLE);

        }
        else
        {
            if(cl.isSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(cl);
        }

        firstNameTv.setText(cl.getClinicName());
        //specialityTv.setText(doc.getSpecialization());
        locationTv.setText(cl.getLocation());

        return convertView;

    }
}
