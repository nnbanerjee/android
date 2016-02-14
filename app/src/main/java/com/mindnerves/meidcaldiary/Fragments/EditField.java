package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Application.MyApi;
import Model.Field;
import Model.FieldElement;
import Model.UpdateField;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 28-Mar-15.
 */
public class EditField extends Fragment {
    private Spinner typeSpinner;
    private Global go;
    private ArrayAdapter<CharSequence> type;
    private EditText fieldNameTv,defaultTv;
    private Button add,backButton;
    private String fieldId;
    private String fieldName = "",defaultType = "",fieldType = "";
    public MyApi api;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        System.out.println("I am here");
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    Fragment frag2 = new SelectTemplate();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2,null);
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

        View view = inflater.inflate(R.layout.add_new_field_template,container,false);
        typeSpinner = (Spinner)view.findViewById(R.id.type);
        type = ArrayAdapter.createFromResource(getActivity(),R.array.type_list,
                android.R.layout.simple_spinner_item);
        type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new SelectTemplate();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temporary = type.getItem(position).toString();
                defaultTv.setText(temporary);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fieldNameTv = (EditText)view.findViewById(R.id.name);
        defaultTv = (EditText)view.findViewById(R.id.df_value);
        go = (Global)getActivity().getApplicationContext();
        showEditData();
        add = (Button)view.findViewById(R.id.add_field);
        add.setText("Update Field");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                int flagValidation = 0;
                String validation = "";
                fieldName = fieldNameTv.getText().toString();
                defaultType = defaultTv.getText().toString();
                fieldType = typeSpinner.getSelectedItem().toString();

                System.out.println("Field Name::::"+fieldName);
                System.out.println("Field Type::::"+fieldType);


                if(fieldName.equals(""))
                {
                    validation = "Please Enter Field Name";
                    flagValidation = 1;
                }
                if(fieldType.equals(""))
                {
                    validation = validation+"\nPlease Enter Default Value";
                    flagValidation = 1;
                }

                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FieldElement element = new FieldElement();
                    element.setName(fieldName);
                    element.setType(fieldType);


                    ArrayList<FieldElement> fields = new ArrayList<FieldElement>();
                    fields.add(element);
                    UpdateField upf = new UpdateField();
                    upf.setFieldId(fieldId);
                    upf.setFields(fields);
                    api.updateField(upf,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            Toast.makeText(getActivity(),"Field Updated Successfully",Toast.LENGTH_SHORT).show();
                            Fragment fragment = new SelectTemplate();

                            FragmentManager fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Select Template").commit();
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

        backButton = (Button)view.findViewById(R.id.cancel);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new SelectTemplate();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2,null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fieldNameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Validation.hasText(fieldNameTv);

            }
        });
        defaultTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(defaultTv);
            }
        });
        return view;
    }



    public void showEditData()
    {
        Field field = go.getField();
        fieldId = field.getFieldId();
        int spinnerPosition = 0;
        spinnerPosition = type.getPosition(field.getFieldType());
        typeSpinner.setSelection(spinnerPosition);
        fieldNameTv.setText(field.getFieldName());
        defaultTv.setText(field.getFieldType());
    }
}
