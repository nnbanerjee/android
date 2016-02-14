package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.R;

import java.util.List;

import Model.ManageFinance;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class ReportFinanceAdapter extends BaseAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    List<ManageFinance> appointment;
    public SharedPreferences session;
    Context context;

    public ReportFinanceAdapter(Context context, List<ManageFinance> appointment) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        countries = context.getResources().getStringArray(R.array.countries);
        this.appointment = appointment;
        session = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return appointment.size();
    }

    @Override
    public Object getItem(int position) {
        return appointment.get(position);
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
            convertView = inflater.inflate(R.layout.report_list_item, parent, false);

            holder.procedureText = (TextView) convertView.findViewById(R.id.procedureText);
            holder.costText = (TextView) convertView.findViewById(R.id.costText);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
     
        holder.procedureText.setText(appointment.get(position).getAppointmentDate());
        holder.costText.setText(appointment.get(position).getTotalInvoice());

        return convertView;
    }

    class ViewHolder {
        private TextView procedureText,costText;
    }
}
