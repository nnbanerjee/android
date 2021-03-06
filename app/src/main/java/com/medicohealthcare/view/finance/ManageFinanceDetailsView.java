package com.medicohealthcare.view.finance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.medicohealthcare.adapter.FinanceProcedureListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.FinanceDetails;
import com.medicohealthcare.model.FinanceReportRequest;
import com.medicohealthcare.util.MedicoCustomErrorHandler;
import com.medicohealthcare.view.home.ParentFragment;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ManageFinanceDetailsView extends ParentFragment {


    CheckBox shareWithPatient;
    EditText discountValue,taxValue,advanceValue,grandTotal,
            discountPercent,taxPercent,totalDueValue;
    TextView noDataFound,invoiceTotal;
    ListView procedure_name;
    FinanceDetails invoiceDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.doctor_finance_details_report, container,false);
        procedure_name = (ListView) view.findViewById(R.id.procedure_name);
        invoiceTotal = (TextView) view.findViewById(R.id.invoiceTotal);
        discountValue = (EditText) view.findViewById(R.id.discountValue);
        discountPercent = (EditText)view.findViewById(R.id.percentageDiscount);
        taxPercent = (EditText)view.findViewById(R.id.percentageTax);
        taxValue = (EditText) view.findViewById(R.id.taxValue);
        advanceValue = (EditText) view.findViewById(R.id.advanceValue);
        totalDueValue = (EditText) view.findViewById(R.id.totalDueValue);
        grandTotal = (EditText)view.findViewById(R.id.grandTotal);
        return view;
    }



    public static Double isNumeric(String str){
        Double d = null;
        try{
            d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return null;
        }
        return d;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer appointMentId = bundle.getInt(APPOINTMENT_ID);
        final Integer loggedInUserId = bundle.getInt(LOGGED_IN_ID);
        final Integer invoiceId = bundle.getInt(INVOICE_ID);
        final Long date = bundle.getLong("report_date");
        final Integer type = bundle.getInt("report_type");

        showBusy();
        FinanceReportRequest doc = new FinanceReportRequest(bundle.getInt(DOCTOR_ID),date, type);
        api.getFinanceSummaryDetails(doc, new Callback<List<FinanceDetails>>() {
            @Override
            public void success(List<FinanceDetails> details, Response response) {
                if(details != null && details.isEmpty() == false)
                {
                    invoiceDetails = details.get(0);

                    if (invoiceDetails != null)
                    {
                        Currency currency = Currency.getInstance(getDefaultCountry().getCurrencyCode());
                        procedure_name.setAdapter(new FinanceProcedureListAdapter(getActivity(), invoiceDetails.procedureSummary,currency, type));
                        double total = 0.0;
                        for (FinanceDetails.ProcedureSummary summary : invoiceDetails.procedureSummary)
                        {
                            total = total + summary.totalCost.doubleValue();
                        }
                        NumberFormat nf = NumberFormat.getCurrencyInstance();
                        nf.setMaximumFractionDigits(2);
                        nf.setCurrency(currency);
                        invoiceTotal.setText(nf.format(total));
                        discountPercent.setText(nf.format(invoiceDetails.discount));
                        taxPercent.setText(nf.format(invoiceDetails.tax));
                        double grand = total * (1 - invoiceDetails.discount / 100) * (1 + invoiceDetails.tax / 100);
                        grandTotal.setText(nf.format(grand));
                        advanceValue.setText(nf.format(invoiceDetails.totalAdvance));
                        totalDueValue.setText(nf.format(grand - invoiceDetails.totalAdvance));
                    }
                }

                hideBusy();
            }

            @Override
            public void failure(RetrofitError error) {
                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });

    }


}
