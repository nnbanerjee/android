package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import Model.AppointmentSlotsByDoctor;
import Model.Slot;
import Utils.UtilSingleInstance;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorClinicFragment extends Fragment {

    String clinicId = "";
    MyApi api;
    SharedPreferences session;
    int shiftCount1,shiftCount2,shiftCount3;
    ImageView closeMenu;
    private AppointmentSlotsByDoctor selectedItemFromList;
    private View layoutView;
    LinearLayout contentLinear;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_clinic_profile_details, container,false);
        layoutView = inflater.inflate(R.layout.doctor_clinic_list_item_slots, null, false);
          contentLinear = (LinearLayout) view.findViewById(R.id.content_linear);
        contentLinear.removeAllViews();

        TextView clinicName = (TextView) view.findViewById(R.id.doctor_name);
        TextView speciality = (TextView) view.findViewById(R.id.general_clinic_text);
        final Button appointmentBtn = (Button) view.findViewById(R.id.appointmentBtn);
        final Button profileBtn = (Button) view.findViewById(R.id.profileBtn);
        TextView appointmentDate = (TextView) view.findViewById(R.id.appointmentDate);
        TextView show_global_tv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        TextView viewAll = (TextView) view.findViewById(R.id.viewAll);
        ImageView doctorImg = (ImageView) view.findViewById(R.id.doctorImg);
        ImageView closeMenu = (ImageView)view.findViewById(R.id.downImg);
       /* TextView slot1Text = (TextView)view.findViewById(R.id.slot1);
        TextView slot2Text = (TextView)view.findViewById(R.id.slot2);
        TextView slot3Text = (TextView)view.findViewById(R.id.slot3);
        TextView slot1Time = (TextView)view.findViewById(R.id.slot_time1);
        TextView slot2Time = (TextView)view.findViewById(R.id.slot_time2);
        TextView slot3Time = (TextView)view.findViewById(R.id.slot_time3);
        TextView slot1Count = (TextView)view.findViewById(R.id.slot_time_text1);
        TextView slot2Count = (TextView)view.findViewById(R.id.slot_time_text2);
        TextView slot3Count = (TextView)view.findViewById(R.id.slot_time_text3);
        Button slot1Button = (Button)view.findViewById(R.id.slot_button1);
        Button slot2Button = (Button)view.findViewById(R.id.slot_button2);
        Button slot3Button = (Button)view.findViewById(R.id.slot_button3);*/
        TextView totalCount = (TextView)view.findViewById(R.id.total_shift_count);
        Button totalRightArrow = (Button)view.findViewById(R.id.total_right_arrow);
        shiftCount1 = 0;
        shiftCount2 = 0;
        shiftCount3 = 0;

        doctorImg.setBackgroundResource(R.drawable.clinic);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        show_global_tv.setText(session.getString("patient_clinicName", "Medical Diary"));
        clinicId = session.getString("patient_clinicId", null);


        Gson gson = new Gson();

        String jsonSelectedClinic = session.getString("selectedClinicFromAllClinic", "");
         selectedItemFromList =gson.fromJson(jsonSelectedClinic, AppointmentSlotsByDoctor.class);

        clinicName.setText(selectedItemFromList.getClinic().getClinicName());
        speciality.setText(selectedItemFromList.getClinic().getSpeciality());
        if(selectedItemFromList.getLastAppointmentl() != null){
            appointmentDate.setText(selectedItemFromList.getLastAppointmentl());
        }
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        Global global = (Global) getActivity().getApplicationContext();
       // List<Clinic> clinicsList = global.getAllClinicsList();

        if (selectedItemFromList != null && selectedItemFromList.getSlots() != null && selectedItemFromList.getSlots().size() > 0) {

            List<Slot> slots =selectedItemFromList.getSlots();
            if (layoutView.getParent() != null) {
                System.out.println("Layout parent is ---->" + layoutView.getParent());
                ((LinearLayout) layoutView.getParent()).removeView(layoutView);
            }
            for (int i = 0; i < slots.size(); i++) {

                layoutView = inflater.inflate(R.layout.doctor_clinic_list_item_slots, null, false);
                layoutView.setTag(i);
                TextView slot1Text = (TextView)layoutView.findViewById(R.id.slot1);
                TextView slot1Time = (TextView)layoutView.findViewById(R.id.slot_time1);
                TextView slot1Count = (TextView)layoutView.findViewById(R.id.slot_time_text1);
                Button slot1Button = (Button)layoutView.findViewById(R.id.slot_button1);
                Button shiftRightArrow1 = (Button)layoutView.findViewById(R.id.slot_button1);

                slot1Text.setText("SLOT " + slots.get(i).getSlotNumber()+" :");
                String slot0StartTime = slots.get(i).getStartTime();
                String slot0EndTime = slots.get(i).getEndTime();
                if (slot0StartTime != null && slot0EndTime != null)
                    slot1Time.setText(getTimeTextValue(UtilSingleInstance.getTimeFromLongDate(slot0StartTime), UtilSingleInstance.getTimeFromLongDate(slot0EndTime)));
                else
                    slot1Time.setText("NA");
                slot1Count.setText(selectedItemFromList.getClinic().getTotalAppointmentCount());
                // shiftCount1 = Integer.parseInt(selectedItemFromList.getShift1().getAppointmentCount());
                shiftRightArrow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("patient_clinicId", selectedItemFromList.getClinic().getIdClinic());
                        editor.putString("patient_clinicName", selectedItemFromList.getClinic().getClinicName());
                        editor.putString("clinic_doctorId", selectedItemFromList.getClinic().getDoctorId());
                        editor.commit();
                        Bundle bun = new Bundle();
                        bun.putString("fragment","from_summary");
                        Fragment fragment = new ShowShift1();
                        fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                    }
                });
                slot1Count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("patient_clinicId", selectedItemFromList.getClinic().getIdClinic());
                        editor.putString("patient_clinicName", selectedItemFromList.getClinic().getClinicName());
                        editor.putString("clinic_doctorId", selectedItemFromList.getClinic().getDoctorId());
                        editor.commit();
                        Bundle bun = new Bundle();
                        bun.putString("fragment", "from_summary");
                        Fragment fragment = new ShowShift1();
                        fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                });
                contentLinear.addView(layoutView);
            }
        }

       /* for(Clinic clinic  : clinicsList){
            if(clinic.getIdClinic().equals(clinicId))
            {
                clinicName.setText(clinic.getClinicName());
                speciality.setText(clinic.getClinicName());
                if(clinic.getBookDate() != null){
                    appointmentDate.setText(clinic.getBookDate() + " "+ clinic.getBookTime());
                }
                if((clinic.getShift1()) != null)
                {
                    slot1Time.setVisibility(View.VISIBLE);
                    slot1Count.setVisibility(View.VISIBLE);
                    slot1Text.setVisibility(View.VISIBLE);
                    slot1Button.setVisibility(View.VISIBLE);
                    slot1Time.setText(clinic.getShift1().getShiftTime());
                    slot1Count.setText(clinic.getShift1().getAppointmentCount());
                    shiftCount1 = Integer.parseInt(clinic.getShift1().getAppointmentCount());
                }
                else
                {
                    slot1Time.setVisibility(View.GONE);
                    slot1Count.setVisibility(View.GONE);
                    slot1Text.setVisibility(View.GONE);
                    slot1Button.setVisibility(View.GONE);
                    shiftCount1 = 0;
                }
                if((clinic.getShift2()) != null)
                {
                    slot2Time.setVisibility(View.VISIBLE);
                    slot2Count.setVisibility(View.VISIBLE);
                    slot2Text.setVisibility(View.VISIBLE);
                    slot2Button.setVisibility(View.VISIBLE);
                    slot2Time.setText(clinic.getShift2().getShiftTime());
                    slot2Count.setText(clinic.getShift2().getAppointmentCount());
                    shiftCount2 = Integer.parseInt(clinic.getShift2().getAppointmentCount());
                }
                else
                {
                    slot2Time.setVisibility(View.GONE);
                    slot2Count.setVisibility(View.GONE);
                    slot2Text.setVisibility(View.GONE);
                    slot2Button.setVisibility(View.GONE);
                    shiftCount2 = 0;
                }
                if((clinic.getShift3()) != null)
                {
                    slot3Time.setVisibility(View.VISIBLE);
                    slot3Count.setVisibility(View.VISIBLE);
                    slot3Text.setVisibility(View.VISIBLE);
                    slot3Button.setVisibility(View.VISIBLE);
                    slot3Time.setText(clinic.getShift3().getShiftTime());
                    slot3Count.setText(clinic.getShift3().getAppointmentCount());
                    shiftCount3 = Integer.parseInt(clinic.getShift3().getAppointmentCount());
                }
                else
                {
                    slot3Time.setVisibility(View.GONE);
                    slot3Count.setVisibility(View.GONE);
                    slot3Text.setVisibility(View.GONE);
                    slot3Button.setVisibility(View.GONE);
                    shiftCount3 = 0;
                }

                int totalAppCount = shiftCount1 + shiftCount2 + shiftCount3;
                totalCount.setText(""+totalAppCount);


            }
        }*/
        GetClinicProfile();
        //getClinicAppointment();
        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBtn.setBackgroundResource(R.drawable.square_blue_color);
                appointmentBtn.setTextColor(Color.parseColor("#ffffff"));
                profileBtn.setBackgroundResource(R.drawable.square_grey_color);
                profileBtn.setTextColor(Color.parseColor("#000000"));

                System.out.println("");
                //ClinicAllDoctorFragment
                Fragment fragment = new DoctorClinicAppointmentFragment();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentBtn.setBackgroundResource(R.drawable.square_grey_color);
                appointmentBtn.setTextColor(Color.parseColor("#000000"));
                profileBtn.setBackgroundResource(R.drawable.square_blue_color);
                profileBtn.setTextColor(Color.parseColor("#ffffff"));
                GetClinicProfile();
            }
        });
        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DoctorAllClinics();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","from_clinic");
                Fragment fragment = new ShowAppointmentTime();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        totalRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putString("fragment","from_clinic");
                Fragment fragment = new ShowAppointmentTime();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void GetClinicProfile(){
        Fragment fragment = new ClinicProfileDetails();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();

    }
    public void getClinicAppointment()
    {
        Fragment fragment = new DoctorClinicAppointmentFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
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


    public void  goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Clinics and Labs");

        Fragment fragment;

        String  logString = session.getString("loginType", null);
        if(logString!=null && logString.equals("Doctor"))
        {
            fragment = new DoctorAllClinics();
        }
        else
        {
            fragment = new PatientAllClinics();
        }

        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
    public String getTimeTextValue(String start, String end) {
        if (start == null) {

            return "No Shift scheduled !!!";
        } else {
            return start + " - " + end;
        }
    }
}
