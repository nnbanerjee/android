package com.medicohealthcare.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DiagnosticStatusRequest;
import com.medicohealthcare.model.RemovePatientTestRequest;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.model.SummaryResponse;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;
import com.medicohealthcare.view.profile.PatientDiagnosticTests;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 04-11-2015.
 */
public class DiagnosticTestAdapter extends HomeAdapter {
    Activity activity;
    List<SummaryResponse.TestPrescribed> alarms;
    LayoutInflater inflater;
    private int loggedInUserId;

    public DiagnosticTestAdapter(Activity activity, List<SummaryResponse.TestPrescribed> alarms, int userId)
    {
        super(activity);
        this.activity = activity;
        this.alarms = alarms;
        loggedInUserId = userId;
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
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.medicine, null);
                setView(convertView,position);
            }
            else
            {
                setView(convertView,position);
            }
            return convertView;
        }

    private void setView(final View convertView, final int position)
    {
        final TextView name = (TextView)convertView.findViewById(R.id.medicine_name);
        name.setTag(alarms.get(position));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = activity.getIntent().getExtras();
                SummaryResponse.TestPrescribed testPrescribed = (SummaryResponse.TestPrescribed)name.getTag();
                args.putInt(PARAM.DIAGNOSTIC_TEST_ID, testPrescribed.testId);
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new PatientDiagnosticTests();
//                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = ((ParentActivity) activity).getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientDiagnosticTests.class.getName()).addToBackStack(PatientDiagnosticTests.class.getName()).commit();
            }
        });
        name.setText(alarms.get(position).testName);
        convertView.setTag(alarms.get(position));
        ImageView close = (ImageView)convertView.findViewById(R.id.close_button);
        close.setTag(alarms.get(position));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Close buttom clicked");
                final SummaryResponse.TestPrescribed testPrescribed = (SummaryResponse.TestPrescribed)v.getTag();
                new AlertDialog.Builder(convertView.getContext())
                        .setTitle("Delete Diagnostic Test")
                        .setMessage("Are you sure you want to delete this diagnostic test?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                showBusy();
                                RemovePatientTestRequest removeMedicineRequest = new RemovePatientTestRequest(testPrescribed.testId, loggedInUserId);
                                api.removePatientDiagnosticTest(removeMedicineRequest, new Callback<ResponseCodeVerfication>() {
                                    @Override
                                    public void success(ResponseCodeVerfication result, Response response) {
                                        hideBusy();
                                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
                                            Toast.makeText(activity, "Diagnostic Test Removed!!!!!", Toast.LENGTH_SHORT).show();
                                            alarms.remove(testPrescribed);
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        hideBusy();
                                        new MedicoCustomErrorHandler(activity).handleError(error);
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
        ImageView alarm = (ImageView)convertView.findViewById(R.id.alarm_button);
        if(alarms.get(position).reminder.byteValue() == 1)
            alarm.setImageResource(R.drawable.ic_alarm_on_black_24dp);
        else
            alarm.setImageResource(R.drawable.ic_alarm_off_black_24dp);
        alarm.setTag(alarms.get(position));
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final SummaryResponse.TestPrescribed prescribed = (SummaryResponse.TestPrescribed)v.getTag();
                final int currentState = prescribed.reminder.intValue();
                final int postState = currentState==1?0:1;
                api.setDiagnosticTestAlarm(new DiagnosticStatusRequest(prescribed.testId,postState), new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication result, Response response) {
//                        progress.dismiss();
                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
                            Toast.makeText(activity, "Diagnostic Test reminder status updated", Toast.LENGTH_SHORT).show();
                            alarms.get(position).reminder = new Integer(postState).byteValue();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        hideBusy();
                        new MedicoCustomErrorHandler(activity).handleError(error);
                    }
                });
            }
        });    }



}
