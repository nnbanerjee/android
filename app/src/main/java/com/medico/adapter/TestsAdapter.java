package com.medico.adapter;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SummaryResponse.TestPrescribed;
import com.mindnerves.meidcaldiary.Fragments.AddDiagnosticTest;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Application.MyApi;
import Model.RemovePatientTestRequest;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**`
 * Created by Narendra on 14-03-2016.
 */
public class TestsAdapter extends BaseAdapter {

    /**
     * Created by User on 04-11-2015.
     */
    Activity activity;
    List<TestPrescribed> alarms;
    LayoutInflater inflater;
    ViewHolder mHolder = null;
    int loggedInUserId;
//    ReminderVM reminder;
    MyApi api;
    private String doctorId;
    SharedPreferences session;
    ProgressDialog progress;
//    private String type;

    public TestsAdapter(Activity activity, List<TestPrescribed> alarms, int loggedInUserId) {
        this.activity = activity;
        this.alarms = alarms;
        this.loggedInUserId = loggedInUserId;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", "0");
//        type = UtilSingleInstance.getUserType(session.getString("loginType", null));
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(activity.getString(R.string.base_url))
                    .setClient(new OkClient())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = restAdapter.create(MyApi.class);
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.medicine, null);
            mHolder = new ViewHolder();

            //{"medicineId":12,"medicineName":"Azeethromycin","startDateTime":1457534000,"endDateTime":1889534000,"reminder":1}
            mHolder.medicineName = (TextView) convertView.findViewById(R.id.medicine_name);
            mHolder.alarm = (ImageView) convertView.findViewById(R.id.alarm_button);
            mHolder.remove = (ImageView) convertView.findViewById(R.id.close_button);
            if (alarms.get(position) != null && alarms.get(position).testName != null && alarms.get(position).testName.equals("No Tests")) {
                mHolder.medicineName.setText(alarms.get(position).testName);
                mHolder.alarm.setVisibility(View.GONE);
                mHolder.remove.setVisibility(View.GONE);
            } else
                mHolder.medicineName.setText(alarms.get(position).testName);
            mHolder.alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int getPosition = (Integer) v.getTag();
                    TestPrescribed medicineVm = alarms.get(getPosition);
                    if (!medicineVm.testName.equals("No Tests")) {
                        Bundle args = new Bundle();
                      /*  args.putString("visitedDate", medicineVm.g.getVisitDate());
                        args.putString("visit", medicineVm.visitType);
                        args.putString("referedBy", medicineVm.referredBy);
                        args.putString("symptomsValue", medicineVm.symptoms);
                        args.putString("diagnosisValue", medicineVm.diagnosis);
                        args.putString("testPrescribedValue", medicineVm.testsPrescribed);*/
                        args.putString("medicinName", medicineVm.testName);

                        Fragment fragment = new AddDiagnosticTest();
                        fragment.setArguments(args);
                        FragmentManager fragmentManger = activity.getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    }
                }
            });
           // convertView.setOnClickListener(new View.OnClickListener() {
          ///      @Override
           //     public void onClick(View v) {
                   // int getPosition = (Integer) v.getTag();
                //    TestPrescribed medicineVm = alarms.get(getPosition);
                  //  if (!medicineVm.getTestName().equals("No Test")) {
                  //      Bundle args = new Bundle();
                 //       args.putString("state", "EDIT");

                      /*  args.putString("testName", medicineVm.getTestName());
                        args.putString("testId", medicineVm.getTestId());
                        args.putString("referedBy", medicineVm.);
                        args.putString("clinicId", medicineVm.getc());
                        args.putString("medicineId", medicineVm.get());
                        args.putString("DoctorInstruction", medicineVm.get);
*/
/*
                        addPatientMedicineSummary.setDoctorInstruction(doctorInstructionValue.getText().toString());
                        if (medicineReminderBtn.isChecked())
                            addPatientMedicineSummary.setReminder("1");
                        else
                            addPatientMedicineSummary.setReminder("0");
                        addPatientMedicineSummary.setTestName(textNameEdit.getText().toString());
                        addPatientMedicineSummary.setDatetime("" + calStartDate);
                        addPatientMedicineSummary.setAppointmentId(appointMentId);
                        addPatientMedicineSummary.setReferredId(referredBy);
                        if(!addNewFlag)
                            addPatientMedicineSummary.setTestId(testId);
                        addPatientMedicineSummary.setClinicId(clinicId);
                        addPatientMedicineSummary.setLoggedinUserId("" + doctorId);
                        addPatientMedicineSummary.setUserType(UtilSingleInstance.getUserType(type));*/



                   //     Fragment fragment = new AddDiagnosticTest();
                  //      fragment.setArguments(args);
                 //       FragmentManager fragmentManger = activity.getFragmentManager();
                //        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
              //      }
              //  }
           // });

            mHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //{"medicineId":8,"loggedinUserId":104,"userType":1}
                    int getPosition = (Integer) v.getTag();
                    final TestPrescribed medicineVm = alarms.get(getPosition);
                    if (!medicineVm.testName.equals("No Tests")) {
                        progress = ProgressDialog.show(activity, "", "getResources().getString(R.string.loading_wait)");
                        // System.out.println("alarm size= "+medicineVm.alarms.size());
                        // reminder.alarmReminderVMList = medicineVm.alarms;

                        RemovePatientTestRequest removePatientTestRequest = new RemovePatientTestRequest(alarms.get(getPosition).testId,loggedInUserId );


                            api.removePatientDiagnosticTest(removePatientTestRequest, new Callback<ResponseCodeVerfication>() {
                                @Override
                                public void success(ResponseCodeVerfication result, Response response) {
                                    progress.dismiss();
                                    if (result.getStatus().equalsIgnoreCase("1")) {
                                        Toast.makeText(activity, "Test Removed!!!!!", Toast.LENGTH_SHORT).show();
                                        alarms.remove(medicineVm);
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    progress.dismiss();
                                    error.printStackTrace();
                                    Toast.makeText(activity, "Failed to remove test", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
            });
            convertView.setTag(mHolder);
            convertView.setTag(R.id.medicine_name, mHolder.medicineName);
            convertView.setTag(R.id.alarm_button, mHolder.alarm);
            convertView.setTag(R.id.close_button, mHolder.remove);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.medicineName.setTag(position);
        mHolder.alarm.setTag(position);
        mHolder.remove.setTag(position);
        //convertView.setTag(position);
        return convertView;
    }


    static class ViewHolder {
        TextView medicineName;
        ImageView alarm, remove;
    }


}
