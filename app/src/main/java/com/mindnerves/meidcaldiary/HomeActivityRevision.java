package com.mindnerves.meidcaldiary;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.util.ImageLoadTask;
import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.Fragments.FileUploadDialog1;
import com.mindnerves.meidcaldiary.Fragments.ManageClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageDelegationFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageDelegationPatient;
import com.mindnerves.meidcaldiary.Fragments.ManageDendencyFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageDoctorProfile;
import com.mindnerves.meidcaldiary.Fragments.ManageMessageNotification;
import com.mindnerves.meidcaldiary.Fragments.ManagePatientFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageProcedure;
import com.mindnerves.meidcaldiary.Fragments.ManageProfilePatient;
import com.mindnerves.meidcaldiary.Fragments.ManageReminder;
import com.mindnerves.meidcaldiary.Fragments.ManageastantFragment;
import com.medico.view.PatientMenusManage;
import com.mindnerves.meidcaldiary.Fragments.ProfileSwitchDialogDoctor;
import com.mindnerves.meidcaldiary.Fragments.ProfileSwitchDialogPatient;
import com.mindnerves.meidcaldiary.Fragments.ShowSpeciality;

import java.util.ArrayList;

import Adapter.MenuAdapter;
import Application.MyApi;
import Model.PersonTemp;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class HomeActivityRevision extends FragmentActivity{
    FragmentManager fragmentManger;
    Button drawerButton,logout;
    int flagActionButton = 0;
    private ArrayList<String> arrayMenu;
    private String logString = "";
    private String loggingId = "";
    DrawerLayout dLayout;
    ListView dList;
    MenuAdapter adapter;
    Fragment fragment;
    Model.Menu mainMenu;
    Button backButton,doctorSearch;
    public MyApi api;
    ImageView profilePicture;
    TextView accountName;
    public static final String IMAGE_URL = "139.162.31.36:9000"+"/getPicture/";
    SharedPreferences session;
    String userEmail,userType;
    @Override
    public final void onBackPressed()
    {

     if(dLayout.isDrawerOpen(dList)) {
                dLayout.closeDrawer(Gravity.LEFT);

            }
            else if(BackStress.staticflag == 1){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are You Sure To Logout?");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BackStress.staticflag = 0;
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("sessionID",userEmail);
                        editor.putString("loginType", userType);
                        editor.commit();
                finish();


                    }
                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Do Nothing");
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout layout = (LinearLayout)findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        session = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggingId = session.getString("sessionID_Revision", null);
        logString = session.getString("type_Revision", null);
        userEmail = session.getString("sessionID",null);
        userType =  session.getString("loginType",null);
        session.edit().putString("sessionID", loggingId).apply();
        session.edit().putString("loginType",logString).apply();
        System.out.println("Original Id:::::"+userEmail);
        System.out.println("Original type:::::"+userType);
        System.out.println("TYpe :::"+logString);
        doctorSearch = (Button)findViewById(R.id.doctor_search);
        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logString.equalsIgnoreCase("Patient")) {
                    fragment = new ShowSpeciality();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Doctor").addToBackStack(null).commit();
                }

            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        BackStress.staticflag = 1;
        Global go = (Global)HomeActivityRevision.this.getApplicationContext();
        go.setLocation("");
       // getActionBar().hide();
        flagActionButton = 0;
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView)findViewById(R.id.account_name);
        mainMenu = new Model.Menu();
        arrayMenu = new ArrayList<String>();
        showMenus();

        for( String str  : arrayMenu){
            System.out.println("str = "+str);
        }

        if(logString.equals("Doctor")){
            fragment = new DoctorMenusManage();
            fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
            api.getProfileDoctor(loggingId,new Callback<PersonTemp>() {
                @Override
                public void success(PersonTemp person, Response response) {
                    new ImageLoadTask("http://"+IMAGE_URL+person.getId(), profilePicture).execute();
                    accountName.setText(person.getName());
                    adapter = new MenuAdapter(HomeActivityRevision.this,arrayMenu, ParentActivity.DOCTOR,""+person.getId());//(new MenuAdapter(this,arrayMenu))
                    System.out.println("Adapter Values "+adapter.getCount());
                    dList.setAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivityRevision.this,R.string.Failed,Toast.LENGTH_SHORT).show();
                }
            });

        }else if(logString.equals("Patient")){
            fragment = new PatientMenusManage();
            fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
            api.getProfilePatient(loggingId,new Callback<PersonTemp>() {
                @Override
                public void success(PersonTemp person, Response response) {
                    new ImageLoadTask("http://"+IMAGE_URL+person.getId(), profilePicture).execute();
                    accountName.setText(person.getName());
                    adapter = new MenuAdapter(HomeActivityRevision.this,arrayMenu,ParentActivity.PATIENT, person.getImageUrl());//(new MenuAdapter(this,arrayMenu))
                    System.out.println("Adapter Values "+adapter.getCount());
                    dList.setAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivityRevision.this,R.string.Failed,Toast.LENGTH_SHORT).show();
                }
            });
        }
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(logString.equals("Doctor")) {
                    ProfileSwitchDialogDoctor profileDialog = ProfileSwitchDialogDoctor.newInstance();
                    profileDialog.show(HomeActivityRevision.this.getFragmentManager(), "Dialog");
                }
                else
                {
                    ProfileSwitchDialogPatient profileDialg = ProfileSwitchDialogPatient.newInstance();
                    profileDialg.show(HomeActivityRevision.this.getFragmentManager(),"Dialog");
                }
            }
        });
        backButton = (Button)findViewById(R.id.back_button);
        backButton.setVisibility(View.INVISIBLE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        dList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        dList.setDividerHeight(1);

        drawerButton = (Button)findViewById(R.id.drawar_button);
        drawerButton.setVisibility(View.VISIBLE);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("I am here");
                System.out.println("FLag button value " + flagActionButton);
                if (flagActionButton == 0) {
                    System.out.println("open");
                    dLayout.openDrawer(dList);
                    flagActionButton = 1;
                }
                else
                {
                    System.out.println("closed");
                    dLayout.closeDrawer(Gravity.LEFT);
                    flagActionButton = 0;

                }
            }
        });
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivityRevision.this);
                alertDialogBuilder.setMessage("Are You Sure To Logout?");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = session.edit();
                        editor.putString("sessionID",userEmail);
                        editor.putString("loginType", userType);
                        editor.commit();
                        finish();

                    }
                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Do Nothing");
                    }
                });

                AlertDialog alertDialog2 = alertDialogBuilder.create();
                alertDialog2.show();
            }
        });
        dList.setSelector(android.R.color.holo_blue_dark);
        dList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> selectedItem, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                String switchCaseId = (String) adapter.getItem(position);
                switch(switchCaseId)
                {
                    case "Manage Profile":
                        if(logString.equalsIgnoreCase("Doctor"))
                        {
                            System.out.println("I am in profile condition:::::::::::::::::::");
                            fragment = new ManageDoctorProfile();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Doctor").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        else if(logString.equalsIgnoreCase("Patient"))
                        {
                            System.out.println("I am in profile condition:::::::::::::::::::");
                            fragment = new ManageProfilePatient();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Doctor").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        break;

                    case "Manage Patient":

                        fragment = new ManagePatientFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Patient").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Clinic":

                        fragment = new ManageClinicFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Clinic").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Assistant":

                        fragment = new ManageastantFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Assistant").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Dependency":

                        fragment = new ManageDendencyFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Dependency").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Reminder":
                        fragment = new ManageReminder();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Reminder").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Delegation":
                        if(logString.equalsIgnoreCase("Doctor")) {
                            fragment = new ManageDelegationFragment();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Delegation").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        else if(logString.equalsIgnoreCase("Patient"))
                        {
                            fragment = new ManageDelegationPatient()
                            ;
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Delegation").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        break;

                    case "Manage Template":
                        //fragment = new ManageTemplate();
                        fragment = new ManageProcedure();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage Template").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Messages And Notification":
                        fragment = new ManageMessageNotification();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage Msg").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                   /* case "Doctor Consultations":

                        fragment = new PatientMenusManage();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;*/

                   /* case "Patients Information":

                        fragment = new DoctorMenusManage();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Patients Information").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;*/

                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult HomeActivity /////////////////////////////////");
        FileUploadDialog1 fileupload = new FileUploadDialog1();
        fileupload.onActivityResult(requestCode,resultCode,data);


        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.replacementFragment);
        //fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.drawar_button:
                     dLayout.openDrawer(dList);
                     return true;
            default:
                 return super.onOptionsItemSelected(item);
        }


    }

    public void showMenus()
    {
        if(logString.equals("Patient"))
        {
            arrayMenu = mainMenu.getPatientMenus();
        }

        if(logString.equals("Doctor"))
        {
            arrayMenu = mainMenu.getDoctorMenus();
        }

        if(logString.equals("Assistant"))
        {
            arrayMenu = mainMenu.getDoctorMenus();
        }

    }

}
