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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Fragments.ClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.DoctorClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.ShowAppointmentTime;
import com.mindnerves.meidcaldiary.Fragments.ShowShift1;
import com.mindnerves.meidcaldiary.Fragments.ShowShift2;
import com.mindnerves.meidcaldiary.Fragments.ShowShift3;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.AppointmentSlotsByDoctor;
import Model.Clinic;
import Model.Slot;
import Utils.UtilSingleInstance;


/**
 * Created by MNT on 23-Feb-15.
 */
public class ClinicDoctorListAdapter extends BaseAdapter {

    private Activity activity;
    public LayoutInflater inflater;
    List<AppointmentSlotsByDoctor>   clinicList;
    int shiftCount1,shiftCount2,shiftCount3;
    private View layoutView;
    private LinearLayout contentLinear;
    private AppointmentSlotsByDoctor selectedItemFromList;

    public ClinicDoctorListAdapter(Activity activity,  List<AppointmentSlotsByDoctor>  clinicList) {
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
        layoutView = inflater.inflate(R.layout.doctor_clinic_list_item_slots, null, false);
        shiftCount1 = 0;
        shiftCount2 = 0;
        shiftCount3 = 0;

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.doctor_clinic_list_item, null);
        contentLinear = (LinearLayout) convertView.findViewById(R.id.content_linear);
        contentLinear.removeAllViews();
        TextView clinicName = (TextView) convertView.findViewById(R.id.doctorName);
        ImageView doctorImg = (ImageView) convertView.findViewById(R.id.doctorImg);
        TextView clinicsSpeciality = (TextView) convertView.findViewById(R.id.doctorSpeciality);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        doctorImg.setBackgroundResource(R.drawable.clinic);

        TextView totalCount = (TextView)convertView.findViewById(R.id.total_shift_count);
        Button rightArrowTotal = (Button)convertView.findViewById(R.id.nextBtn);

        ImageView downImage = (ImageView)convertView.findViewById(R.id.downImg);
        clinicName.setText(clinicList.get(position).getClinic().getClinicName());
        clinicsSpeciality.setText(clinicList.get(position).getClinic().getClinicName());
        new ImageLoadTask(activity.getString(R.string.image_base_url) + clinicList.get(position).getClinic().getImageUrl(), doctorImg).execute();
        downImage.setTag(position);
        rightArrowTotal.setTag(position);

        if (clinicList != null && clinicList.get(position).getSlots() != null && clinicList.get(position).getSlots().size() > 0) {

            List<Slot> slots =clinicList.get(position).getSlots();
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
                slot1Count.setText(clinicList.get(position).getClinic().getTotalAppointmentCount());
               // shiftCount1 = Integer.parseInt(clinicList.get(position).getShift1().getAppointmentCount());
                shiftRightArrow1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("patient_clinicId", clinicList.get(position).getClinic().getIdClinic());
                        editor.putString("patient_clinicName", clinicList.get(position).getClinic().getClinicName());
                        editor.putString("clinic_doctorId", clinicList.get(position).getClinic().getDoctorId());
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
                        editor.putString("patient_clinicId", clinicList.get(position).getClinic().getIdClinic());
                        editor.putString("patient_clinicName", clinicList.get(position).getClinic().getClinicName());
                        editor.putString("clinic_doctorId", clinicList.get(position).getClinic().getDoctorId());
                        editor.commit();
                        Bundle bun = new Bundle();
                        bun.putString("fragment", "from_summary");
                        Fragment fragment = new ShowShift1();
                        fragment.setArguments(bun);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                });
                contentLinear.addView(layoutView);
            }
        }

        int totalAppCount = shiftCount1 + shiftCount2 + shiftCount3;
        totalCount.setText("" + totalAppCount);
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemFromList=clinicList.get((int)v.getTag());
                SharedPreferences session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = session.edit();


                Gson gson = new Gson();
                String json = gson.toJson(selectedItemFromList);
                editor.putString("selectedClinicFromAllClinic", json);


                editor.putString("patient_clinicId", clinicList.get(position).getClinic().getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinic().getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getClinic().getDoctorId());
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
                editor.putString("patient_clinicId", clinicList.get(position).getClinic().getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinic().getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getClinic().getDoctorId());
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
                editor.putString("patient_clinicId", clinicList.get(position).getClinic().getIdClinic());
                editor.putString("patient_clinicName", clinicList.get(position).getClinic().getClinicName());
                editor.putString("clinic_doctorId", clinicList.get(position).getClinic().getDoctorId());
                editor.commit();
                Bundle bun = new Bundle();
                bun.putString("fragment","from_summary");
                Fragment fragment = new ShowAppointmentTime();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });


        return convertView;

    }
    public String getTimeTextValue(String start, String end) {
        if (start == null) {

            return "No Shift scheduled !!!";
        } else {
            return start + " - " + end;
        }
    }
}
