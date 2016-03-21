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
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Fragments.ClinicAppointmentFragment;
import com.mindnerves.meidcaldiary.Fragments.ClinicProfileDetails;
import com.mindnerves.meidcaldiary.Fragments.DoctorProfileDetails;
import com.mindnerves.meidcaldiary.Fragments.PatientAppointmentFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.Clinic;
import Model.ClinicDetailVm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


/**
 * Created by MNT on 23-Feb-15.
 */

//Patient Login
public class ClinicListItemAdapter extends ArrayAdapter<ClinicDetailVm> {

    private final List<ClinicDetailVm> clinicDetailVm;
    private final Activity context;
    public String shift = null;
    public Integer clinicId = null;
    public SharedPreferences session;
    ProgressDialog progress;

   public ClinicListItemAdapter(Activity context, List<ClinicDetailVm> clinicDetailVm) {
        super(context, R.layout.clinic_list_item, clinicDetailVm);
        this.context = context;
        this.clinicDetailVm = clinicDetailVm;
   }

    static class ViewHolder {
        private TextView clinicName,clinicLocation,clinicContact;
        private TextView shift1Time,shift2Time,shift3Time,shift1Days;
        private TextView shift2Days,shift3Days,bookOnline1,bookOnline2;
        private TextView bookOnline3,nextAppointment1,nextAppointment2,nextAppointment3;
        private TextView nextChange1,nextChange2,nextChange3,hasNextAppo;
        private TextView nextAppointment1Value,nextAppointment2Value,nextAppointment3Value;
        private TextView notVisited1,notVisited2,notVisited3,visited1,visited2,visited3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null) {
            session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            final String userId = session.getString("sessionID", null);
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.clinic_list_item, null);
            mHolder = new ViewHolder();

            mHolder.clinicName = (TextView) convertView.findViewById(R.id.clinicName);
            mHolder.clinicLocation = (TextView) convertView.findViewById(R.id.clinicLocation);
            mHolder.clinicContact = (TextView) convertView.findViewById(R.id.clinicContact);
            mHolder.hasNextAppo = (TextView) convertView.findViewById(R.id.hasNextAppo);

            mHolder.shift1Time = (TextView) convertView.findViewById(R.id.shift1Time);
            mHolder.shift2Time = (TextView) convertView.findViewById(R.id.shift2Time);
            mHolder.shift3Time = (TextView) convertView.findViewById(R.id.shift3Time);

            mHolder.shift1Days = (TextView) convertView.findViewById(R.id.shift1Days);
            mHolder.shift2Days = (TextView) convertView.findViewById(R.id.shift2Days);
            mHolder.shift3Days = (TextView) convertView.findViewById(R.id.shift3Days);

            mHolder.bookOnline1 = (TextView) convertView.findViewById(R.id.bookOnline1);
            mHolder.bookOnline2 = (TextView) convertView.findViewById(R.id.bookOnline2);
            mHolder.bookOnline3 = (TextView) convertView.findViewById(R.id.bookOnline3);

            mHolder.nextAppointment1 = (TextView) convertView.findViewById(R.id.nextAppointment1);
            mHolder.nextAppointment2 = (TextView) convertView.findViewById(R.id.nextAppointment2);
            mHolder.nextAppointment3 = (TextView) convertView.findViewById(R.id.nextAppointment3);

            mHolder.nextAppointment1Value = (TextView) convertView.findViewById(R.id.nextAppointment1Value);
            mHolder.nextAppointment2Value = (TextView) convertView.findViewById(R.id.nextAppointment2Value);
            mHolder.nextAppointment3Value = (TextView) convertView.findViewById(R.id.nextAppointment3Value);

            mHolder.nextChange1 = (TextView) convertView.findViewById(R.id.nextChange1);
            mHolder.nextChange2 = (TextView) convertView.findViewById(R.id.nextChange2);
            mHolder.nextChange3 = (TextView) convertView.findViewById(R.id.nextChange3);

            mHolder.notVisited1 = (TextView) convertView.findViewById(R.id.notVisited1);
            mHolder.notVisited2 = (TextView) convertView.findViewById(R.id.notVisited2);
            mHolder.notVisited3 = (TextView) convertView.findViewById(R.id.notVisited3);

            mHolder.visited1 = (TextView) convertView.findViewById(R.id.visited1);
            mHolder.visited2 = (TextView) convertView.findViewById(R.id.visited2);
            mHolder.visited3 = (TextView) convertView.findViewById(R.id.visited3);

