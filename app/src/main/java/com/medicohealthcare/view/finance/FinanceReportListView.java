package com.medicohealthcare.view.finance;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medicohealthcare.adapter.FinanceSummaryListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.FinanceReportRequest;
import com.medicohealthcare.model.FinanceSummary;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import java.util.Currency;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

//
//import Utils.UtilSingleInstance;

//import com.mindnerves.meidcaldiary.Global;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class FinanceReportListView extends ParentFragment
{
    public static int NONE = 0;
    public static int DAILY = 1;
    public static int WEEKLY = 2;
    public static int MONTHLY = 3;
    Button daily,weekly,monthly;
    StickyListHeadersListView reportDates;

    int type = DAILY;

    List<FinanceSummary> model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.finance_report_list, container, false);
        final Bundle bun = getArguments();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Manage Finance");
        daily = (Button)view.findViewById(R.id.daily);
        weekly = (Button)view.findViewById(R.id.weekly);
        monthly = (Button)view.findViewById(R.id.monthly);
        reportDates = (StickyListHeadersListView)view.findViewById(R.id.report_dates);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                daily.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                weekly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                monthly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                loadFinanceReports(DAILY);
                type = DAILY;
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                daily.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                weekly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                monthly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                loadFinanceReports(WEEKLY);
                type = WEEKLY;
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                daily.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                weekly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_default));
                monthly.setBackground(getActivity().getResources().getDrawable(R.drawable.page_selected));
                loadFinanceReports(MONTHLY);
                type = MONTHLY;
            }
        });

        reportDates.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Activity activity = getActivity();
                Bundle bundle = activity.getIntent().getExtras();
                FinanceSummary summary = (FinanceSummary)reportDates.getItemAtPosition(position);
                bundle.putLong("report_date",summary.date);
                bundle.putInt("report_type", type);
                activity.getIntent().putExtras(bundle);
                ParentFragment fragment = new ManageFinanceDetailsView();
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.add(R.id.service, fragment,ManageFinanceDetailsView.class.getName()).addToBackStack(ManageFinanceDetailsView.class.getName()).commit();

            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    goToBack();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onStart()// getAllPatientAppointment()
     {
        super.onStart();
        daily.callOnClick();
     }
    public void loadFinanceReports(final int type)
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        FinanceReportRequest doc = new FinanceReportRequest(bundle.getInt(DOCTOR_ID), type);
        showBusy();
        api.getFinanceSummary(doc, new Callback<List<FinanceSummary>>()
        {
            @Override
            public void success(List<FinanceSummary> reports, Response response)
            {
                hideBusy();

                if (reports != null && !reports.isEmpty())
                {
                    Currency currency = Currency.getInstance(getDefaultCountry().getCurrencyCode());
                    model = reports;
                    reportDates.setAdapter(new FinanceSummaryListAdapter(getActivity(),model,currency,type));
                }
                else
                {
                    Toast.makeText(getActivity(), "No Reports found!", Toast.LENGTH_LONG).show();
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
