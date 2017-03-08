package com.mindnerves.meidcaldiary.Fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
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

import com.medico.view.DoctorMenusManage;
import com.medico.util.AlarmService;
import com.mindnerves.meidcaldiary.BackStress;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.Calendar;

import Adapter.ReminderAdapter;
import Application.MyApi;
import DB.DatabaseHandler;
import Model.NotificationVM;
import Model.Reminder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 17-Mar-15.
 */
public class ManageReminder extends Fragment {

    private ListView listViewManageReminder;
    private ReminderAdapter adapter;
    private String doctorId = "", reminderIds = "";
    private Button addReminder, removeReminder;
    private ArrayList<Reminder> arrayReminder, arrayNew, removeList;
    FragmentManager fragmentManger;
    LinearLayout layout;
    TextView globalTv, accountName;
    ImageView profilePicture;
    RelativeLayout profileLayout;
    Button drawar, logout, back;
    MyApi api;
    NotificationVM vm;
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

    public void goBack() {
        globalTv.setText(type);
        DoctorMenusManage fragment = new DoctorMenusManage();
        fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        back.setVisibility(View.INVISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
      //  medicoLogo.setVisibility(View.VISIBLE);
      //  medicoText.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        logout.setVisibility(View.GONE);
    }

    public void manageScreenIcon() {
        back.setVisibility(View.VISIBLE);
        globalTv.setText("Manage Reminder");
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_reminder, container, false);
        listViewManageReminder = (ListView) view.findViewById(R.id.list_manage_reminder);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        layout = (LinearLayout) getActivity().findViewById(R.id.notification_layout);
        getActivity().getActionBar().hide();
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType",null);
        doctorId = session.getString("sessionID", null);
        back = (Button) getActivity().findViewById(R.id.back_button);
        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        profileLayout = (RelativeLayout) getActivity().findViewById(R.id.home_layout2);
        drawar = (Button) getActivity().findViewById(R.id.drawar_button);
        logout = (Button) getActivity().findViewById(R.id.logout);
     //   medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
     //   medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        refresh = (Button)getActivity().findViewById(R.id.refresh);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        showReminderList();

        addReminder = (Button) view.findViewById(R.id.add_reminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new AddNewReminder();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        removeReminder = (Button) view.findViewById(R.id.remove_reminder);
        removeReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = doValidation();

                if (result.equals("No Reminder")) {
                    Toast.makeText(getActivity(), "No Reminder In List", Toast.LENGTH_LONG).show();

                } else if (result.equals("No Selected Reminder")) {
                    System.out.println("Result Variable Contain  " + result);
                    Toast.makeText(getActivity(), "No Reminder Selected", Toast.LENGTH_LONG).show();


                } else if (result.equals("Normal")) {
                    Toast.makeText(getActivity(), "Reminder Removed", Toast.LENGTH_LONG).show();

                    DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                    for (Reminder rem : removeList) {
                        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);
                        Intent intentDelete = new Intent(getActivity(), AlarmService.class);
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getActivity(), (int) Long.parseLong(rem.getAlarmId()),
                                intentDelete, PendingIntent.FLAG_ONE_SHOT);
                        am.cancel(pendingIntent1);

                        System.out.println("Delete Alarm::::::::::::::::::::::::");
                        db.deleteReminder(rem.getUniqueId());
                    }
                    for(Reminder rem : removeList){
                        rem.setSelected(null);
                        rem.setAlarmId(null);
                        rem.setUniqueId(null);
                    }
                    NotificationVM vm = new NotificationVM();
                    vm.reminders = removeList;
                    api.deleteNotification(vm,new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            if(s.equalsIgnoreCase("success")){
                                 showReminderList();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        manageScreenIcon();
        return view;
    }

    public void showReminderList() {
        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        arrayNew = db.getAllReminder(doctorId);
        arrayReminder = new ArrayList<Reminder>();

        if (arrayNew == null) {

            arrayNew = new ArrayList<Reminder>();
            Reminder rem = new Reminder();
            rem.setTitle("No Remind Time Set");
            rem.setTime("");
            rem.setDate("");
            System.out.println(":::::>I am here<:::::::");
            arrayNew.add(rem);
            System.out.println("IF " + arrayNew.size());

        } else {

            System.out.println("Else " + arrayNew.size());
        }

        adapter = new ReminderAdapter(getActivity(), arrayNew);
        listViewManageReminder.setAdapter(adapter);
        api.getNotification(doctorId,new Callback<NotificationVM>() {
            @Override
            public void success(NotificationVM notificationVM, Response response) {
                vm = notificationVM;
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String doValidation() {
        int len = arrayNew.size();

        if (len == 0) {

            return "No Reminder";
        } else {
            removeList = new ArrayList<Reminder>();
            for (int i = 0; i < len; i++) {

                if ((arrayNew.get(i)).isSelected() == true) {
                    removeList.add(arrayNew.get(i));
                    System.out.println("Array Name " + arrayNew.get(i).getUniqueId() + " Value" + arrayNew.get(i).isSelected());
                }
            }

            if (removeList.size() == 0) {
                return "No Selected Reminder";
            } else {
                System.out.println("Remove Objects ::: " + removeList.size());
                return "Normal";
            }

        }


    }

    public long D2MS(int month, int day, int year, int hour, int minute, int seconds) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, seconds);
        return c.getTimeInMillis();
    }
}