            mHolder.clinicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    SharedPreferences session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("patient_clinicId", vm.getClinicId());
                    System.out.println("patient_clinicId = "+ vm.getClinicId());
                    editor.commit();
                    Fragment fragment = new ClinicProfileDetails();
                    FragmentManager fragmentManger = context.getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
                }
            });

            // Visited call
            mHolder.visited1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift1", 1);
                }
            });

            mHolder.visited2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift2", 1);
                }
            });

            mHolder.visited3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift3", 1);
                }
            });


            mHolder.notVisited1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift1", 0);
                }
            });

            mHolder.notVisited2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift2", 0);
                }
            });

            mHolder.notVisited3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    progress = ProgressDialog.show(context, "", "Loading...Please wait...");
                    saveVisitedData(vm.getDoctorId(),userId,vm.getClinicId(), "shift3", 0);
                }
            });

            mHolder.nextChange1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift1", vm.getAppointmentTime(), vm.getAppointmentDate());
                }
            });

            mHolder.nextChange2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift2", vm.getAppointmentTime(),vm.getAppointmentDate());
                }
            });

            mHolder.nextChange3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift3",vm.getAppointmentTime(),vm.getAppointmentDate());
                }
            });

            mHolder.bookOnline1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift1",null,null);
                }
            });

            mHolder.bookOnline2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift2",null,null);
                }
            });

            mHolder.bookOnline3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPosition = (Integer) v.getTag();
                    ClinicDetailVm vm = clinicDetailVm.get(newPosition);
                    System.out.println("OnClick Position= "+newPosition);
                    getClinicAppointment(vm.getClinicId(), "shift3",null,null);
                }
            });

            convertView.setTag(mHolder);
            convertView.setTag(R.id.clinicName, mHolder.clinicName);
            convertView.setTag(R.id.bookOnline1,mHolder.bookOnline1);
            convertView.setTag(R.id.bookOnline2,mHolder.bookOnline2);
            convertView.setTag(R.id.bookOnline3,mHolder.bookOnline3);
            convertView.setTag(R.id.visited1,mHolder.visited1);
            convertView.setTag(R.id.visited2,mHolder.visited2);
            convertView.setTag(R.id.visited3,mHolder.visited3);
            convertView.setTag(R.id.notVisited1,mHolder.notVisited1);
            convertView.setTag(R.id.notVisited2,mHolder.notVisited2);
            convertView.setTag(R.id.notVisited3,mHolder.notVisited3);
            convertView.setTag(R.id.nextChange1,mHolder.nextChange1);
            convertView.setTag(R.id.nextChange2,mHolder.nextChange2);
            convertView.setTag(R.id.nextChange3,mHolder.nextChange3);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.clinicName.setTag(position);
        mHolder.bookOnline1.setTag(position);
        mHolder.bookOnline2.setTag(position);
        mHolder.bookOnline3.setTag(position);
        mHolder.visited1.setTag(position);
        mHolder.visited2.setTag(position);
        mHolder.visited3.setTag(position);
        mHolder.notVisited1.setTag(position);
        mHolder.notVisited2.setTag(position);
        mHolder.notVisited3.setTag(position);
        mHolder.nextChange1.setTag(position);
        mHolder.nextChange2.setTag(position);
        mHolder.nextChange3.setTag(position);
        mHolder.clinicName.setText(clinicDetailVm.get(position).getClinicName());
        mHolder.clinicLocation.setText(clinicDetailVm.get(position).getClinicLocation());
        if(clinicDetailVm.get(position).isHasNextAppointment()){
            mHolder.hasNextAppo.setText("true");
        }else{
            mHolder.hasNextAppo.setText("false");
        }

        mHolder.clinicContact.setText(clinicDetailVm.get(position).getContactNumber());
        mHolder.shift1Days.setText(getTextOfDays(clinicDetailVm.get(position).getSlot1().getDays()));
        mHolder.shift2Days.setText(getTextOfDays(clinicDetailVm.get(position).getSlot2().getDays()));
        mHolder.shift3Days.setText(getTextOfDays(clinicDetailVm.get(position).getSlot3().getDays()));
        mHolder.shift1Time.setText(getTimeTextValue(clinicDetailVm.get(position).getSlot1().getStartTimes(), clinicDetailVm.get(position).getSlot1().getEndTimes()));
        mHolder.shift2Time.setText(getTimeTextValue(clinicDetailVm.get(position).getSlot2().getStartTimes(), clinicDetailVm.get(position).getSlot2().getEndTimes()));
        mHolder.shift3Time.setText(getTimeTextValue(clinicDetailVm.get(position).getSlot3().getStartTimes(), clinicDetailVm.get(position).getSlot3().getEndTimes()));

        if (clinicDetailVm.get(position).getSlot3().getStartTimes() == null) {
            mHolder.bookOnline3.setVisibility(View.GONE);
        } else {
            mHolder.bookOnline3.setVisibility(View.VISIBLE);
        }

        if (clinicDetailVm.get(position).getSlot2().getStartTimes() == null) {
            mHolder.bookOnline2.setVisibility(View.GONE);
        } else {
            mHolder.bookOnline2.setVisibility(View.VISIBLE);
        }

        if (clinicDetailVm.get(position).getSlot1().getStartTimes() == null) {
            mHolder.bookOnline1.setVisibility(View.GONE);
        } else {
            mHolder.bookOnline1.setVisibility(View.VISIBLE);
        }

        mHolder.nextAppointment1.setVisibility(View.GONE);
        mHolder.nextAppointment2.setVisibility(View.GONE);
        mHolder.nextAppointment3.setVisibility(View.GONE);
        mHolder.nextChange1.setVisibility(View.GONE);
        mHolder.nextChange2.setVisibility(View.GONE);
        mHolder.nextChange3.setVisibility(View.GONE);

        mHolder.nextAppointment1Value.setVisibility(View.GONE);
        mHolder.nextAppointment2Value.setVisibility(View.GONE);
        mHolder.nextAppointment3Value.setVisibility(View.GONE);

        mHolder.notVisited1.setVisibility(View.GONE);
        mHolder.notVisited2.setVisibility(View.GONE);
        mHolder.notVisited3.setVisibility(View.GONE);

        mHolder.visited1.setVisibility(View.GONE);
        mHolder.visited2.setVisibility(View.GONE);
        mHolder.visited3.setVisibility(View.GONE);


        if(mHolder.hasNextAppo.getText().toString().equals("true")){

            if (clinicDetailVm.get(position).getAppointmentShift().equals("shift1")) {
                mHolder.nextAppointment1.setVisibility(View.VISIBLE);
                mHolder.nextChange1.setVisibility(View.VISIBLE);
                mHolder.nextAppointment1Value.setVisibility(View.VISIBLE);
                mHolder.nextAppointment1Value.setText(clinicDetailVm.get(position).getAppointmentDate() + " " + clinicDetailVm.get(position).getAppointmentTime());
                mHolder.bookOnline1.setVisibility(View.INVISIBLE);
                mHolder.notVisited1.setVisibility(View.VISIBLE);
                mHolder.visited1.setVisibility(View.VISIBLE);

            } else if (clinicDetailVm.get(position).getAppointmentShift().equals("shift2")) {
                mHolder.nextAppointment2.setVisibility(View.VISIBLE);
                mHolder.nextChange2.setVisibility(View.VISIBLE);
                mHolder.nextAppointment2Value.setVisibility(View.VISIBLE);
                mHolder.notVisited2.setVisibility(View.VISIBLE);
                mHolder.visited2.setVisibility(View.VISIBLE);
                mHolder.nextAppointment2Value.setText(clinicDetailVm.get(position).getAppointmentDate() + " " + clinicDetailVm.get(position).getAppointmentTime());
                mHolder.bookOnline2.setVisibility(View.INVISIBLE);
            } else if (clinicDetailVm.get(position).getAppointmentShift().equals("shift3")) {
                mHolder.nextAppointment3.setVisibility(View.VISIBLE);
                mHolder.nextChange3.setVisibility(View.VISIBLE);
                mHolder.nextAppointment3Value.setVisibility(View.VISIBLE);
                mHolder.notVisited3.setVisibility(View.VISIBLE);
                mHolder.visited3.setVisibility(View.VISIBLE);
                mHolder.nextAppointment3Value.setText(clinicDetailVm.get(position).getAppointmentDate() + " " + clinicDetailVm.get(position).getAppointmentTime());
                mHolder.bookOnline3.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    public void getClinicAppointment(String id, String shift, String time, String date){

        Bundle args = new Bundle();
        args.putString("clinicId", id);
        args.putString("clinicShift", shift);
        args.putString("appointmentTime", time);
        args.putString("appointmentDate", date);
        System.out.println("ClinicID= "+id);
        Fragment fragment = new PatientAppointmentFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManger = context.getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details,fragment,"Doctor Consultations").addToBackStack(null).commit();
    }

    public String getTextOfDays(String days){

        if(days == null){
            return  "Mon - Sun";
        }
        if(days.contains("Mon") && days.contains("Tue") && days.contains("Wed")
                && days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && days.contains("Sun")){
            return  "Mon - Sun";
        }else{
            return days;
        }
    }

    public String  getTimeTextValue(String start, String end){
        if(start == null){

            return "No Shift scheduled !!!";
        }else{
            return start+" - "+ end;
        }
    }

    public void saveVisitedData(String doctorId, String patientId, String clinicId, String shift, Integer visited){

        MyApi api;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        api.saveVisitedPatientAppointment(doctorId, patientId,clinicId,shift,visited, new Callback<String>() {
            @Override
            public void success(String str, Response response) {
                progress.dismiss();
                Toast.makeText(context, "Saved Successfully !!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });
    }


}