package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medico.model.ResponseCodeVerfication;
import com.mindnerves.meidcaldiary.R;

import java.util.List;

import com.medico.application.MyApi;
import Model.FileUpload;
import Model.RemoveVisitDocument;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by User on 7/13/15.
 */
public class DocumentAdapter extends BaseAdapter {
    Activity activity;
    List<FileUpload> fileList;
    private LayoutInflater inflater;
    public SharedPreferences session;
    String type;
    public MyApi api;
    private String loggedInUserId;
    public DocumentAdapter(Activity activity, List<FileUpload> fileList,String type) {
        this.activity = activity;
        this.fileList = fileList;
        this.type = type;
        session = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggedInUserId =  session.getString("id", "0") ;
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
    public View getView(final int position, View cv, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.document_element, null);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
        TextView fileName = (TextView) convertView.findViewById(R.id.document_name);
        final ImageView image = (ImageView) convertView.findViewById(R.id.document_image);
        final TextView clinicName = (TextView) convertView.findViewById(R.id.clinic_name);
        final TextView doc_category = (TextView) convertView.findViewById(R.id.category_value);
        final ImageView close = (ImageView)convertView.findViewById(R.id.remove_document);
        clinicName.setText(fileList.get(position).getClinicName());
        fileName.setText(fileList.get(position).getFileName());
        doc_category.setText(fileList.get(position).getCategory());
        if (fileList.get(position).getType().equalsIgnoreCase("0")) {
            image.setImageResource(R.drawable.pdf);
            System.out.println("I am in condition pdf :::::::");
        } else if (fileList.get(position).getType().equalsIgnoreCase("1")   ) {
            image.setImageResource(R.drawable.doc);
            System.out.println("I am in condition doc :::::::");
        }

        if(loggedInUserId.equalsIgnoreCase(fileList.get(position).getPersonId())){
            close.setVisibility(View.VISIBLE);
        }else{
            close.setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.removePatientVisitDocuments(new RemoveVisitDocument(fileList.get(position).getFileId(),loggedInUserId),  new Callback<ResponseCodeVerfication>() {
                    @Override
                    public void success(ResponseCodeVerfication s, Response response) {
                        Toast.makeText(activity, "File Deleted Successfully", Toast.LENGTH_SHORT).show();
                        fileList.remove(position);
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
