package com.medicohealthcare.view.appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicQueue;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicSlotView extends ParentFragment {


    SharedPreferences session;
    TextView slotName, slotDays, slotTimings, slotFees, slotDuration, numberOfPatients,appointment_count;
    ImageView rightArrow;
    DoctorClinicQueue model;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.clinic_detailed_slot_view,container,false);
        slotName = (TextView) view.findViewById(R.id.slot_name);
        slotDays = (TextView) view.findViewById(R.id.slotDays);
        slotTimings = (TextView) view.findViewById(R.id.slotTimings);
        slotFees = (TextView) view.findViewById(R.id.slotFees);
        slotDuration = (TextView) view.findViewById(R.id.slotDuration);
        numberOfPatients = (TextView) view.findViewById(R.id.numberOfPatients);
        rightArrow = (ImageView)view.findViewById(R.id.imageView7);
        rightArrow.setVisibility(View.GONE);
        appointment_count = (TextView)view.findViewById(R.id.textView9);
        appointment_count.setVisibility(View.GONE);
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        setTitle("Slot Details");
        if(model != null)
        {
            slotName.setText(model.slotName + " ( " + model.slotNumber + " ) Type: " + (model.slotType == 0 ? "General" : "Prime"));
            slotDays.setText(daysOfWeek(model.daysOfWeek));
            DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
            slotTimings.setText(format.format(new Date(model.timeToStart)) + " - " + format.format(new Date(model.timeToStop)));
            rightArrow.setTag(model);
            if (model.feesConsultation != null)
                slotFees.setText(model.feesConsultation.toString());
            slotDuration.setText(model.visitDuration.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
        setTitle("Slot Details");
    }

    public void setModel(DoctorClinicQueue model)
    {
        this.model = model;
    }
    private String daysOfWeek(String days)
    {
        String[] daysNumber = {"0,1,2,3,4,5,6","0,1,2,3,4,5","0,1,2,3,4","0,1,2,3","0,1,2","0,1","0",
                "1,2,3,4,5,6","1,2,3,4,5","1,2,3,4","1,2,3","1,2","1",
                "2,3,4,5,6","2,3,4,5","2,3,4","2,3","2",
                "3,4,5,6","3,4,5","3,4","3",
                "4,5,6","4,5","4",
                "5,6","5",
                "6"};
        String[] daysWord = {"MON-SUN","MON-SAT","MON-FRI","MON-THU","MON-WED","MON-TUE","MON",
                "TUE-SUN","TUE-SAT","TUE-FRI","TUE-THU","TUE-WED","TUE",
                "WED-SUN","WED-SAT","WED-FRI","WED-THU","WED",
                "THU-SUN","THU-SAT","THU-FRI","THU",
                "FRI-SUN","FRI-SAT","FRI",
                "SAT-SUN","SAT",
                "SUN"};

        for(int i = 0; i < daysNumber.length;i++)
        {
            days = days.replace(daysNumber[i],daysWord[i]);
        }

        return days;

    }
}
