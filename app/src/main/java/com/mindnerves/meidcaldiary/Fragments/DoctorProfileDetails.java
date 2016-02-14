package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Application.MyApi;
import Model.DoctorSearchResponse;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorProfileDetails extends Fragment {

    MyApi api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_profile_fragment, container,false);

        TextView emailId = (TextView) view.findViewById(R.id.emailId);
        TextView mobileId = (TextView) view.findViewById(R.id.mobileId);
        TextView locationId = (TextView) view.findViewById(R.id.locationId);
        TextView specialityId = (TextView) view.findViewById(R.id.specialityId);
        TextView genderId = (TextView) view.findViewById(R.id.genderId);
        TextView dateOfBirthId = (TextView) view.findViewById(R.id.dateOfBirthId);

        Bundle args = getArguments();
        String doctor_email = args.getString("doctorId");
        Global global = (Global) getActivity().getApplicationContext();
        List<DoctorSearchResponse> doctorSearchResponses = global.getDoctorSearchResponses();

        for(int i = 0; i < doctorSearchResponses.size(); i++){
            if(doctor_email.equals(doctorSearchResponses.get(i).getEmailID())){
                DoctorSearchResponse doctorSearchResponse = doctorSearchResponses.get(i);
                emailId.setText(doctorSearchResponse.getEmailID());
                genderId.setText(doctorSearchResponse.getGender());

                if(doctorSearchResponse.getDateOfBirth() != null){
                    SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    try {
                       // System.out.println("Date  in java = "+format.parse(doctorSearchResponse.getDateOfBirth()).toString());
                        Date date = format.parse(doctorSearchResponse.getDateOfBirth());
                        System.out.println(date);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.YEAR);
                        System.out.println("formatedDate : " + formatedDate);
                        dateOfBirthId.setText(formatedDate);
                        //appointment.appointmentDate = format.parse(bookAppointment.appointmentDate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                dateOfBirthId.setText(doctorSearchResponse.getDateOfBirth());
                mobileId.setText(doctorSearchResponse.getMobileNumber());
                locationId.setText(doctorSearchResponse.getLocation());
                specialityId.setText(doctorSearchResponse.getSpeciality());
            }
        }

        return view;
    }
}
