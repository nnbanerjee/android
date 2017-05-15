package com.medico.adapter;

import android.app.Activity;
import android.graphics.Color;
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

        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_message, parent, false);
            holder.layout = (RelativeLayout)convertView.findViewById(R.id.layout);
            holder.message = messages.get(position);
            holder.messageText = (TextView) convertView.findViewById(R.id.text_message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.tick = (ImageView)convertView.findViewById(R.id.message_tick);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Bundle bundle = activity.getIntent().getExtras();
        Integer profileId = bundle.getInt(PARAM.PROFILE_ID);
        Message message = messages.get(position);
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        holder.messageText.setText(message.message);
        holder.time.setText(format.format(message.date));
        holder.tick.setVisibility(message.isRead==1?View.VISIBLE:View.INVISIBLE);
        holder.message = message;
        if(message.senderId.intValue() == profileId.intValue())
        {
            holder.layout.setLeft(50);
            holder.layout.setRight(0);
            holder.layout.setBackgroundColor(Color.WHITE);
        }
        else
        {
            holder.layout.setLeft(0);
            holder.layout.setRight(50);
            holder.layout.setBackgroundResource(R.color.medico_green1);
        }
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
        }
        else
        {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        SimpleDateFormat format = new SimpleDateFormat("DD-MMM");
        holder.text.setText(format.format(messages.get(position).date));
        return convertView;
    }

    @Override
    public long getHeaderId(int position)
    {

        return messages.get(position).date;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder
    {
        private Message message;
        private RelativeLayout layout;
        private TextView messageText,time;
        private ImageView tick;
    }
}
