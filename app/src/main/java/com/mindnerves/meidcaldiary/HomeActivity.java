package com.mindnerves.meidcaldiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindnerves.meidcaldiary.Fragments.DoctorMenusManage;
import com.mindnerves.meidcaldiary.Fragments.DoctorProfileEdit;
import com.mindnerves.meidcaldiary.Fragments.FileUploadDialog;
import com.mindnerves.meidcaldiary.Fragments.ManageClinicFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageDelegationFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageDelegationPatient;
import com.mindnerves.meidcaldiary.Fragments.ManageDendencyDoctor;
import com.mindnerves.meidcaldiary.Fragments.ManageDendencyFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageMessageNotification;
import com.mindnerves.meidcaldiary.Fragments.ManagePatientFragment;
import com.mindnerves.meidcaldiary.Fragments.ManageProcedure;
import com.mindnerves.meidcaldiary.Fragments.ManageProfilePatient;
import com.mindnerves.meidcaldiary.Fragments.ManageReminder;
import com.mindnerves.meidcaldiary.Fragments.ManageReminderPatient;
import com.mindnerves.meidcaldiary.Fragments.ManageastantFragment;
import com.mindnerves.meidcaldiary.Fragments.PatientMenusManage;
import com.mindnerves.meidcaldiary.Fragments.ShowClinicSpecialities;
import com.mindnerves.meidcaldiary.Fragments.ShowPatients;
import com.mindnerves.meidcaldiary.Fragments.ShowSpeciality;
import com.mindnerves.meidcaldiary.Fragments.ShowSpecialityClinics;
import com.mindnerves.meidcaldiary.Fragments.ShowSpecialityDoctor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import Adapter.MenuAdapter;
import Adapter.ProfileDelegationAdapter;
import Adapter.ProfileDependencyAdapter;
import Application.MyApi;
import Model.Delegation;
import Model.Dependent;
import Model.DoctorId;
import Model.DoctorProfile;
import Model.Patient;
import Model.PatientId;
import Model.Person;
import Utils.UtilSingleInstance;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import android.webkit.CookieManager;

public class HomeActivity extends Activity {
    FragmentManager fragmentManger;
    Button drawerButton, logout, notification, messages;
    int flagActionButton = 0;
    private ArrayList<String> arrayMenu;
    private String logString = "";
    private String loggingId = "";
    DrawerLayout dLayout;
    ListView dList;
    MenuAdapter adapter;
    Fragment fragment;
    Model.Menu mainMenu;
    TextView globalTv;
    Button backButton, doctorSearch, clinicSearch, patients;
    public MyApi api;
    ImageView profilePicture;
    TextView accountName, status;
    ProfileDependencyAdapter dependentAdapter;
    SharedPreferences session;
    ProfileDelegationAdapter delegationAdapter;
    ListView dependentList, delegationList;
    TextView profileName;
    TextView profileNamedependent;

    String statusString = "";
    Point p;
    View arrow;
    public DoctorProfile doc;
    Toolbar toolbar;
    ProgressDialog progress;

    @Override
    public final void onBackPressed() {
        System.out.println("back button= " + BackStress.staticflag);
        Log.i("Homeactivity", "Homeactivity->onbackpressed");
        if (dLayout.isDrawerOpen(dList)) {
            dLayout.closeDrawer(Gravity.LEFT);

        } else if (BackStress.staticflag == 1) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.confirm_logout);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BackStress.staticflag = 0;
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
        if (logString.equals("Doctor")) {
            api.getProfileDoctor(loggingId, new Callback<Person>() {
                @Override
                public void success(Person person, Response response) {
                    new ImageLoadTask(getResources().getString(R.string.image_base_url), profilePicture).execute();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (logString.equals("Patient")) {
            api.getProfilePatient(loggingId, new Callback<Person>() {
                @Override
                public void success(Person person, Response response) {
                    new ImageLoadTask(getResources().getString(R.string.image_base_url), profilePicture).execute();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout layout = (LinearLayout) findViewById(R.id.notification_layout);
        layout.setVisibility(View.VISIBLE);
        session = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggingId = session.getString("id", null);
        logString = session.getString("loginType", null);
        statusString = session.getString("status", null);
        notification = (Button) findViewById(R.id.notification_ping);
        messages = (Button) findViewById(R.id.msg_notification);
        System.out.println("TYpe :::" + logString);
        doctorSearch = (Button) findViewById(R.id.doctor_search);
        status = (TextView) findViewById(R.id.status_text);
        status.setText(logString+" Profile");
        patients = (Button) findViewById(R.id.patient_ping);
        clinicSearch = (Button) findViewById(R.id.clinic_notification);
        globalTv = (TextView) findViewById(R.id.show_global_tv);
        arrow = (View) findViewById(R.id.down_arrow);


        doctorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logString.equalsIgnoreCase("Patient")) {
                    fragment = new ShowSpeciality();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                } else {
                    fragment = new ShowSpecialityDoctor();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                }

            }
        });
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        // assuming `cookieKey` and `cookieValue` are not null
                        request.addHeader("Cookie", "PLAY_SESSION" + "=" + CookieManager.getInstance().getCookie("PLAY_SESSION"));
                    }
                })
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        BackStress.staticflag = 1;
        Global go = (Global) HomeActivity.this.getApplicationContext();
        go.setLocation("");
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);
       /* ActionBar actionBar = getActionBar();

        actionBar.show();

        actionBar.setTitle("Home");
*/


