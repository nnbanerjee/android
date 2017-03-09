package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.R;

import com.medico.application.MyApi;

/**
 * Created by MNT on 27-Mar-15.
 */
public class ShowPreviewDialog1 extends DialogFragment {
    Global global;
    ImageView viewImage;
    Button ok;
    String doctorId,patientId,appointmentDate,appointmentTime;
    SharedPreferences session;
    public MyApi api;


    public static ShowPreviewDialog1 newInstance() {
        return new ShowPreviewDialog1();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.show_image_dialog,container,false);
        viewImage = (ImageView)view.findViewById(R.id.preview_image);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

//        FileUpload upload = global.getUpload();
//        getDialog().setTitle(upload.getName());
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
//        System.out.println("Server url::::"+getResources().getString(R.string.image_base_url)+upload.getId());
//        new ImageLoadTask(getResources().getString(R.string.image_base_url)+upload.getId(), viewImage).execute();
//
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowPreviewDialog1.this.getDialog().cancel();
//            }
//        });

        return view;
    }


}
