package com.mindnerves.meidcaldiary.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.mindnerves.meidcaldiary.R;
import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

public class MedicalReg extends WizardStep {

    private EditText etEmailAddrss, etpassword;


    public RadioButton local_save;
    public RadioButton local_personal;
    public RadioButton local_encrypted;


    @ContextVariable
    private String id;
    @ContextVariable
    private String passwordCloud;


    public MedicalReg() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medical_registration,
                container, false);
        System.out.println("I am in Medical Registration::::::::::::::::");
        Spinner spinner = (Spinner) view.findViewById(R.id.bloodGroup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        etEmailAddrss = (EditText) view.findViewById(R.id.id);
        etpassword = (EditText) view.findViewById(R.id.password);
        spinner.setAdapter(adapter);
        final EditText id,password;
        id= (EditText) view.findViewById(R.id.id);
        password= (EditText) view.findViewById(R.id.password);
        local_save= (RadioButton) view.findViewById(R.id.saveLocal);
        local_personal= (RadioButton) view.findViewById(R.id.personalCloud);
        local_encrypted = (RadioButton) view.findViewById(R.id.encrypted);
        final Spinner spinner1 = (Spinner) view.findViewById(R.id.cloudSelect);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.drive_list, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        local_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.INVISIBLE);
                id.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
            }
        });

        local_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
            }
        });

        local_encrypted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setVisibility(View.INVISIBLE);
                id.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
            }
        });

        registerViews();
        return view;
    }

    private void bindDataFields() {
        id = etEmailAddrss.getText().toString();
        passwordCloud = etpassword.getText().toString();

        System.out.println("Password "+passwordCloud);

    }

    private void registerViews() {

        etEmailAddrss.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {

                Validation.isEmailAddress(etEmailAddrss, true);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        etpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(etpassword);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.isEmailAddress(etEmailAddrss, true)) ret = false;
        if (!Validation.hasText(etpassword)) ret = false;

        return ret;
    }

}