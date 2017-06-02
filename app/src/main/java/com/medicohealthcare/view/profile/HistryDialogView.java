package com.medicohealthcare.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.medicohealthcare.adapter.HistoryAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.VisitEditLogRequest;
import com.medicohealthcare.model.VisitEditLogResponse;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 8/11/15.
 */


public class HistryDialogView extends ParentFragment
{

    ListView listView;
    public static int State=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_view,container,false);
        listView = (ListView) view.findViewById(R.id.doctorListView);

        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        final TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        final Bundle bundle = getActivity().getIntent().getExtras();
        final int historyType = bundle.getInt("history_type");
        final int appointmentId = bundle.getInt(APPOINTMENT_ID);
        VisitEditLogRequest req= new VisitEditLogRequest(appointmentId,historyType);
        api.getPatientVisitEditLog(req, new Callback<VisitEditLogResponse>() {
            @Override
            public void success(VisitEditLogResponse summaryHistoryVMs, Response response)
            {
                switch(historyType)
                {
                    case 1:
                    {
                        String history = summaryHistoryVMs.getSymptomLogs();
                        textviewTitle.setText("Symptoms History");
                        if(history != null && history.length() > 0)
                        {
                            List<String> histories = new ArrayList<>();
                            String[] tokens = history.trim().split("<-->");
                            for (int i = 0; i < tokens.length; i++)
                                histories.add(tokens[i]);
                            HistoryAdapter arrayAdapter = new HistoryAdapter(getActivity(), histories);
                            listView.setAdapter(arrayAdapter);
                        }
                        else
                        {
                            String[] arr= new String[]{"No Change History"};
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                            listView.setAdapter(arrayAdapter);
                        }
                        break;
                    }
                    case 2:
                    {
                        String history = summaryHistoryVMs.getDiagnosisLogs();
                        textviewTitle.setText("Diagnosis History");
                        if(history != null && history.length() > 0)
                        {
                            List<String> histories = new ArrayList<>();
                            String[] tokens = history.trim().split("<-->");
                            for (int i = 0; i < tokens.length; i++)
                                histories.add(tokens[i]);
                            HistoryAdapter arrayAdapter = new HistoryAdapter(getActivity(), histories);
                            listView.setAdapter(arrayAdapter);
                        }
                        else
                        {
                            String[] arr= new String[]{"No Change History"};
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                            listView.setAdapter(arrayAdapter);
                        }
                        break;
                    }
                    case 3:
                    {
                        List<String> history = summaryHistoryVMs.getMedicineLogs();
                        textviewTitle.setText("Medicine History");
                        if(history != null && history.size() > 0)
                        {
                            List<String> histories = new ArrayList<String>();
                            for(String medicineHistory : history)
                            {
                                String[] medicineHist = medicineHistory.trim().split("<-->");
                                if(medicineHist != null && medicineHist.length > 0)
                                {
                                    for (int i = 0; i < medicineHist.length; i++)
                                    {
                                        if(medicineHist[i] != null && medicineHist[i].length() > 0)
                                            histories.add(medicineHist[i]);
                                    }
                                }
                            }
                            HistoryAdapter arrayAdapter = new HistoryAdapter(getActivity(), histories);
                            listView.setAdapter(arrayAdapter);
                        }
                        else
                        {
                            String[] arr= new String[]{"No Change History"};
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                            listView.setAdapter(arrayAdapter);
                        }
                        break;
                    }
                    case 4:
                    {
                        List<String> history = summaryHistoryVMs.getTestLogs();
                        textviewTitle.setText("Diagnostic Test History");
                        if(history != null && history.size() > 0)
                        {
                            List<String> histories = new ArrayList<String>();
                            for(String medicineHistory : history)
                            {
                                String[] medicineHist = medicineHistory.trim().split("<-->");
                                if(medicineHist != null && medicineHist.length > 0)
                                {
                                    for (int i = 0; i < medicineHist.length; i++)
                                    {
                                        if(medicineHist[i] != null && medicineHist[i].length() > 0)
                                            histories.add(medicineHist[i]);
                                    }
                                }
                            }
                            HistoryAdapter arrayAdapter = new HistoryAdapter(getActivity(), histories);
                            listView.setAdapter(arrayAdapter);
                        }
                        else
                        {
                            String[] arr= new String[]{"No Change History"};
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.single_item,arr);
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });


    }
}