        flagActionButton = 0;
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        accountName = (TextView) findViewById(R.id.account_name);
        mainMenu = new Model.Menu();
        arrayMenu = new ArrayList<String>();
        showMenus();

        for (String str : arrayMenu) {
            System.out.println("str = " + str);
        }

        if (logString.equals("Doctor")) {
            progress = ProgressDialog.show(this, "", getResources().getString(R.string.loading_wait));
            globalTv.setText("Doctor");
            fragment = new DoctorMenusManage();
            fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
            DoctorId param = new DoctorId(loggingId);

            api.getDoctorLandingPageDetails(param, new Callback<DoctorProfile>() {
                @Override
                public void success(DoctorProfile doc1, Response response)
                {
                    doc = doc1;
                    if (doc != null && doc.getPerson() != null && doc.getPerson().getImageUrl() != null)
                        new ImageLoadTask(doc.getPerson().getImageUrl(), profilePicture).execute();
                    accountName.setText(doc.getPerson().getName());
                    adapter = new MenuAdapter(HomeActivity.this, arrayMenu, logString, doc.getPerson().getImageUrl());//(new MenuAdapter(this,arrayMenu))
                    System.out.println("Adapter Values " + adapter.getCount());
                    dList.setAdapter(adapter);
                    ((DoctorMenusManage) fragment).updateCounts(doc);
//                    Bitmap bmp = BitmapFactory.decodeFile(doc.getPerson().getImageUrl());
//                    profilePicture.setImageBitmap(bmp);
//                    profilePicture.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = session.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(doc);
                    editor.putString("DoctorProfileLoggedInCounts", json);
                    editor.commit();
                    progress.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                     progress.dismiss();
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });

        } else if (logString.equals("Patient")) {
            globalTv.setText("Patient");
            patients.setVisibility(View.GONE);
            managePatientIcons();
            fragment = new PatientMenusManage();
            fragmentManger = getFragmentManager();
            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Patients Information").addToBackStack(null).commit();
            api.getPatientLandingPageDetails(new PatientId(loggingId), new Callback<DoctorProfile>() {
                @Override
                public void success(DoctorProfile person, Response response) {
                    if (person != null && person.getPerson() != null && person.getPerson().getImageUrl() != null)
                        new ImageLoadTask(getResources().getString(R.string.image_base_url) + person.getPerson().getImageUrl(), profilePicture).execute();
                    System.out.println("profile id:::::::" + getResources().getString(R.string.image_base_url) + person.getPerson().getId());
                    SharedPreferences session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = session.edit();
                    editor.putString("patientId", person.getPerson().getPatientId());
                    editor.commit();
                    accountName.setText(person.getPerson().getName());
                    adapter = new MenuAdapter(HomeActivity.this, arrayMenu, logString, person.getPerson().getImageUrl());//(new MenuAdapter(this,arrayMenu))
                    System.out.println("Adapter Values " + adapter.getCount());
                    dList.setAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });

        }

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(HomeActivity.this, p);
            }
        });
        accountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p != null)
                    showPopup(HomeActivity.this, p);
            }
        });
        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logString.equals("Doctor")) {
                    fragment = new ShowPatients();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logString.equals("Doctor")) {
                    fragment = new ManageReminder();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                } else {
                    fragment = new ManageReminderPatient();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                }

            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logString.equals("Doctor")) {
                    fragment = new ManageMessageNotification();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
                } else if (logString.equals("Patient")) {
                    fragment = new ManageMessageNotification();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
                }
            }
        });
        backButton = (Button) findViewById(R.id.back_button);
        backButton.setVisibility(View.INVISIBLE);
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        dList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        dList.setDividerHeight(1);

        clinicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logString.equals("Patient")) {
                    fragment = new ShowSpecialityClinics();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                } else {
                    fragment = new ShowClinicSpecialities();
                    fragmentManger = getFragmentManager();
                    fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                }
            }
        });
        drawerButton = (Button) findViewById(R.id.drawar_button);
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
                } else {
                    System.out.println("closed");
                    dLayout.closeDrawer(Gravity.LEFT);
                    flagActionButton = 0;

                }
            }
        });
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setMessage("R.string.confirm_logout");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        dList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> selectedItem, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                String switchCaseId = (String) adapter.getItem(position);
                switch (switchCaseId) {
                    case "Manage Profile":
                        if (logString.equalsIgnoreCase("Doctor")) {
                            System.out.println("I am in profile condition:::::::::::::::::::");
                            fragment = new DoctorProfileEdit();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Doctor").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        } else if (logString.equalsIgnoreCase("Patient")) {
                            System.out.println("I am in profile condition:::::::::::::::::::");
                            fragment = new ManageProfilePatient();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Doctor").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        break;

                    case "Manage Patient":

                        fragment = new ManagePatientFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Patient").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Clinic":

                        fragment = new ManageClinicFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Clinic").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Assistant":

                        fragment = new ManageastantFragment();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Assistant").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Dependency":
                        if (logString.equalsIgnoreCase("Doctor")) {
                            fragment = new ManageDendencyDoctor();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Dependency").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        } else if (logString.equalsIgnoreCase("Patient")) {
                            fragment = new ManageDendencyFragment();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Dependency").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        }
                        break;

                    case "Manage Reminder":
                        fragment = new ManageReminder();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage_Reminder").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Manage Delegation":
                        if (logString.equalsIgnoreCase("Doctor")) {
                            fragment = new ManageDelegationFragment();
                            fragmentManger = getFragmentManager();
                            fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Delegation").addToBackStack(null).commit();
                            dList.setSelection(position);
                            dLayout.closeDrawer(dList);
                        } else if (logString.equalsIgnoreCase("Patient")) {
                            fragment = new ManageDelegationPatient();
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
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Template").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Messages And Notification":
                        fragment = new ManageMessageNotification();
                        fragmentManger = getFragmentManager();
                        fragmentManger.beginTransaction().replace(R.id.content_frame, fragment, "Manage Msg").addToBackStack(null).commit();
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        break;

                    case "Logout":
                        dList.setSelection(position);
                        dLayout.closeDrawer(dList);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                        alertDialogBuilder.setMessage(R.string.confirm_logout);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackStress.staticflag = 0;
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
                        break;
                }
            }
        });
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {
        int popupWidth = 350;
        int popupHeight = 500;

        // Inflate the popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        dependentList = (ListView) layout.findViewById(R.id.profile_dependent_list);
        delegationList = (ListView) layout.findViewById(R.id.profile_delegation_list);
        profileName = (TextView) layout.findViewById(R.id.profile_name);
        profileNamedependent = (TextView) layout.findViewById(R.id.profile_name_dependent);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 15;
        int OFFSET_Y = 55;

        // Clear the default translucent background
//        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        if (logString.equals("Doctor"))
        {


            profileName.setText(doc.getPerson().getName()+ " (Doctor)");
            profileNamedependent.setText(doc.getPerson().getName() + " (Patient)");


            List<Dependent> depends = doc.getDependents();
            if (depends == null || depends.size() == 0)
            {
                depends = new ArrayList<Dependent>();
                Dependent patient = new Dependent();
                patient.setName("No Dependent Found");
                depends.add(patient);
            }

            dependentAdapter = new ProfileDependencyAdapter(HomeActivity.this, depends);
            dependentList.setAdapter(dependentAdapter);

            List<Delegation> delegates = doc.getDelegates();
            if (delegates == null || delegates.size() == 0)
            {
                delegates = new ArrayList<Delegation>();
                Delegation patient = new Delegation();
                patient.setName("No Dependent Found");
                delegates.add(patient);
            }
            delegationAdapter = new ProfileDelegationAdapter(HomeActivity.this, delegates);
            delegationList.setAdapter(delegationAdapter);



           /* RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(this.getResources().getString(R.string.base_url))
                    .setClient(new OkClient())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            api = restAdapter.create(MyApi.class);
            api.getProfileDoctor(loggingId, new Callback<Person>() {
                @Override
                public void success(Person person, Response response) {
                    profileName.setText(person.getName());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    //Toast.makeText(getActivity(),R.string.Failed,Toast.LENGTH_SHORT).show();
                }
            });*/
           /* api.getAllDoctorDependents(loggingId, new Callback<ArrayList<Patient>>() {
                @Override
                public void success(ArrayList<Patient> arrays, Response response) {
                    List<Patient> patients = new ArrayList<Patient>();

                    if (arrays.size() == 0) {
                        Patient patient = new Patient();
                        patient.setSelected(false);
                        patient.setName("No Dependent Found");
                        patient.setLocation(" ");
                        patients.add(patient);
                    } else {
                        for (Patient pat : arrays) {
                            if ((pat.getStatus()).equals("C")) {
                                patients.add(pat);
                            }

                        }
                    }
                    dependentAdapter = new ProfileDependencyAdapter(HomeActivity.this, patients);
                    dependentList.setAdapter(dependentAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });*/

//            delegationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    final Delegation del = (Delegation) delegationAdapter.getItem(position);
//                    System.out.println("doctor email::::::" + del.getEmailID());
//                    if (!(del.getName().equalsIgnoreCase("No Delegation Found"))) {
//                        String emailId = del.getEmailID();
//                        String type = del.getType();
//                        if (type.equalsIgnoreCase("D")) {
//                            type = "Doctor";
//                        } else if (type.equalsIgnoreCase("P")) {
//                            type = "Patient";
//                        } else if (type.equalsIgnoreCase("A")) {
//                            type = "Assistant";
//                        }
//                        saveToSession(emailId, type);
//                        Intent intObj = new Intent(HomeActivity.this, HomeActivityRevision.class);
//                        startActivity(intObj);
//                    }
//                }
//            });
//
//            api.getAllDelegatesForParent(loggingId, "D", new Callback<ArrayList<Delegation>>() {
//                @Override
//                public void success(ArrayList<Delegation> delegations, Response response) {
//                    System.out.println("Kb Array " + delegations.size());
//                    ArrayList<Delegation> delegatesAdapter = new ArrayList<Delegation>();
//                    if (delegations.size() == 0) {
//                        Delegation del = new Delegation();
//                        del.setName("No Delegation Found");
//                        del.setAccessLevel("");
//                        del.setLocation("");
//                        del.setMobileNumber("");
//                        del.setStatus("");
//                        del.setEmailID("");
//                        del.setAccessLevel("");
//                        del.setType("");
//                        delegatesAdapter.add(del);
//                    } else {
//                        for (Delegation dele : delegations) {
//                            if (dele.getStatus().equalsIgnoreCase("C")) {
//                                delegatesAdapter.add(dele);
//                            }
//                        }
//                    }
//
//                    delegationAdapter = new ProfileDelegationAdapter(HomeActivity.this, delegatesAdapter);
//                    delegationList.setAdapter(delegationAdapter);
//                    System.out.println("Adapter list Count " + delegationAdapter.getCount());
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    error.printStackTrace();
//                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
//                }
//            });

            //ProfileSwitchDialogDoctor profileDialog = ProfileSwitchDialogDoctor.newInstance();
            //profileDialog.show(HomeActivity.this.getFragmentManager(), "Dialog");
        } else if (logString.equals("Patient")) {

            api.getProfilePatient(loggingId, new Callback<Person>() {
                @Override
                public void success(Person person, Response response) {
                    profileName.setText(person.getName());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
            api.getAllPatientDependents(loggingId, new Callback<ArrayList<Patient>>() {
                @Override
                public void success(ArrayList<Patient> patients, Response response) {
                    int flagDependency = 0;
                    ArrayList<Patient> arrayNew = new ArrayList<Patient>();
                    if (patients.size() == 0) {
                        Patient docSr = new Patient();
                        docSr.setSelected(false);
                        docSr.setName("No Dependent Found");
                        docSr.setLocation(" ");
                        docSr.setAccessLevel("");
                        docSr.setStatus("");
                        arrayNew.add(docSr);
                    } else {
                        for (Patient pat : patients) {
                            if ((pat.getStatus()).equals("C")) {
                                arrayNew.add(pat);
                                flagDependency = 1;
                            }

                        }

                        if (flagDependency == 0) {
                            Patient docSr = new Patient();
                            docSr.setSelected(false);
                            docSr.setName("No Dependent Found");
                            docSr.setLocation(" ");
                            docSr.setAccessLevel("");
                            docSr.setStatus("");
                            arrayNew.add(docSr);
                        }
                    }
                    //dependentAdapter = new ProfileDependencyAdapter(HomeActivity.this, arrayNew);
                    // dependentList.setAdapter(dependentAdapter);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
            dependentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Patient pat = (Patient) dependentAdapter.getItem(position);
                    System.out.println("Patient Name::::::" + pat.getName());
                    if (!(pat.getName().equalsIgnoreCase("No Dependent Found"))) {
                        String emailId = pat.getEmailID();
                        saveToSession(emailId, "Patient");
                        Intent intObj = new Intent(HomeActivity.this, HomeActivityRevision.class);
                        startActivity(intObj);
                    }

                }
            });
            api.getAllDelegatesForParent(loggingId, "P", new Callback<ArrayList<Delegation>>() {
                @Override
                public void success(ArrayList<Delegation> delegations, Response response) {
                    System.out.println("Kb Array " + delegations.size());
                    int flagDelegation = 0;
                    ArrayList<Delegation> delegatesAdapter = new ArrayList<Delegation>();
                    if (delegations.size() == 0) {
                        Delegation del = new Delegation();
                        del.setName("No Delegation Found");
                        del.setAccessLevel("");
                        del.setLocation("");
                        del.setMobileNumber("");
                        del.setStatus("");
                        del.setEmailID("");
                        del.setAccessLevel("");
                        del.setType("");
                        delegatesAdapter.add(del);
                    } else {
                        for (Delegation dele : delegations) {
                            if (dele.getStatus().equalsIgnoreCase("C")) {
                                flagDelegation = 1;
                                delegatesAdapter.add(dele);
                            }
                        }

                        if (flagDelegation == 0) {
                            Delegation del = new Delegation();
                            del.setName("No Delegation Found");
                            del.setEmailID("");
                            del.setStatus("");
                            del.setMobileNumber("");
                            del.setType("");
                            del.setLocation("");
                            del.setAccessLevel("");
                            delegatesAdapter.add(del);
                        }
                    }

                    // delegationAdapter = new ProfileDelegationAdapter(HomeActivity.this, delegatesAdapter);
                    // delegationList.setAdapter(delegationAdapter);
                    // System.out.println("Adapter list Count " + delegationAdapter.getCount());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
            delegationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Delegation del = (Delegation) delegationAdapter.getItem(position);
                    System.out.println("doctor email::::::" + del.getEmailID());
                    if (!(del.getName().equalsIgnoreCase("No Delegation Found"))) {
                        String emailId = del.getEmailID();
                        String type = del.getType();
                        if (type.equalsIgnoreCase("D")) {
                            type = "Doctor";
                        } else if (type.equalsIgnoreCase("P")) {
                            type = "Patient";
                        } else if (type.equalsIgnoreCase("A")) {
                            type = "Assistant";
                        }
                        saveToSession(emailId, type);
                        Intent intObj = new Intent(HomeActivity.this, HomeActivityRevision.class);
                        startActivity(intObj);
                    }
                }
            });

            //ProfileSwitchDialogPatient profileDialg = ProfileSwitchDialogPatient.newInstance();
            //profileDialg.show(HomeActivity.this.getFragmentManager(),"Dialog");
        }

        // Getting a reference to Close button, and close the popup when clicked.
        /*Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });*/
    }

    public void managePatientIcons() {
        LinearLayout.LayoutParams lpReminder = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpReminder.setMargins(35, 5, 0, 10);
        lpReminder.width = 50;
        lpReminder.height = 62;
        notification.setLayoutParams(lpReminder);
        LinearLayout.LayoutParams lpMessage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpMessage.setMargins(15, 5, 0, 10);
        lpReminder.width = 50;
        lpReminder.height = 62;
        messages.setLayoutParams(lpMessage);
        LinearLayout.LayoutParams lpDoctorSearch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpDoctorSearch.setMargins(15, 5, 0, 10);
        lpReminder.width = 50;
        lpReminder.height = 62;
        doctorSearch.setLayoutParams(lpDoctorSearch);
        LinearLayout.LayoutParams lpClinicSearch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpReminder.width = 50;
        lpReminder.height = 62;
        lpClinicSearch.setMargins(15, 5, 0, 10);
        clinicSearch.setLayoutParams(lpClinicSearch);
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

    public void saveToSession(String email, String typeId) {
        String userId = email;
        String type = typeId;
        session = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        session.edit().putString("sessionID_Revision", userId).apply();
        session.edit().putString("type_Revision", type).apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in onActivityResult HomeActivity /////////////////////////////////");
        FileUploadDialog fileupload = new FileUploadDialog();
        fileupload.onActivityResult(requestCode, resultCode, data);


        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.replacementFragment);
        //fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    public void showMenus() {
        if (logString.equals("Patient")) {
            arrayMenu = mainMenu.getPatientMenus();
        }

        if (logString.equals("Doctor")) {
            arrayMenu = mainMenu.getDoctorMenus();
        }

        if (logString.equals("Assistant")) {
            arrayMenu = mainMenu.getDoctorMenus();
        }

    }

}
