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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorAppointmentInvoices;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.ProcedureAdapter;
import com.medico.application.MyApi;
import Model.CustomProcedureTemplate;
import Model.PersonAndCategoryId;
import Model.TreatmentField;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class DoctorAppointmentManageProcedure extends Fragment {
    private ListView listProcedure;
    private Button searchButton;
    private List<CustomProcedureTemplate>  arrayProcedure,nameList,caetogryList;
    private ProcedureAdapter adapter;
    private String doctorId;
    SharedPreferences session;
    ProgressDialog progress;
    private EditText searchTv;
    TextView noResult;
    Spinner category;
    public MyApi api;
    private Toolbar toolbar;
    List<CustomProcedureTemplate> originalList;
    private Button procedureTabBtn,invoiceTabBtn;
    String valueFromBundle;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //showTemplateList();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    TextView globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    getFragmentManager().beginTransaction().remove(DoctorAppointmentManageProcedure.this).commit();
                    final Button back = (Button) getActivity().findViewById(R.id.back_button);
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

        View view = inflater.inflate(R.layout.doctor_appointment_manage_procedure,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        procedureTabBtn = (Button) view.findViewById(R.id.summaryBtn);
        invoiceTabBtn = (Button) view.findViewById(R.id.documentationBtn);
        //Check from where it is coming from and enable disable tabs.
        //bun.putString("fragment_from", "TreatmentPlan");
        // bun.putString("fragment_from", "TreatmentInvoices");
        procedureTabBtn.setEnabled(true);
        invoiceTabBtn.setEnabled(false);
        Bundle bun = getArguments();
        if(bun != null) {
            valueFromBundle = bun.getString("fragment");//("fragment", "treatment");
            if (valueFromBundle.equalsIgnoreCase("treatment")) {
                procedureTabBtn.setEnabled(true);
                invoiceTabBtn.setEnabled(false);
            } else {
                procedureTabBtn.setEnabled(false);
                invoiceTabBtn.setEnabled(true);
            }
        }
        listProcedure = (ListView)view.findViewById(R.id.list_procedure);
        noResult = (TextView) view.findViewById(R.id.noResult);
        category = (Spinner)view.findViewById(R.id.category);


        progress =  ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.loading_wait));
        caetogryList = new ArrayList<CustomProcedureTemplate>();

        BackStress.staticflag = 1;
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Procedure");

        final SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
       // getActivity().getActionBar().hide();
        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.search_procedure);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.content_frame);
                if (f instanceof DoctorAppointmentManageProcedure) {
                   // saveData();
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

        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
               // globalTv.setText(getResources().getString(R.string.app_name));
              //  getFragmentManager().beginTransaction().remove(DoctorAppointmentManageProcedure.this).commit();
                //back.setVisibility(View.INVISIBLE);
                if (valueFromBundle.equalsIgnoreCase("TreatmentPlan")) {
                    Fragment fragment = new PatientAppointmentAllTreatmentPlan();
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.replacementTreament, fragment, "Doctor Consultations").addToBackStack(null).commit();
                } else {
                    Fragment fragment = new DoctorAppointmentInvoices();
                    FragmentManager fragmentManger = getActivity().getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                }

            }
        });

        showProcedureList();

        final ArrayAdapter<CharSequence> categoryAdaptor = ArrayAdapter.createFromResource(getActivity(),R.array.category_list,
                android.R.layout.simple_spinner_item);
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temporary = categoryAdaptor.getItem(position).toString();

                caetogryList.clear();
                for( CustomProcedureTemplate showProcedure : arrayProcedure){
                    if(temporary.equals(showProcedure.getCategoryId().toString())){
                        caetogryList.add(showProcedure);
                    }
                }
                if(temporary.equals("All")){
                    caetogryList = arrayProcedure;
                }
                ArrayList arr= new ArrayList();
                arr.addAll(caetogryList);
                adapter = new ProcedureAdapter(getActivity(), arr);
                listProcedure.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //final Global go = (Global)getActivity().getApplicationContext();

        searchTv = (EditText)view.findViewById(R.id.search_template);
        searchButton = (Button)view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String searchText = searchTv.getText().toString();
                if(searchText.equals("")){
                    //Toast.makeText(getActivity(),"Please Enter Text",Toast.LENGTH_SHORT).show();
                    searchTv.setError("Please Enter Text ");
                    ArrayList arr= new ArrayList();
                    arr.addAll(arrayProcedure);
                    adapter = new ProcedureAdapter(getActivity(), arr);
                    listProcedure.setAdapter(adapter);
                    //showTemplateList();
                }
                else {
                    showListBySearch(searchText);
                }
            }
        });
        listProcedure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                List<CustomProcedureTemplate> listOfSubTemplates=new ArrayList<CustomProcedureTemplate>();

                for (int i = 0; i < originalList.size(); i++) {//Create List of same names.
                    String subname = originalList.get(i).getTemplateName();

                    if (subname.equalsIgnoreCase( arrayProcedure.get(position).getTemplateName())){
                        listOfSubTemplates.add(originalList.get(i));
                    }
                }
                UtilSingleInstance.setListOfProcedureTemplates(listOfSubTemplates);

                TextView procedure = (TextView) view.findViewById(R.id.procedure);
                TextView procedureId = (TextView) view.findViewById(R.id.procedureId);

                System.out.println("procedureId = " + procedureId);

                SharedPreferences.Editor editor = session.edit();
                editor.putString("selected_procedure_name", procedure.getText().toString());
                editor.putString("selected_procedure_id", procedureId.getText().toString());
                editor.commit();

                String templateName= arrayProcedure.get(position).getTemplateName();


                Bundle bun = new Bundle();
                bun.putString("fragment",valueFromBundle);
                bun.putString("TemplateName",templateName);
                Fragment fragment = new DoctorAppointmentManageTemplate();
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                if (valueFromBundle.equalsIgnoreCase("treatment"))
                 fragmentManger.beginTransaction().replace(R.id.replacementTreament,fragment,"Manage Template").addToBackStack(null).commit();
                else
                    fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Manage Template").addToBackStack(null).commit();
            }

        });

        return view;
    }

    public void showProcedureList(){

        System.out.println("in doctor Id = "+doctorId);
        PersonAndCategoryId person;

        arrayProcedure = new ArrayList<CustomProcedureTemplate>();
        if (valueFromBundle.equalsIgnoreCase("TreatmentPlan")) {
            person= new PersonAndCategoryId(doctorId,"1");
        }else{
            person= new PersonAndCategoryId(doctorId,"2");
        }

        api.getAllCustomTemplate(person, new Callback<List<CustomProcedureTemplate>>() {

            @Override
            public void success(List<CustomProcedureTemplate> templates, Response response) {
                //arrayProcedure = templates;
                originalList=templates;

                arrayProcedure=newCombineArrayOfCustomProcedureTemplates(templates);
                System.out.println("arrayProcedure = " + arrayProcedure.size());
                if (arrayProcedure.size() == 0) {
                    noResult.setVisibility(View.VISIBLE);
                    listProcedure.setVisibility(View.GONE);
                }
                ArrayList arr= new ArrayList();
                arr.addAll(arrayProcedure);
                adapter = new ProcedureAdapter(getActivity(), arr);
                listProcedure.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public List<CustomProcedureTemplate> newCombineArrayOfCustomProcedureTemplates(List<CustomProcedureTemplate> treatmentPlanList) {

        List<CustomProcedureTemplate> newTreatmentPlanListWithSubNames = new ArrayList<CustomProcedureTemplate>();
        List<List<TreatmentField>> listOflist = new ArrayList<List<TreatmentField>>();
        Map<String, CustomProcedureTemplate> map = new HashMap<String, CustomProcedureTemplate>();
        Map<String, Integer> count = new HashMap<String, Integer>();
        for (int i = 0; i < treatmentPlanList.size(); i++) {//Create List of same names.
            String subname = treatmentPlanList.get(i).getTemplateName();

            if (!map.containsKey(subname)) { //if not duplicate needs to be added in map
                map.put(subname, treatmentPlanList.get(i));
                count.put(subname, 1);
            }else{
                count.put(subname, count.get(subname)+1);
            } /*else { //if duplicate need to to be treated.
                TreatmentPlan treat = new TreatmentPlan();
                treat = map.get(subname);
                treat.getTreatmentFields().addAll(treatmentPlanList.get(i).getTreatmentFields());
                if (listOflist.size() == 0) {//means no records in list hence read headers first and add those as a record
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
                        treat.setTreatmentValues(listOflist);
                    }
                    map.put(subname, treat);
                }
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
                    treat.setTreatmentValues(listOflist);
                }
                map.put(subname, treat);
            }*/
        }
         for (CustomProcedureTemplate value : map.values()) {
            System.out.println("Value = " + value.getTemplateName());
            newTreatmentPlanListWithSubNames.add(value);
        }
        return newTreatmentPlanListWithSubNames;
    }
    public void showListBySearch(String searchText) {

        nameList = new ArrayList<CustomProcedureTemplate>();
        for (CustomProcedureTemplate t : arrayProcedure) {
            if (t.getTemplateName().toLowerCase().contains(searchText.toLowerCase())) {
                nameList.add(t);
            }
        }

        if(nameList.size() > 0){
            noResult.setVisibility(View.GONE);
            listProcedure.setVisibility(View.VISIBLE);
            ArrayList arr= new ArrayList();
            arr.addAll(nameList);
            adapter = new ProcedureAdapter(getActivity(), arr);
            listProcedure.setAdapter(adapter);
        }else{
            noResult.setVisibility(View.VISIBLE);
            listProcedure.setVisibility(View.GONE);
        }

    }
}
