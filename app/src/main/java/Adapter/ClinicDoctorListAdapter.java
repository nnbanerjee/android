package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.ClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.ShowAppointmentTime;
import com.mindnerves.meidcaldiary.Fragments.ShowShift1;
import com.mindnerves.meidcaldiary.Fragments.ShowShift2;
import com.mindnerves.meidcaldiary.Fragments.ShowShift3;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.Clinic;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicDoctorListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<Clinic> clinicList;
    int shiftCount1,shiftCount2,shiftCount3;

    public ClinicDoctorListAdapter(Activity activity, List<Clinic> clinicList) {
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
    public View getView(final int position, View cv, ViewGroup parent) {

        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        shiftCount1 = 0;
        shiftCount2 = 0;
        shiftCount3 = 0;

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_clinic_list_item, null);

        TextView clinicName = (TextView) convertView.findViewById(R.id.doctorName);
        ImageView doctorImg = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView clinicsSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        doctorImg.setBackgroundResource(R.drawable.clinic);
        TextView slot1Text = (TextView)convertView.findViewById(R.id.slot1);
        TextView slot2Text = (TextView)convertView.findViewById(R.id.slot2);
        TextView slot3Text = (TextView)convertView.findViewById(R.id.slot3);
        TextView slot1Time = (TextView)convertView.findViewById(R.id.slot_time1);
        TextView slot2Time = (TextView)convertView.findViewById(R.id.slot_time2);
        TextView slot3Time = (TextView)convertView.findViewById(R.id.slot_time3);
        TextView slot1Count = (TextView)convertView.findViewById(R.id.slot_time_text1);
        TextView slot2Count = (TextView)convertView.findViewById(R.id.slot_time_text2);
        TextView slot3Count = (TextView)convertView.findViewById(R.id.slot_time_text3);
        Button slot1Button = (Button)convertView.findViewById(R.id.slot_button1);
        Button slot2Button = (Button)convertView.findViewById(R.id.slot_button2);
        Button slot3Button = (Button)convertView.findViewById(R.id.slot_button3);
        TextView totalCount = (TextView)convertView.findViewById(R.id.total_shift_count);
        Button rightArrowTotal = (Button)convertView.findViewById(R.id.nextBtn);
        Button shiftRightArrow1 = (Button)convertView.findViewById(R.id.slot_button1);
        Button shiftRightArrow2 = (Button)convertView.findViewById(R.id.slot_button2);
        Button shiftRightArrow3 = (Button)convertView.findViewById(R.id.slot_button3);
        ImageView downImage = (ImageView)convertView.findViewById(R.id.downImg);
        clinicName.setText(clinicList.get(position).getClinicName());
        clinicsSpeciality.setText(clinicList.get(position).getClinicName());

        if((clinicList.get(position).getShift1()) != null)
        {
            slot1Time.setVisibility(View.VISIBLE);
            slot1Count.setVisibility(View.VISIBLE);
            slot1Text.setVisibility(View.VISIBLE);
            slot1Button.setVisibility(View.VISIBLE);
            slot1Time.setText(clinicList.get(position).getShift1().getShiftTime());
            slot1Count.setText(clinicList.get(position).getShift1().getAppointmentCount());
            shiftCount1 = Integer.parseInt(clinicList.get(position).getShift1().getAppointmentCount());
        }
        else
        {
            slot1Time.setVisibility(View.GONE);
            slot1Count.setVisibility(View.GONE);
            slot1Text.setVisibility(View.GONE);
            slot1Button.setVisibility(View.GONE);
            shiftCount1 = 0;
        }
        if((clinicList.get(position).getShift2()) != null)
        {
            slot2Time.setVisibility(View.VISIBLE);
            slot2Count.setVisibility(View.VISIBLE);
            slot2Text.setVisibility(View.VISIBLE);
            slot2Button.setVisibility(View.VISIBLE);
            slot2Time.setText(clinicList.get(position).getShift2().getShiftTime());
            slot2Count.setText(clinicList.get(position).getShift2().getAppointmentCount());
            shiftCount2 = Integer.parseInt(clinicList.get(position).getShift2().getAppointmentCount());
        }
        else
        {
            slot2Time.setVisibility(View.GONE);
            slot2Count.setVisibility(View.GONE);
            slot2Text.setVisibility(View.GONE);
            slot2Button.setVisibility(View.GONE);
            shiftCount2 = 0;
        }
        if((clinicList.get(position).getShift3()) != null)
        {
            slot3Time.setVisibility(View.VISIBLE);
            slot3Count.setVisibility(View.VISIBLE);
            slot3Text.setVisibility(View.VISIBLE);
            slot3Button.setVisibility(View.VISIBLE);
            slot3Time.setText(clinicList.get(position).getShift3().getShiftTime());
            slot3Count.setText(clinicList.get(position).getShift3().getAppointmentCount());
            shiftCount3 = Integer.parseInt(clinicList.get(position).getShift3().getAppointmentCount());        }
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
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Fragment fragment = new DoctorClinicFragment();
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        rightArrowTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowAppointmentTime();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        totalCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowAppointmentTime();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        shiftRightArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift1();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        slot1Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift1();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        shiftRightArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift2();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        slot2Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift2();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        shiftRightArrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift3();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        slot3Count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();
                editor.putString("patient_clinicId", clinicList.get(position).getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowShift3();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        return convertView;

    }
}
