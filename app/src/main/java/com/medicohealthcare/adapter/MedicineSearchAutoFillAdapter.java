package com.medicohealthcare.adapter;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medicohealthcare.model.Medicine;

import java.util.List;

/**
 * Created by Narendra on 07-02-2017.
 */

public class MedicineSearchAutoFillAdapter extends BaseAdapter
{
    List<Medicine> medicineList;
    Fragment fragment;
    public MedicineSearchAutoFillAdapter(Fragment fragment, List<Medicine> medicineList)
    {
        this.medicineList = medicineList;
        this.fragment = fragment;
    }
    @Override
    public int getCount() {
        return medicineList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {

        TextView textView = new TextView(fragment.getActivity());
        textView.setText(medicineList.get(position).toString());
        return textView;

    }
}
