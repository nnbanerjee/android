package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
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
public class AddDialogFragment extends DialogFragment {

    private String templateName = "";
    private String procedureName = "";
    private String doctorId = "";
    private EditText templateTv;
    TextView procedureTv;
    private Button addButton,cancelButton;
    SharedPreferences session;
    public MyApi api;

    static AddDialogFragment newInstance() {
        return new AddDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_template_dialog,container,false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        getDialog().setTitle("Add Template");
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        Global go = (Global)getActivity().getApplicationContext();
        ShowTemplate temp = go.getTemp();
        templateTv = (EditText)view.findViewById(R.id.template_name);
        procedureTv = (TextView)view.findViewById(R.id.procedure_name);

        procedureTv.setText(session.getString("selected_procedure_name",""));

        addButton = (Button)view.findViewById(R.id.add_template);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flagValidation = 0;
                String validation = "";

                String templateName = templateTv.getText().toString();
                String procedureName = procedureTv.getText().toString();

                if(templateName.equals(""))
                {
                    flagValidation = 1;
                    validation = "Please Enter Template Name";
                }
                if(procedureName.equals(""))
                {
                    flagValidation = 1;
                    validation = validation + "\nPlease Enter Procedure Name";

                }

                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Template tem = new Template();
                    tem.setProcedureName(procedureName);
                    tem.setTemplateName(templateName);
                    tem.setDoctorId(doctorId);

                    api.addTemplate(tem,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();

                            if(status == 200)
                            {
                                Toast.makeText(getActivity(),"Template Added Successfully",Toast.LENGTH_SHORT).show();
                                Fragment fragment = new ManageTemplate();
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,fragment,"Manage Template");
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();
                                AddDialogFragment.this.getDialog().dismiss();


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

        cancelButton = (Button)view.findViewById(R.id.cancel_template);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialogFragment.this.getDialog().cancel();
            }
        });

        templateTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(templateTv);
            }
        });

       /* procedureTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(procedureTv);
            }
        });*/
        return view;
    }

   
}
