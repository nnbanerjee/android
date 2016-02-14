package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.FileUpload;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 03-11-2015.
 */
public class StickyDocumentAdapter extends BaseAdapter implements StickyListHeadersAdapter{
    Context context;
    List<FileUpload> fileList;
    private LayoutInflater inflater;

    public StickyDocumentAdapter(Context context,List<FileUpload> fileList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.fileList = fileList;
    }
    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (view == null) {
            holder = new HeaderViewHolder();
            view = inflater.inflate(R.layout.header_all_appointment, viewGroup, false);
            holder.text = (TextView) view.findViewById(R.id.slot);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        holder.text.setText(fileList.get(i).getType());
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return fileList.get(i).getType().charAt(0);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.document_element, parent, false);
            holder.fileName = (TextView)convertView.findViewById(R.id.file_name);
            holder.image = (ImageView)convertView.findViewById(R.id.document_image);
            holder.close = (ImageView)convertView.findViewById(R.id.close_document);
            holder.clinicName = (TextView)convertView.findViewById(R.id.clinic_name);
            holder.doc_category = (TextView)convertView.findViewById(R.id.doc_category);
            holder.fileName.setText(fileList.get(position).getFileName());
            holder.clinicName.setText(fileList.get(position).getClinicName());
            holder.doc_category.setText(fileList.get(position).getCategory());
            if (fileList.get(position).getDocumentType().equalsIgnoreCase(".pdf")) {
                holder.image.setImageResource(R.drawable.pdf_icon);
                System.out.println("I am in condition pdf :::::::");
            } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".doc") || (fileList.get(position).getDocumentType().equalsIgnoreCase(".docx"))) {
                holder.image.setImageResource(R.drawable.doc);
                System.out.println("I am in condition doc :::::::");
            } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".xls") || (fileList.get(position).getDocumentType().equalsIgnoreCase(".xlsx"))) {
                holder.image.setImageResource(R.drawable.xls);
            } else if (fileList.get(position).getDocumentType().equalsIgnoreCase(".txt")) {
                holder.image.setImageResource(R.drawable.txt32);
            } else {
                holder.image.setImageResource(R.drawable.images_icon);
            }
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        return null;
    }
    class HeaderViewHolder {
        TextView text;
    }
    class ViewHolder {
        private TextView fileName,clinicName,doc_category;
        private ImageView image,close;
    }
}
