package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.view.AddClinic;
import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.medico.application.MyApi;
import com.medico.model.Clinic;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 24-Feb-15.
 */
public class EditClinic extends Fragment{

    private String clinicName, email, city, phone, cellnumber, countryMobile, countryLandLine, specialitesString,
			about,description,service,type;
    private EditText ename, eemail, ephone, ecellnumber, countryCodeMobile, countryCodeLandline,
			aboutTv,descriptionTv,serviceTv;
	public MyApi api;
	private Button done, backButton,back,upload;
	private String doctorId = "";
	private String validation = "";
	private int flagValidation = 0;
	private Spinner typeSpinner,countrySpinner;
	private String[] specialities,types;
	private MultiAutoCompleteTextView speciality;
	Global go;
	MultiAutoCompleteTextView multiCity;
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	ArrayList<String> cityList,countryList;
	ArrayAdapter<String> cityAdapter,countryAdapter,typeAdapter;
	private String selectedImagePath;
	Uri selectedImageUri = null;
	ImageView clinicImage;
	private static final int SELECT_PICTURE = 1;
	String clinicId = "";
	ProgressDialog progress;
	String timing = "";
	TextView globalTv;
	Spinner frmhr1_morning,frmmn1_morning,tohr1_morning,tomn1_morning,fram1_morning,toam1_morning;
	Spinner frmhr1_evening,frmmn1_evening,tohr1_evening,tomn1_evening,fram1_evening,toam1_evening;
	ArrayAdapter<CharSequence> hourad,ampmtoAdapter,minad;
    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        System.out.println("I am here");
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    go.setLocation("");
                    go.setClinic(null);
                    go.setClinicSlots(null);
                    Fragment frag2 = new AddClinic();
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
	public void manageScreenIcon() {
		globalTv.setText("Add New Clinic");
		BackStress.staticflag = 0;
		back.setVisibility(View.VISIBLE);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_clinic,container,false);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
		doctorId = session.getString("id", null);
		getActivity().getActionBar().hide();
		go = (Global) getActivity().getApplicationContext();
		back = (Button) getActivity().findViewById(R.id.back_button);
		back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Global go = (Global)getActivity().getApplicationContext();
                go.setLocation("");
                go.setClinic(null);
                go.setClinicSlots(null);
                Fragment frag2 = new AddClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2,null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        //Retrofit Initializtion Code
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
		ename = (EditText) view.findViewById(R.id.name);
		eemail = (EditText) view.findViewById(R.id.email);
		ephone = (EditText) view.findViewById(R.id.landline);
		ecellnumber = (EditText) view.findViewById(R.id.cellphone);
		countryCodeMobile = (EditText) view.findViewById(R.id.pin_code_mobile);
		countryCodeLandline = (EditText) view.findViewById(R.id.pin_code_landline);
		done = (Button) view.findViewById(R.id.done);
		specialities = getResources().getStringArray(R.array.speciality_list);
		speciality = (MultiAutoCompleteTextView) view.findViewById(R.id.speciality_text);
		speciality.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, specialities));
		speciality.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		serviceTv = (EditText)view.findViewById(R.id.service);
		aboutTv = (EditText) view.findViewById(R.id.about);
		descriptionTv = (EditText) view.findViewById(R.id.description);
		multiCity = (MultiAutoCompleteTextView)view.findViewById(R.id.city_multiauto);
		typeSpinner = (Spinner)view.findViewById(R.id.type_spinner);
		types = getResources().getStringArray(R.array.clinic_type_list);
		upload = (Button)view.findViewById(R.id.upload);
		clinicImage = (ImageView)view.findViewById(R.id.clinic_image);
		countrySpinner = (Spinner)view.findViewById(R.id.country_spinner);
		frmhr1_morning = (Spinner)view.findViewById(R.id.hourfrom_morning);
		frmmn1_morning = (Spinner)view.findViewById(R.id.minutefrom_morning);
		fram1_morning = (Spinner)view.findViewById(R.id.ampmfrom_morning);
		tohr1_morning = (Spinner)view.findViewById(R.id.hourto_morning);
		tomn1_morning = (Spinner)view.findViewById(R.id.minuteto_morning);
		toam1_morning = (Spinner)view.findViewById(R.id.ampmto_morning);
		frmhr1_evening = (Spinner)view.findViewById(R.id.hourfrom_evening);
		frmmn1_evening = (Spinner)view.findViewById(R.id.minutefrom_evening);
		fram1_evening = (Spinner)view.findViewById(R.id.ampmfrom_evening);
		tohr1_evening = (Spinner)view.findViewById(R.id.hourto_evening);
		tomn1_evening = (Spinner)view.findViewById(R.id.minuteto_evening);
		toam1_evening = (Spinner)view.findViewById(R.id.ampmto_evening);
		typeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,types);
		typeSpinner.setAdapter(typeAdapter);
		ampmtoAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.am_pm, android.R.layout.simple_spinner_item);
		ampmtoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		hourad = ArrayAdapter.createFromResource(getActivity(),
				R.array.hour_list, android.R.layout.simple_spinner_item);
		hourad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		minad = ArrayAdapter.createFromResource(getActivity(),
				R.array.minute_list, android.R.layout.simple_spinner_item);
		minad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		frmhr1_morning.setAdapter(hourad);
		frmmn1_morning.setAdapter(minad);
		fram1_morning.setAdapter(ampmtoAdapter);
		tohr1_morning.setAdapter(hourad);
		tomn1_morning.setAdapter(minad);
		toam1_morning.setAdapter(ampmtoAdapter);
		frmhr1_evening.setAdapter(hourad);
		frmmn1_evening.setAdapter(minad);
		fram1_evening.setAdapter(ampmtoAdapter);
		tohr1_evening.setAdapter(hourad);
		tomn1_evening.setAdapter(minad);
		toam1_evening.setAdapter(ampmtoAdapter);
		countryList = new ArrayList<String>();
		api.getAllCountry("temp",new Callback<List<String>>() {
			@Override
			public void success(List<String> strings, Response response) {
				for(String country : strings){
					countryList.add(country);
				}
				System.out.println("City List= "+countryList.size());
				countryAdapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1, countryList);
				countrySpinner.setAdapter(countryAdapter);
			}

			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
				Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
			}
		});
		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				cityList = new ArrayList<String>();
				String country = countrySpinner.getSelectedItem().toString();
				api.getAllCity(country,new Callback<List<String>>() {
					@Override
					public void success(List<String> strings, Response response) {
						for(String city : strings){
							cityList.add(city);
						}
						System.out.println("City List= "+cityList.size());
						cityAdapter = new ArrayAdapter<String>(
								getActivity(), android.R.layout.simple_list_item_1, cityList);
						multiCity.setAdapter(cityAdapter);
						multiCity.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
						Toast.makeText(getActivity(),"Fail",Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		multiCity.setThreshold(1);
		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
			}
		});
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clinicName = ename.getText().toString();
                email = eemail.getText().toString();
                phone = ephone.getText().toString();
              //  city = ecity.getText().toString();
                cellnumber = ecellnumber.getText().toString();
                countryMobile = countryCodeMobile.getText().toString();
                countryLandLine = countryCodeLandline.getText().toString();
                specialitesString = speciality.getText().toString();
                flagValidation = 0;
                validation = "";

                if(clinicName.equals("")) {
                    validation = "Please Enter Clinic Name";
                    flagValidation = 1;
                }
                if(email.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Email";
                    flagValidation = 1;
                }
                if(phone.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Landline Number";
                    flagValidation = 1;
                }
                if(countryLandLine.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Country Code Landline";
                    flagValidation = 1;
                }
                if(specialitesString.equals(""))
                {
                    validation = validation+"\n"+"Please Enter Speciality of Clinic";
                    flagValidation = 1;
                }
                if(flagValidation == 1)
                {
                    Toast.makeText(getActivity(),validation,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(cellnumber.equals(""))
                    {
                        cellnumber = null;
                    }
                    else
                    {
                        cellnumber = countryMobile+cellnumber;
                    }
                    Boolean emailBoolean = isValid(email,EMAIL_REGEX);
                    if(!emailBoolean)
                    {
                        Toast.makeText(getActivity(),"Please Enter Email Proper",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        phone = countryLandLine + phone;
                        Clinic cl = new Clinic();
                        cl.setIdClinic(clinicId);
                        cl.setClinicName(clinicName);
                        cl.setEmail(email);
                        cl.setLandLineNumber(phone);
                        cl.setLocation(city);
                        cl.setMobile(cellnumber);
                        cl.setSpeciality(specialitesString);
                        cl.setDoctorId(doctorId);
                        cl.alarmFlag = null;
                        cl.upcomingFlag = null;
                        api.editClinic(cl,new Callback<String>() {
                            @Override
                            public void success(String var, Response response) {
                                System.out.println("Response " + var);
                                int status = response.getStatus();
                                if (status == 200) {
                                    } else {
                                        Toast.makeText(getActivity(), "CLinic Updated Successfully", Toast.LENGTH_LONG).show();
                                        go.setClinic(null);
                                    }


                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        });

                    }

                }
            }
        });
        backButton = (Button)view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Global go = (Global)getActivity().getApplicationContext();
                go.setLocation("");
                Fragment frag2 = new AddClinic();
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag2, null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        showClinicData();
		manageScreenIcon();
        return view;
    }

    public void showClinicData()
    {
		Clinic clinic = go.getClinic();
		int position = 0;
        if(clinic!=null)
        {
			eemail.setText(clinic.getClinicName());
			ephone.setText(clinic.getLandLineNumber());
			multiCity.setText(clinic.getLocation());
			ecellnumber.setText(clinic.getMobile());
			speciality.setText(clinic.getSpeciality());
			aboutTv.setText(clinic.about);
			serviceTv.setText(clinic.service);
			descriptionTv.setText(clinic.description);
			type = typeSpinner.getSelectedItem().toString();
			int typeInt = Integer.parseInt(clinic.type);
			if(typeInt == 0){
				type = "Lab";
			}else if(typeInt == 1){
				type = "Clinic";
			}else{
				type = "Hospital";
			}
			position = typeAdapter.getPosition(type);
			typeSpinner.setSelection(position);
			String timing = clinic.timing;
			String[] times = timing.split(",");
			System.out.println("Length = "+times.length);
			showSpinnerData(times);

        }
    }

    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        }
		return true;
    }

	public void showSpinnerData(String[] times){
		int pos = 0;
		String[] timeFrom3 = times[0].split(" ");
		String[] timeFromHoursMins3 = timeFrom3[0].split(":");
		pos = hourad.getPosition(timeFromHoursMins3[0]);
		frmhr1_morning.setSelection(pos);
		pos = minad.getPosition(timeFromHoursMins3[1]);
		frmmn1_morning.setSelection(pos);
		pos = ampmtoAdapter.getPosition(timeFrom3[1]);
		fram1_morning.setSelection(pos);
		String[] timeTo3 = times[1].split(" ");
		String[] timeToHoursMins3 = timeTo3[0].split(":");
		pos = hourad.getPosition(timeToHoursMins3[0]);
		tohr1_morning.setSelection(pos);
		pos = minad.getPosition(timeToHoursMins3[1]);
		tomn1_morning.setSelection(pos);
		pos = ampmtoAdapter.getPosition(timeTo3[1]);
		toam1_morning.setSelection(pos);

		String[] timeFrom4 = times[2].split(" ");
		String[] timeFromHoursMins4 = timeFrom4[0].split(":");
		pos = hourad.getPosition(timeFromHoursMins4[0]);
		frmhr1_evening.setSelection(pos);
		pos = minad.getPosition(timeFromHoursMins4[1]);
		frmmn1_evening.setSelection(pos);
		pos = ampmtoAdapter.getPosition(timeFrom4[1]);
		fram1_evening.setSelection(pos);
		String[] timeTo4 = times[3].split(" ");
		String[] timeToHoursMins4 = timeTo3[0].split(":");
		pos = hourad.getPosition(timeToHoursMins4[0]);
		tohr1_evening.setSelection(pos);
		pos = minad.getPosition(timeToHoursMins4[1]);
		tomn1_evening.setSelection(pos);
		pos = ampmtoAdapter.getPosition(timeTo4[1]);
		toam1_evening.setSelection(pos);
	}

}
