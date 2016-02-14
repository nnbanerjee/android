package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.PatientAdapter;
import Application.MyApi;
import Model.Patient;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 16-Feb-15.
 */
public class AddPatient extends Fragment {

    private static final String TAG = AddPatient.class.getName();
    private ListView listView;
    private PatientAdapter listAdapter;
    private ArrayList<Patient> docSearchRes;
    private ArrayList<Patient> selectedList;
    public SharedPreferences session = null;
    public MyApi api;
    public String searchText = "";
    private Button searchButton, doneButton, backButton, addNewPatient, back;
    private RadioButton nameRadio, locationRadio,mobileNumber,id;
    private ArrayList<Patient> nameList, mobileNumberList, locationList, finalList,idList;
    private EditText searchTv;
    private String patientIds = "";
    private String doctorId = "";
    private ArrayList<Patient> list;
    ArrayList<Patient> selectedId;
    TextView globalTv;

    @Override
    public void onResume() {
        super.onResume();
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
        Fragment frag2 = new ManagePatientFragment();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void manageScreenIcon() {
        globalTv.setText("Add Patient");
        BackStress.staticflag = 0;
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.add_patient, container, false);
        selectedId = new ArrayList<Patient>();
        docSearchRes = new ArrayList<Patient>();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("id", null);
        listView = (ListView) view.findViewById(R.id.list_add_patient);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        searchTv = (EditText) view.findViewById(R.id.searchET);
        getActivity().getActionBar().hide();
        Global go = (Global) getActivity().getApplicationContext();
        list = go.getPatientList();
        System.out.println("Global List::::::::::::::::::");
        for (Patient p : list) {
            System.out.println("Patient Name ::::" + p.getName());
        }

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
        nameRadio = (RadioButton) view.findViewById(R.id.search_name);
		mobileNumber = (RadioButton)view.findViewById(R.id.search_mobile_number);
		id = (RadioButton)view.findViewById(R.id.search_id);
        locationRadio = (RadioButton) view.findViewById(R.id.search_email);
        nameRadio.setChecked(true);

        searchButton = (Button) view.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				showListView();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                searchText = searchTv.getText().toString();
                Boolean nameBoolean = nameRadio.isChecked();
                Boolean emailBoolean = locationRadio.isChecked();
				Boolean mobileBoolean = mobileNumber.isChecked();
				Boolean idBoolean = id.isChecked();
                nameList = new ArrayList<Patient>();
                mobileNumberList = new ArrayList<Patient>();
                locationList = new ArrayList<Patient>();
				idList = new ArrayList<Patient>();
                if (nameBoolean) {
                    int len = docSearchRes.size();

                    for (int i = 0; i < len; i++) {
                        if (docSearchRes.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {

                            nameList.add(docSearchRes.get(i));
                        }
                    }

                    showListByName();


                }


                if (emailBoolean) {
                    int len = docSearchRes.size();

                    for (int i = 0; i < len; i++) {
                        if (docSearchRes.get(i).location.toLowerCase().contains(searchText.toLowerCase())) {
                            locationList.add(docSearchRes.get(i));
                        }

                    }

                    showListByEmail();
                }
				if (mobileBoolean) {
					int len = docSearchRes.size();

					for (int i = 0; i < len; i++) {
						if (docSearchRes.get(i).mobileNumber.contains(searchText)) {
							mobileNumberList.add(docSearchRes.get(i));
						}

					}

					showListByMobile();
				}

				if (idBoolean) {
					int len = docSearchRes.size();

					for (int i = 0; i < len; i++) {
						if (docSearchRes.get(i).getPatientId().contains(searchText)) {
							idList.add(docSearchRes.get(i));
						}

					}

					showListById();
				}

				if (searchText.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Text", Toast.LENGTH_SHORT).show();
                    showListView();
                }


            }
        });

        doneButton = (Button) view.findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if (result.equals("No Patient")) {
                    Toast.makeText(getActivity(), "No Patient In List", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                } else if (result.equals("No Selected Patient")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Patient Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {
                    int len = selectedList.size();
                    int j = len - 1;

                    for (int i = 0; i < len; i++) {
                        if (i == j) {
                            patientIds = patientIds + selectedList.get(i).getPatientId();
                        } else {
                            patientIds = patientIds + selectedList.get(i).getPatientId() + ",";
                        }
                    }
                    System.out.println("Patient IDs " + patientIds);

                    api.addPatient(doctorId, patientIds, new Callback<String>() {
                        @Override
                        public void success(String response, Response response2) {
                            if (response.equalsIgnoreCase("Success")) {
                                Toast.makeText(getActivity(), "Patients Are Added", Toast.LENGTH_LONG).show();
                                Fragment frag2 = new ManagePatientFragment();
                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, frag2, null);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.addToBackStack(null);
                                ft.commit();
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

                Fragment frag2 = new ManagePatientFragment();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        addNewPatient = (Button) view.findViewById(R.id.add_new_patient);
        addNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag = new AddNewPatient();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_New_Doc");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        manageScreenIcon();
        return view;
    }


    public void showListView() {
        finalList = new ArrayList<Patient>();
        api.searchPatients(searchText, new Callback<ArrayList<Patient>>() {
            @Override
            public void success(ArrayList<Patient> array, Response response) {

                System.out.println("Arrays Value " + array.toString());


                if (array.size() == 0) {
                    Patient docSr = new Patient();

                    docSr.setSelected(false);
                    docSr.setName("No Patient Found");
                    docSr.setLocation("None");
                    array.add(docSr);
                    System.out.println("IF " + array.size());
                    docSearchRes = array;
                } else {


                    docSearchRes = array;
                    selectedId = docSearchRes;
                    ArrayList<Patient> removeList = new ArrayList<Patient>();
                    System.out.println("SelectedID PatientID::::" + docSearchRes.size());
                    int len = array.size();

                    System.out.println("Length::::" + len);

                    System.out.println("SelectedID PatientID::::" + selectedId.size());
                    System.out.println("Array PatientID::::" + len);

                    System.out.println("List size::::::::" + list.size());

                    for (int i = 0; i < docSearchRes.size(); i++) {
                        for (int j = 0; j < list.size(); j++) {
                            if (docSearchRes.get(i).getPatientId().equals(list.get(j).getPatientId())) {
                                removeList.add(docSearchRes.get(i));
                            }

                        }
                    }
                    docSearchRes.removeAll(removeList);
                    if (docSearchRes.size() == 0) {
                        Patient docSr = new Patient();
                        docSr.setSelected(false);
                        docSr.setName("No Patient Found");
                        docSr.setLocation("None");
                        array.add(docSr);
                    }
                    System.out.println("New Size:::::" + docSearchRes.size());

                }
                System.out.println("Krb Url" + response.getUrl());
                finalList.addAll(docSearchRes);

                listAdapter = new PatientAdapter(getActivity(), docSearchRes);

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

    public void showListByName() {
        finalList = new ArrayList<Patient>();
        finalList.clear();
        int lenName = nameList.size();

        if (lenName == 0) {
            Patient docSr = new Patient();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmailID(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        } else {
            finalList.addAll(nameList);
        }

        listAdapter = new PatientAdapter(getActivity(), finalList);

        listView.setAdapter(listAdapter);
        //listAdapter.notifyDataSetChanged();
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }


    public void showListByEmail() {
        finalList = new ArrayList<Patient>();
        finalList.clear();
        int lenEmail = locationList.size();

        if (lenEmail == 0) {
            Patient docSr = new Patient();
            docSr.setSelected(false);
            docSr.setName("No Patient Found");
            docSr.setLocation("None");
            docSr.setEmailID(" ");
            docSr.setMobileNumber(" ");
            finalList.add(docSr);
        } else {
            finalList.addAll(locationList);

        }

        listAdapter = new PatientAdapter(getActivity(), finalList);
        listView.setAdapter(listAdapter);
        System.out.println("Adapter list Count " + listAdapter.getCount());


    }

	public void showListByMobile() {
		finalList = new ArrayList<Patient>();
		finalList.clear();
		int lenMobile = mobileNumberList.size();

		if (lenMobile == 0) {
			Patient docSr = new Patient();
			docSr.setSelected(false);
			docSr.setName("No Patient Found");
			docSr.setLocation("None");
			docSr.setEmailID(" ");
			docSr.setMobileNumber(" ");
			finalList.add(docSr);
		} else {
			finalList.addAll(mobileNumberList);

		}

		listAdapter = new PatientAdapter(getActivity(), finalList);
		listView.setAdapter(listAdapter);
		System.out.println("Adapter list Count " + listAdapter.getCount());


	}

	public void showListById() {
		finalList = new ArrayList<Patient>();
		finalList.clear();
		int lenId = idList.size();

		if (lenId == 0) {
			Patient docSr = new Patient();
			docSr.setSelected(false);
			docSr.setName("No Patient Found");
			docSr.setLocation("None");
			docSr.setEmailID(" ");
			docSr.setMobileNumber(" ");
			finalList.add(docSr);
		} else {
			finalList.addAll(idList);

		}

		listAdapter = new PatientAdapter(getActivity(), finalList);
		listView.setAdapter(listAdapter);
		System.out.println("Adapter list Count " + listAdapter.getCount());


	}

    public String doValidation() {
        int len = docSearchRes.size();

        if (len == 0) {

            return "No Patient";
        } else {
            selectedList = new ArrayList<Patient>();
            for (int i = 0; i < len; i++) {

                if ((docSearchRes.get(i)).isSelected() == true) {
                    selectedList.add(docSearchRes.get(i));
                    System.out.println("Array Name " + docSearchRes.get(i).getPatientId() + " Value" + docSearchRes.get(i).isSelected());

                }
            }

            if (selectedList.size() == 0) {
                return "No Selected Patient";
            } else {
                System.out.println("Selected Objects ::: " + selectedList.size());
                return "Normal";
            }


        }
    }

}