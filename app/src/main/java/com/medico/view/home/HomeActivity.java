package com.medico.view.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.medico.adapter.MenuAdapter;
import com.medico.adapter.ProfileDelegationAdapter;
import com.medico.adapter.ProfileDependencyAdapter;
import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.Delegation;
import com.medico.model.Dependent;
import com.medico.model.DoctorProfile;
import com.medico.model.PersonProfile;
import com.medico.util.FileUploadDialog;
import com.medico.util.PARAM;
import com.medico.util.ServerConnectionAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Narendra on 18-01-2017.
 */

public abstract class HomeActivity extends Activity implements PARAM
{
    public static HomeActivity parent_activity = null;
    public int profileRole = PATIENT;
    public int profileId = 0;
    public int profileStatus = UNREGISTERED;

    protected SharedPreferences session = null;
    public PersonProfile parent = null;

    FragmentManager fragmentManger;
    Button drawerButton, logout;
    int flagActionButton = 0;
    ArrayList<String> arrayMenu;
    public DrawerLayout dLayout;
    public ListView dList;
    MenuAdapter adapter;
    Fragment fragment;
    Menu mainMenu;
    TextView globalTv;
    Button notes, messages, doctorSearch, clinicSearch, patients;
    public MyApi api;
    ImageView profilePicture;
    TextView accountName, status;
    ProfileDependencyAdapter dependentAdapter;
    ProfileDelegationAdapter delegationAdapter;
    ListView dependentList, delegationList;
    TextView profileName;
    TextView profileNamedependent;

    Point p;
    View arrow;
    public PersonProfile personProfile;
    Toolbar toolbar;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                                                .build();
        ImageLoader.getInstance().init(config);

        setParameters();
        createView();

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.GONE);

        mainMenu = new Menu();
        arrayMenu = new ArrayList<String>();
        showMenus();

