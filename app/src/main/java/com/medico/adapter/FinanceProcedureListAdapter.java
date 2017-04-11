package com.medico.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.FinanceDetails;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by User on 6/29/15.
 */
public class FinanceProcedureListAdapter extends BaseAdapter  {

    final public static int NONE = 0;
    final public static int DAILY = 1;
    final public static int WEEKLY = 2;
    final public static int MONTHLY = 3;

    private LayoutInflater inflater;
    List<FinanceDetails.ProcedureSummary> financeSummaries;
    int type;
    Context context;

    public FinanceProcedureListAdapter(Context context, List<FinanceDetails.ProcedureSummary> financeSummaries, int type) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.financeSummaries = financeSummaries;
        this.type = type;
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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.report_procedure_list, parent, false);

            holder.procedureName = (TextView) convertView.findViewById(R.id.procedure_name);
            holder.revenue =(TextView) convertView.findViewById(R.id.total_revenue);
            holder.totalCost = (TextView) convertView.findViewById(R.id.cost_value);
            holder.totalDiscount = (TextView) convertView.findViewById(R.id.discount_value);
            holder.totalTax = (TextView) convertView.findViewById(R.id.tax_value);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        holder.procedureName.setText(financeSummaries.get(position).procedureName.toString());
        holder.revenue.setText(nf.format(financeSummaries.get(position).totalCost));
        holder.totalCost.setText(nf.format(financeSummaries.get(position).totalCost.doubleValue()-financeSummaries.get(position).totalDiscount.doubleValue()+financeSummaries.get(position).totalTax.doubleValue()));
        holder.totalDiscount.setText(nf.format(financeSummaries.get(position).totalDiscount));
        holder.totalTax.setText(nf.format(financeSummaries.get(position).totalTax));
        return convertView;
    }


    class ViewHolder
    {
        private TextView procedureName,revenue,totalCost,totalDiscount,totalTax;
    }
}
