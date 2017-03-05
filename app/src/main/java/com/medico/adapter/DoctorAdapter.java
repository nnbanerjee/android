package com.medico.adapter;

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

import Model.DoctorSearchResponse;


/**
 * Created by MNT on 16-Feb-15.
 */
public class DoctorAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<DoctorSearchResponse> doctorList;
    private int count = 0;
    private TextView firstNameTv;
    private TextView specialityTv;
    private TextView locationTv;
    private CheckBox checkBox;




    public DoctorAdapter(Activity activity, List<DoctorSearchResponse> doctorList) {
        this.activity = activity;
        this.doctorList = doctorList;
    }



    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
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
            convertView = inflater.inflate(R.layout.doctor_element, null);


        final int pos = position;

        firstNameTv = (TextView) convertView.findViewById(R.id.first_name);
        specialityTv = (TextView) convertView.findViewById(R.id.speciality);
        locationTv = (TextView) convertView.findViewById(R.id.doctor_location);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_doctor);




       checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                System.out.println("I am here " + cb.isChecked());

                DoctorSearchResponse doc = (DoctorSearchResponse) cb.getTag();

                System.out.println("Doc " + doc.toString());

                if (cb.isChecked()) {
                    doc.setSelected(true);
                    System.out.println("Doctor Object Value " + doc.isSelected());
                } else {
                    doc.setSelected(false);
                }


            }
        });

        convertView.setTag(doctorList);


        final DoctorSearchResponse doc = doctorList.get(position);

        System.out.println("Value of Doc "+doc.isSelected());

        if((doc.getName()).equals("No Doctor Found"))
        {
            checkBox.setVisibility(View.INVISIBLE);
            specialityTv.setVisibility(View.INVISIBLE);
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
        specialityTv.setText(doc.getSpeciality());
        locationTv.setText(doc.getLocation());
        return convertView;

    }



}
