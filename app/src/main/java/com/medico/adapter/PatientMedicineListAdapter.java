package com.medico.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.MedicineStatusRequest;
import com.medico.model.PatientMedicine;
import com.medico.model.ResponseCodeVerfication;
import com.medico.util.PARAM;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;
import com.medico.view.profile.PatientMedicinReminder;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


/**
 * Created by MNT on 23-Feb-15.
 */

//Doctor Login
public class PatientMedicineListAdapter extends HomeAdapter implements StickyListHeadersAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    List<PatientMedicine> appointments;
    private ProgressDialog progress;

    public PatientMedicineListAdapter(Activity activity, List<PatientMedicine> appointments)
    {
        super(activity);
        this.activity = activity;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {

        if(appointments==null)
            return 0;
        else
            return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.medicine_details, null);
        TextView medicineName = (TextView) convertView.findViewById(R.id.medicine_name);
        TextView medicineType = (TextView) convertView.findViewById(R.id.medicine_type);
        ImageView viewImage = (ImageView) convertView.findViewById(R.id.medicine_icon);
        TextView prescribedBy = (TextView) convertView.findViewById(R.id.prescribed_by_value);

        TextView startDate = (TextView) convertView.findViewById(R.id.medicine_start_value);
        TextView endDate = (TextView) convertView.findViewById(R.id.medicine_end_value);
        TextView medicineStatus = (TextView) convertView.findViewById(R.id.medicine_status_value);

        ImageView rightButton = (ImageView) convertView.findViewById(R.id.nextBtn);

        ImageView sliderButton = (ImageView)convertView.findViewById(R.id.change_btn);
        ImageView alarmIcon = (ImageView)convertView.findViewById(R.id.alarm_button);

        final PatientMedicine model = appointments.get(position);

        medicineName.setText(model.getMedicinName());
        medicineType.setText(activity.getResources().getStringArray(R.array.medicine_type_list)[model.getType().intValue()]);

        viewImage.setBackground(null);
        switch(model.getType().intValue())
        {
            case 0:
                viewImage.setImageResource(R.drawable.medicine_allopath);
                break;
            case 1:
                viewImage.setImageResource(R.drawable.medicine_allopath);
                break;
            case 2:
                viewImage.setImageResource(R.drawable.medicine_allopath);
                break;
            default:
                viewImage.setImageResource(R.drawable.medicine_allopath);
                break;
        }

        if (model.getDoctorName() != null)
        {
            prescribedBy.setText(model.getDoctorName());
        }
        else
            prescribedBy.setText("Self");

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.SHORT);
        startDate.setText(format.format(new Date(model.getStartDate())));
        endDate.setText(format.format(new Date(model.getEndDate())));
        medicineStatus.setText(model.getEndDate() > new Date().getTime()?"Active":"Inactive");
        sliderButton.setBackground(null);
        if(model.getReminder() == 0)
        {
            sliderButton.setImageResource(R.drawable.slider_off);
        }
        else
            sliderButton.setImageResource(R.drawable.slider_on);

        sliderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int currentState = model.getReminder();
                final int postState = currentState==1?0:1;
                api.setMedicineAlarm(new MedicineStatusRequest(model.getMedicineId(),postState), new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication result, Response response) {
//                        progress.dismiss();
                        if (result.getStatus().intValue() == PARAM.STATUS_SUCCESS) {
                            Toast.makeText(activity, "Medicine reminder status updated", Toast.LENGTH_SHORT).show();
                            appointments.get(position).setReminder(new Integer(postState).byteValue());
                            notifyDataSetInvalidated();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
//                        progress.dismiss();
                        error.printStackTrace();
                        Toast.makeText(activity, "Medicine reminder status updation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle args = activity.getIntent().getExtras();
                if(model.getDoctorId() != null)
                    args.putInt(PARAM.DOCTOR_ID, model.getDoctorId().intValue());
                args.putInt(PARAM.PATIENT_ID, model.getPatientId().intValue());
                args.putInt(PARAM.MEDICINE_ID, model.getMedicineId().intValue());
                if(model.getAppointmentId() != null)
                    args.putInt(PARAM.APPOINTMENT_ID, model.getAppointmentId().intValue());
                args.putInt(PARAM.LOGGED_IN_ID, args.getInt(PARAM.PROFILE_ID));
                activity.getIntent().putExtras(args);
                ParentFragment fragment = new PatientMedicinReminder();
                ((ParentActivity)activity).attachFragment(fragment);
                fragment.setArguments(args);
                FragmentManager fragmentManger = ((ParentActivity) activity).getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, PatientMedicinReminder.class.getName()).addToBackStack( PatientMedicinReminder.class.getName()).commit();
            }
        });

        return convertView;

    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        TextView text;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.header_all_appointment, parent, false);
            text = (TextView) convertView.findViewById(R.id.slot);
        }
        else
        {
            text = (TextView) convertView.findViewById(R.id.slot);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(appointments.get(position).getEndDate()));
        text.setText(new Integer(calendar.get(Calendar.YEAR)).toString());
        return convertView;
    }

    @Override
    public long getHeaderId(int position)
    {
        //return the first character of the country as ID because this is what headers are based upon
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(appointments.get(position).getEndDate()));
        return calendar.get(Calendar.YEAR);
    }

}
