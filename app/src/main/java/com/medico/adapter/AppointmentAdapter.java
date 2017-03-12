package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.medico.model.DoctorClinicDetails;
import com.medico.model.PatientAppointmentByDoctor;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Narendra on 10-03-2017.
 */

public class AppointmentAdapter extends BaseAdapter
{

    Activity activity;
    ProgressDialog progress;
    DoctorClinicDetails.ClinicSlots clinicSlot;
    List<PatientAppointmentByDoctor.Appointments> patientAppointments;

    // private RelativeLayout   mainRelative;

    public AppointmentAdapter(Activity context, DoctorClinicDetails.ClinicSlots clinicSlot, List<PatientAppointmentByDoctor.Appointments> patientAppointments) {
        this.activity = context;
        this.clinicSlot = clinicSlot;
        this.patientAppointments = patientAppointments;

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
        return patientAppointments.get(position);
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
        return patientAppointments.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.appointment_list, null);
        }
        Button feedback = (Button) convertView.findViewById(R.id.feedback_btn);
        Button change = (Button) convertView.findViewById(R.id.change_btn);
        Button cancel = (Button) convertView.findViewById(R.id.cancel_btn);
        TextView appointmentDateTime = (TextView)convertView.findViewById(R.id.appointment_date_time);
        Date current = new Date();
        DateFormat formatDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);

        appointmentDateTime.setText(formatDate.format(new Date(patientAppointments.get(position).dateTime)));
        if(current.getTime() > patientAppointments.get(position).dateTime) {
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

        return convertView;
    }

//    private void giveFeedback(Button feedback)
//    {
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String clinicPosition="";
//                String[] tag= ((String) v.getTag()).split("#");
//              //  String clinicPosition = (String) v.getTag();
//                int position= Integer.parseInt(tag[0]);
//
//                if(tag!=null && tag.length>1)
//                  clinicPosition= tag[1];
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
//    }
//
//    private void cancelAppointment(Button cancel)
//    {
//        cancel.setOnClickListener(new View.OnClickListener() {
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
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText()
//
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("Delete Appointment")
//                        .setMessage("Are you sure you want to delete appointment?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                                cancelClinicsAppointmentData(global.clinicDetailsData.getDoctorId(), global.clinicDetailsData.getPatientId(),
//                                        global.clinicDetailsData.getClinicId(), global.clinicDetailsData.getShift());
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//
//
//                /*cancelClinicsAppointmentData(global.clinicDetailsData.getDoctorId(), global.clinicDetailsData.getId(),
//                        global.clinicDetailsData.getIdClinic(), global.clinicDetailsData.getShift());*/
//            }
//        });
//
//    public void cancelClinicsAppointmentData(Integer doctorId, String patientId, Integer clinicId, String shift){
//        MyApi api1;
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api1 = restAdapter.create(MyApi.class);
//        api1.cancelClinicsAppointment(doctorId, patientId, clinicId, shift, new Callback<String>() {
//            @Override
//            public void success(String jsonObject, Response response) {
//                Toast.makeText(getActivity(), "Deleted Successfully !!!", Toast.LENGTH_SHORT).show();
//                getBackWindows();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                error.printStackTrace();
//            }
//        });
//    }
//
//}
//
//    private void reschedule(Button reschedule)
//    {
//        reschedule.setOnClickListener(new View.OnClickListener() {
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
//    }


}
