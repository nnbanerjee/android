package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.RequestDelegationAdapter;
import Adapter.ShowDelegationAdapter;
import com.medico.application.MyApi;
import com.medico.model.Delegation;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MNT on 16-Mar-15.
 */
public class ManageDelegationFragment extends Fragment {

    private ListView listViewManageDelegate,listViewDelegateRequest;
    private String patientId = "";
    private String typeId = "";
    private ShowDelegationAdapter adapter;
    private RequestDelegationAdapter requestAdapter;
    public MyApi api;
    private Button buttonAdd,removeDoc,drawar,logout;
    private ArrayList<Delegation> requestDelegation;
    private ArrayList<Delegation> docSearchRes,arrayNew,removeList,removeListObjects,confirmList;
    FragmentManager fragmentManger;

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
                    TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                    globalTv.setText("Medical Diary");
                    DoctorMenusManage fragment = new DoctorMenusManage();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
                    final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
                    final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
                    final LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
                    drawar = (Button)getActivity().findViewById(R.id.drawar_button);
                    logout = (Button)getActivity().findViewById(R.id.logout);
                    logout.setBackgroundResource(R.drawable.logout);
                    drawar.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    profileLayout.setVisibility(View.VISIBLE);
                    back.setVisibility(View.INVISIBLE);
                    profilePicture.setVisibility(View.VISIBLE);
                    accountName.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_delegation, container, false);
        listViewManageDelegate = (ListView)view.findViewById(R.id.list_manage_dependent);
        listViewDelegateRequest = (ListView)view.findViewById(R.id.list_manage_dependent_wcd);
        getActivity().getActionBar().hide();
        BackStress.staticflag = 1;
        final LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.GONE);
        final Global go = (Global)getActivity().getApplicationContext();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = session.getString("sessionID",null);
        typeId = session.getString("loginType",null);

        if(typeId.equals("Doctor"))
        {
            typeId = "D";
        }
        else if(typeId.equals("Assistant"))
        {
            typeId = "A";
        }
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Delegation");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        drawar.setVisibility(View.GONE);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                DoctorMenusManage fragment = new DoctorMenusManage();
                fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                drawar = (Button)getActivity().findViewById(R.id.drawar_button);
                logout = (Button)getActivity().findViewById(R.id.logout);
                logout.setBackgroundResource(R.drawable.logout);
                drawar.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                profileLayout.setVisibility(View.VISIBLE);
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showDependencyConfirmList();
        showDependencyWaitingList();

        buttonAdd = (Button) view.findViewById(R.id.add_dependent);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                go.setDelegationList(confirmList);
                Fragment frag = new AddDelegation();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag,"Add_Doctor");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        removeDoc = (Button)view.findViewById(R.id.remove_dependent);
       /* removeDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = doValidation();

                if(result.equals("No Delegates"))
                {
                    Toast.makeText(getActivity(),"No Delegate In List",Toast.LENGTH_LONG).show();

                }
                else if(result.equals("No Selected Delegates"))
                {
                    System.out.println("Result Variable Contain  "+result);
                    Toast.makeText(getActivity(),"No Delegate Selected",Toast.LENGTH_LONG).show();
                }
                else if (result.equals("Normal")) {

                    int len = removeList.size();
                    int j = len - 1;

                    ArrayList<RemoveDelegateElement> delegates = new ArrayList<RemoveDelegateElement>();

                    for (int i = 0; i < len; i++) {
                        System.out.println("Selected ids " + removeList.get(i).getId());
                        RemoveDelegateElement rem = new RemoveDelegateElement();
                        rem.setId(removeList.get(i).getId());
                        rem.setType(removeList.get(i).getType());

                        delegates.add(rem);
                    }

                    if(typeId.equals("Doctor"))
                    {
                        typeId = "D";
                    }
                    else if(typeId.equals("Assistant"))
                    {
                        typeId = "A";
                    }

                    RemoveDelegate remove = new RemoveDelegate();
                    remove.setId(patientId);
                    remove.setType(typeId);
                    remove.setDelegates(delegates);

                    api.removeDelegatesForParent(remove,new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            int status = response.getStatus();
                            System.out.println("URL passed " + response.getUrl());
                            if(status == 200)
                            {
                                Toast.makeText(getActivity(), "Delegates Are Removed", Toast.LENGTH_LONG).show();
                                showDependencyConfirmList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });


                }
            }
        });*/
        return view;
    }
    public void showDependencyConfirmList()
    {
      docSearchRes = new ArrayList<Delegation>();

        /*api.getAllDelegatesForParent(patientId, typeId, new Callback<ArrayList<Delegation>>() {
            @Override
            public void success(ArrayList<Delegation> array, Response response) {

                System.out.println("Kb Array " + array.size());
                arrayNew = new ArrayList<Delegation>();
                if (array.size() == 0) {
                    Delegation del = new Delegation();
                    del.setName("No Delegation Found");
                    del.setAccessLevel("");
                    del.setLocation("");
                    del.setMobileNumber("");
                    del.setStatus("");
                    del.setEmailID("");
                    del.setAccessLevel("");
                    del.setType("");
                    arrayNew.add(del);
                } else {
                    ArrayList<Delegation> newArray = array;
                    arrayNew = array;

                }
                confirmList = arrayNew;
                removeListObjects = arrayNew;
                adapter = new ShowDelegationAdapter(getActivity(), arrayNew);
                listViewManageDelegate.setAdapter(adapter);
                System.out.println("Adapter list Count " + adapter.getCount());

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });*/


    }

    public void showDependencyWaitingList()
    {
       docSearchRes = new ArrayList<Delegation>();

       /* api.getAllParentsForDelegates(patientId, typeId, new Callback<ArrayList<Delegation>>() {
            @Override
            public void success(ArrayList<Delegation> array, Response response) {
                int flagDelegation = 0;
                System.out.println("Kb Array " + array.size());

                arrayNew = new ArrayList<Delegation>();
                if (array.size() == 0) {
                    Delegation del = new Delegation();
                    del.setName("No Delegation Found");
                    del.setEmailID("");
                    del.setStatus("");
                    del.setMobileNumber("");
                    del.setType("");
                    del.setLocation("");
                    del.setAccessLevel("");
                    arrayNew.add(del);
                } else {
                    ArrayList<Delegation> newArray = array;


                    for (Delegation del : newArray) {
                        if ((del.getStatus()).equals("WC")) {
                            System.out.println("type::::"+del.getType());
                            flagDelegation = 1;
                            System.out.println("FlagDelegation:::::"+flagDelegation);
                            arrayNew.add(del);
                        }
                    }
                    if(flagDelegation == 0)
                    {
                        Delegation del = new Delegation();
                        del.setName("No Delegation Found");
                        del.setEmailID("");
                        del.setStatus("");
                        del.setMobileNumber("");
                        del.setType("");
                        del.setLocation("");
                        del.setAccessLevel("");
                        arrayNew.add(del);
                    }
                    System.out.println("Waited::::::::::::");

                }

                docSearchRes.addAll(arrayNew);

                requestAdapter = new RequestDelegationAdapter(getActivity(), arrayNew);
                listViewDelegateRequest.setAdapter(requestAdapter);
                System.out.println("Request Adapter::::: " + requestAdapter.getCount());

            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });*/



    }

    public String doValidation()
    {
        int len = removeListObjects.size();

        if(len == 0)
        {

            return "No Delegates";
        }
        else
        {
           /* removeList = new ArrayList<Delegation>();
            for (int i = 0; i < len; i++) {

                if ((removeListObjects.get(i)).getSelected() == true) {
                    removeList.add(removeListObjects.get(i));
                    System.out.println("Array Name " + removeListObjects.get(i).getId() + " Value" + removeListObjects.get(i).getSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Delegates";
            }
            else
            {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }*/

        }
        return null;
    }
}
