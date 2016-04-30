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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import Adapter.HistryAdapter;
import Adapter.MedicineAdapter;
import Application.MyApi;
import Model.AddDependent;
import Model.AddDependentElement;
import Model.Histry;
import Model.MedicinePrescribed;
import Model.Patient;
import Model.SummaryHistoryVM;
import Model.TestPrescribed;
import Model.VisitEditLogResponse;
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
    public static int State=0;

    public VisitEditLogResponse  summaryHistoryVMs = new VisitEditLogResponse();

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

        if (State == 1) {
            if(summaryHistoryVMs!=null && summaryHistoryVMs.getSymptomLogs()!=null  ){
                String[] arr=null;
                try {
                      arr = summaryHistoryVMs.getSymptomLogs().split("\r?\n");
                }catch(Exception e){

                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                listView.setAdapter(arrayAdapter);

            }else{
                String[] arr= new String[]{"No Symptom Data"};
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                listView.setAdapter(arrayAdapter);
            }
        }
        if (State == 2) {
            if(summaryHistoryVMs!=null && summaryHistoryVMs.getDiagnosisLogs()!=null ){
                String[] arr=null;
                try{
                    arr=summaryHistoryVMs.getSymptomLogs().split("\r?\n");
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                    listView.setAdapter(arrayAdapter);
                }catch(Exception e){

                }





            }else{
                String[] arr= new String[]{"No Dignosis Data"};
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                listView.setAdapter(arrayAdapter);
            }

        }
        if (State == 3) {

            if(summaryHistoryVMs!=null && summaryHistoryVMs.getMedicineLogs()!=null && summaryHistoryVMs.getMedicineLogs().size() != 0){
               // String[] arr= ((String[])(summaryHistoryVMs.getMedicineLogs().toArray()));
                String[] stringArray = Arrays.copyOf(summaryHistoryVMs.getMedicineLogs().toArray(), summaryHistoryVMs.getMedicineLogs().toArray().length, String[].class);

                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,stringArray);
                listView.setAdapter(arrayAdapter);

            }else{
                String[] arr= new String[]{"No Medicine Data"};
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                listView.setAdapter(arrayAdapter);
            }

        }
        if (State == 4) {
            if(summaryHistoryVMs!=null && summaryHistoryVMs.getTestLogs()!=null && summaryHistoryVMs.getTestLogs().size() != 0){
              //  String[] arr= ((String[])(summaryHistoryVMs.getTestLogs().toArray()));
                String[] stringArray = Arrays.copyOf(summaryHistoryVMs.getTestLogs().toArray(), summaryHistoryVMs.getTestLogs().toArray().length, String[].class);


                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,stringArray);
                listView.setAdapter(arrayAdapter);

            }else{
                String[] arr= new String[]{"No test Data"};
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                listView.setAdapter(arrayAdapter);
            }
        }
        //HistryAdapter adapter =  new HistryAdapter(getActivity(),summaryHistoryVMs,heading);
        //listView.setAdapter(adapter);

        //System.out.println("histryListData = " + histryListData.size());

        return view;
    }
}
