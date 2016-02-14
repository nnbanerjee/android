package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.ShowProcedure;
import Model.ShowTemplate;
import Model.Template;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class AddNewProcedureDialog extends DialogFragment {

    private String doctorId = "";
    private EditText procedureTv,template_name;
    private Button addButton,cancelButton;
    public MyApi api;
    Spinner category;

    static AddNewProcedureDialog newInstance() {
        return new AddNewProcedureDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_procedure_dialog,container,false);
        getDialog().setTitle("Add Procedure");
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        //Global go = (Global)getActivity().getApplicationContext();

        procedureTv = (EditText)view.findViewById(R.id.procedure_name);
        template_name = (EditText)view.findViewById(R.id.template_name);

        category = (Spinner)view.findViewById(R.id.category);

        template_name.setVisibility(View.GONE);

        final ArrayAdapter<CharSequence> categoryAdaptor = ArrayAdapter.createFromResource(getActivity(),R.array.category_list,
                android.R.layout.simple_spinner_item);
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        addButton = (Button)view.findViewById(R.id.add_template);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(procedureTv.getText().toString().length() == 0){
                    procedureTv.setError("Please Enter Procedure Name");
                }else{
                    ShowProcedure tem = new ShowProcedure();
                    tem.setProcedureName(procedureTv.getText().toString());
                    tem.setCategory(category.getSelectedItem().toString());
                    tem.setDoctorId(doctorId);

                    api.saveDoctorProcedures(tem, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Procedure Added Successfully", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new ManageProcedure();
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment, "Manage Template");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();
                                AddNewProcedureDialog.this.getDialog().dismiss();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_template);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewProcedureDialog.this.getDialog().cancel();
            }
        });

        return view;
    }

   
}
