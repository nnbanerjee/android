package com.medico.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.application.R;
import com.medico.model.FileUpload1;
import com.medico.model.RemoveVisitDocument1;
import com.medico.model.ResponseCodeVerfication;
import com.medico.util.PARAM;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by User on 7/13/15.
 */
public class DocumentAdapter extends HomeAdapter {
    Activity activity;
    List<FileUpload1> fileList;
    private LayoutInflater inflater;
    public SharedPreferences session;
    String type;
    String[] document_category = {"Prescription","DiagnosticTest"};
    String[] document_subcategory = {"X-Ray","CT SCAN"};
    public DocumentAdapter(Activity activity, List<FileUpload1> fileList, String type)
    {
        super(activity);
        this.activity = activity;
        this.fileList = fileList;
        this.type = type;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.document_element, null);
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(activity.getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        api = restAdapter.create(MyApi.class);
        TextView fileName = (TextView) convertView.findViewById(R.id.document_name);
        TextView category = (TextView)convertView.findViewById(R.id.category_value);
        TextView addedBy = (TextView) convertView.findViewById(R.id.added_by_value);
        TextView date = (TextView)convertView.findViewById(R.id.date_value);
        TextView type = (TextView)convertView.findViewById((R.id.type_value));
        TextView clinic = (TextView)convertView.findViewById(R.id.clinic_value);
        ImageView image = (ImageView) convertView.findViewById(R.id.document_image);
        ImageView close = (ImageView)convertView.findViewById(R.id.remove_document);

        FileUpload1 file = fileList.get(position);
        fileName.setText(file.fileName);
        category.setText(document_category[file.category]);
        addedBy.setText(file.personName);
        Date uploaddate = new Date(file.date);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        date.setText(dateFormat.format(uploaddate));
        type.setText(document_subcategory[file.type]);
        clinic.setText(file.clinicName);
        Bundle bundle = activity.getIntent().getExtras();
        int id = bundle.getInt(PARAM.LOGGED_IN_ID);
        close.setEnabled(file.personId==bundle.getInt(PARAM.LOGGED_IN_ID)?true:false);

        if (file.type.intValue() == 0)
        {
            image.setImageResource(R.drawable.document_pdf);
        } else if (file.type.intValue() == 1   ) {
            image.setImageResource(R.drawable.document_png);
        }
        close.setTag(fileList.get(position));


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FileUpload1 file = (FileUpload1)v.getTag();
                Bundle bundle = activity.getIntent().getExtras();
                Integer id = bundle.getInt(PARAM.LOGGED_IN_ID);
                api.removePatientVisitDocuments1(new RemoveVisitDocument1(file.fileId, id),  new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication s, Response response) {
                        Toast.makeText(activity, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                        fileList.remove(file);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(activity, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return convertView;
    }
}
