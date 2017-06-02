package com.medicohealthcare.view.home;

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
import com.medicohealthcare.adapter.MenuAdapter;
import com.medicohealthcare.adapter.ProfileDelegationAdapter;
import com.medicohealthcare.adapter.ProfileDependencyAdapter;
import com.medicohealthcare.application.MyApi;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Delegation;
import com.medicohealthcare.model.Dependent;
import com.medicohealthcare.model.DependentDelegatePerson;
import com.medicohealthcare.model.DoctorProfile;
import com.medicohealthcare.model.PersonProfile;
import com.medicohealthcare.model.ServerResponse;
import com.medicohealthcare.service.ChatServer;
import com.medicohealthcare.util.FileUploadDialog;
import com.medicohealthcare.util.LocationService;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.util.PARAM;
import com.medicohealthcare.util.ServerConnectionAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Narendra on 18-01-2017.
 */

public abstract class HomeActivity extends Activity implements PARAM
{
    public static HomeActivity parent_activity = null;
    public int profileRole = PATIENT;
    public int profileId = 0;
    public int profileStatus = UNREGISTERED;
    public boolean isDependent = false;

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
    ServerConnectionAdapter serverConnectionAdapter;
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
        LocationService locationService = LocationService.getLocationManager(this);
//        locationService.addNotifyListeber(this);
        startChatServer();

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
        profileName = (TextView) layout.findViewById(R.id.profile_name);
        profileNamedependent = (TextView) layout.findViewById(R.id.profile_name_dependent);
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


                profileNamedependent.setVisibility(View.GONE);
                profileName.setText(parent.getPerson().getName() + " (Patient)");
                depends = parent.getDependents();
                delegates = parent.getDelegates();
            }
            else
            {
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
//                profileNamedependent = null;
                depends = personProfile.getDependents();
                delegates = personProfile.getDelegates();
            }
            else
            {
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
                if(personProfile.isDependentProfile())
                {
                    saveToSession(parent.getPerson().getId(), parent.getPerson().getRole(), parent.getPerson().getStatus(),false);
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
                if(personProfile.isDependentProfile())
                {
                    List<Dependent> dependents = parent.getDependents();
                    if(dependents != null && dependents.size() > 0)
                    {
                        for (Dependent dep : dependents)
                        {
                            if (dep.getRelation().equalsIgnoreCase("self"))
                            {
                                selfDependentId = dep.getId();
                                break;
                            }
                        }
                    }
                }
                else
                {
                    List<Dependent> dependents = personProfile.getDependents();
                    if(dependents != null && dependents.size() > 0)
                    {
                        for (Dependent dep : dependents)
                        {
                            if (dep.getRelation().equalsIgnoreCase("self"))
                            {
                                selfDependentId = dep.getId();
                                break;
                            }
                        }
                    }

                }
                if(selfDependentId == -1)
                {
                    DependentDelegatePerson dependent = new DependentDelegatePerson(personProfile.getPerson()) ;
                    create(dependent);
                }
                else
                    launchDependentProfile(selfDependentId);

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
                             saveToSession(del.getId(), PATIENT, del.getStatus(),true)) {
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
                        saveToSession(del.getId(), PATIENT, del.getStatus(),false))
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

    public boolean saveToSession(int id, int type, int status, boolean isDependent )
    {
        int loggedInUserId = session.getInt(LOGGED_IN_ID, -1);
             if (loggedInUserId == id)
            {
                session.edit().putBoolean(IS_PROFILE_OF_LOGGED_IN_USER, true).apply();
                session.edit().remove(DEPENDENT_ID).apply();
                session.edit().remove(DEPENDENT_ROLE).apply();
                session.edit().remove(DEPENDENT_STATUS).apply();
                session.edit().remove(PARENT).apply();
                session.edit().remove(IS_DEPENDENT).apply();
            }
             else if(session.getBoolean(IS_PROFILE_OF_LOGGED_IN_USER,false))
             {
                 session.edit().putBoolean(IS_PROFILE_OF_LOGGED_IN_USER, false).apply();
                 session.edit().putInt(DEPENDENT_ID, id).apply();
                 session.edit().putInt(DEPENDENT_ROLE, type).apply();
                 session.edit().putInt(DEPENDENT_STATUS, status).apply();
                session.edit().putBoolean(IS_DEPENDENT, isDependent).apply();
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
                 session.edit().putBoolean(IS_DEPENDENT, isDependent).apply();
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

    public abstract void showMenus();

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
            isDependent = session.getBoolean(IS_DEPENDENT, false);
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
        serverConnectionAdapter = ServerConnectionAdapter.getServerAdapter(this);
          api = serverConnectionAdapter.getServerAPI();
    }


    public static HomeActivity getParentAtivity()
    {
        return parent_activity;
    }


    public void startChatServer()
    {

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable()
                {
                    public void run()
                    {
                        Intent intent = new Intent(parent_activity, ChatServer.class);
                        intent.putExtra(PROFILE_ID,profileId);
                        HomeActivity.getParentAtivity().startService(intent);
                    }
                }, 0, 10, TimeUnit.SECONDS);
    }

    private void create(final DependentDelegatePerson person)
    {
         api.createDependentProfile(person, new Callback<ServerResponse>()
         {
            @Override
            public void success(ServerResponse s, Response response)
            {
                if (s.status == 1)
                {
                    int selfDependentId = s.profileId;
                    if(personProfile.isDependentProfile())
                    {
                        List<Dependent> dependents = parent.getDependents();
                        if(dependents == null )
                            parent.setDependents(new ArrayList<Dependent>());
                        Dependent dependent1 = new Dependent(selfDependentId,person.getName(),person.relation,person.getStatus());
                        parent.getDependents().add(dependent1);
                    }
                    else
                    {
                        List<Dependent> dependents = personProfile.getDependents();
                        if(dependents == null )
                            personProfile.setDependents(new ArrayList<Dependent>());
                        Dependent dependent1 = new Dependent(selfDependentId,person.getName(),person.relation,person.getStatus());
                        personProfile.getDependents().add(dependent1);
                    }
                    launchDependentProfile(selfDependentId);
                }
            }
            @Override
            public void failure(RetrofitError error)
            {

                new MedicoCustomErrorHandler(HomeActivity.this).handleError(error);
             }
        });
     }

     public void launchDependentProfile(int selfDependentId)
     {
         saveToSession(selfDependentId, PATIENT, UNREGISTERED,true);
         Intent intObj = new Intent(HomeActivity.this, PatientHome.class);
         startActivity(intObj);
         if(personProfile.isDependentProfile())
         {
             finish();
         }
         else
         {
             onPause();
         }
     }
}