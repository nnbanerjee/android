package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.ProcedureAdapter;
import Application.MyApi;
import Model.ShowProcedure;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class PatientInvoiceManageProcedure extends Fragment {
    private ListView listProcedure;
    private Button searchButton;
    private ArrayList<ShowProcedure> arrayProcedure,nameList,caetogryList;
    private ProcedureAdapter adapter;
    private String doctorId;
    SharedPreferences session;
    ProgressDialog progress;
    private EditText searchTv;
    TextView noResult;
    Spinner category;
    public MyApi api;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //showTemplateList();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    Fragment fragment = new PatientAppointmentInvoices();
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

        View view = inflater.inflate(R.layout.doctor_appointment_manage_procedure,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        listProcedure = (ListView)view.findViewById(R.id.list_procedure);
        noResult = (TextView) view.findViewById(R.id.noResult);
        category = (Spinner)view.findViewById(R.id.category);

        progress =  ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        caetogryList = new ArrayList<ShowProcedure>();

        BackStress.staticflag = 1;
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Procedure");

        final SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("doctor_email_from_patient",null);
        getActivity().getActionBar().hide();

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
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                Fragment fragment = new PatientAppointmentInvoices();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment, fragment, "Doctor Consultations").addToBackStack(null).commit();
                final Button back = (Button)getActivity().findViewById(R.id.back_button);
                back.setVisibility(View.INVISIBLE);
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
                for( ShowProcedure showProcedure : arrayProcedure){
                    if(temporary.equals(showProcedure.getCategory().toString())){
                        caetogryList.add(showProcedure);
                    }
                }
                if(temporary.equals("All")){
                    caetogryList = arrayProcedure;
                }

                adapter = new ProcedureAdapter(getActivity(), caetogryList);
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
                    adapter = new ProcedureAdapter(getActivity(), arrayProcedure);
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

                TextView procedure = (TextView) view.findViewById(R.id.procedure);
                TextView procedureId = (TextView) view.findViewById(R.id.procedureId);

                System.out.println("procedureId = "+procedureId);

                SharedPreferences.Editor editor = session.edit();
                editor.putString("selected_procedure_name", procedure.getText().toString());
                editor.putString("selected_procedure_id", procedureId.getText().toString());
                editor.commit();
                Fragment fragment = new PatientInvoiceManageTemplate();
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Doctor Consultations").addToBackStack(null).commit();

            }

        });

        return view;
    }

    public void showProcedureList(){

        System.out.println("in doctor Id = "+doctorId);

        arrayProcedure = new ArrayList<ShowProcedure>();
        api.getAllProcedure(doctorId, new Callback<ArrayList<ShowProcedure>>() {
            @Override
            public void success(ArrayList<ShowProcedure> templates, Response response) {
                arrayProcedure = templates;

                System.out.println("arrayProcedure = "+arrayProcedure.size());
                if(arrayProcedure.size() == 0){
                    noResult.setVisibility(View.VISIBLE);
                    listProcedure.setVisibility(View.GONE);
                }

                adapter = new ProcedureAdapter(getActivity(), arrayProcedure);
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

    public void showListBySearch(String searchText) {

        nameList = new ArrayList<ShowProcedure>();
        for (ShowProcedure t : arrayProcedure) {
            if (t.getProcedureName().toLowerCase().contains(searchText.toLowerCase())) {
                nameList.add(t);
            }
        }

        if(nameList.size() > 0){
            noResult.setVisibility(View.GONE);
            listProcedure.setVisibility(View.VISIBLE);
            adapter = new ProcedureAdapter(getActivity(), nameList);
            listProcedure.setAdapter(adapter);
        }else{
            noResult.setVisibility(View.VISIBLE);
            listProcedure.setVisibility(View.GONE);
        }

    }
}
