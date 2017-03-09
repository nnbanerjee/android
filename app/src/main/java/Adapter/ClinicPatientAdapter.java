package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentFragment;
import com.mindnerves.meidcaldiary.Fragments.ClinicProfileDetails;
import com.mindnerves.meidcaldiary.Fragments.FeedbackFragmentClinicAppointment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.medico.application.MyApi;
import Model.Appointment;
import Model.AppointmentId;
import Model.AppointmentSlotsByDoctor;
import Model.ClinicList;
import com.medico.model.ResponseCodeVerfication;
import Model.Slot;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class ClinicPatientAdapter extends ArrayAdapter<AppointmentSlotsByDoctor> {

    private final List<ClinicList> appointmentDetailsObj;
    public final Activity context;
    public String shift = null;
    public Integer clinicId = null;
    public SharedPreferences session;
    private ProgressDialog progress;
    private Global global;
    private MyApi api;
    private List<AppointmentSlotsByDoctor> clinicSlotsByDoctorList;
    private ClinicList clinicListSingleObj;
    private ViewHolder mHolder = null;
    private int clinicPositionGlobal;
    private ClinicPatientAdapter clinicPatientAdapter;
    private View layoutView;
    private LinearLayout contentLinear;
    private LayoutInflater inflator;
    private String patient_email;
    private String doctorId;
    private List<Appointment> appointmentList;
    boolean flag = false;
    int position;
    // private RelativeLayout   mainRelative;

    public ClinicPatientAdapter(Activity context, List<ClinicList> appointmentDetailsObj, List<AppointmentSlotsByDoctor> clinicSlotsByDoctorList) {
        super(context, R.layout.clinic_list_item, clinicSlotsByDoctorList);
        this.context = context;
        this.appointmentDetailsObj = appointmentDetailsObj;
        this.clinicSlotsByDoctorList = clinicSlotsByDoctorList;
        clinicPatientAdapter = this;
        inflator = context.getLayoutInflater();
        mHolder = new ViewHolder();
        global = (Global) context.getApplicationContext();
        session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patient_email = session.getString("patientId", null);
        doctorId = session.getString("id", null);
    }

    static class ViewHolder {
        private TextView clinicName, clinicLocation, clinicContact;
        private TextView shift1Time, shift1Days;
        private TextView bookOnline;
        private TextView nextAppointment1;
        private TextView change;
        private TextView nextAppointment1Value;
        private TextView cancel, visited1,shift1DayText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        this.position=position;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.clinic_list_item, null);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        layoutView = inflator.inflate(R.layout.slot_layout, null, false);
        contentLinear = (LinearLayout) convertView.findViewById(R.id.content_linear);
        //Remove all views from layout
        contentLinear.removeAllViews();

        System.out.println("patient_email = " + patient_email);

        mHolder.clinicName = (TextView) convertView.findViewById(R.id.clinicName);
        mHolder.clinicLocation = (TextView) convertView.findViewById(R.id.clinicLocation);
        mHolder.clinicContact = (TextView) convertView.findViewById(R.id.clinicContact);
        // mainRelative= (RelativeLayout) convertView.findViewById(R.id.main_relative);
        // mHolder.hasNextAppo = (TextView) convertView.findViewById(R.id.hasNextAppo);
        mHolder.shift1Time = (TextView) layoutView.findViewById(R.id.shift1Time);
        mHolder.shift1Days = (TextView) layoutView.findViewById(R.id.shift1Days);
        mHolder.bookOnline = (TextView) layoutView.findViewById(R.id.bookOnline1);
        mHolder.nextAppointment1 = (TextView) layoutView.findViewById(R.id.nextAppointment1);
        mHolder.nextAppointment1Value = (TextView) layoutView.findViewById(R.id.nextAppointment1Value);
        mHolder.change = (TextView) layoutView.findViewById(R.id.nextChange1);
        mHolder.cancel = (TextView) layoutView.findViewById(R.id.notVisited1);
        mHolder.visited1 = (TextView) layoutView.findViewById(R.id.visited1);
        mHolder.shift1DayText= (TextView) layoutView.findViewById(R.id.shift1DayText);


        convertView.setTag(mHolder);
        convertView.setTag(R.id.clinicName, mHolder.clinicName);
        convertView.setTag(R.id.bookOnline1, mHolder.bookOnline);
        convertView.setTag(R.id.visited1, mHolder.visited1);
        convertView.setTag(R.id.notVisited1, mHolder.cancel);

        //Compare clinic ids from two data structures .

        mHolder.clinicName.setTag(position);
        mHolder.clinicName.setText(clinicSlotsByDoctorList.get(position).getClinic().getClinicName());
        mHolder.clinicLocation.setText(clinicSlotsByDoctorList.get(position).getClinic().getAddress());
        mHolder.clinicContact.setText(clinicSlotsByDoctorList.get(position).getClinic().getMobile());

       /* mHolder.nextAppointment1.setVisibility(View.GONE);
        mHolder.nextChange1.setVisibility(View.GONE);
        mHolder.nextAppointment1Value.setVisibility(View.GONE);
        mHolder.notVisited1.setVisibility(View.GONE);
        mHolder.visited1.setVisibility(View.GONE);
        mHolder.nextAppointment1Value.setText("");*/


        //New code for dynamic slot population.
        if (clinicSlotsByDoctorList != null && clinicSlotsByDoctorList.get(position).getSlots() != null && clinicSlotsByDoctorList.get(position).getSlots().size() > 0) {
            List<Slot> slots =clinicSlotsByDoctorList.get(position).getSlots();
            for (int i = 0; i < slots.size(); i++) {
                layoutView = inflator.inflate(R.layout.slot_layout, null, false);
                layoutView.setTag(i);
                if (layoutView.getParent() != null) {
                    System.out.println("Layout parent is ---->" + layoutView.getParent());
                    ((LinearLayout) layoutView.getParent()).removeView(layoutView);
                }
                mHolder.shift1DayText = (TextView) layoutView.findViewById(R.id.shift1DayText);
                mHolder.shift1Time = (TextView) layoutView.findViewById(R.id.shift1Time);
                mHolder.shift1Days = (TextView) layoutView.findViewById(R.id.shift1Days);
                mHolder.bookOnline = (TextView) layoutView.findViewById(R.id.bookOnline1);
                mHolder.nextAppointment1 = (TextView) layoutView.findViewById(R.id.nextAppointment1);
                mHolder.nextAppointment1Value = (TextView) layoutView.findViewById(R.id.nextAppointment1Value);
                mHolder.change = (TextView) layoutView.findViewById(R.id.nextChange1);
                mHolder.cancel = (TextView) layoutView.findViewById(R.id.notVisited1);
                mHolder.visited1 = (TextView) layoutView.findViewById(R.id.visited1);

                mHolder.bookOnline.setTag(position+"#"+i);
                mHolder.cancel.setTag(position+"#"+i);
                mHolder.visited1.setTag(position+"#"+i);
                mHolder.change.setTag(position+"#"+i);

                if (slots.get(i).getDaysOfWeek() != null)
                    mHolder.shift1Days.setText(getTextOfDays(slots.get(i).getDaysOfWeek()));
                else
                    mHolder.shift1Days.setText("NA");
                if (slots.get(i).getSlotNumber() != null) {
                    mHolder.shift1DayText.setText("SLOT " + slots.get(i).getSlotNumber() + " : ");
                }

                String slot0StartTime = slots.get(i).getStartTime();
                String slot0EndTime = slots.get(i).getEndTime();

                if (slot0StartTime != null && slot0EndTime != null)
                    mHolder.shift1Time.setText(getTimeTextValue(UtilSingleInstance.getTimeFromLongDate(slot0StartTime), UtilSingleInstance.getTimeFromLongDate(slot0EndTime)));
                else
                    mHolder.shift1Time.setText("NA");


                if (slots.get(i).getStartTime() == null) {
                    mHolder.bookOnline.setVisibility(View.GONE);
                } else {
                    mHolder.bookOnline.setVisibility(View.VISIBLE);
                }

                if (appointmentDetailsObj != null &&   appointmentDetailsObj .size() > 0) {
                for (int j = 0; j < appointmentDetailsObj.size(); j++) {
                    if (slots.get(i).getDoctorClinicId().equalsIgnoreCase(appointmentDetailsObj.get(j).getDoctorClinicId())) {
                        clinicListSingleObj = appointmentDetailsObj.get(j);
                        mHolder.visited1.setTag(clinicListSingleObj.getAppointments().get(0).getAppointmentId());
                        long lng = Long.parseLong(appointmentDetailsObj.get(j).getAppointments().get(0).getDateTime());
                        if (lng < Calendar.getInstance().getTimeInMillis()) { //This arrangement for feedback
                            mHolder.nextAppointment1Value.setText(
                                    UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(
                                            appointmentDetailsObj.get(j).getAppointments().get(0).getDateTime()));
                            mHolder.nextAppointment1.setVisibility(View.VISIBLE);
                            mHolder.change.setVisibility(View.INVISIBLE);
                            mHolder.nextAppointment1Value.setVisibility(View.VISIBLE);
                            mHolder.nextAppointment1Value.setTextColor(context.getResources().getColor(R.color.dim_grey));
                            mHolder.bookOnline.setVisibility(View.INVISIBLE);
                            mHolder.cancel.setVisibility(View.GONE);
                            mHolder.visited1.setVisibility(View.VISIBLE);
                        } else { //If the appointmnet is still there ahead of current time
                            mHolder.nextAppointment1Value.setText(
                                    UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(
                                            appointmentDetailsObj.get(j).getAppointments().get(0).getDateTime()));
                            mHolder.nextAppointment1.setVisibility(View.VISIBLE);
                            mHolder.change.setVisibility(View.VISIBLE);
                            mHolder.nextAppointment1Value.setVisibility(View.VISIBLE);
                            mHolder.bookOnline.setVisibility(View.GONE);
                            mHolder.cancel.setVisibility(View.VISIBLE);
                            mHolder.visited1.setVisibility(View.GONE);
                        }
                    } else {
                        mHolder.nextAppointment1.setVisibility(View.GONE);
                        mHolder.change.setVisibility(View.GONE);
                        mHolder.nextAppointment1Value.setVisibility(View.GONE);
                        mHolder.cancel.setVisibility(View.GONE);
                        mHolder.bookOnline.setVisibility(View.VISIBLE);
                        mHolder.visited1.setVisibility(View.GONE);
                        mHolder.nextAppointment1Value.setText("");
                    }
                }
            }else{
                    mHolder.nextAppointment1.setVisibility(View.GONE);
                    mHolder.change.setVisibility(View.GONE);
                    mHolder.nextAppointment1Value.setVisibility(View.GONE);
                    mHolder.cancel.setVisibility(View.GONE);
                    mHolder.visited1.setVisibility(View.GONE);
                    mHolder.nextAppointment1Value.setText("");
            }
                addAllEventListeners();


                contentLinear.addView(layoutView);
            }

        }else{
            mHolder.nextAppointment1.setVisibility(View.GONE);
            mHolder.change.setVisibility(View.GONE);
            mHolder.nextAppointment1Value.setVisibility(View.GONE);
            mHolder.cancel.setVisibility(View.GONE);
            mHolder.visited1.setVisibility(View.GONE);
            mHolder.nextAppointment1Value.setText("");
        }
        return convertView;
    }
    public void addAllEventListeners(){
        mHolder.visited1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clinicPosition="";
                String[] tag= ((String) v.getTag()).split("#");
              //  String clinicPosition = (String) v.getTag();
                int position= Integer.parseInt(tag[0]);

                if(tag!=null && tag.length>1)
                  clinicPosition= tag[1];

               // AppointmentSlotsByDoctor vm = clinicSlotsByDoctorList.get(clinicPosition);
              //  progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                //saveVisitedData(doctorId, patient_email, vm.getClinic().getIdClinic(), "shift1", 1);
               // clinicSlotsByDoctorList.get(clinicPosition).getClinic().getAppointments().get
                Bundle args = new Bundle();
                args.putString("selectedAppointmentId", clinicPosition);
                Fragment fragment = new FeedbackFragmentClinicAppointment();
                fragment.setArguments(args);
                FragmentManager fragmentManger = context.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();

                //show pop up
            }
        });


        //Cancel appointment
        mHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clinicPositionGlobal = (Integer) v.getTag();

                String[] tag= ((String) v.getTag()).split("#");

               final int position= Integer.parseInt(tag[0]);
                clinicPositionGlobal= Integer.parseInt(tag[1]);
                // AppointmentSlotsByDoctor vm = clinicSlotsByDoctorList.get(clinicPositionGlobal);
                //saveVisitedData(doctorId, patient_email, vm.getClinic().getIdClinic(), "shift1", 0);

                // String apptId= getAppointmentForSelectedSlot( clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(),clinicSlotsByDoctorList.get(position).getSlots().get(clinicPositionGlobal).getSlotNumber() )
                String apptId = appointmentDetailsObj.get(position).getAppointments().get(clinicPositionGlobal).getAppointmentId();
                //Retrofit Initialization
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(context.getResources().getString(R.string.base_url))
                        .setClient(new OkClient())
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                api = restAdapter.create(MyApi.class);
                progress = ProgressDialog.show(context, "", context.getResources().getString(R.string.loading_wait));
                api.cancelAppointment(new AppointmentId(apptId), new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication responseCodeVerfication, Response response) {
                        System.out.println(response);
                        progress.dismiss();
                        Toast.makeText(context.getApplicationContext(), "Successfully cancelled Appointment!", Toast.LENGTH_LONG).show();
                        if (appointmentDetailsObj.get(clinicPositionGlobal).getSlotNumber().equals(1)) {
                            mHolder.nextAppointment1.setVisibility(View.INVISIBLE);
                            mHolder.change.setVisibility(View.GONE);
                            mHolder.nextAppointment1Value.setVisibility(View.INVISIBLE);
                            mHolder.nextAppointment1Value.setText(UtilSingleInstance.getInstance().getDateFormattedInStringFormatUsingLong(appointmentDetailsObj.get(position).getAppointments().get(clinicPositionGlobal).getDateTime()));
                            mHolder.bookOnline.setVisibility(View.VISIBLE);
                            mHolder.cancel.setVisibility(View.INVISIBLE);
                            mHolder.visited1.setVisibility(View.GONE);

                            clinicPatientAdapter.notifyDataSetInvalidated();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(context.getApplicationContext(), R.string.Failed, Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        mHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tag= ((String) v.getTag()).split("#");

               int position= Integer.parseInt(tag[0]);
                int slotPosition= Integer.parseInt(tag[1]);
                Slot selectedSlot = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition);

                String slot0StartTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getStartTime();
                String slot0EndTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getEndTime();
                Calendar startTimeCal = Calendar.getInstance();

                startTimeCal.set(Calendar.HOUR, 0);
                startTimeCal.set(Calendar.MINUTE, 0);
                startTimeCal.set(Calendar.SECOND, 0);
                startTimeCal.set(Calendar.AM_PM, Calendar.AM);

                slot0StartTime = "" + startTimeCal.getTimeInMillis();

                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.set(Calendar.HOUR, 23);
                endTimeCal.set(Calendar.MINUTE, 59);
                endTimeCal.set(Calendar.SECOND, 0);
                startTimeCal.set(Calendar.AM_PM, Calendar.PM);
                slot0EndTime = "" + endTimeCal.getTimeInMillis();

                // ClinicList vm = appointmentDetailsObj.get(clinicPosition);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                //getClinicAppointment(vm.getClinic().getIdClinic(), "shift1", null, null);
                getClinicAppointment(selectedSlot, doctorId, clinicSlotsByDoctorList.get(position).getClinic().getDoctorClinicId(), formattedDate, slot0StartTime, slot0EndTime, clinicSlotsByDoctorList.get(position).getClinic().getClinicName(),
                        clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(), clinicListSingleObj,true);
            }
        });


        mHolder.bookOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tag= ((String) v.getTag()).split("#");

                int position= Integer.parseInt(tag[0]);
                int slotPosition= Integer.parseInt(tag[1]);
                Slot selectedSlot = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition);

                String slot0StartTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getStartTime();
                String slot0EndTime = clinicSlotsByDoctorList.get(position).getSlots().get(slotPosition).getEndTime();

                SharedPreferences.Editor editor = session.edit();
                Gson gson = new Gson();
                String json = gson.toJson(selectedSlot);
                editor.putString("SelectedSlotDetailsWithClinic", json);
                editor.putInt("selectedSlotNo", slotPosition);
                editor.commit();


                Calendar startTimeCal = Calendar.getInstance();

                startTimeCal.set(Calendar.HOUR, 0);
                startTimeCal.set(Calendar.MINUTE, 0);
                startTimeCal.set(Calendar.SECOND, 0);
                startTimeCal.set(Calendar.AM_PM, Calendar.AM);

                slot0StartTime = "" + startTimeCal.getTimeInMillis();

                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.set(Calendar.HOUR, 23);
                endTimeCal.set(Calendar.MINUTE, 59);
                endTimeCal.set(Calendar.SECOND, 0);
                startTimeCal.set(Calendar.AM_PM, Calendar.PM);
                slot0EndTime = "" + endTimeCal.getTimeInMillis();

                // ClinicList vm = appointmentDetailsObj.get(clinicPosition);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());

                getClinicAppointment(selectedSlot, doctorId, clinicSlotsByDoctorList.get(position).getClinic().getDoctorClinicId(), formattedDate, slot0StartTime,
                        slot0EndTime, clinicSlotsByDoctorList.get(position).getClinic().getClinicName(), clinicSlotsByDoctorList.get(position).getClinic().getIdClinic(), clinicListSingleObj,false);
            }
        });


        mHolder.clinicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "ClinicPatientAdapter");
                int clinicPosition = (Integer) v.getTag();
                ClinicList vm = appointmentDetailsObj.get(clinicPosition);
                System.out.println("ClinicID= " + vm.getClinic().getIdClinic());
                SharedPreferences.Editor editor = session.edit();
                // global.setAllDoctorClinicList(appointmentDetailsObj);
                editor.putString("patient_clinicId", vm.getClinic().getIdClinic());
                editor.commit();
                Fragment fragment = new ClinicProfileDetails();
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = context.getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
    }
    public Appointment getAppointmentForSelectedSlot(String doctorClinicId){
        Appointment aapt=null;
        for (int i = 0; i < appointmentDetailsObj.size(); i++) {

            if (doctorClinicId != null
                    && doctorClinicId.toString().equalsIgnoreCase(appointmentDetailsObj.get(i).getDoctorClinicId())  ) {
               aapt=appointmentDetailsObj.get(i).getAppointments().get(i);
            }
        }

        return aapt;
    }
    //Clicked on book online button
    public void getClinicAppointment(Slot selectedSlot, String doctorId, String doctorClinicId, String dateinmilis, String startTime, String endTime, String clinicName ,String clinicId,ClinicList selectedClinic,boolean reschedule) {
        System.out.println("Book Online clicked::::::::::::::::");
        SharedPreferences.Editor editor = session.edit();
        // global.setAllDoctorClinicList(appointmentDetailsObj);
        Gson gson = new Gson();
        String json = gson.toJson(selectedClinic);
        editor.putString("selectedClinic", json);
        editor.commit();


        Bundle args = new Bundle();
        args.putString("doctorId", doctorId);
        args.putString("doctorClinicId", doctorClinicId);
        args.putString("appointmentDate", dateinmilis);
        args.putString("clinicId", clinicId);

        args.putString("startTime", startTime);
        args.putString("endTime", endTime);
        args.putString("clinicName", clinicName);
        args.putString("fragment", "ClinicPatientAdapter");

            args.putBoolean("reschedule", reschedule);
        Fragment fragment = new ClinicAppointmentFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManger = context.getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
    }

    public String getTextOfDays(String days) {


        if (days == null || days.equalsIgnoreCase("0,1,2,3,4,5,6")) {
            return "Mon - Sun";
        }
        if (days.contains("0") && days.contains("1") && days.contains("2")
                && days.contains("3") && days.contains("4") && days.contains("5") && days.contains("6")) {
            return "Mon - Sun";
        } else {
            String allDays = "";
            if (days.contains("0")) {
                allDays = allDays + "MON";
            }
            if (days.contains("1")) {
                allDays = allDays + ",TUE";
            }
            if (days.contains("2")) {

                if (days.contains("0") && days.contains("1"))
                    allDays = "MON-WED";
                else
                    allDays = allDays + ",WED";
            }
            if (days.contains("3")) {
                if (days.contains("0") && days.contains("1") && days.contains("2"))
                    allDays = "MON-THU";
                else
                    allDays = allDays + ",THU";
            }
            if (days.contains("4")) {
                if (days.contains("0") && days.contains("1") && days.contains("2") && days.contains("3"))
                    allDays = "MON-FRI";
                else

                    allDays = allDays + ",FRI";
            }
            if (days.contains("5")) {
                if (days.contains("0") && days.contains("1") && days.contains("2") && days.contains("3"))
                    allDays = "MON-SAT";
                else

                    allDays = allDays + ",SAT";
            }
            if (days.contains("6")) {
                allDays = allDays + ",SUN";
            }


            return allDays;
        }
    }

    public String getTimeTextValue(String start, String end) {
        if (start == null) {

            return "No Shift scheduled !!!";
        } else {
            return start + " - " + end;
        }
    }

    public void saveVisitedData(String doctorId, String patientId, String clinicId, String shift, Integer visited) {

        MyApi api;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);


        api.saveVisitedPatientAppointment(doctorId, patientId, clinicId, shift, visited, new Callback<String>() {
            @Override
            public void success(String str, Response response) {
                progress.dismiss();
                Toast.makeText(context, "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


}