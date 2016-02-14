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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
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
public class AddNewField extends Fragment {

    private ListView listView;
    private Spinner typeSpinner,nameSpinner;
    private Button backButton,addButton;
    private EditText df_name,df_value;
    private String fieldName = "",defaultType = "",fieldType = "";
    public MyApi api;
    private String templateId;

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

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        Global go = (Global)getActivity().getApplicationContext();
        ShowTemplate temp;
        temp = go.getTemp();
        templateId = temp.getTemplateId();

        nameSpinner = (Spinner)view.findViewById(R.id.name);
        typeSpinner = (Spinner)view.findViewById(R.id.type);
        df_value = (EditText)view.findViewById(R.id.df_value);
        df_name = (EditText)view.findViewById(R.id.df_name);

        backButton = (Button)view.findViewById(R.id.cancel);
        addButton = (Button)view.findViewById(R.id.add_field);

        final ArrayAdapter<CharSequence> type = ArrayAdapter.createFromResource(getActivity(),R.array.type_list,
                android.R.layout.simple_spinner_item);
        type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type);

        final ArrayAdapter<CharSequence> nameAdaptor = ArrayAdapter.createFromResource(getActivity(),R.array.name_list,
                android.R.layout.simple_spinner_item);
        nameAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nameSpinner.setAdapter(nameAdaptor);

        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temporary = nameAdaptor.getItem(position).toString();
                df_name.setText(temporary);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temporary = type.getItem(position).toString();
                df_value.setText(temporary);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                int flagValidation = 0;
                String validation = "";
                fieldName = nameSpinner.getSelectedItem().toString();
                defaultType = df_value.getText().toString();
                fieldType = typeSpinner.getSelectedItem().toString();

                if(fieldName.equals("")){
                    validation = "Please Enter Field Name";
                    flagValidation = 1;
                }
                if(defaultType.equals(""))
                {
                    validation = validation+"\nPlease Enter Default Value";
                    flagValidation = 1;
                }

                if(flagValidation == 1){
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                }
                else{
                       Field field = new Field();
                       field.setTemplateId(templateId);
                       field.setFieldName(fieldName);
                       field.setFieldType(fieldType);
                       field.setFieldDisplayName(df_name.getText().toString());
                       field.setFieldDefaultValue(df_value.getText().toString());

                       api.addField(field,new Callback<Response>() {
                           @Override
                           public void success(Response response, Response response2) {
                             Toast.makeText(getActivity(),"Field Added Successfully",Toast.LENGTH_SHORT).show();
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

        /*fieldNameTv.addTextChangedListener(new TextWatcher() {
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
        });*/

        df_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(df_value);
            }
        });
        return view;
    }
}
