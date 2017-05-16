package com.medico.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medico.application.R;
import com.medico.model.Message;
import com.medico.util.PARAM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by User on 6/29/15.
 */
public class ChatMessageListAdapter extends HomeAdapter implements StickyListHeadersAdapter {

    private LayoutInflater inflater;
    List<Message> messages;

    public ChatMessageListAdapter(Activity activity, List<Message> messages)
    {
        super(activity);
        inflater = LayoutInflater.from(activity);
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        Bundle bundle = activity.getIntent().getExtras();
        Integer profileId = bundle.getInt(PARAM.PROFILE_ID);
        Message message = messages.get(position);
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_message, parent, false);

            if(message.senderId.intValue() == profileId.intValue())
            {
                holder.messageText = (TextView) convertView.findViewById(R.id.text_message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.tick = (ImageView)convertView.findViewById(R.id.message_tick);
            }
            else
            {
                RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.layout) ;
                RelativeLayout layout1 = (RelativeLayout)convertView.findViewById(R.id.layout1);
                layout.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
                holder.messageText = (TextView) convertView.findViewById(R.id.text_message1);
                holder.time = (TextView) convertView.findViewById(R.id.time1);
                holder.tick = (ImageView)convertView.findViewById(R.id.message_tick1);
            }
            convertView.setTag(holder);
        }
        else
        {
            RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.layout) ;
            RelativeLayout layout1 = (RelativeLayout)convertView.findViewById(R.id.layout1);
            holder = (ViewHolder) convertView.getTag();
            if(message.senderId.intValue() == profileId.intValue())
            {
                layout.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                holder.messageText = (TextView) convertView.findViewById(R.id.text_message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.tick = (ImageView)convertView.findViewById(R.id.message_tick);
            }
            else
            {
                layout.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
                holder.messageText = (TextView) convertView.findViewById(R.id.text_message1);
                holder.time = (TextView) convertView.findViewById(R.id.time1);
                holder.tick = (ImageView)convertView.findViewById(R.id.message_tick1);
            }
        }


        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        holder.messageText.setText(message.message);
        holder.time.setText(format.format(message.date));
        holder.tick.setVisibility(message.isRead==1?View.VISIBLE:View.INVISIBLE);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_chat, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.slot);
            convertView.setTag(holder);
        }
        else
        {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM");
//        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.LONG);
        holder.text.setText(format.format(messages.get(position).date));
        return convertView;
    }

    @Override
    public long getHeaderId(int position)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(messages.get(position).date));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime().getTime();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder
    {
        private TextView messageText,time;
        private ImageView tick;
    }
}
