package com.medico.view;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Fragments.AddAssistant;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.AssistntAdapter;
import com.medico.application.MyApi;
import Model.Assistant;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 19-Feb-15.
 */
public class ManageastantFragment extends Fragment {

    private ListView listassitant;
    private AssistntAdapter listAdapterManageAssitant;
    private ArrayList<Assistant> astntSearchRes;
    private Button removeAstnt, buttonAdd, drawar, logout, back;
    public MyApi api;
    private ArrayList<Assistant> arrayNew;
    private ArrayList<Assistant> removeList;
    private String assitantIds = "";
    private String doctorId;
    FragmentManager fragmentManger;
    LinearLayout layout;
    TextView globalTv, accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    //ImageView medicoLogo,medicoText;
    Button refresh;
    String type;
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
       // medicoLogo.setVisibility(View.VISIBLE);
      //  medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_assistant, container, false);
        listassitant = (ListView) view.findViewById(R.id.list_manage_assistant);
        BackStress.staticflag = 0;
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID", null);
        getActivity().getActionBar().hide();
        type = session.getString("loginType",null);
        BackStress.staticflag = 1;
        final Global go = (Global) getActivity().getApplicationContext();
        back = (Button) getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
       // medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showassistantsList();


        removeAstnt = (Button) view.findViewById(R.id.remove_assistant);
        removeAstnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assitantIds = "";
                String result = doValidation();
                if (result.equals("No Assistants")) {
                    Toast.makeText(getActivity(), "No Assistant In List", Toast.LENGTH_LONG).show();


                } else if (result.equals("No Selected Assistant")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Assistant Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {
                    //Toast.makeText(getActivity(),"Assistant Removed",Toast.LENGTH_LONG).show();
                    int len = removeList.size();
                    int j = len - 1;

                    for (int i = 0; i < len; i++) {
                        System.out.println("Selected ids " + removeList.get(i).getId());
                        if (i == j) {
                            assitantIds = assitantIds + removeList.get(i).getId();
                        } else {
                            System.out.println("add Item " + removeList.get(i).getId());
                            assitantIds = assitantIds + removeList.get(i).getId() + ",";
                        }
                    }

                    System.out.println("Url Parameters:::Doctor ID " + doctorId + "Patient IDs " + assitantIds);


                    api.removeDoctorAssistance(doctorId, assitantIds, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {

                            int status = response.getStatus();
                            System.out.println("URL passed " + response.getUrl());
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Assistants Are Removed", Toast.LENGTH_LONG).show();
                                showassistantsList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();

                        }
                    });
                }
            }
        });


        buttonAdd = (Button) view.findViewById(R.id.add_assistant);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = new AddAssistant();
                Bundle bun = new Bundle();
                go.setAssistantList(arrayNew);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag, "Add_Doctor");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        manageScreenIcon();
        return view;
    }

    public String doValidation() {
        int len = arrayNew.size();

        if (len == 0) {
            return "No Assistants";
        } else {
            removeList = new ArrayList<Assistant>();
            for (int i = 0; i < len; i++) {

                if ((astntSearchRes.get(i)).isSelected() == true) {
                    removeList.add(astntSearchRes.get(i));
                    System.out.println("Array Name " + astntSearchRes.get(i).getId() + " Value" + astntSearchRes.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Assistants";
            } else {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }
    }

    public void manageScreenIcon() {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Assistants");
        drawar.setVisibility(View.GONE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        BackStress.staticflag = 1;
      //  medicoLogo.setVisibility(View.GONE);
      //  medicoText.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
    }

    public void showassistantsList() {
        astntSearchRes = new ArrayList<Assistant>();
        api.getDoctorAssistant(doctorId, new Callback<ArrayList<Assistant>>() {
            @Override
            public void success(ArrayList<Assistant> array, Response response) {

                System.out.println("Kb Array " + array.size());
                arrayNew = new ArrayList<Assistant>();
                if (array.size() == 0) {
                    Assistant assistant = new Assistant();

                    assistant.setSelected(false);
                    assistant.setName("No Assistant Found");
                    assistant.setLocation("None");
                    arrayNew.add(assistant);
                    System.out.println("IF " + arrayNew.size());


                } else {
                    arrayNew = array;
                    System.out.println("Else " + arrayNew.size());
                }

                System.out.println("Arrays Value " + array.toString());

                System.out.println("Krb Url" + response.getUrl());
                astntSearchRes.addAll(arrayNew);

                listAdapterManageAssitant = new AssistntAdapter(getActivity(), arrayNew);

                listassitant.setAdapter(listAdapterManageAssitant);
                //listAdapter.notifyDataSetChanged();
                System.out.println("Adapter list Count " + listAdapterManageAssitant.getCount());

            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
}
