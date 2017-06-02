package com.medicohealthcare.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.medicohealthcare.adapter.ClinicPatientAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicDetails;
import com.medicohealthcare.model.DoctorId;
import com.medicohealthcare.model.DoctorIdPatientId;
import com.medicohealthcare.model.PatientAppointmentByDoctor;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class DoctorPatientClinicAppointmentListView extends ParentFragment {

    ListView clinicListView;
    List<DoctorClinicDetails> clinicDetails;
    PatientAppointmentByDoctor clinicPatientAppointmentsObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view, container, false);
        clinicListView = (ListView)view.findViewById(R.id.doctorListView);


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
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
                        if(clinicDetails != null && clinicDetails.size() > 0) {
                            ClinicPatientAdapter clinicListItemAdapter = new ClinicPatientAdapter(getActivity(), clinicDetails, null);
                            clinicListView.setAdapter(clinicListItemAdapter);
                        }
                        hideBusy();
                        new MedicoCustomErrorHandler(getActivity()).handleError(error);
                    }
                });

                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });


    }


}
