package com.mindnerves.meidcaldiary.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import Model.DoctorSearchResponse;

/**
 * Created by MNT on 07-Apr-15.
 */
public class PatientDocumentsAppointment extends Fragment {

    String doctorId = "";
    String doctor_email;
    MyApi api;
    SharedPreferences session;
    Button drawar_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patient_documents, container,false);

        Global global = (Global) getActivity().getApplicationContext();
        List<DoctorSearchResponse> doctorSearchResponses = global.getDoctorSearchResponses();

        drawar_button = (Button) view.findViewById(R.id.drawar_button);

       /* drawar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater
                        = (LayoutInflater) getActivity().getBaseContext().getSystemService( getActivity().LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup_document, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                        Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                        btnDismiss.setOnClickListener(new Button.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                popupWindow.dismiss();
                            }});

                         popupWindow.showAsDropDown(drawar_button, 50, -30);

            }
        });*/

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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    goToBack();
                    return true;
                }
                return false;
            }
        });
    }


    public void  goToBack(){
        TextView globalTv = (TextView)getActivity().findViewById(R.id.show_global_tv);
        globalTv.setText("Medical Diary");
        Fragment fragment = new PatientAllDoctors();
        FragmentManager fragmentManger = getFragmentManager();
        fragmentManger.beginTransaction().replace(R.id.content_frame,fragment,"Doctor Consultations").addToBackStack(null).commit();
        final Button back = (Button)getActivity().findViewById(R.id.back_button);
        back.setVisibility(View.INVISIBLE);
    }
}