//        locationService.addNotifyListeber(this);

    }

    // The method that displays the popup.
    protected void showPopup(final Activity context, Point p) {
        int popupWidth = 350;
        int popupHeight = 500;

        // Inflate the popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        dependentList = (ListView) layout.findViewById(R.id.profile_dependent_list);
        delegationList = (ListView) layout.findViewById(R.id.profile_delegation_list);


        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 15;
        int OFFSET_Y = 55;

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
        List<Dependent> depends = null;
        List<Delegation> delegates = null;
        boolean selfDependentProfile = false;
        if(parent != null) {
            if(parent.getPerson().getRole() == PATIENT)
            {
                profileName = (TextView) layout.findViewById(R.id.profile_name);
                profileNamedependent = (TextView) layout.findViewById(R.id.profile_name_dependent);
                profileNamedependent.setVisibility(View.GONE);
                profileName.setText(parent.getPerson().getName() + " (Patient)");
                depends = parent.getDependents();
                delegates = parent.getDelegates();
            }
            else
            {
                profileName = (TextView) layout.findViewById(R.id.profile_name);
                profileNamedependent = (TextView) layout.findViewById(R.id.profile_name_dependent);
                profileName.setText(parent.getPerson().getName() + " (Doctor)");
                profileNamedependent.setText(parent.getPerson().getName() + " (Patient)");
                depends = parent.getDependents();
                delegates = parent.getDelegates();
                selfDependentProfile = true;
            }
        }
        else
        {
            if(personProfile.getPerson().getRole() == PATIENT)
            {

                profileName.setText(personProfile.getPerson().getName() + " (Patient)");
                profileNamedependent.setVisibility(View.GONE);
                profileNamedependent = null;
                depends = personProfile.getDependents();
                delegates = personProfile.getDelegates();
            }
            else
            {
                profileName = (TextView) layout.findViewById(R.id.profile_name);
                profileNamedependent = (TextView) layout.findViewById(R.id.profile_name_dependent);
                profileName.setText(personProfile.getPerson().getName() + " (Doctor)");
                profileNamedependent.setText(personProfile.getPerson().getName() + " (Patient)");
                depends = personProfile.getDependents();
                delegates = personProfile.getDelegates();
                selfDependentProfile = true;
            } 
        }
        if (depends == null || depends.size() == 0)
        {
            depends = new ArrayList<Dependent>();
            Dependent patient = new Dependent();
            patient.setName("No Dependent Found");
            depends.add(patient);
        }

        dependentAdapter = new ProfileDependencyAdapter(HomeActivity.this, depends);
        dependentList.setAdapter(dependentAdapter);

        profileName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                popup.dismiss();
                if(personProfile.getDependentProfile())
                {
                    saveToSession(parent.getPerson().getId(), parent.getPerson().getRole(), parent.getPerson().getStatus());
                    finish();
                }

            }
        });
        profileNamedependent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popup.dismiss();
                int selfDependentId = -1;
                if(personProfile.getDependentProfile())
                {
                    List<Dependent> dependents = parent.getDependents();
                    for(Dependent dep: dependents)
                    {
                        if(dep.getRelation().equalsIgnoreCase("self")) {
                            selfDependentId = dep.getId();
                            break;
                        }
                    }
                }
                else
                {
                    List<Dependent> dependents = personProfile.getDependents();
                    for(Dependent dep: dependents)
                    {
                        if(dep.getRelation().equalsIgnoreCase("self")) {
                            selfDependentId = dep.getId();
                            break;
                        }
                    }

                }
                if(selfDependentId == -1)
                {
                    selfDependentId = 102;
                }
                saveToSession(selfDependentId, PATIENT, UNREGISTERED);
                Intent intObj = new Intent(HomeActivity.this, PatientHome.class);
                startActivity(intObj);
                if(personProfile.getDependentProfile())
                {
                    finish();
                }
                else
                {
                    onPause();
                }

            }
        });
        if (delegates == null || delegates.size() == 0)
        {
            delegates = new ArrayList<Delegation>();
            Delegation patient = new Delegation();
            patient.setName("No Dependent Found");
            delegates.add(patient);
        }
        delegationAdapter = new ProfileDelegationAdapter(HomeActivity.this, delegates);
        delegationList.setAdapter(delegationAdapter);

        dependentList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     popup.dismiss();
                     final Dependent del = (Dependent) dependentAdapter.getItem(position);

                     if(!(del.getName().equalsIgnoreCase("No Delegation Found")) &&
                             saveToSession(del.getId(), PATIENT, del.getStatus())) {
                         Intent intObj = new Intent(HomeActivity.this, PatientHome.class);
                         startActivity(intObj);
                         onPause();
                     }
                 }
             }
        );
        delegationList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popup.dismiss();
                final Delegation del = (Delegation) delegationAdapter.getItem(position);
                if (!(del.getName().equalsIgnoreCase("No Delegation Found")) &&
                        saveToSession(del.getId(), PATIENT, del.getStatus()))
                {
                    Intent intObj = new Intent(HomeActivity.this, PatientHome.class);
                    startActivity(intObj);
                    onPause();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume of Main Called::::::::::::::::");

        //  getActionBar().hide();
    }

    // Get the x and y position after the button is draw on screen
