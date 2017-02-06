package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.Reminder;

/**
 * Created by MNT on 17-Mar-15.
 */
public class ReminderAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Reminder> reminders;
    private TextView reminderTextTv,reminderTimeTv,reminderDateTv,showDateTv,showTimeTv;
    private CheckBox checkBox;

    public ReminderAdapter(Activity activity,ArrayList<Reminder> reminders)
    {
        this.activity = activity;
        this.reminders = reminders;
    }
    @Override
    public int getCount() {
        return reminders.size();
    }

    @Override
    public Object getItem(int position) {
        return reminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.reminder_element, null);

        final int pos = position;

        reminderTextTv = (TextView)convertView.findViewById(R.id.reminder_text);
        reminderTimeTv = (TextView)convertView.findViewById(R.id.reminder_time);
        reminderDateTv = (TextView)convertView.findViewById(R.id.reminder_date);
        checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_reminder);
        showDateTv = (TextView)convertView.findViewById(R.id.show_date);
        showTimeTv = (TextView)convertView.findViewById(R.id.show_time);
        showDateTv = (TextView)convertView.findViewById(R.id.show_date);

        String showDateString = "" ;
        if(reminders.get(pos).getDate().equals(""))
        {
                showDateString = "";

        }
        else
        {
            String[] arrayDate = reminders.get(pos).getDate().split("-");

            int day = Integer.parseInt(arrayDate[0]);
            int month = Integer.parseInt(arrayDate[1]);
            int year = Integer.parseInt(arrayDate[2]);

            month = showMonth(month);
            showDateString = day+"/"+month+"/"+year;
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;

                System.out.println("I am here " + cb.isChecked());

                Reminder rem = (Reminder) cb.getTag();

                System.out.println("Doc " + rem.toString());

                if (cb.isChecked()) {
                    rem.setSelected(true);
                    System.out.println("Doctor Object Value " + rem.isSelected());
                } else {
                    rem.setSelected(false);
                }

            }
        });
        convertView.setTag(reminders);
        final Reminder r = reminders.get(position);
        if((r.getTitle()).equals("No Remind Time Set"))
        {
            checkBox.setVisibility(View.INVISIBLE);
            showTimeTv.setVisibility(View.INVISIBLE);
            showDateTv.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(r.isSelected()){
                checkBox.setChecked(true);
            }  else {
                checkBox.setChecked(false);
            }
            checkBox.setTag(r);
        }

        reminderTextTv.setText(r.getTitle());
        reminderTimeTv.setText(r.getTime());
        reminderDateTv.setText(showDateString);

        return convertView;
    }

    public int showMonth(int month)
    {
        int showMonth = month;
        switch(showMonth)
        {
            case 0:
                  showMonth = showMonth + 1;
                  break;
            case 1:
                  showMonth = showMonth + 1;
                  break;
            case 2:
                  showMonth = showMonth + 1;
                  break;
            case 3:
                  showMonth = showMonth + 1;
                  break;
            case 4:
                  showMonth = showMonth + 1;
                  break;
            case 5:
                  showMonth = showMonth + 1;
                  break;
            case 6:
                  showMonth = showMonth + 1;
                  break;
            case 7:
                  showMonth = showMonth + 1;
                  break;
            case 8:
                  showMonth = showMonth + 1;
                  break;
            case 9:
                  showMonth = showMonth + 1;
                  break;
            case 10:
                  showMonth = showMonth + 1;
                 break;
            case 11:
                  showMonth = showMonth + 1;
                  break;

        }
        return showMonth;
    }
}
