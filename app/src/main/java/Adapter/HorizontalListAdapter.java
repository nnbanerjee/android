package Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
public class HorizontalListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<AlarmReminderVM> medicinReminderTables;
    Global global;
    TextView textView1;

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

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.hr_medicin_reminder, null);
        global = (Global) activity.getApplicationContext();

        TextView day = (TextView) convertView.findViewById(R.id.day);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        for (int i = 0; i < medicinReminderTables.get(position).getTimes().size(); i++) {

            textView1 = new TextView(activity);
            textView1.setLayoutParams(new ViewGroup.MarginLayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView1.setText(medicinReminderTables.get(position).getTimes().get(i));
            textView1.setGravity(Gravity.CENTER);
            textView1.setPadding(25, 3, 25, 3);
            textView1.setTag(position + "#" + i);
            // textView1.setEms();
            textView1.setBackgroundColor(activity.getResources().getColor(R.color.grey)); // hex color 0xAARRGGBB
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    //AlarmReminderVM vm = (AlarmReminderVM) tv.getTag();
                    String[] strPos = (String[])( tv.getTag().toString().split("#"));

                    int position=Integer.parseInt(strPos[0]);
                    int selected=Integer.parseInt(strPos[1]);
                    String text = tv.getText().toString();
                    System.out.println("text:::::::" + text);
                    global.setAlarmTime(medicinReminderTables);
                    global.setSelectedPostionOfMedicineScheduleFromHorizontalList(position);
                    global.setSelectedPostionOfMedicineScheduleFromVerticalList(selected);

                   // global.setAlaramObj(vm);
                    global.setTimeChange("Time1");
                    global.setTimeText(text);
                    EditDoseFragment edf = EditDoseFragment.newInstance();
                    edf.show(activity.getFragmentManager(), "Dialog");

                }
            });
            linearLayout.addView(textView1, new ViewGroup.MarginLayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        day.setText(medicinReminderTables.get(position).getAlarmDate());
        global.setAlarmTime(medicinReminderTables);
        convertView.setTag(medicinReminderTables);
        return convertView;
    }
}
