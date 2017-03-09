package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.medico.application.MyApi;
import Model.Schedule;
import Model.TimeNew;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MNT on 02-Mar-15.
 */
public class EditTimeScheduleShowClinic extends Fragment {

    private Spinner frmhr1, frmmn1, tohr1, tomn1, frmhr2, frmmn2, tohr2, tomn2, fram1, toam1, fram2, toam2;
    private Button selectAll, selectAll2, selectAll3,done;
    private Spinner frmhr3,frmmn3,tohr3,tomn3,fram3,toam3;
    private CheckBox monday, tuesday, wednesday, thrustday, friday, saturday, sunday, monday2, tuesday2, wednesday2, thrustday2, friday2, saturday2, sunday2;
    private CheckBox monday3,tuesday3,wednesday3,thrustday3,friday3,saturday3,sunday3;
    private String from1, from2, to1, to2,from3,to3;
    private ArrayList<Schedule> arrayShift1, arrayShift2,totalArrayShift, editedArrayList,arrayShift3;
    private TimeNew toc = new TimeNew();
    private String doctorId;
    private String clinicId;
    private int spinnerPosition = 0;
    private ArrayAdapter<CharSequence> hourad1,hourad2,hourad3;
    private ArrayAdapter<CharSequence> ampmtoAdapter1,ampmtoAdapter2,ampmtoAdapter3;
    private ArrayAdapter<CharSequence> minad1,minad2,minad3;
    private RelativeLayout layout,layout3;
    private String name,location,address;
    private int shiftFlag1,shiftFlag2,shiftFlag3;
    private MyApi api;
    private int flagShift = 0;
    private int shiftFlagOnOff =0;
    private int shiftFlag = 0;
    Global go;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new AddClinic();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2, null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getActionBar().hide();
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Edit Time Schedule");
        View view = inflater.inflate(R.layout.time_selected, container, false);
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new AddClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        monday = (CheckBox) view.findViewById(R.id.monday);
        tuesday = (CheckBox) view.findViewById(R.id.tuesday);
        wednesday = (CheckBox) view.findViewById(R.id.wednesday);
        thrustday = (CheckBox) view.findViewById(R.id.thursday);
        friday = (CheckBox) view.findViewById(R.id.friday);
        saturday = (CheckBox) view.findViewById(R.id.saturday);
        sunday = (CheckBox) view.findViewById(R.id.sunday);
        arrayShift1 = new ArrayList<Schedule>();
        arrayShift2 = new ArrayList<Schedule>();
        arrayShift3 = new ArrayList<Schedule>();
        totalArrayShift = new ArrayList<Schedule>();
        Bundle bun = getArguments();
        go = (Global)getActivity().getApplicationContext();

        editedArrayList = (ArrayList<Schedule>) bun.getSerializable("totalShift");
        System.out.println("ArrayList "+editedArrayList.size());
        //Retrofit Initializtion Code
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        monday3 = (CheckBox) view.findViewById(R.id.monday3);
        tuesday3 = (CheckBox) view.findViewById(R.id.tuesday3);
        wednesday3 = (CheckBox) view.findViewById(R.id.wednesday3);
        thrustday3 = (CheckBox) view.findViewById(R.id.thursday3);
        friday3 = (CheckBox) view.findViewById(R.id.friday3);
        saturday3 = (CheckBox) view.findViewById(R.id.saturday3);
        sunday3 = (CheckBox) view.findViewById(R.id.sunday3);
        frmhr3 =(Spinner) view.findViewById(R.id.hourfrom3);
        frmmn3=(Spinner) view.findViewById(R.id.minutefrom3);
        fram3 =(Spinner) view.findViewById(R.id.ampmfrom3);

        tohr3 =(Spinner) view.findViewById(R.id.hourto3);
        tomn3 =(Spinner) view.findViewById(R.id.minuteto3);
        toam3 =(Spinner) view.findViewById(R.id.ampmto3);

