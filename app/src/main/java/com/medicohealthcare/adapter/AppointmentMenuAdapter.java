package com.medicohealthcare.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.medicohealthcare.application.R;

/**
 * Created by Narendra on 10-03-2017.
 */

public class AppointmentMenuAdapter extends BaseAdapter
{

    Activity activity;
//    ProgressDialog progress;
//    DoctorClinicDetails.ClinicSlots clinicSlot;
//    List<PatientAppointmentByDoctor.Appointments> patientAppointments;
    ClinicAppointmentScheduleAdapter.AppointmentHolder model;
    String[] manageAppointment = {"Add","Cancel","Reschedule","Feedback"};

    // private RelativeLayout   mainRelative;

    public AppointmentMenuAdapter(Activity context, ClinicAppointmentScheduleAdapter.AppointmentHolder model) {
        this.activity = context;
        this.model = model;
//        this.clinicSlot = clinicSlot;
//        this.patientAppointments = patientAppointments;

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
        return manageAppointment[position];
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
        return manageAppointment.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView view = new ImageView(activity);
        view.setImageResource(R.drawable.ic_reorder_black_24dp);
        return view;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.list_view,null);
        ListView listview = (ListView)view.findViewById(R.id.doctorListView);
        listview.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, manageAppointment));
        return listview.getAdapter().getView(position,convertView,parent);
//        TextView text = new TextView(activity);
//        text.setTextColor(Color.BLACK);
//        text.setText(manageAppointment[position]);
//        text.setWidth(300);
//        text.setMar(20);
//        text.setRight(20);
//        text.setTop(20);
//        text.setBottom(20);
//        return text;
    }

//    private void bookOnline(Button bookonline, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details)
//    {
//        bookonline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = activity.getIntent().getExtras();
//                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
//                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
//
//                bundle.putInt(PARAM.CLINIC_ID, clinicDetails.clinic.idClinic);
//                bundle.putString(PARAM.CLINIC_NAME,clinicDetails.clinic.clinicName);
//                bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
//                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,details.doctorClinicId);
//                bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
//                bundle.putLong(PARAM.SLOT_START_DATETIME,details.startTime);
//                bundle.putLong(PARAM.SLOT_END_DATETIME,details.endTime);
//                activity.getIntent().putExtras(bundle);
//                ParentFragment fragment = new ClinicDoctorAppointmentFragment();
//                ((ManagePatientProfile)activity).fragmentList.add(fragment);
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManger = activity.getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service,fragment,"Doctor Consultations").addToBackStack(null).commit();
//
//
//            }
//        });
//    }
//    private void rescheduleAppointment(Button reschedule, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
//    {
//        reschedule.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = activity.getIntent().getExtras();
//                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
//                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
//                bundle.putInt(PARAM.APPOINTMENT_ID,appointments.appointmentId);
//                bundle.putLong(PARAM.APPOINTMENT_DATETIME, appointments.dateTime);
//                bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, appointments.sequenceNumber);
//                bundle.putInt(PARAM.CLINIC_ID, clinicDetails.clinic.idClinic);
//                bundle.putString(PARAM.CLINIC_NAME,clinicDetails.clinic.clinicName);
//                bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
//                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,details.doctorClinicId);
//                bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
//                bundle.putLong(PARAM.SLOT_START_DATETIME,details.startTime);
//                bundle.putLong(PARAM.SLOT_END_DATETIME,details.endTime);
//                activity.getIntent().putExtras(bundle);
//                ParentFragment fragment = new ClinicDoctorAppointmentFragment();
//                ((ManagePatientProfile)activity).fragmentList.add(fragment);
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManger = activity.getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service,fragment,"Doctor Consultations").addToBackStack(null).commit();
//
//
//            }
//        });
//    }
//
//    private void feedbackAppointment(Button feedback, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
//    {
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = activity.getIntent().getExtras();
//                DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
//                String shiftDateTime = formatTime.format(details.startTime) +" - " + formatTime.format(details.endTime);
//                bundle.putInt(PARAM.APPOINTMENT_ID,appointments.appointmentId);
//                bundle.putLong(PARAM.APPOINTMENT_DATETIME, appointments.dateTime);
//                bundle.putInt(PARAM.APPOINTMENT_SEQUENCE_NUMBER, appointments.sequenceNumber);
//                bundle.putInt(PARAM.CLINIC_ID, clinicDetails.clinic.idClinic);
//                bundle.putString(PARAM.CLINIC_NAME,clinicDetails.clinic.clinicName);
//                bundle.putString(PARAM.SLOT_TIME, shiftDateTime);
//                bundle.putInt(PARAM.DOCTOR_CLINIC_ID,details.doctorClinicId);
//                bundle.putInt(PARAM.SLOT_VISIT_DURATION,details.visitDuration);
//                bundle.putLong(PARAM.SLOT_START_DATETIME,details.startTime);
//                bundle.putLong(PARAM.SLOT_END_DATETIME,details.endTime);
//                activity.getIntent().putExtras(bundle);
//                ParentFragment fragment = new FeedbackFragmentClinicAppointment();
//                ((ManagePatientProfile)activity).fragmentList.add(fragment);
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManger = activity.getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service,fragment,"Doctor Consultations").addToBackStack(null).commit();
//
//
//            }
//        });
//    }
//    private void cancelAppointment(Button cancel, final DoctorClinicDetails clinicDetails, final DoctorClinicDetails.ClinicSlots details, final PatientAppointmentByDoctor.Appointments appointments)
//    {
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Close buttom clicked");
//                new AlertDialog.Builder(activity)
//                        .setTitle("Delete Appointment")
//                        .setMessage("Are you sure you want to delete this appointment?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                                progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
//                                RestAdapter restAdapter = new RestAdapter.Builder()
//                                        .setEndpoint(activity.getString(R.string.base_url))
//                                        .setClient(new OkClient())
//                                        .setLogLevel(RestAdapter.LogLevel.FULL)
//                                        .build();
//                                MyApi api = restAdapter.create(MyApi.class);
//                                api.cancelAppointment(new AppointmentId1(appointments.appointmentId), new Callback<AppointmentResponse>() {
//                                    @Override
//                                    public void success(AppointmentResponse result, Response response) {
//                                        progress.dismiss();
////                                        if (result.getStatus().equalsIgnoreCase("1")) {
//                                        Toast.makeText(activity, "Medicine Removed!!!!!", Toast.LENGTH_SHORT).show();
//                                        adapter.notifyDataSetChanged();
////                                        }
//                                    }
//
//                                    @Override
//                                    public void failure(RetrofitError error) {
//                                        progress.dismiss();
//                                        error.printStackTrace();
//                                        Toast.makeText(activity, "Failed to remove medicine", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
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
//            }
//        });
//    }
//
//    public void saveVisitedData(String doctorId, String patientId, String clinicId, String shift, Integer visited) {
//
//        MyApi api;
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(activity.getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
//
//
//        api.saveVisitedPatientAppointment(doctorId, patientId, clinicId, shift, visited, new Callback<String>() {
//            @Override
//            public void success(String str, Response response) {
//                progress.dismiss();
//                Toast.makeText(activity, "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                progress.dismiss();
//                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
//                error.printStackTrace();
//            }
//        });
//    }
}
