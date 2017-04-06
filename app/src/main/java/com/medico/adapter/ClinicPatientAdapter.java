package com.medico.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.AppointmentId1;
import com.medico.model.AppointmentResponse;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.PatientAppointmentByDoctor;
import com.medico.util.PARAM;
import com.medico.view.ClinicDoctorAppointmentFragment;
import com.medico.view.FeedbackFragmentClinicAppointment;
import com.medico.view.ParentActivity;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicPatientAdapter extends BaseAdapter {

    Activity activity;
    ProgressDialog progress;
    List<DoctorClinicDetails> clinicDetails;
    PatientAppointmentByDoctor patientAppointments;

    ClinicPatientAdapter adapter;

    // private RelativeLayout   mainRelative;

    public ClinicPatientAdapter(Activity context, List<DoctorClinicDetails> clinicDetails, PatientAppointmentByDoctor patientAppointments) {
        this.activity = context;
        this.clinicDetails = clinicDetails;
        this.patientAppointments = patientAppointments;
        adapter = this;
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
        return clinicDetails.get(position);
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
        return clinicDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.clinic_list, null);
        }
        TextView clinicName = (TextView)convertView.findViewById(R.id.clinicName);
        TextView clinicLocation = (TextView)convertView.findViewById(R.id.clinicLocation);
        TextView clinicContact = (TextView)convertView.findViewById(R.id.clinicContact);