        monday2 = (CheckBox) view.findViewById(R.id.monday2);
        tuesday2 = (CheckBox) view.findViewById(R.id.tuesday2);
        wednesday2 = (CheckBox) view.findViewById(R.id.wednesday2);
        thrustday2 = (CheckBox) view.findViewById(R.id.thursday2);
        friday2 = (CheckBox) view.findViewById(R.id.friday2);
        saturday2 = (CheckBox) view.findViewById(R.id.saturday2);
        sunday2 = (CheckBox) view.findViewById(R.id.sunday2);

        frmhr1 = (Spinner) view.findViewById(R.id.hourfrom);
        frmmn1 = (Spinner) view.findViewById(R.id.minutefrom);
        fram1 = (Spinner) view.findViewById(R.id.ampmfrom);


        tohr1 = (Spinner) view.findViewById(R.id.hourto);
        tomn1 = (Spinner) view.findViewById(R.id.minuteto);
        toam1 = (Spinner) view.findViewById(R.id.ampmto);

        frmhr2 = (Spinner) view.findViewById(R.id.hourfrom2);
        frmmn2 = (Spinner) view.findViewById(R.id.minutefrom2);
        fram2 = (Spinner) view.findViewById(R.id.ampmfrom2);

        tohr2 = (Spinner) view.findViewById(R.id.hourto2);
        tomn2 = (Spinner) view.findViewById(R.id.minuteto2);
        toam2 = (Spinner) view.findViewById(R.id.ampmto2);

        ampmtoAdapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.am_pm, android.R.layout.simple_spinner_item);
        ampmtoAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourad1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.hour_list, android.R.layout.simple_spinner_item);
        hourad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        minad1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.minute_list, android.R.layout.simple_spinner_item);
        minad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ampmtoAdapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.am_pm, android.R.layout.simple_spinner_item);
        ampmtoAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourad2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.hour_list, android.R.layout.simple_spinner_item);
        hourad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        minad2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.minute_list, android.R.layout.simple_spinner_item);
        minad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hourad3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.hour_list, android.R.layout.simple_spinner_item);
        hourad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        minad3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.minute_list, android.R.layout.simple_spinner_item);
        minad3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ampmtoAdapter3 = ArrayAdapter.createFromResource(getActivity(),
                R.array.am_pm, android.R.layout.simple_spinner_item);
        ampmtoAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frmhr1.setAdapter(hourad1);
        frmhr2.setAdapter(hourad2);
        frmhr3.setAdapter(hourad3);
        tohr1.setAdapter(hourad1);
        tohr2.setAdapter(hourad2);
        tohr3.setAdapter(hourad3);
        frmmn1.setAdapter(minad1);
        frmmn2.setAdapter(minad2);
        frmmn3.setAdapter(minad3);
        tomn1.setAdapter(minad1);
        tomn2.setAdapter(minad2);
        tomn3.setAdapter(minad3);
        fram1.setAdapter(ampmtoAdapter1);
        toam1.setAdapter(ampmtoAdapter1);
        fram2.setAdapter(ampmtoAdapter2);
        toam2.setAdapter(ampmtoAdapter2);
        fram3.setAdapter(ampmtoAdapter3);
        toam3.setAdapter(ampmtoAdapter3);

        final RelativeLayout secondshift = (RelativeLayout) view.findViewById(R.id.secondshift);
        secondshift.setVisibility(View.GONE);
        layout = (RelativeLayout)view.findViewById(R.id.secondshift);
        layout.setVisibility(View.GONE);
        layout3 = (RelativeLayout)view.findViewById(R.id.thirdshift);

        ImageButton thirdShiftAdd = (ImageButton)view.findViewById(R.id.add_shift3);
        thirdShiftAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout3.setVisibility(View.VISIBLE);
                if(shiftFlagOnOff == 0)
                {
                    shiftFlagOnOff = 1;
                }
            }
        });
        ImageButton add = (ImageButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondshift.setVisibility(View.VISIBLE);
                if(shiftFlag == 0)
                {
                    shiftFlag = 1;
                }
            }
        });

        ImageButton removeThirdShift = (ImageButton)view.findViewById(R.id.remove1);
        removeThirdShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout3.setVisibility(View.GONE);
                if(shiftFlagOnOff ==1)
                {
                    shiftFlagOnOff = 0;
                }
            }
        });


        selectAll = (Button) view.findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Text of Button " + selectAll.getText().toString());

                if ((selectAll.getText().toString()).equals("Select All")) {

                    monday.setChecked(true);
                    tuesday.setChecked(true);
                    wednesday.setChecked(true);
                    thrustday.setChecked(true);
                    friday.setChecked(true);
                    saturday.setChecked(true);
                    sunday.setChecked(true);

                    System.out.println("I am in True Section.....");
                    selectAll.setText("DeSelect All");
                } else {
                    monday.setChecked(false);
                    tuesday.setChecked(false);
                    wednesday.setChecked(false);
                    thrustday.setChecked(false);
                    friday.setChecked(false);
                    saturday.setChecked(false);
                    sunday.setChecked(false);


                    selectAll.setText("Select All");
                }

            }

        });

        selectAll2 = (Button) view.findViewById(R.id.select_all2);
        selectAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((selectAll2.getText().toString()).equals("Select All")) {

                    monday2.setChecked(true);
                    tuesday2.setChecked(true);
                    wednesday2.setChecked(true);
                    thrustday2.setChecked(true);
                    friday2.setChecked(true);
                    saturday2.setChecked(true);
                    sunday2.setChecked(true);
                    System.out.println("I am in true Section");

                    selectAll2.setText("DeSelect All");
                } else {
                    monday2.setChecked(false);
                    tuesday2.setChecked(false);
                    wednesday2.setChecked(false);
                    thrustday2.setChecked(false);
                    friday2.setChecked(false);
                    saturday2.setChecked(false);
                    sunday2.setChecked(false);

                    System.out.println("I am in false Section");

                    selectAll2.setText("Select All");
                }

            }

        });
        selectAll3 = (Button) view.findViewById(R.id.select_all3);
        selectAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((selectAll3.getText().toString()).equals("Select All"))
                {
                    monday3.setChecked(true);
                    tuesday3.setChecked(true);
                    wednesday3.setChecked(true);
                    thrustday3.setChecked(true);
                    friday3.setChecked(true);
                    saturday3.setChecked(true);
                    sunday3.setChecked(true);
                    System.out.println("I am in true Section");
                    selectAll3.setText("DeSelect All");
                }
                else
                {
                    monday3.setChecked(false);
                    tuesday3.setChecked(false);
                    wednesday3.setChecked(false);
                    thrustday3.setChecked(false);
                    friday3.setChecked(false);
                    saturday3.setChecked(false);
                    sunday3.setChecked(false);
                    System.out.println("I am in false Section");
                    selectAll3.setText("Select All");
                }
            }
        });
        ImageButton remove = (ImageButton) view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondshift.setVisibility(View.GONE);
                if(shiftFlag == 1)
                {
                    shiftFlag = 0;
                }

            }
        });

        showEditData();

        Button done = (Button) view.findViewById(R.id.time_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftFlag1 = 0;
                shiftFlag2 = 0;
                shiftFlag3 = 0;

                totalArrayShift = new ArrayList<Schedule>();

                from1 = frmhr1.getSelectedItem().toString();
                from1 = from1 + ":" + frmmn1.getSelectedItem().toString();
                from1 = from1 + " " + fram1.getSelectedItem().toString();

                to1 = tohr1.getSelectedItem().toString();
                to1 = to1 + ":" + tomn1.getSelectedItem().toString();
                to1 = to1 + " " + toam1.getSelectedItem().toString();

                try
                {

                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                    DateFormat outputformat = new SimpleDateFormat("HH:mm");
                    Date startDate1 = sdf1.parse(from1);
                    Date endDate1 = sdf1.parse(to1);
                    String outputFrom1 = outputformat.format(startDate1);
                    String outputTo1 = outputformat.format(endDate1);
                    String[] arrayOutputFrom1 = outputFrom1.split(":");
                    String[] arrayOutputTo1 = outputTo1.split(":");
                    int hourFrom1 = Integer.parseInt(arrayOutputFrom1[0]);
                    int minuteFrom1 = Integer.parseInt(arrayOutputFrom1[1]);
                    int hourTo1 = Integer.parseInt(arrayOutputTo1[0]);
                    int minuteTo1 = Integer.parseInt(arrayOutputTo1[1]);
                    System.out.println("Time Condition:::::: "+((hourFrom1<=hourTo1)||(minuteFrom1<minuteTo1)));
                    if ((hourFrom1<=hourTo1)||(minuteFrom1>minuteTo1))
                    {
                        if (hourFrom1 == hourTo1)
                        {
                            if (minuteFrom1 < minuteTo1) {
                                shiftFlag1 = addShift1();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else
                        {
                            shiftFlag1= addShift1();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                    }

                    if(flagShift == 1)
                    {


                        from2 = frmhr2.getSelectedItem().toString();
                        from2 = from2 + ":" + frmmn2.getSelectedItem().toString();
                        from2 = from2 + " " + fram2.getSelectedItem().toString();

                        to2 = tohr2.getSelectedItem().toString();
                        to2 = to2 + ":" + tomn2.getSelectedItem().toString();
                        to2 = to2 + " " + toam2.getSelectedItem().toString();

                        Date startDate2 = sdf1.parse(from2);
                        Date endDate2 = sdf1.parse(to2);
                        String outputFrom2 = outputformat.format(startDate2);
                        String outputTo2 = outputformat.format(endDate2);
                        String[] arrayOutputFrom2 = outputFrom2.split(":");
                        String[] arrayOutputTo2 = outputTo2.split(":");
                        int hourFrom2 = Integer.parseInt(arrayOutputFrom2[0]);
                        int minuteFrom2 = Integer.parseInt(arrayOutputFrom2[1]);
                        int hourTo2 = Integer.parseInt(arrayOutputTo2[0]);
                        int minuteTo2 = Integer.parseInt(arrayOutputTo2[1]);
                        if ((hourFrom2<=hourTo2)||(minuteFrom2>minuteTo2))
                        {
                            if (hourFrom2 == hourTo2) {
                                if (minuteFrom2 < minuteTo2) {
                                    shiftFlag2 = addShift2();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                shiftFlag2 = addShift2();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(shiftFlag == 1)
                    {
                        from2 = frmhr2.getSelectedItem().toString();
                        from2 = from2 + ":" + frmmn2.getSelectedItem().toString();
                        from2 = from2 + " " + fram2.getSelectedItem().toString();

                        to2 = tohr2.getSelectedItem().toString();
                        to2 = to2 + ":" + tomn2.getSelectedItem().toString();
                        to2 = to2 + " " + toam2.getSelectedItem().toString();

                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
                        Date startDate2 = sdf2.parse(from2);
                        Date endDate2 = sdf2.parse(to2);
                        String outputFrom2 = outputformat.format(startDate2);
                        String outputTo2 = outputformat.format(endDate2);
                        String[] arrayOutputFrom2 = outputFrom2.split(":");
                        String[] arrayOutputTo2 = outputTo2.split(":");
                        int hourFrom2 = Integer.parseInt(arrayOutputFrom2[0]);
                        int minuteFrom2 = Integer.parseInt(arrayOutputFrom2[1]);
                        int hourTo2 = Integer.parseInt(arrayOutputTo2[0]);
                        int minuteTo2 = Integer.parseInt(arrayOutputTo2[1]);
                        if ((hourFrom2<=hourTo2)||(minuteFrom2>minuteTo2))
                        {
                            if (hourFrom2 == hourTo2) {
                                if (minuteFrom2 < minuteTo2) {
                                    shiftFlag2 = addShift2();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                shiftFlag2 = addShift2();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                        }

                    }

                    if(shiftFlagOnOff == 1)
                    {
                        from3 = frmhr3.getSelectedItem().toString();
                        from3 = from3 + ":" + frmmn3.getSelectedItem().toString();
                        from3 = from3 + " " + fram3.getSelectedItem().toString();

                        to3= tohr3.getSelectedItem().toString();
                        to3 = to3 + ":" + tomn3.getSelectedItem().toString();
                        to3 = to3 + " " + toam3.getSelectedItem().toString();
                        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm aa");
                        Date startDate3 = sdf3.parse(from3);
                        Date endDate3 = sdf3.parse(to3);
                        String outputFrom3 = outputformat.format(startDate3);
                        String outputTo3 = outputformat.format(endDate3);
                        String[] arrayOutputFrom3 = outputFrom3.split(":");
                        String[] arrayOutputTo3 = outputTo3.split(":");
                        int hourFrom3 = Integer.parseInt(arrayOutputFrom3[0]);
                        int minuteFrom3 = Integer.parseInt(arrayOutputFrom3[1]);
                        int hourTo3 = Integer.parseInt(arrayOutputTo3[0]);
                        int minuteTo3 = Integer.parseInt(arrayOutputTo3[1]);
                        if ((hourFrom3<=hourTo3)||(minuteFrom3>minuteTo3))
                        {
                            if (hourFrom3 == hourTo3) {
                                if (minuteFrom3 < minuteTo3) {
                                    shiftFlag3 = addShift3();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                shiftFlag3 = addShift3();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please Check Time of Shifts", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if((shiftFlag1 == 1)||(shiftFlag2 == 2)||(shiftFlag3 ==3))
                    {

                        System.out.println("Total Array Shifts "+totalArrayShift.toArray());

                        toc.setClinicId(clinicId);
                        toc.setDoctorId(doctorId);
                        toc.setSchedules(totalArrayShift);
                        System.out.println("Time Schedule Added");
                        Fragment frag2 = new EditClinic();
                        go.setClinicSlots(totalArrayShift);
                        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, frag2, null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();

                    }


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }

    public void showEditData() {
        int lenShift = editedArrayList.size();

        for (int i = 0; i < lenShift; i++) {
            Schedule s = new Schedule();
            s = editedArrayList.get(i);

            if (s.getShift().equals("shift1")) {
                System.out.println("Shift1 Day"+s.getDay());
                arrayShift1.add(s);
            }

            if (s.getShift().equals("shift2")) {
                flagShift = 1;
                arrayShift2.add(s);
            }

            if (s.getShift().equals("shift3")) {
                shiftFlagOnOff = 1;
                arrayShift3.add(s);
            }


        }

        int lenShift1 = arrayShift1.size();
        int lenshift2 = arrayShift2.size();
        int lenShift3 = arrayShift3.size();

        if(lenShift1!=0)
        {
            showCheckBoxes1(arrayShift1);
            totalArrayShift.addAll(arrayShift1);
        }
        if(lenshift2!=0)
        {
            layout.setVisibility(View.VISIBLE);
            showCheckBoxes2(arrayShift2);
            totalArrayShift.addAll(arrayShift2);
        }
        if(lenShift3!=0)
        {
            layout3.setVisibility(View.VISIBLE);
            showCheckBoxes3(arrayShift3);
            totalArrayShift.addAll(arrayShift3);
        }


    }

    public void showCheckBoxes1(ArrayList<Schedule> array)
    {
        int lenShifts = array.size();

        for(int i=0;i<lenShifts;i++)
        {
            Schedule s = array.get(i);
            System.out.println("Day "+s.getDay());

            switch (s.getDay())
            {

                case "Mon":
                               monday.setChecked(true);
                               break;
                case "Tue":
                               tuesday.setChecked(true);
                               break;
                case "Wed":
                               wednesday.setChecked(true);
                               break;
                case "Thu":
                               thrustday.setChecked(true);
                               break;
                case "Fri":
                               friday.setChecked(true);
                               break;
                case "Sat":
                               saturday.setChecked(true);
                               break;
                case "Sun":
                               sunday.setChecked(true);
                               break;
            }
        }

        System.out.println("Array From Time"+array.get(0).getFrom());
        String fromTime = array.get(0).getFrom();
        String toTime = array.get(0).getTo();

        System.out.println("Shift1 "+fromTime+" "+toTime);

        String[] timeFrom = fromTime.split(" ");
        String[] timeFromHoursMins = timeFrom[0].split(":");
        System.out.println("Shift1 From "+timeFromHoursMins[0]+" "+timeFromHoursMins[1]);
        spinnerPosition = hourad1.getPosition(timeFromHoursMins[0]);
        frmhr1.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad1.getPosition(timeFromHoursMins[1]);
        frmmn1.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter1.getPosition(timeFrom[1]);
        fram1.setSelection(spinnerPosition);
        spinnerPosition = 0;

        String[] timeTo = toTime.split(" ");
        String[] timeToHoursMins = timeTo[0].split(":");
        spinnerPosition = hourad2.getPosition(timeToHoursMins[0]);
        tohr1.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad2.getPosition(timeToHoursMins[1]);
        tomn1.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter2.getPosition(timeTo[1]);
        toam1.setSelection(spinnerPosition);
        spinnerPosition = 0;



    }

    public void showCheckBoxes2(ArrayList<Schedule> array)
    {
        int lenShifts = array.size();

        for(int i=0;i<lenShifts;i++)
        {
            Schedule s = array.get(i);

            switch (s.getDay())
            {

                case "Mon":
                    monday2.setChecked(true);
                    break;
                case "Tue":
                    tuesday2.setChecked(true);
                    break;
                case "Wed":
                    wednesday2.setChecked(true);
                    break;
                case "Thu":
                    thrustday2.setChecked(true);
                    break;
                case "Fri":
                    friday2.setChecked(true);
                    break;
                case "Sat":
                    saturday2.setChecked(true);
                    break;
                case "Sun":
                    sunday2.setChecked(true);
                    break;
            }
        }

        String fromTime2 = array.get(0).getFrom();
        String toTime2 = array.get(0).getTo();
        System.out.println("Shift2 "+fromTime2+" "+toTime2);

        String[] timeFrom2 = fromTime2.split(" ");
        String[] timeFromHoursMins2 = timeFrom2[0].split(":");
        spinnerPosition = hourad1.getPosition(timeFromHoursMins2[0]);
        frmhr2.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad1.getPosition(timeFromHoursMins2[1]);
        frmmn2.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter1.getPosition(timeFrom2[1]);
        fram2.setSelection(spinnerPosition);
        spinnerPosition = 0;

        String[] timeTo2 = toTime2.split(" ");
        String[] timeToHoursMins2 = timeTo2[0].split(":");
        spinnerPosition = hourad2.getPosition(timeToHoursMins2[0]);
        tohr2.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad2.getPosition(timeToHoursMins2[1]);
        tomn2.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter2.getPosition(timeTo2[1]);
        toam2.setSelection(spinnerPosition);
        spinnerPosition = 0;

    }

    public void showCheckBoxes3(ArrayList<Schedule> array)
    {
        int lenShifts = array.size();

        for(int i=0;i<lenShifts;i++)
        {
            Schedule s = array.get(i);

            switch (s.getDay())
            {

                case "Mon":
                    monday3.setChecked(true);
                    break;
                case "Tue":
                    tuesday3.setChecked(true);
                    break;
                case "Wed":
                    wednesday3.setChecked(true);
                    break;
                case "Thu":
                    thrustday3.setChecked(true);
                    break;
                case "Fri":
                    friday3.setChecked(true);
                    break;
                case "Sat":
                    saturday3.setChecked(true);
                    break;
                case "Sun":
                    sunday3.setChecked(true);
                    break;
            }
        }

        String fromTime3 = array.get(0).getFrom();
        String toTime3 = array.get(0).getTo();
        System.out.println("Shift2 "+fromTime3+" "+toTime3);

        String[] timeFrom3 = fromTime3.split(" ");
        String[] timeFromHoursMins3 = timeFrom3[0].split(":");
        spinnerPosition = hourad3.getPosition(timeFromHoursMins3[0]);
        frmhr3.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad3.getPosition(timeFromHoursMins3[1]);
        frmmn3.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter3.getPosition(timeFrom3[1]);
        fram3.setSelection(spinnerPosition);
        spinnerPosition = 0;

        String[] timeTo3 = toTime3.split(" ");
        String[] timeToHoursMins3 = timeTo3[0].split(":");
        spinnerPosition = hourad3.getPosition(timeToHoursMins3[0]);
        tohr3.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = minad3.getPosition(timeToHoursMins3[1]);
        tomn3.setSelection(spinnerPosition);
        spinnerPosition = 0;
        spinnerPosition = ampmtoAdapter3.getPosition(timeTo3[1]);
        toam3.setSelection(spinnerPosition);
        spinnerPosition = 0;

    }

    public int addShift1()
    {
        arrayShift1 = new ArrayList<Schedule>();
        System.out.println("From1 " + from1);
        System.out.println("To1 " + to1);
        if (monday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Mon");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }

        if (tuesday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Tue");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }

        if (wednesday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Wed");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }
        if (thrustday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Thu");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }

        if (friday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Fri");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }

        if (saturday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Sat");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);

        }

        if (sunday.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift1");
            s.setDay("Sun");
            s.setFrom(from1);
            s.setTo(to1);
            arrayShift1.add(s);
        }

        totalArrayShift.addAll(arrayShift1);
        return 1;

    }

    public int addShift2()
    {
        arrayShift2 = new ArrayList<Schedule>();
        from2 = frmhr2.getSelectedItem().toString();
        from2 = from2 + ":" + frmmn2.getSelectedItem().toString();
        from2 = from2 + " " + fram2.getSelectedItem().toString();

        to2 = tohr2.getSelectedItem().toString();
        to2 = to2 + ":" + tomn2.getSelectedItem().toString();
        to2 = to2 + " " + toam2.getSelectedItem().toString();

        if (monday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Mon");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (tuesday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Tue");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (wednesday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Wed");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (thrustday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Thu");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (friday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Fri");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (saturday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Sat");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);

        }

        if (sunday2.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift2");
            s.setDay("Sun");
            s.setFrom(from2);
            s.setTo(to2);
            arrayShift2.add(s);


        }

        totalArrayShift.addAll(arrayShift2);
        return 2;
    }

    public int addShift3()
    {
        arrayShift3 = new ArrayList<Schedule>();

        from3 = frmhr3.getSelectedItem().toString();
        from3 = from3 + ":" + frmmn3.getSelectedItem().toString();
        from3 = from3 + " " + fram3.getSelectedItem().toString();

        to3 = tohr3.getSelectedItem().toString();
        to3 = to3 + ":" + tomn3.getSelectedItem().toString();
        to3 = to3 + " " + toam3.getSelectedItem().toString();


        if (monday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Mon");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (tuesday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Tue");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (wednesday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Wed");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (thrustday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Thu");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (friday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Fri");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (saturday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Sat");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);

        }

        if (sunday3.isChecked()) {
            Schedule s = new Schedule();
            s.setShift("shift3");
            s.setDay("Sun");
            s.setFrom(from3);
            s.setTo(to3);
            arrayShift3.add(s);


        }

        totalArrayShift.addAll(arrayShift3);
        return 3;
    }

}

