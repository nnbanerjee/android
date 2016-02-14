package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.AssistntAdapter;
import Application.MyApi;
import Model.Assistant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 13-Mar-15.
 */
public class AddAssistant extends Fragment {

    private ListView listView;
    private EditText searchTv;
    private AssistntAdapter listAdapter;
    private String doctorId = "";
    public MyApi api;
    public String searchText = "", assistantIds = "";
    private ArrayList<Assistant> docSearchRes, selectedList, alreadyAdded, selectedId;
    private Button searchButton, doneButton, addNewButton, backButton, back;
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

    public void manageScreenIcon() {
        globalTv.setText("Add Assistant");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }

    public void goBack() {
        Fragment frag2 = new ManageastantFragment();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_assistant, container, false);
        listView = (ListView) view.findViewById(R.id.list_manage_assistant);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        selectedId = new ArrayList<Assistant>();
        searchTv = (EditText) view.findViewById(R.id.searchET);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        Global go = (Global) getActivity().getApplicationContext();
        alreadyAdded = go.getAssistantList();
        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showListView();
        searchButton = (Button) view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = searchTv.getText().toString();

                if (searchText.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                    showListView();
                } else {
                    showListView();
                }


            }
        });

        addNewButton = (Button) view.findViewById(R.id.add_new_doctor);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddNewAssistant();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_New_Doc");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        doneButton = (Button) view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if (result.equals("No Assistant")) {
                    Toast.makeText(getActivity(), "No Assistant In List", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                } else if (result.equals("No Selected Assistant")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Assistant Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {
                    int len = selectedList.size();
                    int j = len - 1;

                    for (int i = 0; i < len; i++) {
                        if (i == j) {
                            assistantIds = assistantIds + selectedList.get(i).getId();
                        } else {
                            assistantIds = assistantIds + selectedList.get(i).getId() + ",";
                        }
                    }
                    System.out.println("Assistant IDs " + assistantIds);

                    api.addDoctorsAssistants(doctorId, assistantIds, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();

                            System.out.println("URl " + response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Assistants Are Added", Toast.LENGTH_LONG).show();
                                FragmentManager fm = getFragmentManager();
                                fm.popBackStack();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();


                        }
                    });

                }
            }
        });

        backButton = (Button) view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new ManageastantFragment();
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

        api.searchAssistants(searchText, new Callback<ArrayList<Assistant>>() {
            @Override
            public void success(ArrayList<Assistant> array, Response response) {

                System.out.println("Arrays Value " + array.toString());

                docSearchRes = new ArrayList<Assistant>();
                if (array.size() == 0) {
                    Assistant docSr = new Assistant();

                    docSr.setSelected(false);
                    docSr.setName("No Assistant Found");
                    docSr.setLocation("None");
                    array.add(docSr);
                    System.out.println("IF " + array.size());
                    docSearchRes = array;
                } else {
                    docSearchRes = array;
                    ArrayList<Assistant> removeList = new ArrayList<Assistant>();

                    for (int i = 0; i < docSearchRes.size(); i++) {
                        for (int j = 0; j < alreadyAdded.size(); j++) {
                            removeList.add(docSearchRes.get(i));
                        }
                    }

                    docSearchRes.removeAll(removeList);

                    System.out.println("SelectedID PatientID::::" + docSearchRes.size());
                    if (docSearchRes.size() == 0) {
                        Assistant docSr = new Assistant();

                        docSr.setSelected(false);
                        docSr.setName("No Assistant Found");
                        docSr.setLocation("None");
                        array.add(docSr);
                        System.out.println("IF " + array.size());
                        docSearchRes = array;
                    }
                    System.out.println("Else " + docSearchRes.size());
                }

                System.out.println("Krb Url" + response.getUrl());


                listAdapter = new AssistntAdapter(getActivity(), docSearchRes);

                listView.setAdapter(listAdapter);
                //listAdapter.notifyDataSetChanged();
                System.out.println("Adapter list Count " + listAdapter.getCount());
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    public String doValidation() {
        int len = docSearchRes.size();

        if (len == 0) {

            return "No Assistant";
        } else {
            selectedList = new ArrayList<Assistant>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    selectedList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getId() + " Value" + docSearchRes.get(i).isSelected());

                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Assistant";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }


        }

    }
}