// (It's important to note that we can't get the position in the onCreate(),
// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        TextView account_name = (TextView) findViewById(R.id.account_name);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        account_name.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    public boolean saveToSession(int id, int type, int status )
    {
        int loggedInUserId = session.getInt(LOGGED_IN_ID, -1);
             if (loggedInUserId == id)
            {
                session.edit().putBoolean(IS_PROFILE_OF_LOGGED_IN_USER, true).apply();
                session.edit().remove(DEPENDENT_ID).apply();
                session.edit().remove(DEPENDENT_ROLE).apply();
                session.edit().remove(DEPENDENT_STATUS).apply();
                session.edit().remove(PARENT).apply();
            }
             else if(session.getBoolean(IS_PROFILE_OF_LOGGED_IN_USER,false))
             {
                 session.edit().putBoolean(IS_PROFILE_OF_LOGGED_IN_USER, false).apply();
                 session.edit().putInt(DEPENDENT_ID, id).apply();
                 session.edit().putInt(DEPENDENT_ROLE, type).apply();
                 session.edit().putInt(DEPENDENT_STATUS, status).apply();

                 Gson gson = new Gson();
                 String json = gson.toJson(personProfile);
                 session.edit().putString(PARENT, json).apply();
             }
             else
             {
                session.edit().putBoolean(IS_PROFILE_OF_LOGGED_IN_USER, false).apply();
                session.edit().putInt(DEPENDENT_ID, id).apply();
                session.edit().putInt(DEPENDENT_ROLE, type).apply();
                session.edit().putInt(DEPENDENT_STATUS, status).apply();
                Gson gson = new Gson();
                String json = gson.toJson(parent);
                session.edit().putString(PARENT, json).apply();
            }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult HomeActivity /////////////////////////////////");
        FileUploadDialog fileupload = new FileUploadDialog();
        fileupload.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        // super.onCreateOptionsMenu(menu);
        if (!dLayout.isDrawerOpen(dList)) {
            // dLayout.closeDrawer(Gravity.LEFT);
            getMenuInflater().inflate(R.menu.menu, menu);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.drawar_button:
                dLayout.openDrawer(dList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMenus()
    {

        arrayMenu = mainMenu.getDoctorMenus();

    }

    protected abstract void createView();

    protected void setParameters()
    {
        session = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(session.getBoolean(IS_PROFILE_OF_LOGGED_IN_USER, false))
        {
            profileId = session.getInt(LOGGED_IN_ID, PATIENT);
            profileRole = session.getInt(LOGGED_IN_USER_ROLE, PATIENT);
            profileStatus = session.getInt(LOGGED_IN_USER_STATUS, UNREGISTERED);
        }
        else
        {
            profileId = session.getInt(DEPENDENT_ID, PATIENT);
            profileRole = session.getInt(DEPENDENT_ROLE, PATIENT);
            profileStatus = session.getInt(DEPENDENT_STATUS, UNREGISTERED);
            String parent_string = session.getString(PARENT, null);
            Gson gson = new Gson();
            parent = gson.fromJson(parent_string, DoctorProfile.class);
            System.out.println("I am here");
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

          api = ServerConnectionAdapter.getServerAdapter(this).getServerAPI();
    }


//    protected void manageProfile()
//    {
//        System.out.println("i am here::::::::::::");
//        Bundle bundle = new Bundle();
//        setSettingParameters(bundle);
//        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.MANAGE_PROFILE_VIEW);
//        Intent intObj = new Intent(this, ManagePersonSettings.class);
//        intObj.putExtras(bundle);
//        startActivity(intObj);
//        onPause();
//        dLayout.closeDrawer(dList);
//    }
//    protected void manageDependents()
//    {
//        Bundle bundle = new Bundle();
//        setSettingParameters(bundle);
//        bundle.putInt(PARAM.SETTING_VIEW_ID, PARAM.DEPENDENT_SETTING_VIEW);
//        Intent intObj = new Intent(this, ManagePersonSettings.class);
//        intObj.putExtras(bundle);
//        startActivity(intObj);
//        onPause();
//        dLayout.closeDrawer(dList);
//    }
//
//    protected void termsAndConditions()
//    {
////        fragment = new ManageMessageNotification();
////        fragmentManger = getFragmentManager();
////        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
////        dList.setSelection(position);
////        dLayout.closeDrawer(dList);
//    }
//    protected void logout()
//    {
////        dList.setSelection(position);
//        dLayout.closeDrawer(dList);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
//        alertDialogBuilder.setMessage(R.string.confirm_logout);
//        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                BackStress.staticflag = 0;
//                SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putBoolean("USER_STATUS",false).commit();
//                finish();
//
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                System.out.println("Do Nothing");
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    public static HomeActivity getParentAtivity()
    {
        return parent_activity;
    }
//    protected void setSettingParameters(Bundle bundle)
//    {
//        if(parent != null)
//        {
//            bundle.putInt(PARAM.LOGGED_IN_ID, parent.getPerson().getId());
//            bundle.putInt(PARAM.LOGGED_IN_USER_ROLE, parent.getPerson().getRole());
//            bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, parent.getPerson().getStatus());
//        }
//        else
//        {
//            bundle.putInt(PARAM.LOGGED_IN_ID, profileId);
//            bundle.putInt(PARAM.LOGGED_IN_USER_ROLE,profileRole);
//            bundle.putInt(PARAM.LOGGED_IN_USER_STATUS, profileStatus);
//        }
//        bundle.putInt(PARAM.PROFILE_ID, profileId);
//        bundle.putInt(PARAM.PROFILE_ROLE, profileRole);
//        bundle.putInt(PARAM.PROFILE_STATUS,profileStatus);
//
//    }
}