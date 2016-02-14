package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Model.SummaryHistoryVM;


/**
 * Created by MNT on 23-Feb-15.
 */
public class HistryAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    List<SummaryHistoryVM> histryList = new ArrayList<SummaryHistoryVM>();
    private String header;

    public HistryAdapter(Activity activity, List<SummaryHistoryVM> histryList, String headingText) {
        this.activity = activity;
        this.histryList = histryList;
        this.header = headingText;
    }

    @Override
    public int getCount() {
        return histryList.size();
    }

    @Override
    public Object getItem(int position) {
        return histryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.show_histry_item, null);

        TextView headingText = (TextView) convertView.findViewById(R.id.headingText);
        TextView previousText = (TextView) convertView.findViewById(R.id.previousText);
        TextView user = (TextView) convertView.findViewById(R.id.user);
        TextView date = (TextView) convertView.findViewById(R.id.date);


        if (header.equalsIgnoreCase("Symptoms Histry")) {
            System.out.println("Symptom History:::::::::::" + histryList.size());
            if (histryList.size() >= 2) {
                if ((position - 1) >= 0) {
                    System.out.println("position= " + position);
                    System.out.println("symptoms = " + histryList.get(position - 1).symptoms);
                    previousText.setText(histryList.get(position - 1).symptoms);
                } else {
                    previousText.setText("None");
                }
            }

            headingText.setText(histryList.get(position).symptoms);
        } else if (header.equalsIgnoreCase("Diagnosis Histry")) {
            System.out.println("Symptom History:::::::::::" + histryList.size());
            if (histryList.size() >= 2) {
                if ((position - 1) >= 0) {
                    System.out.println("diagnosis = " + histryList.get(position - 1).diagonosis);
                    previousText.setText(histryList.get(position - 1).diagonosis);
                } else {
                    previousText.setText("None");
                }
            }
            headingText.setText(histryList.get(position).diagonosis);
        } else if (header.equalsIgnoreCase("Prescribed Histry")) {
            System.out.println("Symptom History:::::::::::" + histryList.size());
            if (histryList.size() >= 2) {
                if ((position - 1) >= 0) {
                    System.out.println("diagnosis = " + histryList.get(position - 1).medicalPrescribed);
                    previousText.setText(histryList.get(position - 1).medicalPrescribed);
                } else {
                    previousText.setText("None");
                }
            }
            headingText.setText(histryList.get(position).medicalPrescribed);
        } else if (header.equalsIgnoreCase("Test Histry")) {
            System.out.println("Symptom History:::::::::::" + histryList.size());
            if (histryList.size() >= 2) {
                if ((position - 1) >= 0) {
                    System.out.println("diagnosis = " + histryList.get(position - 1).testPrescribed);
                    previousText.setText(histryList.get(position - 1).testPrescribed);
                } else {
                    previousText.setText("None");
                }

            }
            headingText.setText(histryList.get(position).testPrescribed);
        }

        user.setText(histryList.get(position).type);
        date.setText(histryList.get(position).curDate);

        return convertView;

    }
}
