package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.HistryAdapter;
import Application.MyApi;
import Model.AddDependent;
import Model.AddDependentElement;
import Model.Histry;
import Model.Patient;
import Model.SummaryHistoryVM;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 8/11/15.
 */


public class ShowHistryDialog extends DialogFragment {

    public MyApi api;
    public String heading;
    TextView headingText;
    ListView listView;

    public List<SummaryHistoryVM>  summaryHistoryVMs = new ArrayList<SummaryHistoryVM>();

    public static ShowHistryDialog newInstance() {
        return new ShowHistryDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_histry,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        headingText = (TextView) view.findViewById(R.id.headingText);
        listView = (ListView) view.findViewById(R.id.listView);

        headingText.setText(heading);
        HistryAdapter adapter =  new HistryAdapter(getActivity(),summaryHistoryVMs,heading);
        listView.setAdapter(adapter);

        //System.out.println("histryListData = " + histryListData.size());

        return view;
    }
}
