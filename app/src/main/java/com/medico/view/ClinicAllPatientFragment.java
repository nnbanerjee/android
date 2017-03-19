package com.medico.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.medico.adapter.ClinicPatientAdapter;
import com.medico.model.DoctorClinicDetails;
import com.medico.model.DoctorId;
import com.medico.model.DoctorIdPatientId;
import com.medico.model.PatientAppointmentByDoctor;
import com.medico.application.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ClinicAllPatientFragment extends ParentFragment {

    ListView clinicListView;
    ProgressDialog progress;
    List<DoctorClinicDetails> clinicDetails;
    PatientAppointmentByDoctor clinicPatientAppointmentsObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_profile_fragment, container, false);
        clinicListView = (ListView)view.findViewById(R.id.clinicListView);


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctorId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        api.getClinicsByDoctor1(new DoctorId(doctorId.toString()), new Callback<List<DoctorClinicDetails>>() {
            @Override
            public void success(List<DoctorClinicDetails> clinicDetailsreturn, Response response) {

                clinicDetails = clinicDetailsreturn;
                api.getPatientAppointmentsByDoctor1(new DoctorIdPatientId(doctorId, patientId), new Callback<PatientAppointmentByDoctor>() {
                    @Override
                    public void success(PatientAppointmentByDoctor clinicDetailVm, Response response) {


                        clinicPatientAppointmentsObj = clinicDetailVm;
                        if(clinicDetails != null && clinicDetails.size() > 0) {
                            ClinicPatientAdapter clinicListItemAdapter = new ClinicPatientAdapter(getActivity(), clinicDetails, clinicPatientAppointmentsObj);
                            clinicListView.setAdapter(clinicListItemAdapter);

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                        if(clinicDetails != null && clinicDetails.size() > 0) {
                            ClinicPatientAdapter clinicListItemAdapter = new ClinicPatientAdapter(getActivity(), clinicDetails, null);
                            clinicListView.setAdapter(clinicListItemAdapter);
                        }
                    }
                });


                //[{"clinicId":2,"clinicName":"demo2","slots":[{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5},{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,4,5,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5},{"slotNumber":1,"name":"shift1","daysOfWeek":"0,1,2,3,4,5,6","startTime":-62072762400000,"endTime":-62072748000000,"numberOfAppointmentsForToday":5}],"upcomingAppointment":null,"lastAppointmentl":null}]

                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });


    }

//    public void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            ListView appointments   = (ListView)view.findViewById(R.id.clinicSlots);
//            setListViewHeightBasedOnChildren2(appointments);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//            view.setMinimumHeight(view.getMeasuredHeight());
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.setMinimumHeight(totalHeight);
//        listView.requestLayout();
//    }
//    public void setListViewHeightBasedOnChildren2(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            ListView appointments   = (ListView)view.findViewById(R.id.clinicAppointments);
//            setListViewChildHeightBasedOnChildren(appointments);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//            view.setMinimumHeight(view.getMeasuredHeight());
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.setMinimumHeight(totalHeight);
////        listView.requestLayout();
//    }
//    public void setListViewChildHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//            view.setMinimumHeight(view.getMeasuredHeight());
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.setMinimumHeight(totalHeight);
////        listView.requestLayout();
//    }


}
