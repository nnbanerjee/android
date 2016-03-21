package com.mindnerves.meidcaldiary.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindnerves.meidcaldiary.Global;
import com.mindnerves.meidcaldiary.ImageLoadTask;
import com.mindnerves.meidcaldiary.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import Application.ImageUtil;
import Application.MyApi;
import Model.FileUpload;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by MNT on 27-Mar-15.
 */
public class ShowPreviewDialog extends DialogFragment {
    Global global;
    ImageView viewImage;
    Button ok;
    String doctorId,patientId,appointmentDate,appointmentTime;
    SharedPreferences session;
    public MyApi api;


    static ShowPreviewDialog newInstance() {
        return new ShowPreviewDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.show_image_dialog,container,false);
        viewImage = (ImageView)view.findViewById(R.id.preview_image);
        ok = (Button)view.findViewById(R.id.ok);
        global = (Global) getActivity().getApplicationContext();
        session = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        FileUpload upload = global.getUpload();
        getDialog().setTitle(upload.getName());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        System.out.println("Server url::::"+getResources().getString(R.string.image_base_url)+upload.getId());
        new ImageLoadTask(getResources().getString(R.string.image_base_url)+upload.getId(), viewImage).execute();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPreviewDialog.this.getDialog().cancel();
            }
        });

        return view;
    }


}
