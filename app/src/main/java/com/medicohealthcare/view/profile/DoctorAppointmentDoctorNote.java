package com.medicohealthcare.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.AppointmentId1;
import com.medicohealthcare.model.DoctorNotes;
import com.medicohealthcare.model.ResponseCodeVerfication;
import com.medicohealthcare.model.SearchParameter;
import com.medicohealthcare.model.Symptom;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentActivity;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentDoctorNote extends ParentFragment {

    MultiAutoCompleteTextView symptomsValue,diagnosisValue;
    EditText doctorNotes;
    DoctorNotes doctorNotesModel = new DoctorNotes();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_doctor_note, container,false);
        setHasOptionsMenu(true);
        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        doctorNotes = (EditText) view.findViewById(R.id.doctorNotes);
        symptomsValue = (MultiAutoCompleteTextView)view.findViewById(R.id.symptomsValue);
        Symptom[] options = {};
        ArrayAdapter<Symptom> symptomAdapter = new ArrayAdapter<Symptom>(getActivity(), android.R.layout.simple_dropdown_item_1line,options);
        symptomsValue.setAdapter(symptomAdapter);
        symptomsValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        symptomsValue.setThreshold(1);
        symptomsValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1);
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSymptom(new SearchParameter(searchText, 1, 1, 10, 1), new Callback<List<Symptom>>() {
                        @Override
                        public void success(List<Symptom> symptomList, Response response)
                        {
                            ArrayAdapter array = (ArrayAdapter<Symptom>)symptomsValue.getAdapter();
                            array.clear();
                            array.addAll(symptomList);
                            array.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            hideBusy();
//                            new MedicoCustomErrorHandler(getActivity(),false).handleError(error);
                        }
                    });
                }

            }
        });
        diagnosisValue = (MultiAutoCompleteTextView)view.findViewById(R.id.diagnosisValue);
        Symptom[] options1 = {};
        ArrayAdapter<Symptom> diagnosticAdapter = new ArrayAdapter<Symptom>(getActivity(), android.R.layout.simple_dropdown_item_1line,options1);
        diagnosisValue.setAdapter(diagnosticAdapter);
        diagnosisValue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        diagnosisValue.setThreshold(1);
        diagnosisValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().substring(s.toString().lastIndexOf(',')+1);
                if(searchText.length() > 0 )
                {
                    api.searchAutoFillSymptom(new SearchParameter(searchText, 1, 1, 10, 2), new Callback<List<Symptom>>() {
                        @Override
                        public void success(List<Symptom> symptomList, Response response)
                        {
                            ArrayAdapter array = (ArrayAdapter<Symptom>)diagnosisValue.getAdapter();
                            array.clear();
                            array.addAll(symptomList);
                            array.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
//                                            hideBusy();
//                            new MedicoCustomErrorHandler(getActivity()).handleError(error);
                        }
                    });
                }

            }
        });


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        showBusy();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointmentId = bundle.getInt(APPOINTMENT_ID);

        api.getPatientVisitDoctorNotes(new AppointmentId1(appointmentId), new Callback<DoctorNotes>() {
            @Override
            public void success(DoctorNotes doctorNotesResponse, Response response) {
                //Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                doctorNotesModel = doctorNotesResponse;
                    if (doctorNotesResponse.getIdNotes() != null) {
                        if(doctorNotesResponse.symptoms != null)
                        symptomsValue.setText(doctorNotesResponse.symptoms);
                        if(doctorNotesResponse.diagnosis != null)
                        diagnosisValue.setText(doctorNotesResponse.diagnosis);
                        if(doctorNotesResponse.doctorNotes != null)
                        doctorNotes.setText(doctorNotesResponse.doctorNotes);
                    }

                    hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }

    public void saveDoctorNotesData(DoctorNotes doctorNotesModel){

        showBusy();
        if(doctorNotesModel.getIdNotes() == null){
            api.addPatientVisitDoctorNotes(doctorNotesModel, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    hideBusy();
                }

                @Override
                public void failure(RetrofitError error)
                {
                    hideBusy();
                    new MedicoCustomErrorHandler(getActivity()).handleError(error);
                }
            });
        }else{
            api.updatePatientVisitDoctorNotes(doctorNotesModel, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Update successfully !!!", Toast.LENGTH_LONG).show();
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
    @Override
    public boolean isChanged()
    {
        return doctorNotesModel.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        doctorNotesModel.setAppointmentId(bundle1.getInt(APPOINTMENT_ID));
        doctorNotesModel.setSymptoms(symptomsValue.getText().toString());
        doctorNotesModel.setDiagnosis(diagnosisValue.getText().toString());
        doctorNotesModel.setDoctorNotes(doctorNotes.getText().toString());
    }
    @Override
    public boolean save()
    {
        if(doctorNotesModel.canBeSaved())
        {
            saveDoctorNotesData(doctorNotesModel);
            doctorNotesModel.isChanged = false;
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return doctorNotesModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {
    }
    @Override
    public void onResume() {
        super.onResume();
        setTitle("Visit Details");
        setHasOptionsMenu(true);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        setHasOptionsMenu(false);
    }
    @Override
    public void onStop()
    {
        super.onStop();
        setHasOptionsMenu(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {   menu.clear();
        inflater.inflate(R.menu.patient_visist_summary, menu);
        MenuItem menuItem = menu.findItem(R.id.save_summary);
//        menuItem.setIcon(R.drawable.ic_add_white_24dp);
        super.onCreateOptionsMenu(menu,inflater);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ParentActivity activity = ((ParentActivity) getActivity());
        int id = item.getItemId();
        switch (id) {
            case R.id.save_summary:
            {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            case R.id.add_invoice:
            {
                ((DoctorAppointmentInvoices)fragment).addInvoice();
                return true;
            }
            case R.id.add_payment:
            {
                ((DoctorAppointmentInvoices)fragment).addPayment();
                return true;
            }
            case R.id.exit:
            {
                ((ParentActivity)getActivity()).goHome();
                return false;
            }
            default:
            {
                return false;
            }

        }
    }

}
