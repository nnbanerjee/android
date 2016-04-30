package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.TemplateAdapter;
import Application.MyApi;
import Model.CustomProcedureTemplate;
import Model.ShowTemplate;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class DoctorAppointmentManageTemplate extends Fragment {
    private ListView listViewManageTemplate;
    private Button searchButton;
    private List<CustomProcedureTemplate> arrayTemplate,nameList;
    private TemplateAdapter adapter;
    private String doctorId,procedureName;
    //ProgressDialog progress;
    private EditText searchTv;
    public MyApi api;
    Toolbar toolbar;
    String valueFromBundle,templateName;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        showTemplateList();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    getFragmentManager().beginTransaction().remove(DoctorAppointmentManageTemplate.this).commit();
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

        View view = inflater.inflate(R.layout.doctor_appointment_manage_template,container,false);
        listViewManageTemplate = (ListView)view.findViewById(R.id.list_template);
        //progress =  ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        BackStress.staticflag = 1;
        Bundle bun= getArguments();
        if(bun!=null){
            valueFromBundle=  bun.getString("fragment");
            templateName=  bun.getString("TemplateName");
        }

        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Template");
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
        procedureName = session.getString("selected_procedure_name",null);
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
       // getActivity().getActionBar().hide();
       /* RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);*/
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
              // globalTv.setText("Manage Procedure");
              //  Fragment fragment = new ManageProcedure();
              //  FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
              //  ft.replace(R.id.content_frame, fragment, "Select Template");
             //   ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
              //  ft.addToBackStack(null);
            //    ft.commit();
            //    back.setVisibility(View.INVISIBLE);

                Fragment fragment = new DoctorAppointmentManageProcedure();
                Bundle bun = new Bundle();
                bun.putString("fragment",valueFromBundle);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                if (valueFromBundle.equalsIgnoreCase("treatment"))
                    fragmentManger.beginTransaction().replace(R.id.replacementTreament,fragment,"Manage Template").addToBackStack(null).commit();
                else
                    fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Manage Template").addToBackStack(null).commit();

            }
        });
        showTemplateList();

        final Global go = (Global)getActivity().getApplicationContext();

        searchTv = (EditText)view.findViewById(R.id.search_template);
        searchButton = (Button)view.findViewById(R.id.search_template_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String searchText = searchTv.getText().toString();
                if(searchText.equals("")){
                    Toast.makeText(getActivity(),"Please Enter Text",Toast.LENGTH_SHORT).show();
                    showTemplateList();
                }
                else {
                    showListBySearch(searchText);
                }
            }
        });
        listViewManageTemplate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //System.out.println("Array Template size::::::::::::"+arrayTemplate.size());
                    if(!(arrayTemplate.get(0).getTemplateName().equals("No Template Found"))) {
                       ;
                        CustomProcedureTemplate temp = (CustomProcedureTemplate) adapter.getItem(position);
                      UtilSingleInstance.setCustomProcedureTemplate(temp);

                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        if (valueFromBundle.equalsIgnoreCase("treatment")) {
                            Fragment fragment = new SelectDoctorTemplate();
                            //  ft.beginTransaction().replace(R.id.replacementTreament,fragment,"Manage Template").addToBackStack(null).commit();
                            String templatesubName= ((CustomProcedureTemplate) adapter.getItem(position)).getTemplateSubName();
                            Bundle bun= new Bundle();
                            bun.putString("TemplateSubName",templatesubName);
                            bun.putString("TemplateName",templateName);
                            fragment.setArguments(bun);
                            ft.replace(R.id.replacementTreament, fragment, "Select Template");
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                        }else {
                            //fragmentManger.beginTransaction().replace(R.id.replacementFragment,fragment,"Manage Template").addToBackStack(null).commit();
                            Fragment fragment = new SelectDoctorInvoiceTemplate();
                            ft.replace(R.id.replacementFragment, fragment, "Select Template");
                            // ft.replace(R.id.replacementTreament, fragment, "Select Template");
                            String templatesubName= ((CustomProcedureTemplate) adapter.getItem(position)).getTemplateSubName();
                            Bundle bun= new Bundle();
                            bun.putString("TemplateSubName",templatesubName);
                            bun.putString("TemplateName",templateName);
                            fragment.setArguments(bun);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }
                }

        });

        return view;
    }

    public void showTemplateList(){

        List<CustomProcedureTemplate> listOfSubTemplates=new ArrayList<CustomProcedureTemplate>();
        listOfSubTemplates=UtilSingleInstance.getListOfProcedureTemplates( );

        if(listOfSubTemplates.size() == 0){
            CustomProcedureTemplate temp = new CustomProcedureTemplate();
            temp.setTemplateName("No Template Found");
            //temp.setProcedureName("");
            arrayTemplate.add(temp);
        }
        adapter = new TemplateAdapter(getActivity(),listOfSubTemplates);
        listViewManageTemplate.setAdapter(adapter);
        arrayTemplate=UtilSingleInstance.getListOfProcedureTemplates();

       /* arrayTemplate = new ArrayList<ShowTemplate>();
        api.getAllTemplate(doctorId,procedureName,new Callback<ArrayList<ShowTemplate>>() {
            @Override
            public void success(ArrayList<ShowTemplate> templates, Response response) {
                arrayTemplate = templates;

                if(arrayTemplate.size() == 0){
                    ShowTemplate temp = new ShowTemplate();
                    temp.setTemplateName("No Template Found");
                    temp.setProcedureName("");
                    arrayTemplate.add(temp);
                }
                adapter = new TemplateAdapter(getActivity(),arrayTemplate);
                listViewManageTemplate.setAdapter(adapter);

                progress.dismiss();
           }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        });*/


        //System.out.println("Template size::::::"+arrayTemplate.size());

    }

    public void showListBySearch(String searchText)
    {
           /* int flagSearch = 0;
            int flagNotSearch = 0;
            nameList = new ArrayList<ShowTemplate>();
            for(ShowTemplate t:arrayTemplate)
            {
                if(t.getTemplateName().toLowerCase().contains(searchText.toLowerCase()))
                {
                    nameList.add(t);
                    flagSearch++;

                }
                else
                {
                    flagNotSearch++;
                }
            }

            if(flagSearch >= 1) {
                arrayTemplate.clear();
                System.out.println("i am here.......");
                arrayTemplate.addAll(nameList);
                adapter = new TemplateAdapter(getActivity(), nameList);
                listViewManageTemplate.setAdapter(adapter);
            }
            else
            {
                arrayTemplate.clear();
                ShowTemplate temp = new ShowTemplate();
                temp.setTemplateName("No Template Found");
                temp.setProcedureName("");
                nameList.add(temp);

                adapter = new TemplateAdapter(getActivity(), nameList);
                listViewManageTemplate.setAdapter(adapter);

            }
*/
    }
}
