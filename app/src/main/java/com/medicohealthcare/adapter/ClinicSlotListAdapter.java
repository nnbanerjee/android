package com.medicohealthcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicSlotListAdapter extends HomeAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    DoctorClinicDetails details;

    public ClinicSlotListAdapter(Activity activity, DoctorClinicDetails details)
    {
        super(activity);
        this.activity = activity;
        this.details = details;
    }

    @Override
    public int getCount()
    {
        return details.slots == null? 0:details.slots.size();
    }

    @Override
    public Object getItem(int position) {
        return details.slots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.clinic_detailed_slot_view, null);
        DoctorClinicDetails.ClinicSlots slot = details.slots.get(position);
        TextView slotName = (TextView) convertView.findViewById(R.id.slot_name);
        TextView slotDays = (TextView) convertView.findViewById(R.id.slotDays);
        TextView slotTimings = (TextView) convertView.findViewById(R.id.slotTimings);
        TextView slotFees = (TextView) convertView.findViewById(R.id.slotFees);
        TextView slotDuration = (TextView) convertView.findViewById(R.id.slotDuration);
        TextView numberOfPatients = (TextView) convertView.findViewById(R.id.numberOfPatients);
        ImageView rightArrow = (ImageView)convertView.findViewById(R.id.imageView7);
        TextView appointment_count = (TextView)convertView.findViewById(R.id.textView9);
        appointment_count.setText(new Integer(slot.getAppointmentCounts()).toString());
        slotName.setText(slot.name + " ( " + slot.slotNumber + " ) Type: " + (slot.slotType==0?"General":"Prime"));
        slotDays.setText(daysOfWeek(slot.daysOfWeek));
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        slotTimings.setText(format.format(new Date(slot.startTime)) + " - " + format.format(new Date(slot.endTime)));
        rightArrow.setTag(slot);
        if(slot.feesConsultation != null)
            slotFees.setText(slot.feesConsultation.toString());
        slotDuration.setText(slot.visitDuration.toString());
        if(slot.counts != null)
            numberOfPatients.setText(new Integer(slot.counts.size()).toString());
        else
            numberOfPatients.setText(new Integer(0).toString());

        return convertView;

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
