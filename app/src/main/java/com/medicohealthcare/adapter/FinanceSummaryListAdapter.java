package com.medicohealthcare.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medicohealthcare.application.R;
import com.medicohealthcare.model.FinanceSummary;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class FinanceSummaryListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    final public static int NONE = 0;
    final public static int DAILY = 1;
    final public static int WEEKLY = 2;
    final public static int MONTHLY = 3;

    private LayoutInflater inflater;
    List<FinanceSummary> financeSummaries;
    int type;
    Activity context;
    Currency currency;

    public FinanceSummaryListAdapter(Activity context, List<FinanceSummary> financeSummaries, Currency currency, int type) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.financeSummaries = financeSummaries;
        this.type = type;
        this.currency = currency;
    }

    @Override
    public int getCount() {
        return financeSummaries.size();
    }

    @Override
    public Object getItem(int position) {
        return financeSummaries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.report_date_list, parent, false);
            holder.date = (TextView) convertView.findViewById(R.id.visit_date);
            holder.revenue = (TextView) convertView.findViewById(R.id.total_revenue);
            holder.rightArrow = (ImageView)convertView.findViewById(R.id.rightArrow);
            holder.summary = financeSummaries.get(position);
            convertView.setTag(holder);
//            holder.rightArrow.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    Bundle bundle = context.getIntent().getExtras();
//                    bundle.putLong("report_date",holder.summary.date);
//                    bundle.putInt("report_type", type);
//                    context.getIntent().putExtras(bundle);
//                    ParentFragment fragment = new ManageFinanceDetailsView();
//                    FragmentTransaction ft = context.getFragmentManager().beginTransaction();
//                    ft.add(R.id.service, fragment,ManageFinanceDetailsView.class.getName()).addToBackStack(ManageFinanceDetailsView.class.getName()).commit();
//
//                }
//            });
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;
//        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(financeSummaries.get(position).date));
        SimpleDateFormat format;
        switch (type)
        {
            case DAILY:
                format = new SimpleDateFormat("dd-MMMMMMMMM (EEEEEEEE)", Locale.getDefault());
                holder.date.setText(format.format(new Date(financeSummaries.get(position).date)) );
                break;
            case WEEKLY:
                format = new SimpleDateFormat("WW-MMMMMMMMM", Locale.getDefault());
                holder.date.setText(format.format(new Date(financeSummaries.get(position).date)) );
                break;
            case MONTHLY:
                format = new SimpleDateFormat("MMMMMMMMM", Locale.getDefault());
                holder.date.setText(format.format(new Date(financeSummaries.get(position).date)) );
                break;
            default:
                format = new SimpleDateFormat("dd-MMMMMMMMM", Locale.getDefault());
        }
//        SimpleDateFormat format = new SimpleDateFormat("dd-MMMMMMMMM", Locale.getDefault());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setCurrency(currency);
        nf.setMaximumFractionDigits(2);
        holder.revenue.setText(nf.format(financeSummaries.get(position).totalCost));

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_all_appointment, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.slot);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(financeSummaries.get(position).date));
        holder.text.setText(new Integer(calendar.get(Calendar.YEAR)).toString());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(financeSummaries.get(position).date));
        return calendar.get(Calendar.YEAR);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        private TextView date,revenue;
        private ImageView rightArrow;
        private FinanceSummary summary;
    }
}
