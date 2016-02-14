package Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindnerves.meidcaldiary.Fragments.AddDelegation;
import com.mindnerves.meidcaldiary.Fragments.ShowChat;
import com.mindnerves.meidcaldiary.R;

import java.util.ArrayList;

import Model.Chat;

/**
 * Created by MNT on 07-Apr-15.
 */
public class ChatAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Chat> chatList;
    LayoutInflater inflater;
    ImageView image;
    TextView nameTv;
    Button chatButton;

    public ChatAdapter(Activity activity,ArrayList<Chat> chatList)
    {
        this.activity = activity;
        this.chatList = chatList;
    }
    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cv, ViewGroup parent) {
        if(inflater == null)
        {
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View convertView = cv;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.chat_element, null);

        image = (ImageView)convertView.findViewById(R.id.show_icon);
        nameTv = (TextView)convertView.findViewById(R.id.name_chat);

        chatButton = (Button)convertView.findViewById(R.id.msg_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Chat Button::::::");
                Fragment frag = new ShowChat();
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, frag,"show chat");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        if(chatList.get(position).getType().equals("D"))
        {
            image.setBackgroundResource(R.drawable.doctor_image);
        }
        else if(chatList.get(position).getType().equals("P"))
        {
            image.setBackgroundResource(R.drawable.patient);
        }
        else if(chatList.get(position).getType().equals("A"))
        {
            image.setBackgroundResource(R.drawable.assistant);
        }


        nameTv.setText(chatList.get(position).getName());
        return convertView;
    }
}
