package com.medico.view.profile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.AppointmentFeedback;
import com.medico.model.ResponseCodeVerfication;
import com.medico.application.R;
import com.medico.view.home.ParentActivity;
import com.medico.view.home.ParentFragment;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 14-10-2015.
 */
public class FeedbackFragmentClinicAppointment extends ParentFragment
{
    CheckBox[] ratings = new CheckBox[5];
    CheckBox recommdation_text;
    RelativeLayout visitedLayout;
    RadioButton visited, notVisited;
    TextView feedbackDate;
    Button save;
    EditText review;
    String type;
    int visitedValue;
    boolean isPatient = false;

    AppointmentFeedback appointmentFeedback = new AppointmentFeedback();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback, container, false);
        setHasOptionsMenu(true);
        ratings[0] = (CheckBox) view.findViewById(R.id.rating_value1);
        ratings[1] = (CheckBox) view.findViewById(R.id.rating_value2);
        ratings[2] = (CheckBox) view.findViewById(R.id.rating_value3);
        ratings[3] = (CheckBox) view.findViewById(R.id.rating_value4);
        ratings[4] = (CheckBox) view.findViewById(R.id.rating_value5);
        CleckListener lister = new CleckListener();
        for(int m = 0; m < ratings.length; m++)
        {
            ratings[m].setOnClickListener(lister);
        }

        visitedLayout = (RelativeLayout) view.findViewById(R.id.visited_layout);
        visited = (RadioButton) view.findViewById(R.id.visited_radio);
        feedbackDate = (TextView) view.findViewById(R.id.feedback_date);
        notVisited = (RadioButton) view.findViewById(R.id.not_visited_radio);
        save = (Button) view.findViewById(R.id.save_feedback);
        review = (EditText) view.findViewById(R.id.reviews);
        recommdation_text = (CheckBox)view.findViewById(R.id.recommdation_check);
        Bundle bun= getArguments();
        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visited.setChecked(true);
                notVisited.setChecked(false);
                if(isPatient)
                    visitedLayout.setVisibility(View.VISIBLE);
                else
                    visitedLayout.setVisibility(View.INVISIBLE);
            }
        });
        notVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notVisited.setChecked(true);
                visited.setChecked(false);
                visitedLayout.setVisibility(View.INVISIBLE);
            }
        });
        Bundle bundle = getActivity().getIntent().getExtras();
        Integer role = bundle.getInt(PROFILE_ROLE);
//        if(bundle.getInt(LOGGED_IN_ID) == bundle.getInt(DOCTOR_ID))
        if(role.intValue() == DOCTOR)
        {
            isPatient = false;
            view.findViewById(R.id.visited_layout).setVisibility(View.INVISIBLE);
        }
        else {
            view.findViewById(R.id.visited_layout).setVisibility(View.VISIBLE);
            isPatient = true;
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                if(canBeSaved())
                {
                    saveAndSummary(appointmentFeedback);
                }

                }});

        return view;
    }

//    private void saveFeedbackForDoctor(AppointmentFeedback feedback)
//    {
//        api.setAppointmentStatus(feedback, new Callback<ResponseCodeVerfication>() {
//                @Override
//                public void success(ResponseCodeVerfication clinicAppointment, Response response) {
//                    Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
//                    getActivity().onBackPressed();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    error.printStackTrace();
//                    Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//    }
    private void save(AppointmentFeedback feedback)
    {
        api.setAppointmentVisitStatus(feedback, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication clinicAppointment, Response response) {
                Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveAndSummary(AppointmentFeedback feedback)
    {
        api.setAppointmentVisitStatus(feedback, new Callback<ResponseCodeVerfication>() {
            @Override
            public void success(ResponseCodeVerfication clinicAppointment, Response response) {
                Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                ParentFragment fragment = new DoctorAppointmentInformation();
                ((ParentActivity)getActivity()).attachFragment(fragment);
                fragment.setArguments(bun);
                FragmentManager fragmentManger = getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, "Doctor Consultations").addToBackStack(null).commit();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.add);
        menuItem.setTitle("SAVE");
    }

    @Override
    public boolean isChanged()
    {
        return appointmentFeedback.isChanged();
    }
    @Override
    public void update()
    {
        Bundle bundle = getActivity().getIntent().getExtras();
        appointmentFeedback.appointmentId = bundle.getInt(APPOINTMENT_ID);
        appointmentFeedback.setVisitReview(review.getText().toString());
        boolean isRatingSet = false;
        for(int i = ratings.length -1; i >=0; i--)
        {
            if(ratings[i].isChecked()) {
                appointmentFeedback.setVisitRating(new Integer(i+1).byteValue());
                isRatingSet = true;
                break;
            }
        }
        if(isRatingSet == false)
            appointmentFeedback.setVisitRating(new Integer(0).byteValue());

        appointmentFeedback.setRecommendation(recommdation_text.isChecked()?new Integer(1).byteValue():new Integer(0).byteValue());
        appointmentFeedback.setAppointmentVisitStatus(visited.isChecked()?new Integer(1).byteValue():new Integer(0).byteValue());
    }
    @Override
    public boolean save()
    {
        save(appointmentFeedback);
        return true;
    }

    @Override
    public boolean canBeSaved()
    {
        return appointmentFeedback.canBeSaved(isPatient);
    }

    @Override
    public void setEditable(boolean editable)
    {


    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        ManagePatientProfile activity = ((ManagePatientProfile) getActivity());
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
            case R.id.home: {

            }
            break;

        }
        return true;
    }

    class CleckListener implements View.OnClickListener {

        public void onClick(View v) {
//            final int id = v.getId();
//            switch (id) {
//                case R.id.rating_value1:
//                    for (int i = 0; i < ratings.length; i++) {
//
//                        if (i > 0 && ratings[i].isChecked())
//                            ratings[i].callOnClick();
//
//                    }
//                    break;
//                case R.id.rating_value2:
//                    for (int i = 0; i < ratings.length; i++) {
//                        if (i < 1 && !ratings[i].isChecked())
//                            ratings[i].callOnClick();
//                        else if (i > 1 && ratings[i].isChecked())
//                            ratings[i].callOnClick();
//
//                    }
//                    break;
//                // even more buttons here
//                case R.id.rating_value3:
//                    for (int i = 0; i < ratings.length; i++) {
//                        if (i < 2 && !ratings[i].isChecked())
//                            ratings[i].callOnClick();
//                        else if (i > 2 && ratings[i].isChecked())
//                            ratings[i].callOnClick();
//
//                    }
//                    break;
//                case R.id.rating_value4:
//                    for (int i = 0; i < ratings.length; i++) {
//                        if (i < 3 && !ratings[i].isChecked())
//                            ratings[i].callOnClick();
//                        else if (i > 13 && ratings[i].isChecked())
//                            ratings[i].callOnClick();
//
//                    }
//                    break;
//                // even more buttons here
//                case R.id.rating_value5:
//                    for (int i = 0; i < ratings.length; i++) {
//                        if (i < 4 && !ratings[i].isChecked())
//                            ratings[i].callOnClick();
//
//                    }
//                    break;
//            }
        }
    }

}
