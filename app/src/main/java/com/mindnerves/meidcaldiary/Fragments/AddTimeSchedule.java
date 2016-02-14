package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Application.MyApi;
import Model.DoctorClinicSchedule;
import Model.Schedule;
import Model.TimeNew;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 26-Feb-15.
 */
public class AddTimeSchedule extends Fragment {

    private Spinner frmhr1,frmmn1,tohr1,tomn1,fram1,toam1,autoConfirmSpinner,availabilitySpinner;
    private Button selectAll;
    private CheckBox monday,tuesday,wednesday,thrustday,friday,saturday,sunday;
    private String from1,to1;
    private ArrayList<Schedule> arrayShift1,totalArrayShift;
    private MyApi api;
    Global go;
	TextView visitTime;
	String doctorId;
	String clinicId;
	SharedPreferences session;
	EditText slotNameTv,numberOfPatientsTv,slotNumberTv;
	String autoConfirmString,availabilityString,daysOfWeek,numberOfPatients,slotNumber,slotName,slotType,startTime,endTime,visitDuration;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    Fragment frag2 = new AddNewClinic();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2,null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getActionBar().hide();
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Add Time Schedule");
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        go = (Global)getActivity().getApplicationContext();
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag2 = new AddNewClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2,null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        View view = inflater.inflate(R.layout.time_selected,container,false);
        monday = (CheckBox) view.findViewById(R.id.monday);
        tuesday = (CheckBox) view.findViewById(R.id.tuesday);
        wednesday = (CheckBox) view.findViewById(R.id.wednesday);
        thrustday = (CheckBox) view.findViewById(R.id.thursday);
        friday = (CheckBox) view.findViewById(R.id.friday);
        saturday = (CheckBox) view.findViewById(R.id.saturday);
        sunday = (CheckBox) view.findViewById(R.id.sunday);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
		Bundle bun = getArguments();
		if(bun != null){
			if(bun.getString("clinicId") != null){
				clinicId = bun.getString("clinicId");
			}
		}
		session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		doctorId = session.getString("id", null);
        frmhr1 =(Spinner) view.findViewById(R.id.hourfrom);
        frmmn1 =(Spinner) view.findViewById(R.id.minutefrom);
        fram1 =(Spinner) view.findViewById(R.id.ampmfrom);
		autoConfirmSpinner = (Spinner)view.findViewById(R.id.auto_confirm_spinner);
		availabilitySpinner = (Spinner)view.findViewById(R.id.availability_spinner);
		visitTime = (TextView)view.findViewById(R.id.visit_minute);
		slotNameTv = (EditText)view.findViewById(R.id.slot_name);
		numberOfPatientsTv = (EditText)view.findViewById(R.id.number_patients);
		slotNumberTv = (EditText)view.findViewById(R.id.slot_number);
        tohr1 =(Spinner) view.findViewById(R.id.hourto);
        tomn1 =(Spinner) view.findViewById(R.id.minuteto);
        toam1 =(Spinner) view.findViewById(R.id.ampmto);
        ArrayAdapter<CharSequence> ampmtoAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.am_pm, android.R.layout.simple_spinner_item);
        ampmtoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> hourad = ArrayAdapter.createFromResource(getActivity(),
                R.array.hour_list, android.R.layout.simple_spinner_item);
        hourad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> minad = ArrayAdapter.createFromResource(getActivity(),
                R.array.minute_list, android.R.layout.simple_spinner_item);
        minad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final ArrayAdapter<CharSequence> autoConfirm = ArrayAdapter.createFromResource(getActivity(),
				R.array.online_appointment, android.R.layout.simple_spinner_item);
		minad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final ArrayAdapter<CharSequence> availability = ArrayAdapter.createFromResource(getActivity(),
				R.array.doctor_status_list, android.R.layout.simple_spinner_item);
		minad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frmhr1.setAdapter(hourad);
        tohr1.setAdapter(hourad);
        frmmn1.setAdapter(minad);
        tomn1.setAdapter(minad);
        fram1.setAdapter(ampmtoAdapter);
        toam1.setAdapter(ampmtoAdapter);
		autoConfirmSpinner.setAdapter(autoConfirm);
		availabilitySpinner.setAdapter(availability);
		numberOfPatientsTv.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				try {


					from1 = frmhr1.getSelectedItem().toString();
					from1 = from1 + ":" + frmmn1.getSelectedItem().toString();
					from1 = from1 + " " + fram1.getSelectedItem().toString();
					System.out.println("From1 = "+from1);

					int calculateMinutes = 0;
					to1 = tohr1.getSelectedItem().toString();
					to1 = to1 + ":" + tomn1.getSelectedItem().toString();
					to1 = to1 + " " + toam1.getSelectedItem().toString();
					System.out.println("From1 = "+to1);
					SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
					Date startDate1 = sdf1.parse(from1);
					Date endDate1 = sdf1.parse(to1);
					long diff = endDate1.getTime() - startDate1.getTime();
					System.out.println("Diff= "+diff);
					long diffMinutes = diff / (60 * 1000);
					System.out.println("diffMinutes= "+diffMinutes);
					calculateMinutes =Integer.parseInt(""+diffMinutes);
					int patients = Integer.parseInt(numberOfPatientsTv.getText().toString());
					int difference = calculateMinutes/patients;
					System.out.println("Difference= "+difference);
					visitTime.setText(""+difference);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment frag2 = new AddNewClinic();
                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, frag2,null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });

        selectAll = (Button) view.findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Text of Button "+selectAll.getText().toString());

                if((selectAll.getText().toString()).equals("Select All"))
                {

                    monday.setChecked(true);
                    tuesday.setChecked(true);
                    wednesday.setChecked(true);
                    thrustday.setChecked(true);
                    friday.setChecked(true);
                    saturday.setChecked(true);
                    sunday.setChecked(true);

                    System.out.println("I am in True Section.....");
                    selectAll.setText("DeSelect All");
                }
                else
                {
                    monday.setChecked(false);
                    tuesday.setChecked(false);
                    wednesday.setChecked(false);
                    thrustday.setChecked(false);
                    friday.setChecked(false);
                    saturday.setChecked(false);
                    sunday.setChecked(false);


                    selectAll.setText("Select All");
                }

            }

        });
        Button done = (Button)view.findViewById(R.id.time_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				from1 = frmhr1.getSelectedItem().toString();
				from1 = from1 + ":" + frmmn1.getSelectedItem().toString();
				from1 = from1 + " " + fram1.getSelectedItem().toString();

				to1 = tohr1.getSelectedItem().toString();
				to1 = to1 + ":" + tomn1.getSelectedItem().toString();
				to1 = to1 + " " + toam1.getSelectedItem().toString();
				slotName = slotNameTv.getText().toString();
				slotNumber = slotNumberTv.getText().toString();
				autoConfirmString = autoConfirmSpinner.getSelectedItem().toString();
				availabilityString = availabilitySpinner.getSelectedItem().toString();
				numberOfPatients = numberOfPatientsTv.getText().toString();
				visitDuration = visitTime.getText().toString();
				daysOfWeek = getDaysOfWeek();
				String validation = "";
				int flagValidation = 0;
				if (slotName.equals("")) {
					validation = "Please Enter Slot Name";
					flagValidation = 1;
				}
				if(slotNumber.equals("")){
					validation = validation + "\\nPlease Enter slot Number";
					flagValidation = 1;
				}
				if(autoConfirmString.equals("Select")){
					validation = validation + "\\nPlease Select Proper AutoConfirm";
					flagValidation = 1;
				}
				if(availabilityString.equals("Select")){
					validation = validation + "\\nPlease Select Proper Availability";
					flagValidation = 1;
				}
				if(daysOfWeek.equals("")){
					validation = validation + "\\nPlease Select one  Day";
					flagValidation = 1;
				}

				if(flagValidation == 1){
					Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
				}else{
					DoctorClinicSchedule doctorClinicSchedule = new DoctorClinicSchedule();
					doctorClinicSchedule.doctorId = Integer.parseInt(doctorId);
					doctorClinicSchedule.clinicId = Integer.parseInt(clinicId);
					if(autoConfirmString.equalsIgnoreCase("Never")){
						doctorClinicSchedule.autoConfirm = 0;
					}else if(autoConfirmString.equalsIgnoreCase("Always")) {
						doctorClinicSchedule.autoConfirm = 1;
					}else if(autoConfirmString.equalsIgnoreCase("Always except current")){
						doctorClinicSchedule.autoConfirm = 2;
					}else{
						doctorClinicSchedule.autoConfirm = 3;
					}
					if(availabilityString.equalsIgnoreCase("Available")){
						doctorClinicSchedule.availability = 0;
					}else{
						doctorClinicSchedule.availability = 1;
					}
					doctorClinicSchedule.daysOfWeek = daysOfWeek;
					doctorClinicSchedule.numberOfPatients = Integer.parseInt(numberOfPatients);
					doctorClinicSchedule.slotName = slotName;
					doctorClinicSchedule.slotNumber = Integer.parseInt(slotNumber);
					doctorClinicSchedule.slotType = 0;
					doctorClinicSchedule.startTime = from1;
					doctorClinicSchedule.endTime = to1;
					doctorClinicSchedule.visitDuration = Integer.parseInt(visitDuration);
					api.addTimeSchedule(doctorClinicSchedule,new Callback<Response>() {
						@Override
						public void success(Response response, Response response2) {
							Bundle bundle = new Bundle();
							Fragment frag2 = new ShowDoctorClinicSchedule();
							bundle.putString("new_clinicId",clinicId);
							frag2.setArguments(bundle);
							FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
							ft.replace(R.id.content_frame, frag2, "Add_Clinic");
							ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
							ft.addToBackStack(null);
							ft.commit();
						}

						@Override
						public void failure(RetrofitError error) {
							error.printStackTrace();
							Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
						}
					});
				}

			}

        });


        return view;
    }

    public String getDaysOfWeek()
    {
      	String daysOfWeek = "";
        if (monday.isChecked()) {
			daysOfWeek = daysOfWeek + "0";
        }

        if (tuesday.isChecked()) {
			daysOfWeek = daysOfWeek +","+ "1";

        }

        if (wednesday.isChecked()) {
			daysOfWeek = daysOfWeek + ","+"2";

        }
        if (thrustday.isChecked()) {
			daysOfWeek = daysOfWeek +","+ "3";

        }

        if (friday.isChecked()) {
			daysOfWeek = daysOfWeek +","+ "4";

        }

        if (saturday.isChecked()) {
			daysOfWeek = daysOfWeek +","+ "5";
        }

        if (sunday.isChecked()) {
			daysOfWeek = daysOfWeek +","+ "6";
        }
        return daysOfWeek;

    }



}
