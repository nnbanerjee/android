package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import com.medico.model.Clinic;
import Model.ClinicDetailVm;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ClinicProfileDetails extends Fragment {

    MyApi api;
    SharedPreferences session;
    String clinicId = "";
    Global global;
    ClinicDetailVm clinicDetails;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_full_details, container,false);

        TextView assistant = (TextView) view.findViewById(R.id.assistant);
        TextView emailId = (TextView) view.findViewById(R.id.emailId);
        TextView mobileId = (TextView) view.findViewById(R.id.mobileId);
        TextView locationId = (TextView) view.findViewById(R.id.locationId);
        TextView specialityId = (TextView) view.findViewById(R.id.specialityId);
        TextView landline = (TextView) view.findViewById(R.id.landline);
        Button back = (Button)getActivity().findViewById(R.id.back_button);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        clinicId = session.getString("patient_clinicId",null);
        Bundle bun = getArguments();
        global = (Global) getActivity().getApplicationContext();
        List<Clinic> clinicsList = global.getAllClinicsList();
        List<ClinicDetailVm> clinicDetailVm = global.getClinicDetailVm();
        System.out.print("clinicId = "+clinicId);
        if(bun == null)
        {
            System.out.println("Bundle Condition:::::::");
            if(clinicDetailVm != null) {
                System.out.println("Bundle Condition:::::::"+clinicDetailVm.size());
                if(clinicDetailVm.size() != 0) {
                    for (ClinicDetailVm vm : clinicDetailVm) {
                        if (vm.getClinicId().equals("" + clinicId)) {
                            assistant.setText(vm.getClinicName());
                            emailId.setText(vm.clinicEmail);
                            mobileId.setText(vm.clinicMobile);
                            locationId.setText(vm.getClinicLocation());
                            landline.setText(vm.getContactNumber());
                            specialityId.setText(vm.clinicSpeciality);
                            clinicDetails = vm;
                            break;
                        }
                    }
                }else
                {

                    if(clinicsList != null) {
                        System.out.println("clinicList true::::::::::");
                        for (Clinic clinic : clinicsList) {
                            System.out.print("clinicId in for = " + clinic.getIdClinic());
                            if (clinic.getIdClinic().equals(clinicId)) {
                                assistant.setText(clinic.getClinicName());
                                emailId.setText(clinic.getEmail());
                                mobileId.setText(clinic.getMobile());
                                locationId.setText(clinic.getLocation());
                                landline.setText(clinic.getLandLineNumber());
                                specialityId.setText(clinic.speciality);
                                break;
                            }
                        }
                    }
                }
            }
        }else{
            if(clinicsList != null) {
                System.out.println("clinicList true::::::::::");
                for (Clinic clinic : clinicsList) {
                    System.out.print("clinicId in for = " + clinic.getIdClinic());
                    if (clinic.getIdClinic().equals(clinicId)) {
                        assistant.setText(clinic.getClinicName());
                        emailId.setText(clinic.getEmail());
                        mobileId.setText(clinic.getMobile());
                        locationId.setText(clinic.getLocation());
                        landline.setText(clinic.getLandLineNumber());
                        specialityId.setText(clinic.speciality);
                        break;
                    }
                }
            }else{
                if(clinicDetailVm != null) {
                    for (ClinicDetailVm vm : clinicDetailVm) {
                        if (vm.getClinicId().equals(clinicId)) {
                            System.out.println("Clinic Name= "+vm.getClinicName());
                            System.out.println("Clinic Email= "+vm.clinicEmail);
                            System.out.println("Clinic Mobile= "+vm.clinicMobile);
                            System.out.println("Clinic location= "+vm.getClinicLocation());
                            System.out.println("Clinic Contact Number= "+vm.getContactNumber());
                            System.out.println("Clinic Speciality= "+vm.clinicSpeciality);
                            assistant.setText(vm.getClinicName());
                            emailId.setText(vm.clinicEmail);
                            mobileId.setText(vm.clinicMobile);
                            locationId.setText(vm.getClinicLocation());
                            landline.setText(vm.getContactNumber());
                            specialityId.setText(vm.clinicSpeciality);
                            clinicDetails = vm;
                            break;
                        }
                    }
                }
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        return view;
    }
    public void goToBack() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if(bundle.getString("fragment") != null){
                if(bundle.getString("fragment").equals("ClinicPatientAdapter")){
                    Fragment fragment = new ClinicAllPatientFragment();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            }
        } else {
            if (clinicDetails != null) {
                SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_DoctorName", clinicDetails.getDoctorName());
                editor.putString("patient_DoctorId", clinicDetails.getDoctorId());
                editor.putString("patient_DoctorEmail", clinicDetails.getDoctorEmail()); // value to store
                editor.commit();
                global.setClinicDetailVm(null);
                Bundle args = new Bundle();
                args.putString("doctorId", clinicDetails.getDoctorEmail());
                args.putString("fragment", "from_adapter");
                Fragment fragment = new DoctorDetailsFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        }
    }

}
