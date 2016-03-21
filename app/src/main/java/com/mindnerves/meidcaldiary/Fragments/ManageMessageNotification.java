package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.Toast;

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Adapter.ChatAdapter;
import Application.MyApi;
import Model.Chat;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ManageMessageNotification extends Fragment {

    String doctorId = "";
    String type = "";
    MyApi api;
    ListView listViewMsg;
    ArrayList<Chat> arrayList;
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
                    goBack();
                    final Button back = (Button)getActivity().findViewById(R.id.back_button);
                    back.setVisibility(View.INVISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_msg,container,false);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Messages");
        listViewMsg = (ListView)view.findViewById(R.id.chat_list);
        getActivity().getActionBar().hide();
        BackStress.staticflag = 1;
        final LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        layout.setVisibility(View.GONE);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        doctorId = session.getString("sessionID",null);
        type = session.getString("loginType",null);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        final Button drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        drawar.setVisibility(View.GONE);
        final Button logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
                globalTv.setText("Medical Diary");
                goBack();
                back.setVisibility(View.INVISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.VISIBLE);
            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        showChatList();
        return view;
    }

    public void goBack()
    {
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        final LinearLayout layoutMenus = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        final LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.notification_layout);
        Button drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        Button logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        layoutMenus.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        if(type.equals("Patient"))
        {
            Fragment fragment = new PatientMenusManage();
            FragmentManager fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        }
        else if(type.equals("Doctor"))
        {
            DoctorMenusManage fragment = new DoctorMenusManage();
            fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        }
    }
    public void showChatList()
    {
        arrayList = new ArrayList<Chat>();
        api.getChatMember(doctorId,type,new Callback<ArrayList<Chat>>() {
            @Override
            public void success(ArrayList<Chat> chats, Response response) {
                arrayList = chats;
                ChatAdapter adapter = new ChatAdapter(getActivity(),chats);
                listViewMsg.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
