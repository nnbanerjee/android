package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import Adapter.TimeAdapter;
import Adapter.TimeAdapterNew;
import Model.Schedule;
import Model.Time;

/**
 * Created by MNT on 26-Feb-15.
 */
public class AddTimeNew extends Fragment {

    private ListView listViewTime;
    private TimeAdapterNew listAdapterManageTime;
    private ArrayList<Schedule> arrayDay;
    private Time toc = new Time();
    String clinicId = "";
    String doctorId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_timing_new,container,false);
        listViewTime = (ListView)view.findViewById(R.id.list_day);
        getActivity().getActionBar().hide();
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Time");
        Bundle bun = getArguments();
        clinicId = bun.get("clinicId").toString();
        doctorId = bun.get("doctorId").toString();
        Toast.makeText(getActivity(),"Clinic ID "+clinicId,Toast.LENGTH_LONG).show();

        System.out.println("I am here.............");
        toc.setClinicId("1");
        toc.setDoctorId("1");

        arrayDay = new ArrayList<Schedule>();
        Schedule s1 = new Schedule();
        s1.setDay("Monday");
        arrayDay.add(s1);

        Schedule s2 = new Schedule();
        s2.setDay("Tuesday");
        arrayDay.add(s2);

        Schedule s3 = new Schedule();
        s3.setDay("Wednesday");
        arrayDay.add(s3);

        Schedule s4 = new Schedule();
        s4.setDay("Thursday");
        arrayDay.add(s4);

        Schedule s5 = new Schedule();
        s5.setDay("Friday");
        arrayDay.add(s5);

        Schedule s6 = new Schedule();
        s6.setDay("Saturday");
        arrayDay.add(s6);

        Schedule s7 = new Schedule();
        s7.setDay("Sunday");
        arrayDay.add(s7);


        showListTime();

        listViewTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Schedule s =  (Schedule)listAdapterManageTime.getItem(position);
               System.out.println("Schedule "+s.toString());
               Fragment frag2 = new AddTimeSchedule();
               Bundle bun1 = new Bundle();

               System.out.println("Clinic Id from Add TIme "+clinicId);
               bun1.putString("clinicId",clinicId);
               bun1.putString("doctorId",doctorId);
               frag2.setArguments(bun1);
               FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
               ft.replace(R.id.content_frame,frag2,"Add_Clinic");
               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
               ft.addToBackStack(null);
               ft.commit();



            }
        });

        return view;
    }

    public void showListTime()
    {
        System.out.println("ArraySize "+arrayDay.size());
        listAdapterManageTime = new TimeAdapterNew(getActivity(),arrayDay);
        listViewTime.setAdapter(listAdapterManageTime);

        System.out.println("Adapter list Count "+listViewTime.getCount());
    }
}
