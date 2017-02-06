package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorAppointmentInformation;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HorizontalListView;
import com.mindnerves.meidcaldiary.R;
import com.mindnerves.meidcaldiary.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.AllProcedureAdapter;
import Application.MyApi;
import Model.TotalInvoice;
import Model.TreatmentField;
import Model.TreatmentPlan;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class DoctorAppointmentAllTreatmentPlan extends Fragment {

    MyApi api;
    public SharedPreferences session;
    Global global;
    private MultiAutoCompleteTextView symptomsValue, diagnosisValue;
    private EditText doctorNotes;
    private CheckBox shareWithPatient;
    private String doctor_email, appointmentTime;
    private Button addNewTreatment;
    private  ListView treatmentPlanList;
    private TextView noDataFound;
    private HorizontalListView fieldList1, fieldList;
    private String appointmentDate, doctorId, patientEmail, appointMentId;
    private ProgressDialog progress;
    private int share = 0;
    private Toolbar toolbar;


    public void populateSession(){
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        patientEmail = session.getString("patientEmail", null);
        appointMentId = session.getString("appointmentId", "");
        System.out.println("appointmentId Id:::::::" + appointMentId);
    }
    // private ImageView deleteProcedure;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_appointment_all_treatment_plan, container, false);

        populateSession();

        global = (Global) getActivity().getApplicationContext();
        shareWithPatient = (CheckBox) view.findViewById(R.id.share_with_patient);
        fieldList1 = (HorizontalListView) view.findViewById(R.id.fieldList1);
        fieldList = (HorizontalListView) view.findViewById(R.id.fieldList);
        noDataFound = (TextView) view.findViewById(R.id.noDataFound);
        treatmentPlanList = (ListView) view.findViewById(R.id.treatmentPlanList);
        addNewTreatment = (Button) view.findViewById(R.id.addNewTreatment);


        /*addNewTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DoctorAppointmentManageProcedure();
                Bundle bun = new Bundle();
                bun.putString("fragment", "treatment");
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementTreament, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });*/
        toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.add_treatment_plan);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.replacementFragment);
                if (f instanceof DoctorAppointmentTreatmentPlan) {
                   /* Fragment fragment = new DoctorAppointmentManageProcedure();
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.replacementTreament, fragment, "Doctor Consultations").addToBackStack(null).commit();*/
                    Fragment fragment = new DoctorAppointmentManageProcedure();
                    Bundle bun = new Bundle();
                    bun.putString("fragment", "treatment");
                    fragment.setArguments(bun);
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.replacementTreament, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }
                return true;
            }
        });
        shareWithPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean check = shareWithPatient.isChecked();
                System.out.println("Status:::::" + check);

                System.out.println("Status:::::" + check);
                System.out.println("PatientId:::::" + patientEmail);
                TotalInvoice invoice = new TotalInvoice();
                invoice.setAppointmentDate(appointmentDate);
                invoice.setAppointmentTime(appointmentTime);
                invoice.setDoctorId(doctorId);
                invoice.setPatientId(patientEmail);
                invoice.setGrandTotal("0.0");
                invoice.setDiscount("0.0");
                invoice.setTaxValue("0.0");
                if (check) {
                    invoice.setShareWithPatient(1);
                    share = 1;
                } else {
                    invoice.setShareWithPatient(0);
                    share = 0;
                }
               /* api.saveShareWithPatientTotalInvoice(invoice, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        int status = response.getStatus();
                        if (status == 200) {
                            if (share == 1) {
                                Toast.makeText(getActivity(), "Data Shared With Patient", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Data Not Shared With Patient", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });*/

            }
        });
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        getAllTreamentPlan();

        final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }


    public List<TreatmentPlan> newCombineArrayWithSubNameAndFields(List<TreatmentPlan> treatmentPlanList) {

        List<TreatmentPlan> newTreatmentPlanListWithSubNames = new ArrayList<TreatmentPlan>();
        //  List<List<TreatmentField>> listOflist = new ArrayList<List<TreatmentField>>();
        Map<String, TreatmentPlan> map = new HashMap<String, TreatmentPlan>();
        for (int i = 0; i < treatmentPlanList.size(); i++) {//Create List of same names.
            String subname = treatmentPlanList.get(i).getTemplateSubName();

            if (!map.containsKey(subname)) { //if not duplicate needs to be added in map
                //map.put(subname, treatmentPlanList.get(i));
                List<List<TreatmentField>> listOflist = new ArrayList<List<TreatmentField>>();
                TreatmentPlan treat = treatmentPlanList.get(i);
                if (treat.getTreatmentFields() != null && treat.getTreatmentFields().size() > 0) {
                    if (listOflist.size() == 0) {//means no records in list hence read headers first and add those as a record
                        List<TreatmentField> listofTreatMentField = new ArrayList<TreatmentField>();
                        for (int y = 0; y < treat.getTreatmentFields().size(); y++) {
                            TreatmentField treatment = new TreatmentField();
                            treatment.setFieldId(treat.getTreatmentFields().get(y).getFieldId());
                            treatment.setTreatmentAttributeId(treat.getTreatmentFields().get(y).getTreatmentAttributeId());
                            treatment.setFieldName(treat.getTreatmentFields().get(y).getFieldName());
                            treatment.setValue(treat.getTreatmentFields().get(y).getValue());
                            treatment.setTreatmentId(treat.getTreatmentFields().get(y).getTreatmentId());
                            listofTreatMentField.add(treatment);
                        }
                        if (treat.getTreatmentFields().size() > 0) {
                            listOflist.add(listofTreatMentField);
                            treat.addTreatmentValues(listOflist);
                        }
                        map.put(subname, treat);
                    }
                    listOflist = new ArrayList<List<TreatmentField>>();
                    List<TreatmentField> listofTreatMentField = new ArrayList<TreatmentField>();
                    for (int k = 0; k < treat.getTreatmentFields().size(); k++) {
                        TreatmentField treatment = new TreatmentField();
                        treatment.setFieldId(treat.getTreatmentFields().get(k).getFieldId());
                        treatment.setTreatmentAttributeId(treat.getTreatmentFields().get(k).getTreatmentAttributeId());
                        treatment.setFieldName(treat.getTreatmentFields().get(k).getFieldName());
                        treatment.setValue(treat.getTreatmentFields().get(k).getValue());
                        treatment.setTreatmentId(treat.getTreatmentFields().get(k).getTreatmentId());
                        listofTreatMentField.add(treatment);
                    }
                    if (treat.getTreatmentFields().size() > 0) {
                        listOflist.add(listofTreatMentField);
                        treat.addTreatmentValues(listOflist);
                    }
                    map.put(subname, treat);
                }

            } else { //if duplicate need to to be treated.
                TreatmentPlan treat = new TreatmentPlan();
                List<List<TreatmentField>> listOflist = new ArrayList<List<TreatmentField>>();
                treat = map.get(subname);
                treat.getTreatmentFields().addAll(treatmentPlanList.get(i).getTreatmentFields());
               /* if (listOflist.size() == 0) {//means no records in list hence read headers first and add those as a record
                    List<TreatmentField> listofTreatMentField = new ArrayList<TreatmentField>();
                    for (int y = 0; y < treatmentPlanList.get(i).getTreatmentFields().size(); y++) {
                        TreatmentField treatment = new TreatmentField();
                        treatment.setFieldId(treatmentPlanList.get(i).getTreatmentFields().get(y).getFieldId());
                        treatment.setTreatmentAttributeId(treatmentPlanList.get(i).getTreatmentFields().get(y).getTreatmentAttributeId());
                        treatment.setFieldName(treatmentPlanList.get(i).getTreatmentFields().get(y).getFieldName());
                        treatment.setValue(treatmentPlanList.get(i).getTreatmentFields().get(y).getValue());
                        treatment.setTreatmentId(treatmentPlanList.get(i).getTreatmentFields().get(y).getTreatmentId());
                        listofTreatMentField.add(treatment);
                    }
                    if (treatmentPlanList.get(i).getTreatmentFields().size() > 0) {
                        listOflist.add(listofTreatMentField);
                        treat.addTreatmentValues(listOflist);
                    }
                    map.put(subname, treat);
                }*/
                List<TreatmentField> listofTreatMentField = new ArrayList<TreatmentField>();
                for (int k = 0; k < treatmentPlanList.get(i).getTreatmentFields().size(); k++) {
                    TreatmentField treatment = new TreatmentField();
                    treatment.setFieldId(treatmentPlanList.get(i).getTreatmentFields().get(k).getFieldId());
                    treatment.setTreatmentAttributeId(treatmentPlanList.get(i).getTreatmentFields().get(k).getTreatmentAttributeId());
                    treatment.setFieldName(treatmentPlanList.get(i).getTreatmentFields().get(k).getFieldName());
                    treatment.setValue(treatmentPlanList.get(i).getTreatmentFields().get(k).getValue());
                    treatment.setTreatmentId(treatmentPlanList.get(i).getTreatmentFields().get(k).getTreatmentId());
                    listofTreatMentField.add(treatment);
                }
                if (treatmentPlanList.get(i).getTreatmentFields().size() > 0) {
                    listOflist.add(listofTreatMentField);
                    treat.addTreatmentValues(listOflist);
                }
                map.put(subname, treat);
            }
        }
        for (TreatmentPlan value : map.values()) {
            System.out.println("Value = " + value.getTemplateSubName());
            newTreatmentPlanListWithSubNames.add(value);
        }
        return newTreatmentPlanListWithSubNames;
    }

    public void getAllTreamentPlan() {
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        api.getPatientVisitTreatmentPlan(appointMentId, "1",  new Callback<List<TreatmentPlan>>() {
            @Override
            public void success(List<TreatmentPlan> treatmentPlan, Response response) {

                treatmentPlan = newCombineArrayWithSubNameAndFields(treatmentPlan);

                if (treatmentPlan == null) {
                    treatmentPlanList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "No Data!", Toast.LENGTH_LONG).show();
                } else {
                    treatmentPlanList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                }
                AllProcedureAdapter allProcedureAdapter = new AllProcedureAdapter(getActivity(), treatmentPlan);
                UtilSingleInstance.setTreatmentPlan(treatmentPlan);
                treatmentPlanList.setAdapter(allProcedureAdapter);
                Utility.setListViewHeightBasedOnChildren(treatmentPlanList);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                progress.dismiss();
                error.printStackTrace();
            }
        });

    }

    public void goToBack() {
        //Fragment fragment = new PatientAllAppointment();
        Fragment fragment = new DoctorAppointmentInformation();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Doctor Consultations").addToBackStack(null).commit();
      /*  final Button back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);*/


    }
}
