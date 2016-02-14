package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.AddDialogFragment;
import com.mindnerves.meidcaldiary.Fragments.EditDoseFragment;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;
import java.util.List;
import Model.AlarmReminderVM;


/**
 * Created by MNT on 16-Feb-15.
 */
public class HorizontalListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<AlarmReminderVM> medicinReminderTables;
    Global global;

    public HorizontalListAdapter(Activity activity, List<AlarmReminderVM> medicinReminderTables) {
        this.activity = activity;
        this.medicinReminderTables = medicinReminderTables;
    }

    @Override
    public int getCount() {
        return medicinReminderTables.size();
    }

    @Override
    public Object getItem(int position) {
        return medicinReminderTables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        if(inflater == null){
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.hr_medicin_reminder, null);
        global = (Global) activity.getApplicationContext();
        final TextView day = (TextView) convertView.findViewById(R.id.day);
        final TextView time1 = (TextView) convertView.findViewById(R.id.time1);
        final TextView time2 = (TextView) convertView.findViewById(R.id.time2);
        final TextView time3 = (TextView) convertView.findViewById(R.id.time3);
        final TextView time4 = (TextView) convertView.findViewById(R.id.time4);
        final TextView time5 = (TextView) convertView.findViewById(R.id.time5);
        final TextView time6 = (TextView) convertView.findViewById(R.id.time6);
        day.setText(medicinReminderTables.get(position).getAlarmDate());
        time1.setText(medicinReminderTables.get(position).getTime1());
        time2.setText(medicinReminderTables.get(position).getTime2());
        time3.setText(medicinReminderTables.get(position).getTime3());
        time4.setText(medicinReminderTables.get(position).getTime4());
        time5.setText(medicinReminderTables.get(position).getTime5());
        time6.setText(medicinReminderTables.get(position).getTime6());
        global.setAlarmTime(medicinReminderTables);
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time1.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlarmTime(medicinReminderTables);
                global.setAlaramObj(vm);
                global.setTimeChange("Time1");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");
             /*   EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");*/

            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time2.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlaramObj(vm);
                global.setTimeChange("Time2");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");

            }
        });
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time3.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlaramObj(vm);
                global.setTimeChange("Time3");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");

            }
        });
        time4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time4.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlaramObj(vm);
                global.setTimeChange("Time4");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");

            }
        });
        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time5.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlaramObj(vm);
                global.setTimeChange("Time5");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");

            }
        });
        time6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView)v;
                AlarmReminderVM vm = (AlarmReminderVM)tv.getTag();
                String text = time6.getText().toString();
                System.out.println("text:::::::"+text);
                global.setAlaramObj(vm);
                global.setTimeChange("Time6");
                global.setTimeText(text);
                EditDoseFragment edf = EditDoseFragment.newInstance();
                edf.show(activity.getFragmentManager(),"Dialog");

            }
        });
        time1.setTag(medicinReminderTables.get(position));
        time2.setTag(medicinReminderTables.get(position));
        time3.setTag(medicinReminderTables.get(position));
        time4.setTag(medicinReminderTables.get(position));
        time5.setTag(medicinReminderTables.get(position));
        time6.setTag(medicinReminderTables.get(position));
        convertView.setTag(medicinReminderTables);
        return convertView;

    }
}
