package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.mindnerves.meidcaldiary.BackStress;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.medico.application.MyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by MNT on 24-Feb-15.
 */
public class AddNewClinic extends Fragment {

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
    TextView globalTv;
    ArrayList<String> cityList,countryList;
    ArrayAdapter<String> cityAdapter,countryAdapter;
    private String selectedImagePath;
    Uri selectedImageUri = null;
    ImageView clinicImage;
    private static final int SELECT_PICTURE = 1;
    String clinicId = "";
    ProgressDialog progress;
	String timing = "";
	Spinner frmhr1_morning,frmmn1_morning,tohr1_morning,tomn1_morning,fram1_morning,toam1_morning;
	Spinner frmhr1_evening,frmmn1_evening,tohr1_evening,tomn1_evening,fram1_evening,toam1_evening;

    @Override
    public void onResume() {
        super.onStop();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        System.out.println("I am here");
        final Global go = (Global) getActivity().getApplicationContext();
        String locationString = go.getLocation();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    goBack();
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

    public void goBack() {
        final Global go = (Global) getActivity().getApplicationContext();
        go.setLocation("");
        go.setClinic(null);
        go.setClinicSlots(null);
        Fragment frag2 = new AddClinic();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, frag2, null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_clinic, container, false);
        SharedPreferences session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        globalTv = (TextView) getActivity().findViewById(R.id.show_global_tv);
        doctorId = session.getString("sessionID", null);
        getActivity().getActionBar().hide();
        go = (Global) getActivity().getApplicationContext();
        back = (Button) getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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
        typeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,types));
		ArrayAdapter<CharSequence> ampmtoAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.am_pm, android.R.layout.simple_spinner_item);
		ampmtoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> hourad = ArrayAdapter.createFromResource(getActivity(),
				R.array.hour_list, android.R.layout.simple_spinner_item);
		hourad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<CharSequence> minad = ArrayAdapter.createFromResource(getActivity(),
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

				Bundle bundle = new Bundle();
				Fragment frag2 = new ShowDoctorClinicSchedule();
				bundle.putString("new_clinicId","3");
				frag2.setArguments(bundle);
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.replace(R.id.content_frame, frag2, "Add_Clinic");
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
                /*clinicName = ename.getText().toString();
                email = eemail.getText().toString();
                phone = ephone.getText().toString();
                city = multiCity.getText().toString();
                cellnumber = ecellnumber.getText().toString();
                countryMobile = countryCodeMobile.getText().toString();
                countryLandLine = countryCodeLandline.getText().toString();
                specialitesString = specialization.getText().toString();
                about = aboutTv.getText().toString();
                service = serviceTv.getText().toString();
                description = descriptionTv.getText().toString();
                type = typeSpinner.getSelectedItem().toString();
                flagValidation = 0;
				timing = timing+frmhr1_morning.getSelectedItem().toString()+":"
						 +frmmn1_morning.getSelectedItem().toString()+" "+fram1_morning.getSelectedItem().toString();
				timing = timing + ","+tohr1_morning.getSelectedItem().toString()+":"
						 +tomn1_morning.getSelectedItem().toString()+" "+toam1_morning.getSelectedItem().toString();
				timing = timing +","+frmhr1_evening.getSelectedItem().toString()+":"
						+frmmn1_evening.getSelectedItem().toString()+" "+fram1_evening.getSelectedItem().toString();
				timing = timing + ","+tohr1_evening.getSelectedItem().toString()+":"
						+tomn1_evening.getSelectedItem().toString()+" "+toam1_evening.getSelectedItem().toString();
                validation = "";

                if (clinicName.equals("")) {
                    validation = "Please Enter Clinic Name";
                    flagValidation = 1;
                }
                if (email.equals("")) {
                    validation = validation + "\n" + "Please Enter Email";
                    flagValidation = 1;
                }
                if (phone.equals("")) {
                    validation = validation + "\n" + "Please Enter Landline Number";
                    flagValidation = 1;
                }
                if(cellnumber.equals("")){
                    cellnumber = null;
                }else{
                   cellnumber = countryMobile+cellnumber;
                }
                if (countryLandLine.equals("")) {
                    validation = validation + "\n" + "Please Enter Country Code Landline";
                    flagValidation = 1;
                }
                if (specialitesString.equals("")) {
                    validation = validation + "\n" + "Please Enter Speciality of Clinic";
                    flagValidation = 1;
                }
                if (service.equals("")) {
                    validation = validation + "\n" + "Please Enter Service of Clinic";
                    flagValidation = 1;
                }
                if (description.equals("")) {
                    validation = validation + "\n" + "Please Enter Description of Clinic";
                    flagValidation = 1;
                }
                if (city.equals("")) {
                    validation = validation + "\n" + "Please Enter City of Clinic";
                    flagValidation = 1;
                }
                if (flagValidation == 1) {
                    Toast.makeText(getActivity(), validation, Toast.LENGTH_SHORT).show();
                } else {
                    String[] cityArray = city.split(",");
                    System.out.println("City= "+cityArray[0]);
					city = cityArray[0];
                    phone = countryLandLine + phone;
                    Clinic cl = new Clinic();
                    cl.setClinicName(clinicName);
                    cl.setEmail(email);
                    cl.setLandLineNumber(phone);
                    cl.setMobile(cellnumber);
                    cl.setSpecialization(specialitesString);
                    cl.setDoctorId(doctorId);
                    cl.setLocation(city);
                    cl.about = about;
                    cl.service = service;
                    cl.description = description;
                    cl.type = type;
					cl.timing = timing;
                    cl.alarmFlag = null;
                    cl.upcomingFlag = null;
                    progress = ProgressDialog.show(getActivity(), "", "getResources().getString(R.string.loading_wait)");
                    api.addClinic(cl, new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            if (s != null) {
                                clinicId = s;
                                if(selectedImageUri != null){
                                    File file = new File(selectedImagePath);
                                    TypedFile picture = new TypedFile("application/octet-stream",file);
                                    api.savePictureClinic(clinicId,picture,new Callback<String>() {
                                       @Override
                                       public void success(String s, Response response) {
                                           if(s.equalsIgnoreCase("Success")) {
                                               Toast.makeText(getActivity(), "Clinic Basic Information Saved", Toast.LENGTH_SHORT).show();
                                               System.out.println("Clinic Id = " + s);
                                               progress.dismiss();
											   Bundle bundle = new Bundle();
											   Fragment frag2 = new ShowDoctorClinicSchedule();
											   bundle.putString("new_clinicId",clinicId);
											   frag2.setArguments(bundle);
											   FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
											   ft.replace(R.id.content_frame, frag2, "Add_Clinic");
											   ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
											   ft.addToBackStack(null);
											   ft.commit();

                                           }else{
                                               progress.dismiss();
                                           }
                                       }

                                       @Override
                                       public void failure(RetrofitError error) {
                                           Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                           error.printStackTrace();
                                           progress.dismiss();
                                       }
                                    });
                                }
                            }else{
                                Toast.makeText(getActivity(), "Clinic Basic Information Saved", Toast.LENGTH_SHORT).show();
                                System.out.println("Clinic Id = "+s);
                                clinicId = s;
                                progress.dismiss();
								Bundle bundle = new Bundle();
								Fragment frag2 = new ShowDoctorClinicSchedule();
								bundle.putString("new_clinicId",s);
								frag2.setArguments(bundle);
								FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
								ft.replace(R.id.content_frame, frag2, "Add_Clinic");
								ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
								ft.addToBackStack(null);
								ft.commit();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                            progress.dismiss();
                        }
                    });

                }*/
            }
        });

        backButton = (Button) view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        manageScreenIcon();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            System.out.println("Image Path : " + selectedImagePath);
            clinicImage.setImageURI(selectedImageUri);
            go.setUri(selectedImageUri);
        }
    }


    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = super.getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public static boolean isValid(String emailText, String regex) {
        if (!Pattern.matches(regex, emailText)) {
            return false;
        }
        return true;
    }


}
