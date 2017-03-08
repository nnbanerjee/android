package com.medico.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.medico.model.AppointmentId1;
import com.medico.model.DoctorNotes;
import com.medico.model.ResponseCodeVerfication;
import com.medico.model.SearchParameter;
import com.medico.model.Symptom;
import com.mindnerves.meidcaldiary.R;

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
    ProgressDialog progress;
    DoctorNotes doctorNotesModel = new DoctorNotes();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_doctor_note, container,false);
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
//                            error.printStackTrace();
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
//                            error.printStackTrace();
                        }
                    });
                }

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStart()
    {
        super.onStart();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
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

                    progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                progress.dismiss();
            }
        });
    }

    public void saveDoctorNotesData(DoctorNotes doctorNotesModel){

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        if(doctorNotesModel.getIdNotes() == null){
            api.addPatientVisitDoctorNotes(doctorNotesModel, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
                }
            });
        }else{
            api.updatePatientVisitDoctorNotes(doctorNotesModel, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    Toast.makeText(getActivity(), "Update successfully !!!", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    progress.dismiss();
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
    protected void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        doctorNotesModel.setAppointmentId(bundle1.getInt(APPOINTMENT_ID));
        doctorNotesModel.setSymptoms(symptomsValue.getText().toString());
        doctorNotesModel.setDiagnosis(diagnosisValue.getText().toString());
        doctorNotesModel.setDoctorNotes(doctorNotes.getText().toString());
    }
    @Override
    protected boolean save()
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
    protected boolean canBeSaved()
    {
        return doctorNotesModel.canBeSaved();
    }
    @Override
    protected void setEditable(boolean editable)
    {
    }


}
