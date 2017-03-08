package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorAppointmentInvoices;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.TemplateDetailsAdapter;
import Application.MyApi;
import Model.CustomProcedureTemplate;
import Model.Field;
import Model.ResponseAddTemplates;
import Model.TemplateField;
import Model.TreatmentField;
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
public class SelectDoctorInvoiceTemplate extends Fragment {

    private ListView listViewFieldTemplate;
    private ArrayList<Field> fieldArrayList, removeList;
    private TemplateDetailsAdapter adapter;
    private TextView procedureNameTv;
    private Button addNew, editButton, saveButton, removeButton;
    private String doctorId, procedureId, patientEmail, appointmentDate, appointmentTime;
    private String templateId = "";
    private ArrayList<Field> selected;
    public MyApi api;
    Button addBtn;
    CustomProcedureTemplate customProcedureTemplate;
    String patientId;
    String templateName, templateSubName, appointmentId;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new ManageTemplate();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2, null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_procedure_template, container, false);
        listViewFieldTemplate = (ListView) view.findViewById(R.id.field_list);
        // TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        //BackStress.staticflag = 0;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        procedureId = session.getString("selected_procedure_id", null);
        patientEmail = session.getString("patientEmail", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);
        customProcedureTemplate = UtilSingleInstance.getCustomProcedureTemplate();
        procedureNameTv = (TextView) view.findViewById(R.id.procedure_name);
       /* final Global go = (Global)getActivity().getApplicationContext();
        ShowTemplate temp = go.getTemp();*/
        templateId = customProcedureTemplate.getTemplateId();
        procedureNameTv.setText(customProcedureTemplate.getTemplateSubName());
       /* templateId = temp.getTemplateId();
        procedureNameTv.setText(temp.getProcedureName());*/

        addBtn = (Button) view.findViewById(R.id.addBtn);
        doctorId = session.getString("sessionID", null);
        patientId = session.getString("patientId", null);

        appointmentId = session.getString("appointmentId", null);
        //globalTv.setText(""+temp.getProcedureName());
        Bundle bun = getArguments();
        if (bun != null) {
            templateName = bun.getString("TemplateName");
            templateSubName = bun.getString("TemplateSubName");
        }



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* TreatmentPlan treatmentPlan = new TreatmentPlan();
                treatmentPlan.setDoctorId(doctorId);
                treatmentPlan.setProcedureId(procedureId);
                treatmentPlan.setTemplateId(templateId);
                treatmentPlan.setPatientId(patientEmail);
                treatmentPlan.setPatientAppointmentDate(appointmentDate);
                treatmentPlan.setPatientAppointmentTime(appointmentTime);
                saveTreatmentPlan(treatmentPlan);*/
                addTemplate();
            }
        });

        showList();
        return view;
    }

    public void addTemplate() {
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        treatmentPlan.setAppointmentId(appointmentId);
        treatmentPlan.setDoctorId(doctorId);
        treatmentPlan.setPatientId(patientId);
        // treatmentPlan.setParentId();
        treatmentPlan.setCategoryId("2");//for invoice its 2
        treatmentPlan.setTemplateName(templateName);
        treatmentPlan.setTemplateSubName(templateSubName);

        List<TreatmentField> treatmentFields = new ArrayList<TreatmentField>();
        List<TemplateField> fieldArrayList = customProcedureTemplate.getTemplateFields();

        for (TemplateField field : fieldArrayList) {
            TreatmentField treatment = new TreatmentField();
            treatment.setFieldId(field.getFieldId());
            treatment.setFieldName(field.getFieldName());
            treatment.setValue(field.getValue());
            treatmentFields.add(treatment);
        }
        treatmentPlan.setTreatmentFields(treatmentFields);
        treatmentPlan.setTreatmentValues(null);
        saveTreatmentPlan(treatmentPlan);
    }

    public void saveTreatmentPlan(TreatmentPlan treatmentPlan) {
        api.addPatientVisitInvoice(treatmentPlan, new Callback<ResponseAddTemplates>() {
            @Override
            public void success(ResponseAddTemplates temp, Response response) {
                //System.out.println("Saved Successfully !!!");
                Toast.makeText(getActivity(), "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                Fragment fragment = new DoctorAppointmentInvoices();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showList() {

        List<TemplateField> fieldArrayList= customProcedureTemplate.getTemplateFields();
        // fieldArrayList = fields;

        if(fieldArrayList.size() == 0){
            TemplateField fe = new TemplateField();
            //fe.set.setFieldType("");
            fe.setFieldName("No Field Found");
            fe.setTemplateId("");
            adapter = new TemplateDetailsAdapter(getActivity(),fieldArrayList);
            listViewFieldTemplate.setAdapter(adapter);

        }else{
            adapter = new TemplateDetailsAdapter(getActivity(),fieldArrayList);
            listViewFieldTemplate.setAdapter(adapter);
        }


     /*   fieldArrayList = new ArrayList<Field>();
        System.out.println("fieldArrayList = " + fieldArrayList.size());
        api.getAllFields(templateId, new Callback<ArrayList<Field>>() {
            @Override
            public void success(ArrayList<Field> fields, Response response) {

                fieldArrayList = fields;

                if (fieldArrayList.size() == 0) {
                    Field fe = new Field();
                    fe.setFieldType("");
                    fe.setFieldName("No Field Found");
                    fe.setTemplateId("");
                    fieldArrayList.add(fe);
                }
                //System.out.println("Field List size::::"+fieldArrayList.size());
                //Commented by raviraj
                 adapter = new TemplateDetailsAdapter(getActivity(),fieldArrayList);
                listViewFieldTemplate.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    public String doValidationForEdit() {

        int len = fieldArrayList.size();
        selected = new ArrayList<Field>();

        for (int i = 0; i < len; i++) {
            if (fieldArrayList.get(i).getSelected() == true) {
                selected.add(fieldArrayList.get(i));
            }
        }

        if (selected.size() == 1) {
            System.out.println("I am here:::::::::::::");
            return "Normal";
        } else {
            return "Abnormal";
        }


    }

    public String doValidation() {
        removeList = new ArrayList<Field>();
        int flag = 0;
        int len = fieldArrayList.size();

        for (int i = 0; i < len; i++) {
            if (fieldArrayList.get(i).getSelected() == true) {
                System.out.println(fieldArrayList.get(i).getFieldName() + "::::::::");
                removeList.add(fieldArrayList.get(i));
            } else {
                flag++;
            }
        }
        System.out.println("Size:::::" + removeList.size());
        if (flag == len) {
            Toast.makeText(getActivity(), "Please Select Field From List", Toast.LENGTH_SHORT).show();
            return "abnormal";
        } else {
            return "normal";
        }

    }
}
