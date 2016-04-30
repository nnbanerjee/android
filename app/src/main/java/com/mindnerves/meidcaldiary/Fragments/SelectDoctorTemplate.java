package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.FieldAdapter;
import Adapter.TemplateDetailsAdapter;
import Application.MyApi;
import Model.CustomProcedureTemplate;
import Model.Field;
import Model.ResponseAddTemplates;
import Model.ShowTemplate;
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
public class SelectDoctorTemplate extends Fragment {

    private ListView listViewFieldTemplate;
    private ArrayList<TreatmentField> fieldArrayList, removeList;
    private TemplateDetailsAdapter adapter;
    private TextView procedureNameTv;
    private Button addNew, editButton, saveButton, removeButton;
    private String doctorId, procedureId, patientEmail, appointmentDate, appointmentTime;
    private String templateId = "", patientId;
    private ArrayList<TreatmentField> selected;
    public MyApi api;
    Button addBtn;
    CustomProcedureTemplate customProcedureTemplate;
    private Toolbar toolbar;
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
        //TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        //BackStress.staticflag = 0;
        toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.add_procedure);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.content_frame);
                if (f instanceof SelectDoctorTemplate) {
                    addTemplate();
                }

                return true;
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        procedureId = session.getString("selected_procedure_id", null);
        patientId = session.getString("patientId", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);

        appointmentId = session.getString("appointmentId", null);

        procedureNameTv = (TextView) view.findViewById(R.id.procedure_name);
        final Global go = (Global) getActivity().getApplicationContext();
        customProcedureTemplate = UtilSingleInstance.getCustomProcedureTemplate();
        templateId = customProcedureTemplate.getTemplateId();
        procedureNameTv.setText(customProcedureTemplate.getTemplateSubName());

        addBtn = (Button) view.findViewById(R.id.addBtn);

        Bundle bun = getArguments();
        if (bun != null) {
            templateName = bun.getString("TemplateName");
            templateSubName = bun.getString("TemplateSubName");
        }


        //globalTv.setText(""+temp.getProcedureName());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        treatmentPlan.setCategoryId("1");//for procedure its 1
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
        api.addPatientVisitTreatmentPlan(treatmentPlan, new Callback<ResponseAddTemplates>() {
            @Override
            public void success(ResponseAddTemplates treatment, Response response) {
                //System.out.println("Saved Successfully !!!");
                Toast.makeText(getActivity(), "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                Fragment fragment = new DoctorAppointmentTreatmentPlan();
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
        //  fieldArrayList = new ArrayList<Field>();
        //System.out.println("fieldArrayList = "+fieldArrayList.size());


        List<TemplateField> fieldArrayList = customProcedureTemplate.getTemplateFields();
        // fieldArrayList = fields;

        if (fieldArrayList.size() == 0) {
            TemplateField fe = new TemplateField();
            //fe.set.setFieldType("");
            fe.setFieldName("No Field Found");
            fe.setTemplateId("");
            adapter = new TemplateDetailsAdapter(getActivity(), fieldArrayList);
            listViewFieldTemplate.setAdapter(adapter);

        } else {
            adapter = new TemplateDetailsAdapter(getActivity(), fieldArrayList);
            listViewFieldTemplate.setAdapter(adapter);
        }

    }

    public String doValidationForEdit() {

        int len = fieldArrayList.size();
        selected = new ArrayList<TreatmentField>();

        for (int i = 0; i < len; i++) {
            /* if(fieldArrayList.get(i).getSelected() == true)
             {
                 selected.add(fieldArrayList.get(i));
             }*/
        }

        if (selected.size() == 1) {
            System.out.println("I am here:::::::::::::");
            return "Normal";
        } else {
            return "Abnormal";
        }


    }

    public String doValidation() {
        removeList = new ArrayList<TreatmentField>();
        int flag = 0;
        int len = fieldArrayList.size();

        for (int i = 0; i < len; i++) {
           /* if(fieldArrayList.get(i).getSelected() == true)
            {
                System.out.println(fieldArrayList.get(i).getFieldName()+"::::::::");
                removeList.add(fieldArrayList.get(i));
            }
            else
            {
                flag++;
            }*/
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