//        ListView clinicSlots   = (ListView)convertView.findViewById(R.id.clinicSlots);
        // access your linear layout
        LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.clinicSlots);
        // load the xml structure of your row
        DoctorClinicDetails details = clinicDetails.get(position);
        clinicName.setText(details.clinic.clinicName);
        clinicLocation.setText(details.clinic.address);
        clinicContact.setText(details.clinic.landLineNumber.toString());
        if(details.slots != null && details.slots.size() > 0) {
            layout.removeAllViews();
//            SlotAppointmentAdapter slotPatientAdapter = new SlotAppointmentAdapter(activity, details, patientAppointments, details.slots);
//            clinicSlots.setAdapter(slotPatientAdapter);
            for(DoctorClinicDetails.ClinicSlots slot:details.slots) {
                View child = activity.getLayoutInflater().inflate(R.layout.slot_list, null);
                addSlots(child,details,slot);
                layout.addView(child);

                if(patientAppointments != null && patientAppointments.clinicList != null && patientAppointments.clinicList.size() > 0) {
                    List<PatientAppointmentByDoctor.Appointments> appointmentses = getAppointments(patientAppointments, slot);
                    if(appointmentses != null && appointmentses.size() > 0) {
                        for (PatientAppointmentByDoctor.Appointments appointments : appointmentses) {
                            View subchild = activity.getLayoutInflater().inflate(R.layout.appointment_list, null);
                            addAppointment(subchild, details,slot, appointments);
                            layout.addView(subchild);
                        }
                    }
                }
            }
        }
        return convertView;
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
    private void addSlots(View convertView, DoctorClinicDetails doctorClinicDetails, DoctorClinicDetails.ClinicSlots details )
    {
        TextView shiftName = (TextView)convertView.findViewById(R.id.shift_name);
        TextView shiftDays = (TextView)convertView.findViewById(R.id.shiftDays);
        TextView shiftTime = (TextView)convertView.findViewById(R.id.shiftTime);
        Button bookOnline = (Button)convertView.findViewById(R.id.bookOnline);
        shiftName.setText("Slot " + details.slotNumber + " : ");
        shiftDays.setText(daysOfWeek(details.daysOfWeek));
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
        shiftTime.setText(shiftDateTime);
        bookOnline.setTag(details);
        bookOnline(bookOnline, doctorClinicDetails, details);
    }
    private void addAppointment(View convertView, final DoctorClinicDetails clinicDetails, DoctorClinicDetails.ClinicSlots slot, PatientAppointmentByDoctor.Appointments appointments)
    {
        Button feedback = (Button) convertView.findViewById(R.id.feedback_btn);
        Button change = (Button) convertView.findViewById(R.id.change_btn);
        Button cancel = (Button) convertView.findViewById(R.id.cancel_btn);
        TextView appointmentDateTime = (TextView)convertView.findViewById(R.id.appointment_date_time);
        Date current = new Date();
        DateFormat formatDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
         appointmentDateTime.setText(formatDate.format(new Date(appointments.dateTime)));
        if(current.getTime() > appointments.dateTime) {
            feedback.setVisibility(View.VISIBLE);
            change.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
        else
        {
            feedback.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
        rescheduleAppointment(change,clinicDetails, slot, appointments);
        feedbackAppointment(feedback,clinicDetails, slot, appointments);
        cancelAppointment(cancel,clinicDetails,slot,appointments);
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
    private void bookOnline(Button bookonline, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details)
    {
        bookonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    private void rescheduleAppointment(Button reschedule, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
    {
        reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = activity.getIntent().getExtras();
                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
                bundle.putInt(PARAM.APPOINTMENT_ID,appointments.appointmentId);
                bundle.putLong(PARAM.APPOINTMENT_DATETIME, appointments.dateTime);
                bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, appointments.sequenceNumber);
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

    private void feedbackAppointment(Button feedback, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
    {
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = activity.getIntent().getExtras();
                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
                bundle.putInt(PARAM.APPOINTMENT_ID,appointments.appointmentId);
                bundle.putLong(PARAM.APPOINTMENT_DATETIME, appointments.dateTime);
                bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, appointments.sequenceNumber);
                bundle.putInt(PARAM.CLINIC_ID, clinicDetails.clinic.idClinic);
                bundle.putString(PARAM.CLINIC_NAME,clinicDetails.clinic.clinicName);
                bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,details.doctorClinicId);
                bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
                bundle.putLong(PARAM.SLOT_START_DATETIME,details.startTime);
                bundle.putLong(PARAM.SLOT_END_DATETIME,details.endTime);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new FeedbackFragmentClinicAppointment();
                ((ParentActivity)activity).fragmentList.add(fragment);
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = activity.getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service,fragment,"Doctor Consultations").addToBackStack(null).commit();


            }
        });
    }
    private void cancelAppointment(Button cancel, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
    {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Close buttom clicked");
                new AlertDialog.Builder(activity)
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this appointment?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                                RestAdapter restAdapter = new RestAdapter.Builder()
                                        .setEndpoint(activity.getString(R.string.base_url))
                                        .setClient(new OkClient())
                                        .setLogLevel(RestAdapter.LogLevel.FULL)
                                        .build();
                                MyApi api = restAdapter.create(MyApi.class);
                                api.cancelAppointment(new AppointmentId1(appointments.appointmentId), new Callback<AppointmentResponse>() {
                                    @Override
                                    public void success(AppointmentResponse result, Response response) {
                                        progress.dismiss();
//                                        if (result.getStatus().equalsIgnoreCase("1")) {
                                            Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
//                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        progress.dismiss();
                                        error.printStackTrace();
                                        Toast.makeText(activity, "Failed to remove medicine", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
    }


//    public void addAllEventListeners(){
//        mHolder.visited1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String clinicPosition="";
//                String[] tag= ((String) v.getTag()).split("#");
//              //  String clinicPosition = (String) v.getTag();
//                int position= Integer.parseInt(tag[0]);
//
//                if(tag!=null && tag.length>1)
//                  clinicPosition= tag[1];
//
//               // AppointmentSlotsByDoctor vm = clinicSlotsByDoctorList.get(clinicPosition);
//              //  progress = ProgressDialog.show(context, "", "Loading...Please wait...");
//                //saveVisitedData(doctorId, patient_email, vm.getClinic().getIdClinic(), "shift1", 1);
//               // clinicSlotsByDoctorList.get(clinicPosition).getClinic().getAppointments().get
//                Bundle args = new Bundle();
//                args.putString("selectedAppointmentId", clinicPosition);
//                Fragment fragment = new FeedbackFragmentClinicAppointment();
//                fragment.setArguments(args);
//                FragmentManager fragmentManger = context.getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
//
//                //show pop up
//            }
//        });
//
//
//        //Cancel appointment
//        mHolder.cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //clinicPositionGlobal = (Integer) v.getTag();
//
//                String[] tag= ((String) v.getTag()).split("#");
//
//               final int position= Integer.parseInt(tag[0]);
//                clinicPositionGlobal= Integer.parseInt(tag[1]);
//                // AppointmentSlotsByDoctor vm = clinicSlotsByDoctorList.get(clinicPositionGlobal);
//                //saveVisitedData(doctorId, patient_email, vm.getClinic().getIdClinic(), "shift1", 0);
//
//                // String apptId= getAppointmentForSelectedSlot( clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(),clinicSlotsByDoctorList.get(position).getSlots().get(clinicPositionGlobal).getSlotNumber() )
//                String apptId = appointmentDetailsObj.get(position).getAppointments().get(clinicPositionGlobal).getAppointmentId();
//                //Retrofit Initialization
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint(context.getResources().getString(R.string.base_url))
//                        .setClient(new OkClient())
//                        .setLogLevel(RestAdapter.LogLevel.FULL)
//                        .build();
//                api = restAdapter.create(MyApi.class);
//                progress = ProgressDialog.show(context, "", context.getResources().getString(R.string.loading_wait));
//                api.cancelAppointment(new AppointmentId(apptId), new Callback<ResponseCodeVerfication>() {
//                    @Override
//                    public void success(ResponseCodeVerfication responseCodeVerfication, Response response) {
//                        System.out.println(response);
//                        progress.dismiss();
//                        Toast.makeText(context.getApplicationContext(), "Successfully cancelled Appointment!", Toast.LENGTH_LONG).show();
//                        if (appointmentDetailsObj.get(clinicPositionGlobal).getSlotNumber().equals(1)) {
//                            mHolder.nextAppointment1.setVisibility(View.INVISIBLE);
//                            mHolder.change.setVisibility(View.GONE);
//                            mHolder.nextAppointment1Value.setVisibility(View.INVISIBLE);
//                            mHolder.nextAppointment1Value.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(appointmentDetailsObj.get(position).getAppointments().get(clinicPositionGlobal).getDateTime()));
//                            mHolder.bookOnline.setVisibility(View.VISIBLE);
//                            mHolder.cancel.setVisibility(View.INVISIBLE);
//                            mHolder.visited1.setVisibility(View.GONE);
//
//                            clinicPatientAdapter.notifyDataSetInvalidated();
//                        }
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        error.printStackTrace();
//                        progress.dismiss();
//                        Toast.makeText(context.getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();
//
//                    }
//                });
//
//
//            }
//        });
//
//        mHolder.change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] tag= ((String) v.getTag()).split("#");
//
//               int position= Integer.parseInt(tag[0]);
//                int slotPosition= Integer.parseInt(tag[1]);
//                Slot selectedSlot = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition);
//
//                String slot0StartTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getStartTime();
//                String slot0EndTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getEndTime();
//                Calendar startTimeCal = Calendar.getInstance();
//
//                startTimeCal.set(Calendar.HOUR, 0);
//                startTimeCal.set(Calendar.MINUTE, 0);
//                startTimeCal.set(Calendar.SECOND, 0);
//                startTimeCal.set(Calendar.AM_PM, Calendar.AM);
//
//                slot0StartTime = "" + startTimeCal.getTimeInMillis();
//
//                Calendar endTimeCal = Calendar.getInstance();
//                endTimeCal.set(Calendar.HOUR, 23);
//                endTimeCal.set(Calendar.MINUTE, 59);
//                endTimeCal.set(Calendar.SECOND, 0);
//                startTimeCal.set(Calendar.AM_PM, Calendar.PM);
//                slot0EndTime = "" + endTimeCal.getTimeInMillis();
//
//                // ClinicList vm = appointmentDetailsObj.get(clinicPosition);
//                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                String formattedDate = df.format(c.getTime());
//                //getClinicAppointment(vm.getClinic().getIdClinic(), "shift1", null, null);
//                getClinicAppointment(selectedSlot, doctorId, clinicSlotsByDoctorList.get(position).getClinic().getDoctorClinicId(), formattedDate, slot0StartTime, slot0EndTime, clinicSlotsByDoctorList.get(position).getClinic().getClinicName(),
//                        clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(), clinicListSingleObj,true);
//            }
//        });
//
//
//        mHolder.bookOnline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] tag= ((String) v.getTag()).split("#");
//
//                int position= Integer.parseInt(tag[0]);
//                int slotPosition= Integer.parseInt(tag[1]);
//                Slot selectedSlot = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition);
//
//                String slot0StartTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getStartTime();
//                String slot0EndTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getEndTime();
//
//                SharedPreferences.Editor editor = session.edit();
//                Gson gson = new Gson();
//                String json = gson.toJson(selectedSlot);
//                editor.putString("SelectedSlotDetailsWithClinic", json);
//                editor.putInt("selectedSlotNo", slotPosition);
//                editor.commit();
//
//
//                Calendar startTimeCal = Calendar.getInstance();
//
//                startTimeCal.set(Calendar.HOUR, 0);
//                startTimeCal.set(Calendar.MINUTE, 0);
//                startTimeCal.set(Calendar.SECOND, 0);
//                startTimeCal.set(Calendar.AM_PM, Calendar.AM);
//
//                slot0StartTime = "" + startTimeCal.getTimeInMillis();
//
//                Calendar endTimeCal = Calendar.getInstance();
//                endTimeCal.set(Calendar.HOUR, 23);
//                endTimeCal.set(Calendar.MINUTE, 59);
//                endTimeCal.set(Calendar.SECOND, 0);
//                startTimeCal.set(Calendar.AM_PM, Calendar.PM);
//                slot0EndTime = "" + endTimeCal.getTimeInMillis();
//
//                // ClinicList vm = appointmentDetailsObj.get(clinicPosition);
//                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                String formattedDate = df.format(c.getTime());
//
//                getClinicAppointment(selectedSlot, doctorId, clinicSlotsByDoctorList.get(position).getClinic().getDoctorClinicId(), formattedDate, slot0StartTime,
//                        slot0EndTime, clinicSlotsByDoctorList.get(position).getClinic().getClinicName(), clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(), clinicListSingleObj,false);
//            }
//        });
//
//
//        mHolder.clinicName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("fragment", "ClinicPatientAdapter");
//                int clinicPosition = (Integer) v.getTag();
//                ClinicList vm = appointmentDetailsObj.get(clinicPosition);
//                System.out.println("ClinicID= " + vm.getClinic().getIdClinic());
//                SharedPreferences.Editor editor = session.edit();
//                // global.setAllDoctorClinicList(appointmentDetailsObj);
//                editor.putString("patient_clinicId", vm.getClinic().getIdClinic());
//                editor.commit();
//                Fragment fragment = new ClinicProfileDetails();
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManger = context.getFragmentManager();
//                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
//            }
//        });
//    }
//    public Appointment getAppointmentForSelectedSlot(String doctorClinicId){
//        Appointment aapt=null;
//        for (int i = 0; i < appointmentDetailsObj.size(); i++) {
//
//            if (doctorClinicId != null
//                    && doctorClinicId.toString().equalsIgnoreCase(appointmentDetailsObj.get(i).getDoctorClinicId())  ) {
//               aapt=appointmentDetailsObj.get(i).getAppointments().get(i);
//            }
//        }
//
//        return aapt;
//    }
    //Clicked on book online button


    public void saveVisitedData(String doctorId, String patientId, String clinicId, String shift, Integer visited) {

        MyApi api;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);


        api.saveVisitedPatientAppointment(doctorId, patientId, clinicId, shift, visited, new Callback<String>() {
            @Override
            public void success(String str, Response response) {
                progress.dismiss();
                Toast.makeText(activity, "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


}