package com.mindnerves.meidcaldiary.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.io.File;

public class MedicalRegDoctor extends WizardStep {


    private EditText etEmailAddrss, etpassword;
    public RadioButton local_save;
    public RadioButton local_personal;
    public RadioButton local_encrypted;
    Button uploadDocument;
    Uri selectedDocumentUri;
    String selectedDocumentPath = "";
    private static final int SELECT_PICTURE = 1;
    TextView documentPath;
    @ContextVariable
    private String id;
    @ContextVariable
    private String passwordCloud;
    @ContextVariable
    private Uri documentUri;
    private Spinner practiceName;
    public MedicalRegDoctor() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medical_registration_doctor,
                container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.bloodGroup);

        etEmailAddrss = (EditText) view.findViewById(R.id.id);
        etpassword = (EditText) view.findViewById(R.id.password);
        final EditText id, password;
        id = (EditText) view.findViewById(R.id.id);
        password = (EditText) view.findViewById(R.id.password);
        uploadDocument = (Button)view.findViewById(R.id.upload_document);
        local_save = (RadioButton) view.findViewById(R.id.saveLocal);
        local_personal = (RadioButton) view.findViewById(R.id.personalCloud);
        local_encrypted = (RadioButton) view.findViewById(R.id.encrypted);
        documentPath = (TextView)view.findViewById(R.id.document_name);
        practiceName = (Spinner)view.findViewById(R.id.practice_name);
        ArrayAdapter<CharSequence> practiceNameAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.practice_name, android.R.layout.simple_spinner_item);
        practiceNameAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        practiceName.setAdapter(practiceNameAdapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bloodgroup_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        Spinner spinnerspl = (Spinner) view.findViewById(R.id.speciality);
        ArrayAdapter<CharSequence> adapterspl = ArrayAdapter.createFromResource(getActivity(),
                R.array.speciality_list, android.R.layout.simple_spinner_item);
        adapterspl.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerspl.setAdapter(adapterspl);
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
        uploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        registerViews();
        return view;
    }

    private void bindDataFields() {
        id = etEmailAddrss.getText().toString();
        passwordCloud = etpassword.getText().toString();
        documentUri = selectedDocumentUri;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            selectedDocumentUri = data.getData();
            selectedDocumentPath = getPath(selectedDocumentUri);
            System.out.println("Image Path : " + selectedDocumentPath);
            File file = new File(selectedDocumentPath);
            documentPath.setText(file.getName());
            documentUri = selectedDocumentUri;
        }
    }
    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.isEmailAddress(etEmailAddrss, true)) ret = false;
        if (!Validation.hasText(etpassword)) ret = false;

        return ret;
    }

}