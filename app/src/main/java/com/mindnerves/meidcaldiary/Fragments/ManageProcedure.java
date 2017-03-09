package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.ProcedureAdapter;
import com.medico.application.MyApi;
import Model.ShowProcedure;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 27-Mar-15.
 */
public class ManageProcedure extends Fragment {
    private ListView listProcedure;
    private Button addNewProcedure, searchButton, drawar, logout, back;
    private ArrayList<ShowProcedure> arrayProcedure, nameList, caetogryList;
    private ProcedureAdapter adapter;
    private String doctorId;
    SharedPreferences session;
    ProgressDialog progress;
    private EditText searchTv;
    TextView noResult;
    Spinner category;
    public MyApi api;
    FragmentManager fragmentManger;
    LinearLayout layout;
    TextView globalTv, accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
   // ImageView medicoLogo,medicoText;
    Button refresh;
    String type;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        //showTemplateList();
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

    public void goBack()
    {
        globalTv.setText(type);

        DoctorMenusManage fragment = new DoctorMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
      //  medicoLogo.setVisibility(View.VISIBLE);
     //   medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_procedure, container, false);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);
        listProcedure = (ListView) view.findViewById(R.id.list_procedure);
        noResult = (TextView) view.findViewById(R.id.noResult);
        addNewProcedure = (Button) view.findViewById(R.id.addNewProcedure);
        category = (Spinner) view.findViewById(R.id.category);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
        progress = ProgressDialog.show(getActivity(), "", "Loading...Please wait...");
        caetogryList = new ArrayList<ShowProcedure>();
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        final SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        //getActivity().getActionBar().hide();
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
      //  medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
       // medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();
            }
        });

        final ArrayAdapter<CharSequence> categoryAdaptor = ArrayAdapter.createFromResource(getActivity(), R.array.category_list,
                android.R.layout.simple_spinner_item);
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temporary = categoryAdaptor.getItem(position).toString();

                caetogryList.clear();
                for (ShowProcedure showProcedure : arrayProcedure) {
                    if (temporary.equals(showProcedure.getCategory().toString())) {
                        caetogryList.add(showProcedure);
                    }
                }
                if (temporary.equals("All")) {
                    caetogryList = arrayProcedure;
                }
                ArrayList arr= new ArrayList();
                arr.addAll(caetogryList);
                adapter = new ProcedureAdapter(getActivity(), arr);
                listProcedure.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showProcedureList();

        final Global go = (Global) getActivity().getApplicationContext();
        addNewProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewProcedureDialog adf = AddNewProcedureDialog.newInstance();
                adf.show(getFragmentManager(), "Dialog");
                showProcedureList();

            }
        });
        searchTv = (EditText) view.findViewById(R.id.search_template);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String searchText = searchTv.getText().toString();
                if (searchText.equals("")) {
                    //Toast.makeText(getActivity(),"Please Enter Text",Toast.LENGTH_SHORT).show();
                    searchTv.setError("Please Enter Text ");
                    ArrayList arr= new ArrayList();
                    arr.addAll(arrayProcedure);
                    adapter = new ProcedureAdapter(getActivity(), arr);
                    listProcedure.setAdapter(adapter);
                    //showTemplateList();
                } else {
                    showListBySearch(searchText);
                }
            }
        });
        listProcedure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView procedure = (TextView) view.findViewById(R.id.procedure);

                SharedPreferences.Editor editor = session.edit();
                editor.putString("selected_procedure_name", procedure.getText().toString());
                editor.commit();
                Fragment fragment = new ManageTemplate();
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Template").addToBackStack(null).commit();
            }

        });
        manageScreenIcon();
        return view;
    }

    public void showProcedureList() {
        arrayProcedure = new ArrayList<ShowProcedure>();
        api.getAllProcedure(doctorId, new Callback<ArrayList<ShowProcedure>>() {
            @Override
            public void success(ArrayList<ShowProcedure> templates, Response response) {
                arrayProcedure = templates;

                if (arrayProcedure.size() == 0) {
                    noResult.setVisibility(View.VISIBLE);
                    listProcedure.setVisibility(View.GONE);
                }
                ArrayList arr= new ArrayList();
                arr.addAll(arrayProcedure);
                adapter = new ProcedureAdapter(getActivity(), arr);
                listProcedure.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void manageScreenIcon() {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Procedure");
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        BackStress.staticflag = 1;
     //   medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }

    public void showListBySearch(String searchText) {

        nameList = new ArrayList<ShowProcedure>();
        for (ShowProcedure t : arrayProcedure) {
            if (t.getProcedureName().toLowerCase().contains(searchText.toLowerCase())) {
                nameList.add(t);
            }
        }

        if (nameList.size() > 0) {
            noResult.setVisibility(View.GONE);
            listProcedure.setVisibility(View.VISIBLE);
            ArrayList arr= new ArrayList();
            arr.addAll(nameList);
            adapter = new ProcedureAdapter(getActivity(), arr);
            listProcedure.setAdapter(adapter);
        } else {
            noResult.setVisibility(View.VISIBLE);
            listProcedure.setVisibility(View.GONE);
        }

    }
}
