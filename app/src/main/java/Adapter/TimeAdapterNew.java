package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.ShowClinic;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import Model.Schedule;


/**
 * Created by MNT on 26-Feb-15.
 */
public class TimeAdapterNew extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private  int shiftNumber;
    private ArrayList<Schedule> time;
    private TextView dayTv,shiftFromTv1,shifttoTv1,shiftFromTv2,shifttoTv2;
    private String shiftFrom,shiftTo;

    public TimeAdapterNew(Activity activity, ArrayList<Schedule> time) {

        this.activity = activity;
        this.time = time;

    }

    @Override
    public int getCount() {
        return time.size();
    }

    @Override
    public Object getItem(int position) {
        return time.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;

        if (null == convertView) {
            row = inflater.inflate(R.layout.clinic_element_new, null);
        } else {
            row = convertView;
        }

        dayTv = (TextView) row.findViewById(R.id.day);
      //  String shift = time.get(i).getShift();
        String day = time.get(position).getDay();
        System.out.println("Day " + day);
        dayTv.setText(day);

      /*  dayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag2 = new ShowClinic();
                Bundle bun = new Bundle();
                bun.putString("name",clinicList.get(pos).getClinicName());
                bun.putString("address",clinicList.get(pos).getAddress());
                bun.putString("location",clinicList.get(pos).getLocation());
                bun.putString("id",clinicList.get(pos).getIdClinic());
                frag2.setArguments(bun);
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,frag2,"Add_Clinic");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        */

        return row;
    }
}
