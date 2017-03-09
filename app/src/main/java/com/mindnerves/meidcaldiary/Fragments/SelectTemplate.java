package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
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

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.FieldAdapter;
import com.medico.application.MyApi;
import Model.Field;
import Model.ShowTemplate;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class SelectTemplate extends Fragment {

    private ListView listViewFieldTemplate;
    private ArrayList<Field> fieldArrayList;
    List<Field> removeList;
    private FieldAdapter adapter;
    private TextView procedureNameTv;
    private Button addNew,editButton,saveButton,removeButton;
    private String doctorId;
    private String templateId = "";
    private ArrayList<Field> selected;
    public MyApi api;
    Button save,delete,add;
    Global global;

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
        View view = inflater.inflate(R.layout.procedure_template,container,false);
        listViewFieldTemplate = (ListView)view.findViewById(R.id.field_list);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        BackStress.staticflag = 0;
        //global = (Global) getActivity().getApplicationContext();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        procedureNameTv = (TextView)view.findViewById(R.id.procedure_name);

        removeList = new ArrayList<Field>();

        save = (Button) view.findViewById(R.id.save);
        add = (Button) view.findViewById(R.id.add);
        delete = (Button) view.findViewById(R.id.delete);

        global = (Global) getActivity().getApplication();
        ShowTemplate temp = global.getTemp();

        removeList = global.getRemoveFieldList();

        templateId = temp.getTemplateId();
        procedureNameTv.setText(temp.getProcedureName());
        globalTv.setText(""+temp.getProcedureName());

        showList();

        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageTemplate();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTemplatesAllFields();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddNewField();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,fragment,"Select Template");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(removeList.size() == 0){
                    Toast.makeText(getActivity(), "Please select Field", Toast.LENGTH_SHORT).show();
                }else{
                   // System.out.println("global.getRemoveFieldList().size() = "+removeList.size());
                    api.removeSelectedFields(removeList,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();
                            if(status == 200){
                                //Toast.makeText(getActivity(),"Fields Are Removed",Toast.LENGTH_SHORT).show();
                                showList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    public void saveTemplatesAllFields(){
        api.addTemplateAllField(fieldArrayList,new Callback<Response>() {
            @Override
            public void success(Response response, Response resp) {
                //System.out.print("in response");
                //adapter = new FieldAdapter(getActivity(),fieldArrayList);
                //listViewFieldTemplate.setAdapter(adapter);
                Toast.makeText(getActivity(),"Saved Successfully !!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showList(){
        fieldArrayList = new ArrayList<Field>();
        api.getAllFields(templateId, new Callback<ArrayList<Field>>() {
            @Override
            public void success(ArrayList<Field> fields, Response response) {

                //System.out.println("fields = "+fields.size());

                fieldArrayList = fields;

                if (fieldArrayList.size() == 0) {
                    fieldArrayList.add(new Field("", templateId, "Name", "Name", "String", "Dummy"));
                    fieldArrayList.add(new Field("", templateId, "Cost", "Cost", "USD", "0"));
                    fieldArrayList.add(new Field("", templateId, "Procedure Date", "Procedure Date", "Date", "18-06-2015"));
                    fieldArrayList.add(new Field("", templateId, "Currency", "Currency", "Currency", "USD"));
                    fieldArrayList.add(new Field("", templateId, "Discount", "Discount", "%", "0"));
                    fieldArrayList.add(new Field("", templateId, "Taxes", "Taxes", "%", "0"));
                    fieldArrayList.add(new Field("", templateId, "Total", "Total", "Numeric", "0"));
                    fieldArrayList.add(new Field("", templateId, "Note", "Note", "String", "Dummy Text"));
                }

                adapter = new FieldAdapter(getActivity(), fieldArrayList);
                listViewFieldTemplate.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
