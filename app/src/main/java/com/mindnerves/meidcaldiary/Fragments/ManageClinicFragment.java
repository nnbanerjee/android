package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import java.util.List;
import Adapter.ClinicAdapter;
import Application.MyApi;
import Model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 23-Feb-15.
 */
public class ManageClinicFragment extends Fragment {

    private Button addClinic,removeClinic,drawar,logout,back;
    private ArrayList<Clinic> arrayClinic,arrayNew,removeList;
    private String searchText = "";
    private ClinicAdapter listAdapterClinic;
    private ListView listViewManageClinic;
    String clinicIds = "";
    MyApi api;
    private String doctorId = "";
    FragmentManager fragmentManger;
    LinearLayout layout;
    TextView globalTv, accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    ImageView medicoLogo,medicoText;
    Button refresh;
    String type;
	Global go;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
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
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        medicoLogo.setVisibility(View.VISIBLE);
        medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_clinic,container,false);
        listViewManageClinic= (ListView)view.findViewById(R.id.list_manage_clinic);
        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        getActivity().getActionBar().hide();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("type",null);
        doctorId = session.getString("id",null);
		go = (Global) getActivity().getApplicationContext();
        layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        back = (Button)getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        logout = (Button)getActivity().findViewById(R.id.logout);
        medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
        medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        showClinicList();
        addClinic = (Button)view.findViewById(R.id.add_clinic);
        addClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new AddClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,frag2,"Add_Clinic");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        removeClinic = (Button)view.findViewById(R.id.remove_clinic);
        removeClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();
                clinicIds = "";

                if(result.equals("No Clinic"))
                {
                    Toast.makeText(getActivity(),"No Clinic In List",Toast.LENGTH_LONG).show();


                }
                else if(result.equals("No Selected Clinics"))
                {
                    System.out.println("Result Variable Contain  "+result);
                    Toast.makeText(getActivity(),"No Clinics Selected",Toast.LENGTH_LONG).show();


                }
                else if (result.equals("Normal"))
                {
                    Toast.makeText(getActivity(),"Doctors Removed",Toast.LENGTH_LONG).show();
                    int len = removeList.size();
                    int j = len-1;

                    for(int i=0;i<len;i++)
                    {
                        if(i == j)
                        {
                            clinicIds = clinicIds+removeList.get(i).getIdClinic();
                        }
                        else
                        {
                            clinicIds = clinicIds+removeList.get(i).getIdClinic()+",";
                        }
                    }
                    System.out.println("Clinics IDs "+clinicIds);
					System.out.println("Length= "+clinicIds.length());
                    api.removeDoctorClinic(doctorId,clinicIds, new Callback<String>() {
                        @Override
                        public void success(String response, Response response2) {
                            if(response.equalsIgnoreCase("Success")) {
                                Toast.makeText(getActivity(),"Doctors Are Removed",Toast.LENGTH_LONG).show();
								showClinicList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                            error.printStackTrace();

                        }
                    });

                }

            }
        });

        listViewManageClinic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Clinic c = (Clinic) listAdapterClinic.getItem(position);
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
        manageScreenIcon();
        return view;
    }

    public String doValidation()
    {
        int len = arrayNew.size();

        if(len == 0)
        {

            return "No Clinic";
        }
        else
        {
            removeList = new ArrayList<Clinic>();
            for (int i = 0; i < len; i++) {

                if ((arrayNew.get(i)).isSelected() == true) {
                    removeList.add(arrayNew.get(i));
                    System.out.println("Array Name " + arrayNew.get(i).getIdClinic() + " Value" + arrayNew.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Clinics";
            }
            else
            {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }

    }
    public void manageScreenIcon() {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Clinic");
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        BackStress.staticflag = 1;
        medicoLogo.setVisibility(View.GONE);
        medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.INVISIBLE);
    }
     public void showClinicList()
    {
        arrayClinic = new ArrayList<Clinic>();
        api.doctorClinics(doctorId, new Callback<List<Clinic>>() {
            @Override
            public void success(List<Clinic> array, Response response) {

                System.out.println("Kb Array " + array.size());

                arrayNew = new ArrayList<Clinic>();
                if (array.size() == 0) {
                    Clinic clinicSr = new Clinic();

                    clinicSr.setSelected(false);
                    clinicSr.setClinicName("No Clinic Found");
                    clinicSr.setLocation("None");
                    arrayNew.add(clinicSr);
                    System.out.println("IF " + arrayNew.size());


                } else {
                    arrayNew = (ArrayList<Clinic>) array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Arrays Size " + arrayNew.size());

                System.out.println("Krb Url" + response.getUrl());
                System.out.println("Arrays values "+arrayNew.toArray());

                listAdapterClinic = new ClinicAdapter(getActivity(), arrayNew);
                System.out.println("ListAdapterClinic "+listAdapterClinic.getCount());
                listViewManageClinic.setAdapter(listAdapterClinic);
                System.out.println("List view Manage "+listViewManageClinic.getSelectedItem());
                System.out.println("Adapter list Count " + listViewManageClinic.getCount());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
