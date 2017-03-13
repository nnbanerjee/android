package com.medico.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.PatientAppointmentByDoctor;
import com.mindnerves.meidcaldiary.R;

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

    // private RelativeLayout   mainRelative;

    public ClinicPatientAdapter(Activity context, List<DoctorClinicDetails> clinicDetails, PatientAppointmentByDoctor patientAppointments) {
        this.activity = context;
        this.clinicDetails = clinicDetails;
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
        ListView clinicSlots   = (ListView)convertView.findViewById(R.id.clinicSlots);

        DoctorClinicDetails details = clinicDetails.get(position);
        clinicName.setText(details.clinic.clinicName);
        clinicLocation.setText(details.clinic.address);
        clinicContact.setText(details.clinic.landLineNumber.toString());
        if(details.slots != null && details.slots.size() > 0) {
            SlotAppointmentAdapter slotPatientAdapter = new SlotAppointmentAdapter(activity, details, patientAppointments, details.slots);
            clinicSlots.setAdapter(slotPatientAdapter);
        }
        return convertView;
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