package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;
import java.util.ArrayList;
import Adapter.ShowTimeScheduleAdapter;
import Model.Day;
import Model.Schedule;

/**
 * Created by MNT on 27-Feb-15.
 */
public class ShowTimeSchedule extends Fragment{

    private ListView listViewTime;
    private ArrayList<Schedule> arraySchedule;
    private ArrayList<Day> arrayDays;
    private ShowTimeScheduleAdapter listAdapterManageTime;
    private String doctorId,clinicId;
    private Button doneButton;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_timing_new,container,false);
        listViewTime = (ListView)view.findViewById(R.id.list_day);
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Show Time Schedule");
        getActivity().getActionBar().hide();
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
        arrayDays = new ArrayList<Day>();
        Bundle bun = getArguments();
        arraySchedule = (ArrayList<Schedule>)bun.getSerializable("totalShift");
        System.out.println("Array Schedules "+arraySchedule.toArray());
        doctorId = bun.getString("doctorId");
        clinicId = bun.getString("clinicId");
        Day d1 = new Day();
        d1.setDay("Mon");
        Day d2 = new Day();
        d2.setDay("Tue");
        Day d3 = new Day();
        d3.setDay("Wed");
        Day d4 = new Day();
        d4.setDay("Thu");
        Day d5 = new Day();
        d5.setDay("Fri");
        Day d6 = new Day();
        d6.setDay("Sat");
        Day d7 = new Day();
        d7.setDay("Sun");
        arrayDays.add(d1);
        arrayDays.add(d2);
        arrayDays.add(d3);
        arrayDays.add(d4);
        arrayDays.add(d5);
        arrayDays.add(d6);
        arrayDays.add(d7);
        addArrayElements();
        showListTime();

        listViewTime.setSelector(android.R.color.holo_blue_light);
        listViewTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = new EditTimeSchedule();
                Bundle bun = new Bundle();
                bun.putString("clinicId",clinicId);
                bun.putString("doctorId",doctorId);
                bun.putSerializable("totalShift",arraySchedule);
                System.out.println("ShowTImeSchedule "+arraySchedule.toArray());
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Manage_Doctor").commit();

            }
        });



        doneButton = (Button)view.findViewById(R.id.list_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new AddClinic();
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
        System.out.println("ArrayDays "+arrayDays.size());
        listAdapterManageTime = new ShowTimeScheduleAdapter(getActivity(),arrayDays);
        listViewTime.setAdapter(listAdapterManageTime);
        System.out.println("Adapter list Count "+listViewTime.getCount());
    }

    public void addArrayElements()
    {
          int lenDb = arraySchedule.size();
          int lenDay = arrayDays.size();

          for(int i=0;i<lenDay;i++)
          {
              for(int j=0;j<lenDb;j++)
              {
                  if(arrayDays.get(i).getDay().equals(arraySchedule.get(j).getDay()))
                  {

                      if(arraySchedule.get(j).getShift().equals("shift1"))
                      {
                          arrayDays.get(i).setFrom1(arraySchedule.get(j).getFrom());
                          arrayDays.get(i).setTo1((arraySchedule.get(j).getTo()));

                      }
                      if(arraySchedule.get(j).getShift().equals("shift2"))
                      {

                          arrayDays.get(i).setFrom2(arraySchedule.get(j).getFrom());
                          arrayDays.get(i).setTo2(arraySchedule.get(j).getTo());
                      }
                      System.out.println("Condition 3:::::::::::::::"+arraySchedule.get(j).getShift().equals("shift3"));
                      if(arraySchedule.get(j).getShift().equals("shift3"))
                      {

                          arrayDays.get(i).setFrom3(arraySchedule.get(j).getFrom());
                          arrayDays.get(i).setTo3(arraySchedule.get(j).getTo());
                      }

                  }
              }
          }

          for(int i=0;i<lenDay;i++)
          {
              System.out.println("Days "+arrayDays.get(i));
              System.out.println("From1 "+arrayDays.get(i).getFrom1());
              System.out.println("From2 "+arrayDays.get(i).getFrom2());
              System.out.println("To1 "+arrayDays.get(i).getTo1());
              System.out.println("To2 "+arrayDays.get(i).getTo2());
          }

    }
}
