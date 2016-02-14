package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.mindnerves.meidcaldiary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Application.MyApi;
import Model.FileUpload;
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
    String type;
    public MyApi api;
    public DocumentAdapter(Activity activity, List<FileUpload> fileList,String type) {
        this.activity = activity;
        this.fileList = fileList;
        this.type = type;
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
        TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
        final ImageView image = (ImageView) convertView.findViewById(R.id.document_image);
        final TextView clinicName = (TextView) convertView.findViewById(R.id.clinic_name);
        final TextView doc_category = (TextView) convertView.findViewById(R.id.doc_category);
        final ImageView close = (ImageView)convertView.findViewById(R.id.close_document);
        clinicName.setText(fileList.get(position).getClinicName());
        fileName.setText(fileList.get(position).getName());
        doc_category.setText(fileList.get(position).getCategory());
        if (fileList.get(position).getDocumentType().equalsIgnoreCase(".pdf")) {
            image.setImageResource(R.drawable.pdf_icon);
            System.out.println("I am in condition pdf :::::::");
        } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".doc") || (fileList.get(position).getDocumentType().equalsIgnoreCase(".docx"))) {
            image.setImageResource(R.drawable.doc);
            System.out.println("I am in condition doc :::::::");
        } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".xls") || (fileList.get(position).getDocumentType().equalsIgnoreCase(".xlsx"))) {
            image.setImageResource(R.drawable.xls);
        } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".txt")) {
            image.setImageResource(R.drawable.txt32);
        } else {
            image.setImageResource(R.drawable.images_icon);
        }
        if(type.equalsIgnoreCase(fileList.get(position).getType())){
            close.setVisibility(View.VISIBLE);
        }else{
            close.setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.deleteFile(""+fileList.get(position).getId(),new retrofit.Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Toast.makeText(activity,"File Deleted Successfully",Toast.LENGTH_SHORT).show();
                        fileList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(activity,"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return convertView;
    }
}
