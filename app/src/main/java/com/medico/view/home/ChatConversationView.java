package com.medico.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medico.adapter.ChatMessageListAdapter;
import com.medico.application.R;
import com.medico.model.Message;
import com.medico.model.MessageRequest;
import com.medico.model.ServerResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Narendra on 11-03-2016.
 */
public class ChatConversationView extends ParentFragment
{


    Button sendbutton;
    StickyListHeadersListView messageList;
    EditText sendText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.chat_conversation,
                container, false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);

        Bundle bundle = getActivity().getIntent().getExtras();
        final int profileId = bundle.getInt(PROFILE_ID);
        int role = bundle.getInt(PERSON_ROLE);
        int gender = bundle.getInt(PERSON_GENDER);
        final int Id = bundle.getInt(PERSON_ID);
        String url = bundle.getString(PERSON_URL);
        String name = bundle.getString(PERSON_NAME);
        textviewTitle.setText(name);
        switch(role)
        {
            case DOCTOR:
                textviewTitle.setBackground(getActivity().getResources().getDrawable(R.drawable.doctor_default));
                textviewTitle.setText("Dr. " + name);
            case PATIENT:
                textviewTitle.setBackground(getActivity().getResources().getDrawable(R.drawable.patient_default));
                textviewTitle.setText((gender==0?"Mr. ":"Ms.") + name);
            case ASSISTANT:
                textviewTitle.setBackground(getActivity().getResources().getDrawable(R.drawable.assistant_default));
                textviewTitle.setText((gender==0?"Mr. ":"Ms.") + name);
        }
        messageList = (StickyListHeadersListView)view.findViewById(R.id.chat_messages);
        sendText = (EditText)view.findViewById(R.id.sendtext);
        sendbutton = (Button)view.findViewById(R.id.sendbutton);
        hideBusy();
        sendbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                api.sendMessages(new Message(sendText.getText().toString(),profileId,Id ),new Callback<ServerResponse>()
                {
                    @Override
                    public void success(ServerResponse serverResponse, Response response)
                    {
                        if(serverResponse.status == 1)
                        {
                            System.out.print("Message is successfully sent");
                            sendText.setText("");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {

                        error.printStackTrace();
                    }
                });
            }
        });
        return view;

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        String personName = bundle.getString("person_name");

        int profileId = bundle.getInt(PROFILE_ID);
        int role = bundle.getInt(PERSON_ROLE);
        int gender = bundle.getInt(PERSON_GENDER);
        int Id = bundle.getInt(PERSON_ID);
        String url = bundle.getString(PERSON_URL);
        String profileName = bundle.getString(PERSON_NAME);
        api.getMessages(new MessageRequest(profileId, Id, 1, 10),new Callback<List<Message>>()
        {
            @Override
            public void success(List<Message> messages, Response response)
            {
                ChatMessageListAdapter adapter = new ChatMessageListAdapter(getActivity(), messages);
                messageList.setAdapter(adapter);

            }

            @Override
            public void failure(RetrofitError error)
            {

                error.printStackTrace();
            }
        });
    }

}
