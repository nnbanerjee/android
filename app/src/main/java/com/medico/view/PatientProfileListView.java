package com.medico.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.medico.model.DoctorId;
import com.medico.model.PatientProfileList;
import com.medico.model.PatientShortProfile;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Adapter.DoctorPatientListAdapter;
import Utils.PARAM;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class PatientProfileListView extends com.medico.view.ParentFragment {


    SharedPreferences session;
    ListView doctorListView;
    ProgressDialog progress;
//    TextView globalTv,accountName;
//    Button drawar,refresh,logout,back;
//    ImageView profilePicture;
//    RelativeLayout profileLayout,homeLayout;
//    public  String  doctorId="";
    //ImageView medicoLogo,medicoText;
//    String type;
//    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.patient_doctors_list,container,false);

        doctorListView = (ListView) view.findViewById(R.id.doctorListView);
//        doctorListView = (ListView)getActivity().findViewById(R.id.doctorListView);

        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
//        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        back = (Button)getActivity().findViewById(R.id.back_button);
//        homeLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout1);

//        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
//        accountName = (TextView) getActivity().findViewById(R.id.account_name);
//        logout = (Button)getActivity().findViewById(R.id.logout);
//        logout.setVisibility(View.GONE);
       // medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
       // medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
//        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
//        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
//        refresh = (Button)getActivity().findViewById(R.id.refresh);
//        type = session.getString("loginType",null);
//        doctorId = session.getString("id",null);

      /*  getActivity(). getActionBar().show();
        getActivity(). getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f6599")));
        getActivity(). getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity(). getActionBar().setTitle("Patient Profiles");*/

//        toolbar=(Toolbar)getActivity().findViewById(R.id.my_toolbar);
//        toolbar.setVisibility(View.VISIBLE);
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.menu);

      //  getActivity().setActionBar(toolbar);


//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBack();
//
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                logout.setBackgroundResource(R.drawable.logout);
//                getActivity().finish();
//                Intent i = new Intent(getActivity(), HomeActivity.class);
//                startActivity(i);
//            }
//        });
//        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        SharedPreferences.Editor editor = session.edit();
//                        editor.putString("doctorId", doctorId);
//                        editor.putString("patientId", allPatientsProfiles.getPatientlist()[i].getPatientId().toString());
//                        editor.commit();
                Bundle bun = getActivity().getIntent().getExtras();
                PatientShortProfile profile = (PatientShortProfile)adapterView.getAdapter().getItem(i);
                        ParentFragment fragment = new PatientVisitDatesView();
                        ((ManagePatientProfile)getActivity()).fragmentList.add(fragment);
                        bun.putInt(PARAM.PATIENT_ID, profile.getPatientId().intValue());
                        getActivity().getIntent().putExtras(bun);
                      fragment.setArguments(bun);
                        FragmentManager fragmentManger = getActivity().getFragmentManager();
                        fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.networkInterceptors().add(new LoggingInterceptor());
//
//        //Retrofit Initialization
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getResources().getString(R.string.base_url))
//                .setClient(new OkClient(okHttpClient))
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//
//                .build();

//        api = restAdapter.create(MyApi.class);
//        manageScreenIcons();
//        showDoctorList();
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        DoctorId doc= new DoctorId(new Integer(bundle.getInt(PARAM.DOCTOR_ID)).toString());
        api.getPatientProfileList(doc, new Callback<List<PatientShortProfile>>() {
            @Override
            public void success(final List<PatientShortProfile> allPatientsProfiles, Response response) {
//                Global global = (Global) getActivity().getApplicationContext();
//                global.setAllPatients(new PatientProfileList(allPatientsProfiles));
                DoctorPatientListAdapter adapter = new DoctorPatientListAdapter(getActivity(), new PatientProfileList(allPatientsProfiles));
                doctorListView.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        // Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        System.out.println("Inside fragment AllDoctorsPatient --->onCreateOptionsMenu()");

//         toolbar.inflateMenu(R.menu.menu);
       //super.onCreateOptionsMenu(menu, inflater);

    }

    public void manageScreenIcons()
    {
//        drawar.setVisibility(View.GONE);
       // medicoLogo.setVisibility(View.GONE);
       // medicoText.setVisibility(View.GONE);
//        logout.setVisibility(View.GONE);
//         logout.setBackgroundResource(R.drawable.home_jump);
//        profileLayout.setVisibility(View.GONE);
//        profilePicture.setVisibility(View.GONE);
//        accountName.setVisibility(View.GONE);
//        globalTv.setText("Patient Profiles");
//        back.setVisibility(View.VISIBLE);
//        refresh.setVisibility(View.GONE);
      //homeLayout.setVisibility(View.GONE);
    }
    public void goToBack(){
//        globalTv.setText(type);
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
//        logout.setBackgroundResource(R.drawable.logout);
//        logout.setVisibility(View.GONE);
//        drawar.setVisibility(View.VISIBLE);
//        profileLayout.setVisibility(View.VISIBLE);
//        profilePicture.setVisibility(View.VISIBLE);
//        accountName.setVisibility(View.VISIBLE);
//        back.setVisibility(View.GONE);
//        toolbar.setVisibility(View.GONE);
//        drawar.setVisibility(View.VISIBLE);
//        refresh.setVisibility(View.VISIBLE);
        //homeLayout.setVisibility(View.VISIBLE);
       // medicoLogo.setVisibility(View.VISIBLE);
       // medicoText.setVisibility(View.VISIBLE);
    }
}
