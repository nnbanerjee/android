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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.DoctorMenusManage;
import com.mindnerves.meidcaldiary.Global;
import com.medico.view.HomeActivity;
import com.mindnerves.meidcaldiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapter.ManageFinanceAdapter;
import com.medico.application.MyApi;
import Model.AllProcedureVm;
import Model.AllTemplateVm;
import Model.AllTreatmentPlanVm;
import Model.Field;
import Model.ManageFinance;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class AllManageFinance extends Fragment {

    public MyApi api;
    SharedPreferences session;
    StickyListHeadersListView allAppointments;
    ProgressDialog progress;
    TextView globalTv,selectMode,accountName;
    Global global;
    String selectModeValue;
    boolean checkMonth,checkWeek,checkDay;
    List<ManageFinance> manageFinancesList, monthFinanceList,weekFinanceList;
    Button drawar,logout,back,dayBtn,weekBtn,monthBtn;
    ImageView profilePicture;//,medicoLogo,medicoText;
    RelativeLayout profileLayout;
    String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_manage_finance,container,false);
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));

        allAppointments = (StickyListHeadersListView) view.findViewById(R.id.allAppointments);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        global = (Global) getActivity().getApplicationContext();
        back = (Button)getActivity().findViewById(R.id.back_button);
        selectMode = (TextView) view.findViewById(R.id.selectMode);

        profilePicture = (ImageView) getActivity().findViewById(R.id.profile_picture);
        accountName = (TextView) getActivity().findViewById(R.id.account_name);
        logout = (Button)getActivity().findViewById(R.id.logout);
       // medicoLogo = (ImageView)getActivity().findViewById(R.id.global_medico_logo);
      //  medicoText = (ImageView)getActivity().findViewById(R.id.home_icon);
        profileLayout = (RelativeLayout)getActivity().findViewById(R.id.home_layout2);
        drawar = (Button)getActivity().findViewById(R.id.drawar_button);
        type = session.getString("type",null);
        dayBtn = (Button) view.findViewById(R.id.dayBtn);
        weekBtn = (Button) view.findViewById(R.id.weekBtn);
        monthBtn = (Button) view.findViewById(R.id.monthBtn);

        selectMode.setText("day");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBack();

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logout.setBackgroundResource(R.drawable.logout);
                getActivity().finish();
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });

        globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);


        //Retrofit Initialization
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        showDoctorManageFinanceList();

        dayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkDay) {
                    checkMonth = false;
                    checkWeek = false;
                    checkDay = true;

                    dayBtn.setTextColor(Color.parseColor("#ffffff"));
                    weekBtn.setTextColor(Color.parseColor("#000000"));
                    monthBtn.setTextColor(Color.parseColor("#000000"));

                    dayBtn.setBackgroundResource(R.drawable.square_blue_color);
                    weekBtn.setBackgroundResource(R.drawable.square_grey_color);
                    monthBtn.setBackgroundResource(R.drawable.square_grey_color);
                    selectMode.setText("day");
                    selectModeValue = "day";
                    setDayData();
                }
            }
        });

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkWeek) {
                    checkMonth = false;
                    checkWeek = true;
                    checkDay = false;

                    dayBtn.setTextColor(Color.parseColor("#000000"));
                    weekBtn.setTextColor(Color.parseColor("#ffffff"));
                    monthBtn.setTextColor(Color.parseColor("#000000"));

                    dayBtn.setBackgroundResource(R.drawable.square_grey_color);
                    weekBtn.setBackgroundResource(R.drawable.square_blue_color);
                    monthBtn.setBackgroundResource(R.drawable.square_grey_color);
                    selectMode.setText("week");
                    selectModeValue = "week";
                    setWeekData();
                }
            }
        });

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkMonth){
                    checkMonth = true;
                    checkWeek = false;
                    checkDay = false;

                    dayBtn.setTextColor(Color.parseColor("#000000"));
                    weekBtn.setTextColor(Color.parseColor("#000000"));
                    monthBtn.setTextColor(Color.parseColor("#ffffff"));

                    dayBtn.setBackgroundResource(R.drawable.square_grey_color);
                    weekBtn.setBackgroundResource(R.drawable.square_grey_color);
                    monthBtn.setBackgroundResource(R.drawable.square_blue_color);
                    selectMode.setText("month");
                    selectModeValue = "month";
                    setMonthData();
                }
            }
        });

        allAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView date1 = (TextView) view.findViewById(R.id.date1);
                Fragment fragment = new AllReportManageFinance();

                System.out.println("selectMode.getText().toString() = "+selectMode.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("Date", date1.getText().toString());
                bundle.putString("mode", selectMode.getText().toString());
                bundle.putString("modeValue", selectModeValue);
                fragment.setArguments(bundle);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
            }
        });
        manageScreenIcons();
        return view;
    }
    public void manageScreenIcons(){
        globalTv.setText("Manage Finance");
        drawar.setVisibility(View.GONE);
      //  medicoLogo.setVisibility(View.GONE);
       // medicoText.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
        logout.setBackgroundResource(R.drawable.home_jump);
        profileLayout.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }
    public void setDayData(){
        showDoctorManageFinanceList();
    }

    public void setWeekData(){

        weekFinanceList = new ArrayList<ManageFinance>();
        selectModeValue = "";
        if(manageFinancesList.size() == 0){
            Toast.makeText(getActivity(), "No Data Found ....", Toast.LENGTH_SHORT).show();
        }else{
            for(ManageFinance manageFinance : manageFinancesList){
                boolean checkMonth = false;

                if(weekFinanceList.size() != 0){
                    for(ManageFinance weekLeft : weekFinanceList){

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                        Date date1 = null;
                        Date date2 = null;

                        try {
                            date1 = formatter.parse(weekLeft.getAppointmentDate());
                            date2 = formatter.parse(manageFinance.getAppointmentDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        selectModeValue = manageFinance.getAppointmentDate();
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(date1);
                        cal2.setTime(date2);
                        System.out.println("week data cal1  = "+cal1.getTime()+" cal2 = "+cal2.getTime());
                        for(int i = 0; i < 7; i++){
                            if(cal1.compareTo(cal2) == 0){
                                checkMonth = true;
                                //System.out.println("week data  repeate = "+weekLeft.getAppointmentDate());
                                Integer count = weekLeft.getTotalCount() + manageFinance.getTotalCount();
                                String currency = weekLeft.getTotalInvoice().substring(weekLeft.getTotalInvoice().length() - 3, weekLeft.getTotalInvoice().length());
                                String number1Only = weekLeft.getTotalInvoice().replaceAll("[^0-9]", "");
                                String number2Only = manageFinance.getTotalInvoice().replaceAll("[^0-9]", "");
                                int total = Integer.parseInt(number1Only) + Integer.parseInt(number2Only);
                                weekLeft.setTotalInvoice(""+total+" "+currency);
                                weekLeft.setTotalCount(count);
                                selectModeValue = selectModeValue +" , "+ weekLeft.getAppointmentDate();
                                weekLeft.setAppointmentDate(weekLeft.getAppointmentDate().substring(0,2)+" - "+manageFinance.getAppointmentDate());
                            }
                            cal2.add(Calendar.DAY_OF_WEEK, 1);
                            i++;
                        }
                    }
                }

                if(!checkMonth){
                    System.out.println("week data ///////////////");
                    weekFinanceList.add(manageFinance);
                }
            }
            ManageFinanceAdapter adapter = new ManageFinanceAdapter(getActivity().getApplicationContext(), weekFinanceList,"week");
            allAppointments.setAdapter(adapter);
        }
    }

    public void setMonthData(){

        monthFinanceList = new ArrayList<ManageFinance>();

        if(manageFinancesList.size() == 0){
            Toast.makeText(getActivity(), "No Data Found ....", Toast.LENGTH_SHORT).show();
        }else{
            for(ManageFinance manageFinance : manageFinancesList){
                boolean checkMonth = false;

                if(monthFinanceList.size() != 0){
                    for(ManageFinance monthLeft : monthFinanceList){
                        String date1 = monthLeft.getAppointmentDate().substring(2,5);
                        String date2 = manageFinance.getAppointmentDate().substring(2,5);
                        if(date1.equals(date2)){
                            checkMonth = true;
                            Integer count = monthLeft.getTotalCount() + manageFinance.getTotalCount();
                            String currency = monthLeft.getTotalInvoice().substring(monthLeft.getTotalInvoice().length() - 3, monthLeft.getTotalInvoice().length());
                            String number1Only = monthLeft.getTotalInvoice().replaceAll("[^0-9]", "");
                            String number2Only = manageFinance.getTotalInvoice().replaceAll("[^0-9]", "");
                            int total = Integer.parseInt(number1Only) + Integer.parseInt(number2Only);
                            monthLeft.setTotalInvoice(""+total+" "+currency);
                            monthLeft.setTotalCount(count);
                        }
                    }
                }

                if(!checkMonth){
                    monthFinanceList.add(manageFinance);
                }
            }
            ManageFinanceAdapter adapter = new ManageFinanceAdapter(getActivity().getApplicationContext(), monthFinanceList,"month");
            allAppointments.setAdapter(adapter);
        }
    }

    public void showDoctorManageFinanceList(){
        String  doctorId = session.getString("sessionID",null);

        api.getAllDoctorFinance(doctorId, new Callback<List<AllTreatmentPlanVm>>() {
            @Override
            public void success(List<AllTreatmentPlanVm> allFinance, Response response) {

                global.setAllFinance(allFinance);

                Integer totalAmount = 0;
                Integer totalCount = 0;
                String currency = "";
                String appointmentDate = "";

                manageFinancesList = new ArrayList<ManageFinance>();

                for(AllTreatmentPlanVm  allTreatmentPlanVm : allFinance){
                    for( AllProcedureVm allProcedureVm : allTreatmentPlanVm.procedure){
                        for(AllTemplateVm allTemplateVm : allProcedureVm.allTemplate){
                            for(Field field : allTemplateVm.templates){
                                if(field.getFieldDisplayName().equals("Cost")){
                                    totalAmount = totalAmount + Integer.parseInt(field.getFieldDefaultValue());
                                }
                                if(field.getFieldDisplayName().equals("Currency")){
                                    currency = field.getFieldDefaultValue();
                                }
                            }
                        }
                        appointmentDate = allTreatmentPlanVm.patientAppointmentDate;
                        totalCount++;
                    }

                    boolean checkDate = false;
                    Integer num = 0;
                    for(ManageFinance manageFinance : manageFinancesList){

                        if(manageFinance.getAppointmentDate().equals(appointmentDate)){
                            checkDate = true;
                            String numberOnly = manageFinance.getTotalInvoice().replaceAll("[^0-9]", "");
                            num = Integer.parseInt(numberOnly) + totalAmount;
                            manageFinance.setTotalInvoice(""+num +" "+currency);
                            manageFinance.setTotalCount(totalCount);
                            totalCount++;
                        }
                    }

                    if(!checkDate){
                        manageFinancesList.add(new ManageFinance(appointmentDate, totalAmount+" "+currency, totalCount));
                    }
                    totalCount = 0;
                    totalAmount = 0;
                }

                ManageFinanceAdapter adapter = new ManageFinanceAdapter(getActivity().getApplicationContext(), manageFinancesList,"day");
                allAppointments.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
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
        globalTv.setText(type);
        Fragment fragment = new DoctorMenusManage();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        logout.setBackgroundResource(R.drawable.logout);
        logout.setVisibility(View.GONE);
        drawar.setVisibility(View.VISIBLE);
        profileLayout.setVisibility(View.VISIBLE);
        profilePicture.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
        back.setVisibility(View.GONE);
       // medicoLogo.setVisibility(View.VISIBLE);
       // medicoText.setVisibility(View.VISIBLE);
    }


}
