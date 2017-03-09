package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.TemplateDetailsAdapter;
import com.medico.application.MyApi;
import Model.Field;
import Model.ShowTemplate;
import Model.TreatmentPlan;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class SelectPatientInvoiceTemplate extends Fragment {

    private ListView listViewFieldTemplate;
    private ArrayList<Field> fieldArrayList,removeList;
    private TemplateDetailsAdapter adapter;
    private TextView procedureNameTv;
    private Button addNew,editButton,saveButton,removeButton;
    private String doctorId,procedureId,patientEmail,appointmentDate,appointmentTime;
    private String templateId = "";
    private ArrayList<Field> selected;
    public MyApi api;
    Button addBtn;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fragment = new PatientInvoiceManageTemplate();
                    FragmentManager fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_procedure_template,container,false);
        listViewFieldTemplate = (ListView)view.findViewById(R.id.field_list);
       // TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        //BackStress.staticflag = 0;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("doctor_email_from_patient",null);
        procedureId = session.getString("selected_procedure_id", null);
        patientEmail = session.getString("patientEmail", null);
        appointmentDate = session.getString("doctor_patient_appointmentDate", null);
        appointmentTime = session.getString("doctor_patient_appointmentTime", null);

        procedureNameTv = (TextView)view.findViewById(R.id.procedure_name);
        final Global go = (Global)getActivity().getApplicationContext();
        ShowTemplate temp = go.getTemp();
        templateId = temp.getTemplateId();
        procedureNameTv.setText(temp.getProcedureName());

        addBtn = (Button) view.findViewById(R.id.addBtn);

        //globalTv.setText(""+temp.getProcedureName());

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
            }
        });

        showList();
        return view;
    }

    public void saveTreatmentPlan(TreatmentPlan treatmentPlan){
        api.saveInvoicesData(treatmentPlan, new Callback<TreatmentPlan>() {
            @Override
            public void success(TreatmentPlan temp, Response response) {
                //System.out.println("Saved Successfully !!!");
                Toast.makeText(getActivity(), "Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                Fragment fragment = new PatientAppointmentInvoices();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                final Button back = (Button)getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showList(){
        fieldArrayList = new ArrayList<Field>();
        System.out.println("fieldArrayList = "+fieldArrayList.size());
        api.getAllFields(templateId,new Callback<ArrayList<Field>>() {
            @Override
            public void success(ArrayList<Field> fields, Response response) {

                   fieldArrayList = fields;

                    if(fieldArrayList.size() == 0){
                        Field fe = new Field();
                        fe.setFieldType("");
                        fe.setFieldName("No Field Found");
                        fe.setTemplateId("");
                        fieldArrayList.add(fe);
                    }
                    //System.out.println("Field List size::::"+fieldArrayList.size());
                //Commented by raviraj
                    //adapter = new TemplateDetailsAdapter(getActivity(),fieldArrayList);
                    listViewFieldTemplate.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public String doValidationForEdit()
    {

        int len = fieldArrayList.size();
        selected = new ArrayList<Field>();

        for(int i=0;i<len;i++)
        {
             if(fieldArrayList.get(i).getSelected() == true)
             {
                 selected.add(fieldArrayList.get(i));
             }
        }

        if(selected.size() == 1)
        {
            System.out.println("I am here:::::::::::::");
            return "Normal";
        }
        else
        {
            return "Abnormal";
        }


    }

    public String doValidation()
    {
        removeList = new ArrayList<Field>();
        int flag =0;
        int len = fieldArrayList.size();

        for(int i=0;i<len;i++)
        {
            if(fieldArrayList.get(i).getSelected() == true)
            {
                System.out.println(fieldArrayList.get(i).getFieldName()+"::::::::");
                removeList.add(fieldArrayList.get(i));
            }
            else
            {
                flag++;
            }
        }
        System.out.println("Size:::::"+removeList.size());
        if(flag == len)
        {
            Toast.makeText(getActivity(),"Please Select Field From List",Toast.LENGTH_SHORT).show();
            return "abnormal";
        }
        else
        {
            return "normal";
        }

    }
}
