package com.mindnerves.meidcaldiary.Fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.Calendar;

import com.medico.application.MyApi;

import com.medico.model.ResponseCodeVerfication;

import Model.TreatmentPlan;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class ChangeFeildValueDialog extends DialogFragment {
    private Listener mListener;
    private EditText procedure_name;
    private Button add_template,cancel_template;
    public MyApi api;
    private ArrayAdapter<CharSequence> currencyAdapter;
    private Calendar calendar1 = Calendar.getInstance();
    SharedPreferences session;
    Button calen;
    private Spinner currencySpinner;
    String editableValue,doctor_email,patientEmail,fieldType,editableFieldValue;

    static ChangeFeildValueDialog newInstance() {
        return new ChangeFeildValueDialog();
    }
    public static interface Listener {

        void returnData(String result);
    }
    public void setListener(Listener listener) {
        mListener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_field_dialog,container,false);
        getDialog().setTitle("Change Value");
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        procedure_name = (EditText)view.findViewById(R.id.procedure_name);
        doctor_email = session.getString("patient_doctor_email", null);
        patientEmail = session.getString("doctor_patientEmail", null);
        fieldType = session.getString("editableFieldType",null);

        editableFieldValue = session.getString("editableFieldValue",null);
        add_template = (Button)view.findViewById(R.id.add_template);
        cancel_template = (Button)view.findViewById(R.id.cancel_template);
        currencySpinner = (Spinner)view.findViewById(R.id.category);
        calen = (Button)view.findViewById(R.id.calen);
        System.out.println("FieldTYpe:::::::"+fieldType);
        editableValue = session.getString("editableFieldDefaultValue","");
        currencyAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.currency_list,
                android.R.layout.simple_spinner_item);
        procedure_name.setText(editableValue);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        if(fieldType.equals("String")) {
            procedure_name.setText(editableValue);
        }else if(fieldType.equals("USD"))
        {
            calen.setVisibility(View.GONE);
            procedure_name.setVisibility(View.VISIBLE);
            currencySpinner.setVisibility(View.GONE);
        }
        else if(fieldType.equals("Date"))
        {
            calen.setVisibility(View.VISIBLE);
        }
        else if(fieldType.equals("Currency"))
        {
            calen.setVisibility(View.GONE);
            procedure_name.setVisibility(View.GONE);
            currencySpinner.setVisibility(View.VISIBLE);
            currencySpinner.setAdapter(currencyAdapter);

        }
        else if(fieldType.equals("%"))
        {
            calen.setVisibility(View.GONE);
            procedure_name.setVisibility(View.VISIBLE);
            currencySpinner.setVisibility(View.GONE);
            procedure_name.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate();
            }
        });
        add_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences.Editor editor = session.edit();
                editor.putString("editableValue", changeValue.getText().toString());
                editor.commit();*/


                /*Field field = new Field();
                field.setFieldId(session.getString("editableFieldId",""));
                field.setTemplateId(session.getString("editableTemplateId", ""));
                field.setFieldDisplayName(session.getString("editableFieldDisplayName", ""));
                field.setFieldName(session.getString("editableFieldName",""));
                field.setFieldType(session.getString("editableFieldType",""));
                field.setFieldDefaultValue(procedure_name.getText().toString());
                field.setDoctorId(doctor_email);
                field.setPatientId(patientEmail);
                updateFieldTemplate(field);

                Fragment fragment = new DoctorAppointmentTreatmentPlan();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();
*/
                if (mListener != null) {
                    mListener.returnData(procedure_name.getText().toString());
                }

                dismiss();

                //Toast.makeText(getActivity(), "Changed ", Toast.LENGTH_SHORT).show();
            }
        });


        cancel_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFeildValueDialog.this.getDialog().cancel();
            }
        });

        return view;
    }

    public void setStartDate(){

        new DatePickerDialog(getActivity(),d1,calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar1.set(Calendar.YEAR,year);
            calendar1.set(Calendar.MONTH,monthOfYear);
            calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateStartDate();
        }

    };

    public void updateStartDate()
    {
        // editStartDate.setText(formate.format(calendar1.getTime()));
        procedure_name.setText("" + calendar1.get(Calendar.DAY_OF_MONTH) + "-" + UtilSingleInstance.showMonth(calendar1.get(Calendar.MONTH))+"-"+calendar1.get(Calendar.YEAR));
    }

    public void updateFieldTemplate(TreatmentPlan treatmentPlan){


        api.updatePatientVisitTreatmentPlan(treatmentPlan, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication responseCodeVerfication, Response response) {
                Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                ChangeFeildValueDialog.this.getDialog().cancel();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }


   
}
