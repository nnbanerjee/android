package com.medico.view.settings;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.medico.adapter.ClinicSlotAdapter;
import com.medico.application.R;
import com.medico.model.Clinic1;
import com.medico.model.ClinicId;
import com.medico.model.ClinicSlotDetails;
import com.medico.model.Country;
import com.medico.model.DoctorClinicRequest;
import com.medico.model.ServerResponse;
import com.medico.util.GeoUtility;
import com.medico.util.ImageLoadTask;
import com.medico.view.ParentFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 8/7/15.
 */
public class ClinicProfileEditView extends ParentFragment  implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static int SELECT_PICTURE = 1;
    public static int SELECT_DOCUMENT = 2;
    ProgressDialog progress;
    MenuItem menuItem;
    ImageView profilePic,addClinicSlot;
    Button profilePicUploadBtn,location_delete_button,current_location_button;
    TextView id;
    EditText name, email, country, city, mobile;
    Spinner mobile_country;
    EditText landline;
    Spinner specialization;
    AutoCompleteTextView mAutocompleteView;
    protected GoogleApiClient mGoogleApiClient;
    Clinic1 clinicModel;
    ListView slotListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.clinic_profile_edit_view,container,false);
        setHasOptionsMenu(true);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        profilePicUploadBtn = (Button) view.findViewById(R.id.upload_pic);
        id = (TextView) view.findViewById(R.id.person_id);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        mobile = (EditText) view.findViewById(R.id.mobile_number) ;
        mobile_country = (Spinner) view.findViewById(R.id.country_code);
        landline = (EditText) view.findViewById(R.id.landline_value);
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.location);
        location_delete_button = (Button) view.findViewById(R.id.location_delete_button);
        current_location_button = (Button) view.findViewById(R.id.current_location_button);
        country = (EditText) view.findViewById(R.id.country_spinner);
        city = (EditText) view.findViewById(R.id.city);
        specialization = (Spinner) view.findViewById(R.id.specialization);
        addClinicSlot = (ImageView)view.findViewById(R.id.add_alarm);
        slotListView = (ListView)view.findViewById(R.id.alarm_list);
        Bundle bundle = getActivity().getIntent().getExtras();
        switch (bundle.getInt(CLINIC_TYPE)) {
            case CLINIC:
                textviewTitle.setText("Clinic Profile");
                profilePic.setImageResource(R.drawable.clinic_default);
                String[] clinicProfessions = getActivity().getResources().getStringArray(R.array.patient_professions);
                ArrayAdapter<String> clinicArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, clinicProfessions);
                specialization.setAdapter(clinicArrayAdapter);
                break;
            case PATHOLOGY:
                textviewTitle.setText("Pathology Profile");
                profilePic.setImageResource(R.drawable.clinic_default);
                String[] pathologyProfessions = getActivity().getResources().getStringArray(R.array.assistant_professions);
                ArrayAdapter<String> pathology_professionsArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, pathologyProfessions);
                specialization.setAdapter(pathology_professionsArrayAdapter);
                break;
            case DIAGNOSTIC:
                textviewTitle.setText("Diagnostic Profile");
                profilePic.setImageResource(R.drawable.clinic_default);
                String[] diagnosticProfessions = getActivity().getResources().getStringArray(R.array.assistant_professions);
                ArrayAdapter<String> diagnostic_professionsArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, diagnosticProfessions);
                specialization.setAdapter(diagnostic_professionsArrayAdapter);
                break;
            case ONLINE_CONSULATION:
                textviewTitle.setText("Online Consultation");
                profilePic.setImageResource(R.drawable.clinic_default);
                String[] onlineProfessions = getActivity().getResources().getStringArray(R.array.assistant_professions);
                ArrayAdapter<String> online_professionsArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, onlineProfessions);
                specialization.setAdapter(online_professionsArrayAdapter);
                break;
            case HOME_VISIT:
                textviewTitle.setText("Home Visit");
                profilePic.setImageResource(R.drawable.clinic_default);
                String[] homeProfessions = getActivity().getResources().getStringArray(R.array.assistant_professions);
                ArrayAdapter<String> home_professionsArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, homeProfessions);
                specialization.setAdapter(home_professionsArrayAdapter);
                break;
       }
        addClinicSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = getActivity().getIntent().getExtras();
                args.putInt(DOCTOR_CLINIC_ID,0);
                getActivity().getIntent().putExtras(args);
                ParentFragment fragment = new ClinicSlotEditView();
                ((ManagePersonSettings)getActivity()).registerView(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }
        });
        slotListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);

                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        progress = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading_wait));
        final Integer doctorId = bundle.getInt(DOCTOR_ID);
        final Integer clinicId = bundle.getInt(CLINIC_ID);
        final Integer clinicType = bundle.getInt(CLINIC_TYPE);
        final Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        SpinnerAdapter countryListAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_layout, countriesList);
        mobile_country.setAdapter(countryListAdapter);
        if(clinicId != null && clinicId.intValue() > 0 && clinicType != null && clinicType.intValue() >= 0) {
            api.getClinicDetails(new ClinicId(clinicId), new Callback<Clinic1>() {
                @Override
                public void success(Clinic1 clinic, Response response) {
                    if (clinic != null && clinic.idClinic != null) {
                        clinicModel = clinic;
                        String url = clinic.imageUrl;
                        if(url != null && url.trim().length() > 0)
                            new ImageLoadTask(url, profilePic).execute();
                        id.setText(clinic.idClinic.toString());
                        name.setText(clinic.clinicName);
                        mobile.setText(clinic.mobile.toString());
                        email.setText(clinic.email);
                        landline.setText(clinic.landLineNumber.toString());
                        mAutocompleteView.setText(clinic.address);
                        country.setText(clinic.country);
                        city.setText(clinic.city);
                        specialization.setSelection(getSpecializationIndex(clinic.speciality,clinicType));
                        new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, clinicModel);
                        if (clinic.addedBy != null && clinic.addedBy.intValue() == loggedinUserId.intValue())
                        {
                            menuItem.setEnabled(true);
                            setEditableAll(true);
                        }
                        else
                        {
                            menuItem.setEnabled(false);
                            setEditableAll(false);
                        }
                        api.getAllSlotDetails(new DoctorClinicRequest(doctorId,clinicId), new Callback<List<ClinicSlotDetails>>() {
                            @Override
                            public void success(List<ClinicSlotDetails> clinicslots, Response response) {
                                if (clinicslots != null && clinicslots.size() > 0)
                                {
                                    ClinicSlotAdapter adapter = new ClinicSlotAdapter(getActivity(), clinicslots, loggedinUserId);
                                    slotListView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.printStackTrace();
                                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        });

                    }
                    setEditable(false);
                    progress.dismiss();


                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
            });
        }
        else
        {
            clinicModel = new Clinic1();
            clinicModel.type = clinicType;
            clinicModel.addedBy = loggedinUserId;
            new GeoUtility(getActivity(), mAutocompleteView, country, city, location_delete_button, current_location_button, clinicModel);
            setEditable(true);
            progress.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void save(Clinic1 person)
    {
        if(person.idClinic != null) {
            api.updateClinic(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            api.addClinic(person, new Callback<ServerResponse>() {
                @Override
                public void success(ServerResponse s, Response response) {
                    if (s.status == 1)
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
                }
            });

        }
    }


    @Override
    public boolean isChanged()
    {
        return clinicModel.isChanged();
    }
    @Override
    public void update()
    {
        if(clinicModel.idClinic != null) {
            Bundle bundle1 = getActivity().getIntent().getExtras();
            clinicModel.address = (mAutocompleteView.getText().toString());
            clinicModel.city = (city.getText().toString());
            clinicModel.country = (country.getText().toString().trim());
            clinicModel.speciality = (specialization.getSelectedItem().toString());
            clinicModel.location = (mobile_country.getSelectedItem().toString());
        }
        else
        {
            clinicModel.address = (mAutocompleteView.getText().toString());
            clinicModel.city = (city.getText().toString());
            clinicModel.country = (country.getText().toString().trim());
            clinicModel.speciality = (specialization.getSelectedItem().toString());
            clinicModel.location = (mobile_country.getSelectedItem().toString());
            clinicModel.speciality = (specialization.getSelectedItem().toString());
            clinicModel.clinicName = (name.getText().toString());
            clinicModel.mobile = (new Long(mobile.getText().toString()));
            clinicModel.email = (email.getText().toString());
            clinicModel.location = (mobile_country.getSelectedItem().toString());

        }
    }
    @Override
    public boolean save()
    {
        if(clinicModel.canBeSaved())
        {
            save(clinicModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return clinicModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {
        name.setEnabled(editable);
        mobile.setEnabled(editable);
        email.setEnabled(editable);
    }
    public void setEditableAll(boolean editable)
    {
        mobile.setEnabled(editable);
        email.setEnabled(editable);
//        menuItem.setEnabled(editable);
        profilePicUploadBtn.setEnabled(editable);
        location_delete_button.setEnabled(editable);
        current_location_button.setEnabled(editable);
        name.setEnabled(editable);
        mobile_country.setEnabled(editable);
        landline.setEnabled(editable);
        specialization.setEnabled(editable);
        mAutocompleteView.setEnabled(editable);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.add);
        menuItem.setIcon(R.drawable.save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add: {
                update();
                if (isChanged()) {
                    if (canBeSaved()) {
                        save();
                    } else {
                        Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                    }
                } else if (canBeSaved()) {
                    Toast.makeText(getActivity(), "Nothing has changed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please fill-in all the mandatory fields", Toast.LENGTH_LONG).show();
                }


            }
            break;
        }
        return true;
    }

    private int getBloodgroupIndex(String bloodgroup)
    {
        String[] bloodgroups = getActivity().getResources().getStringArray(R.array.bloodgroup_list);
        for(int i = 0; i < bloodgroups.length; i++)
        {
            if(bloodgroups[i].equalsIgnoreCase(bloodgroup))
                return i;
        }
        return 0;
    }

    private int getCountryIndex(String isdCode)
    {
        int i = 0;
        for(Country country : countriesList)
        {
            if(country.toString().equalsIgnoreCase(isdCode))
                return i;
        }
        return 0;
    }
    private int getSpecializationIndex(String specialization, int profile)
    {
        String[] relations = getActivity().getResources().getStringArray(profile==PATIENT?R.array.patient_professions:R.array.assistant_professions);
        for(int i = 0; i < relations.length; i++)
        {
            if(relations[i].equalsIgnoreCase(specialization))
                return i;
        }
        return 0;
    }
}
