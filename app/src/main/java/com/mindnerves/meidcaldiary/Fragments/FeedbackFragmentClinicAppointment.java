package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Model.AppointmentStatus;

import com.medico.model.ResponseCodeVerfication;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 14-10-2015.
 */
public class FeedbackFragmentClinicAppointment extends Fragment {
    ImageView star1, star2, star3, star4, star5;
    int flagStar1, flagStar2, flagStar3, flagStar4, flagStar5;
    RelativeLayout visitedLayout;
    RadioButton visited, notVisited;
    SharedPreferences session;
    String patientId = "";
    String doctorId = "";
    String clinicId = "";
    String appointmentDate = "";
    String appointmentTime = "";
    TextView feedbackDate;
    public MyApi api;
    Button save,back;
    int starCount = 0;
    EditText review;
    String type;
    int visitedValue;
    String appointmnetId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback, container, false);
        star1 = (ImageView) view.findViewById(R.id.first_star);
        star2 = (ImageView) view.findViewById(R.id.second_star);
        star3 = (ImageView) view.findViewById(R.id.third_star);
        star4 = (ImageView) view.findViewById(R.id.fourth_star);
        star5 = (ImageView) view.findViewById(R.id.fifth_star);
        visitedLayout = (RelativeLayout) view.findViewById(R.id.visited_layout);
        visited = (RadioButton) view.findViewById(R.id.visited_radio);
        feedbackDate = (TextView) view.findViewById(R.id.feedback_date);
        notVisited = (RadioButton) view.findViewById(R.id.not_visited_radio);
        save = (Button) view.findViewById(R.id.save_feedback);
        back = (Button) getActivity().findViewById(R.id.back_button);
        review = (EditText) view.findViewById(R.id.reviews);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);

        flagStar1 = 0;
        flagStar2 = 0;
        flagStar3 = 0;
        flagStar4 = 0;
        flagStar5 = 0;
        visited.setChecked(true);
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        type = session.getString("loginType", null);
       /* if (type.equalsIgnoreCase("Patient")) {
            userId = session.getString("sessionID", "");
        } else if (type.equalsIgnoreCase("Doctor")) {
            userId = session.getString("Doctor_patientEmail", "");
        }
        */
        Bundle bun= getArguments();

        appointmnetId=bun.getString("selectedAppointmentId");
        patientId = session.getString("sessionID", null);
        doctorId = session.getString("patient_doctorId", null);
        clinicId = session.getString("patient_clinicId", null);
        appointmentDate = session.getString("patient_appointment_date", null);
        appointmentTime = session.getString("patient_appointment_time", null);
        System.out.println("DoctorId= " + doctorId);
        System.out.println("patientId= " + patientId);
        System.out.println("clinicId= " + clinicId);
        System.out.println("appointmentDate= " + appointmentDate);
        System.out.println("appointmentTime= " + appointmentTime);
        feedbackDate.setText(appointmentDate + " " + appointmentTime);
        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visited.setChecked(true);
                notVisited.setChecked(false);
                visitedLayout.setVisibility(View.VISIBLE);
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
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagStar1 == 0) {
                    firstStar();
                    flagStar1 = 1;
                } else {
                    showUnSelected();
                    flagStar1 = 0;
                }
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagStar2 == 0) {
                    secondStar();
                    flagStar2 = 1;
                } else {
                    showUnSelected();
                    flagStar2 = 0;
                }

            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagStar3 == 0) {
                    thirdStar();
                    flagStar3 = 1;
                } else {
                    showUnSelected();
                    flagStar3 = 0;
                }
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagStar4 == 0) {
                    fourthStar();
                    flagStar4 = 1;
                } else {
                    showUnSelected();
                    flagStar4 = 0;
                }
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagStar5 == 0) {
                    fifthStar();
                    flagStar5 = 1;
                } else {
                    showUnSelected();
                    flagStar5 = 0;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean radioButtonValue = visited.isChecked();
                Boolean radioButtonNotVisitedValue = notVisited.isChecked();
                if(radioButtonValue)
                    visitedValue=1;
                else if (radioButtonNotVisitedValue)
                    visitedValue=0;
                else
                    visitedValue=2;

                if (type.equalsIgnoreCase("Patient")) {
                    //userId = session.getString("sessionID", "");w
                } else if (type.equalsIgnoreCase("Doctor")) {
                   // userId = session.getString("Doctor_patientEmail", "");
                    //0=not visited 1=visited 2=unknown
                    AppointmentStatus status=new AppointmentStatus(appointmnetId,""+visitedValue);
                    api.setAppointmentStatus(status, new Callback<ResponseCodeVerfication>() {
                        @Override
                        public void success(ResponseCodeVerfication clinicAppointment, Response response) {
                            Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
                           // goBack();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
               /* Boolean radioButtonValue = visited.isChecked();
                if (radioButtonValue) {
                    FeedbackVM vm = new FeedbackVM();
                    vm.doctorId = doctorId;
                    vm.patientId = patientId;
                    vm.clinicId = clinicId;
                    vm.appointmentDate = appointmentDate;
                    vm.appointmentTime = appointmentTime;
                    vm.reviews = review.getText().toString();
                    if (flagStar1 == 1) {
                        starCount = 1;
                    } else if (flagStar2 == 1) {
                        starCount = 2;
                    } else if (flagStar3 == 1) {
                        starCount = 3;
                    } else if (flagStar4 == 1) {
                        starCount = 4;
                    } else {
                        starCount = 5;
                    }
                    vm.star = "" + starCount;
                    vm.visited = "visited";
                    api.saveFeedbackPatient(vm, new Callback<ClinicAppointment>() {
                        @Override
                        public void success(ClinicAppointment clinicAppointment, Response response) {
                            Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
                            goBack();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    FeedbackVM vm = new FeedbackVM();
                    vm.doctorId = doctorId;
                    vm.patientId = patientId;
                    vm.clinicId = clinicId;
                    vm.appointmentDate = appointmentDate;
                    vm.appointmentTime = appointmentTime;
                    vm.visited = "Not visited";
                    api.saveFeedbackPatient(vm, new Callback<ClinicAppointment>() {
                        @Override
                        public void success(ClinicAppointment clinicAppointment, Response response) {
                            Toast.makeText(getActivity(), "Feedback Saved!!!!!!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        return view;
    }


    public void showUnSelected() {
        star1.setBackgroundResource(R.drawable.unselected_star);
        star2.setBackgroundResource(R.drawable.unselected_star);
        star3.setBackgroundResource(R.drawable.unselected_star);
        star4.setBackgroundResource(R.drawable.unselected_star);
        star5.setBackgroundResource(R.drawable.unselected_star);
    }

    public void firstStar() {
        star1.setBackgroundResource(R.drawable.selected_star);
        star2.setBackgroundResource(R.drawable.unselected_star);
        star3.setBackgroundResource(R.drawable.unselected_star);
        star4.setBackgroundResource(R.drawable.unselected_star);
        star5.setBackgroundResource(R.drawable.unselected_star);
    }

    public void secondStar() {
        star1.setBackgroundResource(R.drawable.selected_star);
        star2.setBackgroundResource(R.drawable.selected_star);
        star3.setBackgroundResource(R.drawable.unselected_star);
        star4.setBackgroundResource(R.drawable.unselected_star);
        star5.setBackgroundResource(R.drawable.unselected_star);
    }

    public void thirdStar() {
        star1.setBackgroundResource(R.drawable.selected_star);
        star2.setBackgroundResource(R.drawable.selected_star);
        star3.setBackgroundResource(R.drawable.selected_star);
        star4.setBackgroundResource(R.drawable.unselected_star);
        star5.setBackgroundResource(R.drawable.unselected_star);
    }

    public void fourthStar() {
        star1.setBackgroundResource(R.drawable.selected_star);
        star2.setBackgroundResource(R.drawable.selected_star);
        star3.setBackgroundResource(R.drawable.selected_star);
        star4.setBackgroundResource(R.drawable.selected_star);
        star5.setBackgroundResource(R.drawable.unselected_star);
    }

    public void fifthStar() {
        star1.setBackgroundResource(R.drawable.selected_star);
        star2.setBackgroundResource(R.drawable.selected_star);
        star3.setBackgroundResource(R.drawable.selected_star);
        star4.setBackgroundResource(R.drawable.selected_star);
        star5.setBackgroundResource(R.drawable.selected_star);
    }
    public void goBack(){

        Fragment fragment = new ClinicAllPatientFragment();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_details, fragment, "Doctor Consultations").addToBackStack(null).commit();
       /* Fragment fragment = new PatientAppointmentStatus();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();*/
    }
}
