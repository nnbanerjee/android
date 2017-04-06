package com.medico.adapter;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.PatientAppointmentByDoctor;
import com.medico.util.PARAM;
import com.medico.view.ClinicDoctorAppointmentFragment;
import com.medico.view.ParentActivity;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class SlotAppointmentAdapter extends BaseAdapter
{

    Activity activity;
    ProgressDialog progress;
    DoctorClinicDetails clinicDetails;
    PatientAppointmentByDoctor patientAppointments;
    List<DoctorClinicDetails.ClinicSlots> slots;

    // private RelativeLayout   mainRelative;

    public SlotAppointmentAdapter(Activity context, DoctorClinicDetails clinicDetails, PatientAppointmentByDoctor patientAppointments, List<DoctorClinicDetails.ClinicSlots> slots) {
        this.activity = context;
        this.clinicDetails = clinicDetails;
        this.patientAppointments = patientAppointments;
        this.slots = slots;

    }
    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position)
    {
        return slots.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public int getCount()
    {
        return slots.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.slot_list, null);
        }
        TextView shiftName = (TextView)convertView.findViewById(R.id.shift_name);
        TextView shiftDays = (TextView)convertView.findViewById(R.id.shiftDays);
        TextView shiftTime = (TextView)convertView.findViewById(R.id.shiftTime);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        ListView appointments   = (ListView)convertView.findViewById(R.id.clinicAppointments);
        DoctorClinicDetails.ClinicSlots details = clinicDetails.slots.get(position);
        System.out.println("DEBUG Slot id " + details.doctorClinicId + " Position " + position);

        shiftName.setText("Slot " + details.slotNumber + " : ");
        shiftDays.setText(daysOfWeek(details.daysOfWeek));
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
        shiftTime.setText(shiftDateTime);
        bookOnline.setTag(details);
        bookOnline(bookOnline);
        if(patientAppointments != null && patientAppointments.clinicList != null && patientAppointments.clinicList.size() > 0) {

            List<PatientAppointmentByDoctor.Appointments> appointmentss = getAppointments(patientAppointments,details);
            if(appointmentss != null && appointmentss.size() > 0) {
                AppointmentAdapter appointmentAdapter = new AppointmentAdapter(activity, details, appointmentss);
                appointments.setAdapter(appointmentAdapter);
            }
        }
        return convertView;
    }

    private String daysOfWeek(String days)
    {
        String[] daysNumber = {"0,1,2,3,4,5,6","0,1,2,3,4,5","0,1,2,3,4","0,1,2,3","0,1,2","0,1","0",
                    "1,2,3,4,5,6","1,2,3,4,5","1,2,3,4","1,2,3","1,2","1",
                    "2,3,4,5,6","2,3,4,5","2,3,4","2,3","2",
                    "3,4,5,6","3,4,5","3,4","3",
                    "4,5,6","4,5","3",
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


    private List<PatientAppointmentByDoctor.Appointments> getAppointments(PatientAppointmentByDoctor patientAppointments, DoctorClinicDetails.ClinicSlots clinicSlot )
    {
        for(PatientAppointmentByDoctor.ClinicAppointment appoints : patientAppointments.clinicList)
        {
            if(appoints.doctorClinicId.intValue() == clinicSlot.doctorClinicId.intValue())
            {
                return appoints.appointments;
            }
        }
        return null;
    }
    private void bookOnline(Button bookonline)
    {
        bookonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoctorClinicDetails.ClinicSlots details = (DoctorClinicDetails.ClinicSlots)v.getTag();
                Bundle bundle = activity.getIntent().getExtras();
                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);

                bundle.putInt(PARAM.CLINIC_ID, clinicDetails.clinic.idClinic);
                bundle.putString(PARAM.CLINIC_NAME,clinicDetails.clinic.clinicName);
                bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,details.doctorClinicId);
                bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
                bundle.putLong(PARAM.SLOT_START_DATETIME,details.startTime);
                bundle.putLong(PARAM.SLOT_END_DATETIME,details.endTime);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new ClinicDoctorAppointmentFragment();
                ((ParentActivity)activity).fragmentList.add(fragment);
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service,fragment,"Doctor Consultations").addToBackStack(null).commit();


            }
        });
    }

}
