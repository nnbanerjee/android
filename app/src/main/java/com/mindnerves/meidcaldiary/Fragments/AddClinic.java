package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Adapter.ShowClinicAdapter;
import Application.MyApi;
import com.medico.model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 23-Feb-15.
 */
public class AddClinic extends Fragment implements Serializable {

    private static final String TAG = AddDoctor.class.getName();
    private ListView listViewAddClinic;
    public SharedPreferences session = null;
    private ShowClinicAdapter listClinicAdapter;
    public MyApi api;
    public String searchText = "";
    private Button addNewButton, buttonDone, searchButton, backButton, back;
    private EditText searchTv;
    private String clinicIds = "";
    private String patientId = "";
    private List<Clinic> arrayNew, selectedList;
    Global go;
    TextView globalTv;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goBack() {
        Fragment frag2 = new ManageClinicFragment();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void manageScreenIcon() {
        globalTv.setText("Add Clinic");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_clinic, container, false);
        listViewAddClinic = (ListView) view.findViewById(R.id.list_add_clinic);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        BackStress.staticflag = 0;
        searchTv = (EditText) view.findViewById(R.id.searchET);
       // getActivity().getActionBar().hide();
        go = (Global) getActivity().getApplicationContext();
        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID", null);


        showListView();

        addNewButton = (Button) view.findViewById(R.id.add_new_clinic);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new AddNewClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, "Add_Clinic");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        searchButton = (Button) view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                searchText = searchTv.getText().toString();
                if (searchText.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                    showListView();
                } else {
                    showListView();
                }


            }
        });


        listViewAddClinic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Clinic c = (Clinic) listClinicAdapter.getItem(position);
                if (c.getClinicName().equals("No Clinic Found")) {

                    Toast.makeText(getActivity(), "No Clinic Found", Toast.LENGTH_SHORT).show();

                } else {
                    go.setClinic(c);
                    Fragment frag2 = new EditClinic();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2, "Add_Clinic");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        backButton = (Button) view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new ManageClinicFragment();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        manageScreenIcon();
        return view;
    }

    public void showListView() {
        api.searchClinic(searchText, new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> array, Response response) {

                System.out.println("Arrays Value " + array.toString());

                arrayNew = new ArrayList<Clinic>();
                if (array.size() == 0) {
                    Clinic clinicSr = new Clinic();
                    clinicSr.setSelected(false);
                    clinicSr.setClinicName("No Clinic Found");
                    clinicSr.setLocation("None");
                    array.add(clinicSr);
                    System.out.println("IF " + array.size());
                    arrayNew = array;
                } else {
                    arrayNew = array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Krb Url" + response.getUrl());


                listClinicAdapter = new ShowClinicAdapter(getActivity(), arrayNew);

                listViewAddClinic.setAdapter(listClinicAdapter);
                //listAdapter.notifyDataSetChanged();
                System.out.println("Adapter list Count " + listClinicAdapter.getCount());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();

                ArrayList<Clinic> arrayNew = new ArrayList<Clinic>();
                if (arrayNew.size() == 0) {
                    Clinic clinicSr = new Clinic();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("No Clinic Found ");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    clinicSr.setSelected(false);
                    clinicSr.setClinicName("No Clinic Found");
                    clinicSr.setLocation("None");
                    arrayNew.add(clinicSr);
                    System.out.println("IF " + arrayNew.size());

                    listClinicAdapter = new ShowClinicAdapter(getActivity(), arrayNew);

                    listViewAddClinic.setAdapter(listClinicAdapter);

                }
            }
        });
    }


    public String doValidation() {
        int len = arrayNew.size();

        if (len == 0) {

            return "No Clinic";
        } else {
            selectedList = new ArrayList<Clinic>();
            for (int i = 0; i < len; i++) {

                if ((arrayNew.get(i)).isSelected() == true) {
                    selectedList.add(arrayNew.get(i));
                    System.out.println("Array Name " + arrayNew.get(i).getIdClinic() + " Value" + arrayNew.get(i).isSelected());
                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Clinics";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }

        }

    }

}
