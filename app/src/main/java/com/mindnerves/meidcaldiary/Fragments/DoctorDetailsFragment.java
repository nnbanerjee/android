package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.ClinicListItemAdapter;
import Application.MyApi;
import Model.ClinicDetailVm;
import Model.DoctorSearchResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Patient Login
public class DoctorDetailsFragment extends Fragment {

    String doctorId = "";
    String doctor_email;
    MyApi api;
    SharedPreferences session;
    ImageView closeMenu;
    String fragmentString = "";
    TextView show_global_tv;
    Button back;
    Global global;
    String clinicId = "";
    String shift = "";
    String speciality = "";
    String appointmentDateString = "";
    String appointmentTimeString = "";
    String type = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_profile_details, container, false);
        TextView doctorName = (TextView) view.findViewById(R.id.doctorName);
        TextView doctorSpeciality = (TextView) view.findViewById(R.id.doctorSpeciality);
        final Button clinicsBtn = (Button) view.findViewById(R.id.clinicsBtn);
        final Button profileBtn = (Button) view.findViewById(R.id.profileBtn);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointmentDate);
        TextView lastAppointmentValue = (TextView) view.findViewById(R.id.lastAppointmentValue);
        show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        ImageView viewAll = (ImageView) view.findViewById(R.id.viewAll);
        TextView lastVisitedValue = (TextView) view.findViewById(R.id.lastVisitedValue);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        closeMenu = (ImageView) view.findViewById(R.id.downImg);

        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);

        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        Bundle args = getArguments();
        doctor_email = args.getString("doctorId");
        fragmentString = args.getString("fragment");
        if(args.getString("specialization") != null){
            speciality = args.getString("specialization");
        }

        global = (Global) getActivity().getApplicationContext();
        List<DoctorSearchResponse> doctorSearchResponses = global.getDoctorSearchResponses();

        for (int i = 0; i < doctorSearchResponses.size(); i++) {
            if (doctor_email.equals(doctorSearchResponses.get(i).getEmailID())) {
                DoctorSearchResponse doctorSearchResponse = doctorSearchResponses.get(i);
                doctorName.setText(doctorSearchResponse.getName());
                if (doctorSearchResponses.get(i).getBookDate() != null) {
                    if (doctorSearchResponses.get(i).getBookDate().equals("")) {
                        appointmentDate.setText("None");
                    } else {
                        appointmentDate.setText(doctorSearchResponses.get(i).getBookDate() + " " + doctorSearchResponses.get(i).getBookTime());
                    }
                } else {
                    appointmentDate.setText("None");
                }
                doctorId = doctorSearchResponse.getDoctorId();
                doctorSpeciality.setText(doctorSearchResponse.getSpeciality());
                if (doctorSearchResponse.lastVisited == null) {
                    lastVisitedValue.setText("Not Visited ");
                } else {
                    lastVisitedValue.setText(doctorSearchResponse.lastVisited);
                }
                if (doctorSearchResponses.get(i).getPreviousAppointment() != null) {
                    if (!doctorSearchResponses.get(i).getPreviousDate().equals("")) {
                        lastAppointmentValue.setText(doctorSearchResponses.get(i).getPreviousDate() + " " + doctorSearchResponses.get(i).previousAppointment);
                    }
                } else {
                    lastAppointmentValue.setText("None");
                }
                clinicId = "" + doctorSearchResponse.getClinicId();
                appointmentDateString = doctorSearchResponse.getBookDate();
                appointmentTimeString = doctorSearchResponse.getBookTime();
                shift = doctorSearchResponse.getShift();
            }
        }
        if (fragmentString != null) {
            if (fragmentString.equalsIgnoreCase("PatientAppointmentFragment")) {
                clinicsBtn.setBackgroundResource(R.drawable.square_blue_color);
                clinicsBtn.setTextColor(Color.parseColor("#ffffff"));
                profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                profileBtn.setTextColor(Color.parseColor("#000000"));
                System.out.println("DoctorId:::::::::" + doctorId);
                Bundle argument = new Bundle();
                argument.putString("doctorId", doctorId);
                argument.putString("doctorId", doctor_email);
                Fragment fragment = new ClinicAllDoctorFragment();
                fragment.setArguments(argument);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
            } else if (fragmentString.equalsIgnoreCase("appointmentDate")) {
                if (shift != null) {
                    System.out.println("ClinicID= " + clinicId);
                    System.out.println("clinic Shift= " + shift);
                    System.out.println("appointmentDate= " + appointmentDateString);
                    System.out.println("appointmentTime= " + appointmentTimeString);
                    clinicsBtn.setBackgroundResource(R.drawable.square_blue_color);
                    clinicsBtn.setTextColor(Color.parseColor("#ffffff"));
                    profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                    profileBtn.setTextColor(Color.parseColor("#000000"));
                    getAllClinicsDetails();
                }

            } else {
                getDoctorAppintment();
            }
        } else {
            getDoctorAppintment();
        }


        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientAllAppointment();// DoctorProfileDetails();
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_doctor_email", doctor_email);
                editor.putString("patient_doctorId", doctorId);
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment", "doctor_details_fragment");
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });

        clinicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clinicsBtn.setBackgroundResource(R.drawable.square_blue_color);
                clinicsBtn.setTextColor(Color.parseColor("#ffffff"));
                profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                profileBtn.setTextColor(Color.parseColor("#000000"));
                getDoctorAppintment();

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clinicsBtn.setBackgroundResource(R.drawable.square_grey_color);
                clinicsBtn.setTextColor(Color.parseColor("#000000"));
                profileBtn.setBackgroundResource(R.drawable.square_blue_color);
                profileBtn.setTextColor(Color.parseColor("#ffffff"));
                Bundle args = getArguments();
                Fragment fragment = new DoctorProfileDetails();
                args.putString("doctorId", doctor_email);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }
        });
        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Patient")) {
                    Fragment fragment = new PatientAllDoctors();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("specialization", speciality);
                    Fragment frag = new ShowDoctorSpecialitywise();
                    frag.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag, "Add_New_Doc");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        manageScreenIcons();
        return view;
    }

    public void getDoctorAppintment() {

        Bundle args = new Bundle();
        args.putString("doctorId", doctorId);
        args.putString("doctorId", doctor_email);
        //ClinicDetailsFragment
        Fragment fragment = new DoctorAllClinicFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public void getAllClinicsDetails() {
        api.getAllClinicsDetailsList(doctorId, "1", new Callback<List<ClinicDetailVm>>() {
            @Override
            public void success(List<ClinicDetailVm> clinicDetailVm, Response response) {

                Global global = (Global) getActivity().getApplicationContext();
                global.setClinicDetailVm(clinicDetailVm);
                Bundle appointmentArg = new Bundle();
                appointmentArg.putString("clinicId", clinicId);
                appointmentArg.putString("clinicShift", shift);
                appointmentArg.putString("appointmentTime", appointmentTimeString);
                appointmentArg.putString("appointmentDate", appointmentDateString);
                appointmentArg.putString("fragment", "appointmentDate");
                Fragment fragment = new PatientAppointmentFragment();
                fragment.setArguments(appointmentArg);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public void manageScreenIcons() {
        show_global_tv.setText(session.getString("patient_DoctorName", "Medical Diary"));
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goToBack() {
        System.out.println("I am here:::::::::::::::");
        System.out.println("value of fragment String= " + fragmentString);
        if (fragmentString.equals("doctorAdapter")) {
            TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
            globalTv.setText("Add Doctor");
            Bundle specialityBun = new Bundle();
            specialityBun.putString("specialization", global.specialityString);
            Fragment fragment = new AddDoctorSpecialityWise();
            fragment.setArguments(specialityBun);
            FragmentManager fragmentManger = getActivity().getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
        }else if(fragmentString.equals("ShowDoctorAdapter")){
            Bundle bundle = new Bundle();
            bundle.putString("specialization", speciality);
            Fragment frag = new ShowDoctorSpecialitywise();
            frag.setArguments(bundle);
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, frag, "Add_New_Doc");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
        else {
            TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
            globalTv.setText("Medical Diary");
            Fragment fragment = new PatientAllDoctors();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
            final Button back = (Button) getActivity().findViewById(R.id.back_button);
            back.setVisibility(View.INVISIBLE);
        }
    }
}
