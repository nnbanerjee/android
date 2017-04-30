package com.medico.view.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.PatientProfile;
import com.medico.util.PARAM;
import com.medico.view.appointment.ManagePatientAppointment;
import com.medico.view.profile.DoctorConsultations;


/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class PatientMenusManage extends Fragment {

    private TextView doctorsCount,appointmentCount,medicalAlertCount,feedback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_home_menu,container,false);
        RelativeLayout doctorConsultation = (RelativeLayout) view.findViewById(R.id.doctorConsultation);
        RelativeLayout manageAppointment = (RelativeLayout) view.findViewById(R.id.manageAppointment);
        RelativeLayout medicineAlarm = (RelativeLayout) view.findViewById(R.id.medicineAlarm);
        RelativeLayout feedback_layout = (RelativeLayout) view.findViewById(R.id.medicineAlarm);

        doctorsCount = (TextView)view.findViewById(R.id.doctor_count) ;
        appointmentCount = (TextView)view.findViewById(R.id.appointment_count) ;
        medicalAlertCount = (TextView)view.findViewById(R.id.medicalAlertCount) ;
        feedback = (TextView)view.findViewById(R.id.feedback_count) ;

        doctorConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.PATIENT_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, HomeActivity.getParentAtivity().profileStatus);
                Intent intObj = new Intent(getActivity(), DoctorConsultations.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });

        manageAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putInt(PARAM.PROFILE_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.LOGGED_IN_ID, HomeActivity.getParentAtivity().profileId);
                bundle.putInt(PARAM.PROFILE_ROLE, HomeActivity.getParentAtivity().profileRole);
                bundle.putInt(PARAM.PROFILE_STATUS, HomeActivity.getParentAtivity().profileStatus);
                Intent intObj = new Intent(getActivity(), ManagePatientAppointment.class);
                intObj.putExtras(bundle);
                startActivity(intObj);
                onPause();
            }
        });
        medicineAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


    public void updateCounts(PatientProfile patient) {
        if(patient!=null)
        {
            doctorsCount.setText(new Integer(patient.getDoctorsCount()).toString());
            appointmentCount.setText(new Integer(patient.getAppointmentsCount()).toString());
            medicalAlertCount.setText(new Integer(patient.getMedicineCount()).toString());
            feedback.setText(new Integer(patient.getMedicineCount()).toString());
        }
    }
}
