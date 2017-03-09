package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.LinearLayout;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.view.ViewGroup.LayoutParams;

import Adapter.ReportFinanceAdapter;
import com.medico.application.MyApi;
import Model.AllProcedureVm;
import Model.AllTemplateVm;
import Model.AllTreatmentPlanVm;
import Model.Field;
import Model.ManageFinance;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AllReportManageFinance extends Fragment {

    public MyApi api;
    SharedPreferences session;
    ProgressDialog progress;
    TextView globalTv,timeText,totalAmountValue,
            totalDueValue,discountValue,taxValue,
            advanceValue,grandTotalValue;
    ListView financeDetailsList;
    Button drawar,logout,back,chartText,detailText;
    LinearLayout chartLayout;
    //int[] pieChartValues={25,15,20,40};
    List<ManageFinance> manageFinancesList;
    String date,mode,modeValue;
    Global global;
    List<AllTreatmentPlanVm> allFinance;
    public static final String TYPE = "type";
    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_report_manage_finance,container,false);

        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        chartLayout = (LinearLayout) view.findViewById(R.id.chartLayout);
        back = (Button)getActivity().findViewById(R.id.back_button);
        chartText = (Button)view.findViewById(R.id.chartText);
        timeText = (TextView) view.findViewById(R.id.timeText);
        detailText = (Button)view.findViewById(R.id.detailText);
        totalDueValue = (TextView) view.findViewById(R.id.totalDueValue);

        discountValue = (TextView) view.findViewById(R.id.discountValue);
        taxValue = (TextView) view.findViewById(R.id.taxValue);
        advanceValue = (TextView) view.findViewById(R.id.advanceValue);

        totalAmountValue = (TextView) view.findViewById(R.id.totalAmountValue);
        grandTotalValue = (TextView) view.findViewById(R.id.grandTotalValue);
        financeDetailsList = (ListView) view.findViewById(R.id.financeDetailsList);
        global = (Global) getActivity().getApplicationContext();
        back.setVisibility(View.VISIBLE);
        mode = "day";
        Bundle args = getArguments();
        if (args != null) {
           date = args.getString("Date");
           mode = args.getString("mode");
           modeValue = args.getString("modeValue");
        }

        timeText.setText(date);

        allFinance = new ArrayList<AllTreatmentPlanVm>();
        allFinance = global.getAllFinance();
        manageFinancesList = new ArrayList<ManageFinance>();

        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        drawar.setVisibility(View.GONE);

        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.home_jump);

        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Manage Finance");

        if(mode.equals("day")){
            setDayValue();
            System.out.println("day //////////////");
        }else if(mode.equals("month")){
            setMonthValue();
            System.out.println("month //////////////");
        }else if(mode.equals("week")){
            setWeekValue();
            System.out.println("week //////////////");
        }

        ReportFinanceAdapter adapter = new ReportFinanceAdapter(getActivity().getApplicationContext(), manageFinancesList);
        financeDetailsList.setAdapter(adapter);

        chartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartText.setTextColor(Color.parseColor("#ffffff"));
                chartText.setBackgroundResource(R.drawable.square_blue_color);
                detailText.setTextColor(Color.parseColor("#000000"));
                detailText.setBackgroundResource(R.drawable.square_grey_color);

                financeDetailsList.setVisibility(View.GONE);
                chartLayout.setVisibility(View.VISIBLE);

            }
        });

        detailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailText.setTextColor(Color.parseColor("#ffffff"));
                detailText.setBackgroundResource(R.drawable.square_blue_color);
                chartText.setTextColor(Color.parseColor("#000000"));
                chartText.setBackgroundResource(R.drawable.square_grey_color);

                financeDetailsList.setVisibility(View.VISIBLE);
                chartLayout.setVisibility(View.GONE);
            }
        });

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(18);
        mRenderer.setLegendTextSize(18);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        if (mChartView == null) {
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            chartLayout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }

        fillPieChart();

        return view;
    }

    public void setWeekValue(){

        System.out.println("selectModeValue ////////// = "+modeValue);
        String treatmentName = "";
        String cost = "";
        String currency = "";

        double total = 0;
        double totalGrand = 0;
        double totalDue = 0;
        double discount = 0;
        double tax = 0;
        double advance = 0;

        String dates[] = modeValue.split(",");

        for(int i = 0; i <= dates.length-1; i++){

            for(AllTreatmentPlanVm allTreatmentPlanVm : allFinance){
                if(dates[i].trim().equals(allTreatmentPlanVm.patientAppointmentDate)){

                    for( AllProcedureVm allProcedureVm : allTreatmentPlanVm.procedure){
                        for(AllTemplateVm allTemplateVm : allProcedureVm.allTemplate){
                            treatmentName = allTemplateVm.templateName;
                            for(Field field : allTemplateVm.templates){
                                if(field.getFieldDisplayName().equals("Cost")){
                                    cost = field.getFieldDefaultValue();
                                }
                                if(field.getFieldDisplayName().equals("Currency")){
                                    currency = field.getFieldDefaultValue();
                                }
                            }
                            manageFinancesList.add(new ManageFinance(treatmentName,cost+" "+currency,1));
                        }
                    }

                    discount = discount + Double.parseDouble(allTreatmentPlanVm.discount);
                    tax = tax + Double.parseDouble(allTreatmentPlanVm.tax);
                    advance = advance + Double.parseDouble(allTreatmentPlanVm.advance);
                    totalGrand = totalGrand + Double.parseDouble(allTreatmentPlanVm.grandTotal);
                    total = total + Double.parseDouble(allTreatmentPlanVm.total);
                    //totalGrand = totalGrand + Double.parseDouble(allTreatmentPlanVm.grandTotal);
                    //totalDue = totalDue + Double.parseDouble(allTreatmentPlanVm.totalDue);
                    String number1Only = String.valueOf(totalGrand).replaceAll("[^0-9]", "");
                    String number2Only = allTreatmentPlanVm.grandTotal.replaceAll("[^0-9]", "");
                    totalGrand = (Integer.parseInt(number1Only) + Integer.parseInt(number2Only));

                    number1Only = String.valueOf(total).replaceAll("[^0-9]", "");
                    number2Only = allTreatmentPlanVm.total.replaceAll("[^0-9]", "");
                    total = (Integer.parseInt(number1Only) + Integer.parseInt(number2Only));
                }
            }
        }

        totalDueValue.setText(totalDue+" "+currency);
        totalAmountValue.setText(total +" "+currency);
        grandTotalValue.setText(totalGrand +" "+currency);
        discountValue.setText(discount +" "+currency);
        taxValue.setText(tax +" "+currency);
        advanceValue.setText(advance +" "+currency);
    }

    public void setMonthValue(){

        double totalGrand = 0;
        double totalDue = 0;
        String currency = "";
        double discount = 0;
        double tax = 0;
        double advance = 0;
        double total = 0;

        for(AllTreatmentPlanVm allTreatmentPlanVm : allFinance){

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 = null;
            Date date2 = null;

            try {
                date1 = formatter.parse(date);
                date2 = formatter.parse(allTreatmentPlanVm.patientAppointmentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);

            if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
                String treatmentName = "";
                String cost = "";

                for( AllProcedureVm allProcedureVm : allTreatmentPlanVm.procedure){

                    for(AllTemplateVm allTemplateVm : allProcedureVm.allTemplate){
                        treatmentName = allTemplateVm.templateName;
                        for(Field field : allTemplateVm.templates){
                            if(field.getFieldDisplayName().equals("Cost")){
                                cost = field.getFieldDefaultValue();
                            }
                            if(field.getFieldDisplayName().equals("Currency")){
                                currency = field.getFieldDefaultValue();
                            }
                        }

                        boolean financecheck = false;
                        for(ManageFinance manageFinance : manageFinancesList){
                            if(manageFinance.getAppointmentDate().equals(treatmentName)){
                                financecheck = true;

                                for(Field field : allTemplateVm.templates){
                                    if(field.getFieldDisplayName().equals("Cost")){
                                        cost = field.getFieldDefaultValue();
                                    }
                                }

                                String number1Only = cost.replaceAll("[^0-9]", "");
                                String number2Only = manageFinance.getTotalInvoice().replaceAll("[^0-9]", "");
                                cost = "" +(Integer.parseInt(number1Only) + Integer.parseInt(number2Only));
                                manageFinance.setTotalInvoice(cost+" "+currency);
                            }
                        }

                        if(!financecheck){
                            manageFinancesList.add(new ManageFinance(treatmentName,cost+" "+currency,1));
                        }
                    }
                }

                /*discount = discount + Double.parseDouble(allTreatmentPlanVm.discount);
                tax = tax + Double.parseDouble(allTreatmentPlanVm.tax);
                advance = advance + Double.parseDouble(allTreatmentPlanVm.advance);*/
                if(!allTreatmentPlanVm.discount.equals("")){
                    discount = discount + Double.parseDouble(allTreatmentPlanVm.discount);
                }
                if(!allTreatmentPlanVm.tax.equals("")){
                    tax = tax + Double.parseDouble(allTreatmentPlanVm.tax);
                }
                if(!allTreatmentPlanVm.advance.equals("")){
                    advance = advance + Double.parseDouble(allTreatmentPlanVm.advance);
                }

                String number1Only = String.valueOf(totalGrand).replaceAll("[^0-9]", "");
                String number2Only = allTreatmentPlanVm.grandTotal.replaceAll("[^0-9]", "");
                totalGrand = (Integer.parseInt(number1Only) + Integer.parseInt(number2Only));

                number1Only = String.valueOf(total).replaceAll("[^0-9]", "");
                number2Only = allTreatmentPlanVm.total.replaceAll("[^0-9]", "");
                total = (Integer.parseInt(number1Only) + Integer.parseInt(number2Only));

                //totalGrand = totalGrand + Double.parseDouble(allTreatmentPlanVm.grandTotal);
                //totalDue = totalDue + Double.parseDouble(allTreatmentPlanVm.totalDue);
            }
        }

        totalDueValue.setText(totalDue+" "+currency);
        totalAmountValue.setText(total +" "+currency);
        grandTotalValue.setText(totalGrand +" "+currency);
        discountValue.setText(discount +" "+currency);
        taxValue.setText(tax +" "+currency);
        advanceValue.setText(advance +" "+currency);
    }

    public void setDayValue(){

        double totalGrand = 0;
        double totalDue = 0;
        String currency = "";
        double discount = 0;
        double tax = 0;
        double advance = 0;

        for(AllTreatmentPlanVm allTreatmentPlanVm : allFinance){

            if(allTreatmentPlanVm.patientAppointmentDate.equals(date)){

                String treatmentName = "";
                String cost = "";

                for( AllProcedureVm allProcedureVm : allTreatmentPlanVm.procedure){

                    for(AllTemplateVm allTemplateVm : allProcedureVm.allTemplate){
                        treatmentName = allTemplateVm.templateName;
                        for(Field field : allTemplateVm.templates){
                            if(field.getFieldDisplayName().equals("Cost")){
                                cost = field.getFieldDefaultValue();
                            }
                            if(field.getFieldDisplayName().equals("Currency")){
                                currency = field.getFieldDefaultValue();
                            }
                        }
                        manageFinancesList.add(new ManageFinance(treatmentName,cost+" "+currency,1));
                    }
                }
                totalDueValue.setText(allTreatmentPlanVm.totalDue +" "+currency);
                totalAmountValue.setText(allTreatmentPlanVm.total);
                grandTotalValue.setText(allTreatmentPlanVm.grandTotal+" "+currency);
            }

            if(!allTreatmentPlanVm.discount.equals("")){
                discount = discount + Double.parseDouble(allTreatmentPlanVm.discount);
            }
            if(!allTreatmentPlanVm.tax.equals("")){
                tax = tax + Double.parseDouble(allTreatmentPlanVm.tax);
            }
            if(!allTreatmentPlanVm.advance.equals("")){
                advance = advance + Double.parseDouble(allTreatmentPlanVm.advance);
            }

            System.out.println("allTreatmentPlanVm.total = "+allTreatmentPlanVm.total);
        }

        discountValue.setText(discount +" "+currency);
        taxValue.setText(tax +" "+currency);
        advanceValue.setText(advance +" "+currency);
    }

    public void fillPieChart(){
        for(int i=0; i<manageFinancesList.size(); i++){

            String numberOnly = manageFinancesList.get(i).getTotalInvoice().replaceAll("[^0-9]", "");
            int num = Integer.parseInt(numberOnly);

            mSeries.add(manageFinancesList.get(i).getAppointmentDate()+" " + (mSeries.getItemCount() + 1), num);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }

    public void goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Medical Diary");

        Fragment fragment = new AllManageFinance();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        final ImageView profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        final TextView accountName = (TextView) getActivity().findViewById(R.id.account_name);
        final RelativeLayout profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        logout = (Button)getActivity().findViewById(R.id.logout);
        logout.setBackgroundResource(R.drawable.logout);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
    }


}
