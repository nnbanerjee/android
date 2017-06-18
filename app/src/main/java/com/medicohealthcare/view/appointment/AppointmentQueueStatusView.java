package com.medicohealthcare.view.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.DoctorClinicId;
import com.medicohealthcare.model.DoctorClinicQueue;
import com.medicohealthcare.model.DoctorClinicQueueStatus;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AppointmentQueueStatusView extends ParentFragment
{

    TextView que_status,total_value, booked_value, current_value, remaining_value, number_of_appointments_value,
            number_of_appointments_booked_value,current_appointment_number_value, number_of_people_reported_value,
            number_of_people_confirmed_value,number_of_people_hold_value,number_of_appointments_cancelled_value,
            number_of_people_consulted_value,number_of_people_notvisited_value;

    DoctorClinicQueue model;
    DoctorClinicQueueStatus status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.appointment_queue_status, container, false);
        que_status = (TextView) view.findViewById(R.id.que_status);
        total_value = (TextView) view.findViewById(R.id.total_value);
        booked_value = (TextView) view.findViewById(R.id.booked_value);
        current_value = (TextView) view.findViewById(R.id.current_value);
        remaining_value = (TextView) view.findViewById(R.id.remaining_value);
        number_of_appointments_value = (TextView) view.findViewById(R.id.number_of_appointments_value);
        number_of_appointments_booked_value = (TextView) view.findViewById(R.id.number_of_appointments_booked_value);
        current_appointment_number_value = (TextView) view.findViewById(R.id.current_appointment_number_value);
        number_of_people_reported_value = (TextView) view.findViewById(R.id.number_of_people_reported_value);
        number_of_people_confirmed_value = (TextView) view.findViewById(R.id.number_of_people_confirmed_value);
        number_of_people_hold_value = (TextView) view.findViewById(R.id.number_of_people_hold_value);
        number_of_appointments_cancelled_value = (TextView) view.findViewById(R.id.number_of_appointments_cancelled_value);
        number_of_people_consulted_value = (TextView) view.findViewById(R.id.number_of_people_consulted_value);
        number_of_people_notvisited_value = (TextView) view.findViewById(R.id.number_of_people_notvisited_value);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Manage Queue");

    }
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        final Integer doctId = bundle.getInt(DOCTOR_ID);
        final Integer patientId = bundle.getInt(PATIENT_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final Integer doctorclinicId = bundle.getInt(DOCTOR_CLINIC_ID);
        api.getClinicQueueStatusDetails(new DoctorClinicId(doctorclinicId), new Callback<DoctorClinicQueueStatus>()
        {
            @Override
            public void success(DoctorClinicQueueStatus queue, Response response)
            {
                if(queue != null && queue.status.intValue() == 1)
                {
                    status = queue;
                    que_status.setText(queue.queStatus.intValue() == 1?"STARTED":"NOT STARTED");
                    total_value.setText(queue.numberOfAppointments.toString());
                    booked_value.setText(queue.numberOfPeopleBooked.toString());
                    current_value.setText(queue.numberOfPatientConsulted.toString());
                    remaining_value.setText(new Integer(queue.numberOfPeopleBooked.intValue() - queue.numberOfPatientConsulted.intValue() - queue.numberOfPatientNotVisited.intValue()).toString());
                    number_of_appointments_value.setText(queue.numberOfAppointments.toString());
                    number_of_appointments_booked_value.setText(queue.numberOfPeopleBooked.toString());
                    current_appointment_number_value.setText(queue.currentAppointmentNumber.toString());
                    number_of_people_reported_value.setText(queue.numberOfPatientReported.toString());
                    number_of_people_confirmed_value.setText(queue.numberOfPatientConfirmed.toString());
                    number_of_people_hold_value.setText(queue.consultationOnHold.toString());
                    number_of_appointments_cancelled_value.setText(queue.numberOfPeopleCancelled.toString());
                    number_of_people_consulted_value.setText(queue.numberOfPatientConsulted.toString());
                    number_of_people_notvisited_value.setText(queue.numberOfPatientNotVisited.toString());
                }
                else
                    que_status.setText("NOT STARTED");

            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });

        setTitle("Manage Queue");
    }

    public void setModel(DoctorClinicQueue queue)
    {
        this.model = queue;
    }



}
