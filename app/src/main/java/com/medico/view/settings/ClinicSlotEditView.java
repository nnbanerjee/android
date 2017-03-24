package com.medico.view.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.datepicker.SlideDateTimeListener;
import com.medico.datepicker.SlideDateTimePicker;
import com.medico.model.ClinicSlotDetails;
import com.medico.model.DoctorClinicId;
import com.medico.model.Person;
import com.medico.model.ResponseCodeVerfication;
import com.medico.view.ParentFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import Model.AlarmReminderVM;
//import Model.ReminderDate;

//import android.app.DatePickerDialog;



//import Model.MedicineSchedule;

/**
 * Created by MNT on 07-Apr-15.
 */
//add medicine
public class ClinicSlotEditView extends ParentFragment {

    EditText slotNames,startTime,endTime,visitDuration,numberOfPatients,doctorFeeValue;
    Spinner slotNumber,currency_value,assistant_list;
    ImageView starttimestampImg,endtimestampImg;
    CheckBox[] days = new CheckBox[7];
    RadioButton[] policies = new RadioButton[4];
    RadioButton generalSlotType, primeSlotType;

    ClinicSlotDetails slotModel = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.clinic_slot_edit_view, container, false);
        setHasOptionsMenu(true);

        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Edit Slot");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        //Load Ui controls

        slotNames = (EditText)view.findViewById(R.id.slotName);
        startTime = (EditText)view.findViewById(R.id.startTime);
        endTime = (EditText)view.findViewById(R.id.endTime);
        visitDuration = (EditText)view.findViewById(R.id.visitDuration);
        numberOfPatients = (EditText)view.findViewById(R.id.numberOfPatients);
        doctorFeeValue = (EditText)view.findViewById(R.id.doctorFeeValue);

        slotNumber = (Spinner) view.findViewById(R.id.slotNumber);
        currency_value = (Spinner) view.findViewById(R.id.currency_value);
        assistant_list = (Spinner) view.findViewById(R.id.assistant_list);

        starttimestampImg = (ImageView)view.findViewById(R.id.starttimestampImg);
        endtimestampImg = (ImageView)view.findViewById(R.id.endtimestampImg);

        days[0] = (CheckBox)view.findViewById(R.id.monday);
        days[1] = (CheckBox)view.findViewById(R.id.tuesday);
        days[2] = (CheckBox)view.findViewById(R.id.wednesday);
        days[3] = (CheckBox)view.findViewById(R.id.thursday);
        days[4] = (CheckBox)view.findViewById(R.id.friday);
        days[5] = (CheckBox)view.findViewById(R.id.saturday);
        days[6] = (CheckBox)view.findViewById(R.id.sunday);

        generalSlotType = (RadioButton) view.findViewById(R.id.generalSlotType);
        primeSlotType  = (RadioButton) view.findViewById(R.id.primeSlotType);

        policies[0] = (RadioButton) view.findViewById(R.id.always);
        policies[1] = (RadioButton) view.findViewById(R.id.alwaysExceptCurrentSlot);
        policies[2] = (RadioButton) view.findViewById(R.id.alwaysExceptCurrentDay);
        policies[3] = (RadioButton) view.findViewById(R.id.never);

        starttimestampImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(startTime);
            }
        });
        endtimestampImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(endTime);

            }
        });
        visitDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(visitDuration.getText().length() > 0)
                {
                    updateDurationAndPatientNumber();
                }
            }
        });
        numberOfPatients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                updateDurationAndPatientNumber();
            }
        });

        return view;
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



    //Declaration
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        int doctorId = bundle.getInt(DOCTOR_ID);
        Integer doctorClinicId = bundle.getInt(DOCTOR_CLINIC_ID);
        final int logged_in_id = bundle.getInt(LOGGED_IN_ID);
        if(doctorClinicId != null && doctorClinicId.intValue() > 0) {
            api.getSlotDetail(new DoctorClinicId(doctorClinicId), new Callback<ClinicSlotDetails>() {
                @Override
                public void success(ClinicSlotDetails clinicSlotDetails, Response response)
                {
                    slotModel = clinicSlotDetails;

                    policies[clinicSlotDetails.autoConfirm].setChecked(true);
                    StringTokenizer tokenizer = new StringTokenizer(clinicSlotDetails.daysOfWeek,",");
                    for(;tokenizer.hasMoreTokens();)
                    {
                        days[new Integer(tokenizer.nextToken()).intValue()].setChecked(true);
                    }
                    doctorFeeValue.setText(clinicSlotDetails.feesConsultation.toString());
                    numberOfPatients.setText(clinicSlotDetails.numberOfPatients.toString());
                    slotNames.setText(clinicSlotDetails.slotName);
                    slotNumber.setSelection(clinicSlotDetails.slotNumber.intValue() -1);
                    if(clinicSlotDetails.slotType.intValue() == 0 )
                        generalSlotType.setChecked(true);
                    else
                        primeSlotType.setChecked(true);
                    DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
                    startTime.setText(dateFormat.format(new Date(clinicSlotDetails.timeToStart)));
                    endTime.setText(dateFormat.format(new Date(clinicSlotDetails.TimeToStop)));
                    visitDuration.setText(clinicSlotDetails.visitDuration.toString());
                    setAssistant(clinicSlotDetails.assistantId);

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            });
        }
        else
        {
            slotModel = new ClinicSlotDetails();

        }
    }

    public void save(ClinicSlotDetails clinicSlotDetails) {

        if (clinicSlotDetails.doctorClinicId == null || clinicSlotDetails.doctorClinicId.intValue() == 0 )
        {
            api.createSlot(clinicSlotDetails, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication jsonObject, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Save successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        }
        else
        {
            System.out.println("I am Update Conidtion::::::::::::::::::::");
            api.updateSlot(clinicSlotDetails, new Callback<ResponseCodeVerfication>() {
                @Override
                public void success(ResponseCodeVerfication reminderVM, Response response) {
                    System.out.println("Response::::::" + response.getStatus());
                    Toast.makeText(getActivity(), "Updated successfully !!!", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                 }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        }
    }


    public void setTime(final TextView dateField) {
        final Calendar calendar = Calendar.getInstance();

        SlideDateTimeListener listener = new SlideDateTimeListener() {

            @Override
            public void onDateTimeSet(Date date)
            {
                DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
                dateField.setText(format.format(date));
                slotModel.timeToStart = date.getTime();
                updateDurationAndPatientNumber();
            }

        };
        Date date = null;
        if(dateField.getText().toString().trim().length() > 0)
        {
            DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
            try {
                date = format.parse(dateField.getText().toString());
            }
            catch(ParseException e)
            {
                date = new Date();
            }

        }

        SlideDateTimePicker pickerDialog = new SlideDateTimePicker.Builder(((AppCompatActivity)getActivity()).getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                .build();
        pickerDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
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

    @Override
    public boolean isChanged()
    {
        return slotModel.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle1 = getActivity().getIntent().getExtras();
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        slotModel.autoConfirm = new Integer(getPolicySelectedIndex()).byteValue();
        slotModel.daysOfWeek = getDaysOfWeek();
        slotModel.feesConsultation = new Integer(doctorFeeValue.getText().toString());
        slotModel.numberOfPatients = new Integer(numberOfPatients.getText().toString());
        slotModel.slotName = slotNames.getText().toString();
        slotModel.slotNumber = new Integer(slotNumber.getSelectedItem().toString()).byteValue();
        slotModel.slotType = new Integer(generalSlotType.isChecked()?0:1).byteValue();
        slotModel.visitDuration = new Integer(visitDuration.getText().toString());
        slotModel.clinicId = bundle1.getInt(CLINIC_ID);
        slotModel.doctorId = bundle1.getInt(DOCTOR_ID);
        Person assistant = ((Person)assistant_list.getSelectedItem());
        if(assistant != null)
            slotModel.assistantId = assistant.getId();
    }
    @Override
    public boolean save()
    {
        if(slotModel.canBeSaved())
        {
            save(slotModel);
            return true;
        }
        return false;
    }
    @Override
    public boolean canBeSaved()
    {
        return slotModel.canBeSaved();
    }
    @Override
    public void setEditable(boolean editable)
    {

    }

    private void updateDurationAndPatientNumber()
    {

    }

    private int getPolicySelectedIndex()
    {
        for(int i = 0; i < policies.length; i++)
        {
            if(policies[i].isChecked())
                return i;
        }
        return 0;
    }

    private String getDaysOfWeek()
    {
        String daysOfWeek = null;
        for(int i = 0; i < days.length; i++)
        {
            if(days[i].isChecked()) {
                if(daysOfWeek == null)
                    daysOfWeek = new Integer(i).toString();
                else
                    daysOfWeek = daysOfWeek + "," + new Integer(i).toString();
            }
        }
        return daysOfWeek;
    }
    private void setAssistant(Integer assistantId)
    {
        SpinnerAdapter adapter = assistant_list.getAdapter();
        if( adapter != null && !adapter.isEmpty() && assistantId != null)
        {
        }
    }
}
