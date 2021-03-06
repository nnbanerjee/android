package com.medicohealthcare.view.home;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.medicohealthcare.adapter.ChatPersonListAdapter;
import com.medicohealthcare.application.R;
import com.medicohealthcare.model.Person;
import com.medicohealthcare.model.ProfileId;
import com.medicohealthcare.service.Constants;
import com.medicohealthcare.util.MedicoCustomErrorHandler;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MNT on 07-Apr-15.
 */

//Doctor Login
public class ChatPersonListView extends ParentFragment {

    ListView listView;
    ChatPersonListAdapter adapter;
    MessageStateReceiver mDownloadStateReceiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.list_view,container,false);
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getString(R.string.chat_person_list));
        listView = (ListView) view.findViewById(R.id.doctorListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
//                setHasOptionsMenu(false);
                Bundle bun = getActivity().getIntent().getExtras();
                Person profile = (Person)adapterView.getAdapter().getItem(i);
                ParentFragment fragment = new ChatConversationView();
                bun.putString(PERSON_NAME, profile.getName());
                bun.putInt(PERSON_GENDER, profile.getGender().intValue());
                bun.putString(PERSON_URL, profile.getImageUrl());
                bun.putInt(PERSON_ROLE, profile.getId().intValue());
                bun.putInt(PERSON_ID, profile.getId().intValue());
                getActivity().getIntent().putExtras(bun);
                FragmentManager fragmentManger = getActivity().getFragmentManager();
                fragmentManger.beginTransaction().add(R.id.service, fragment, ChatConversationView.class.getName()).addToBackStack(ChatConversationView.class.getName()).commit();
            }
        });
        registerChatMessage();
        return view;
    }

    //This will show all patients list for logged in doctor
    @Override
    public void onStart()
    {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        showBusy();
        Integer loggedinUserId = bundle.getInt(LOGGED_IN_ID);
        Integer profileId = bundle.getInt(PROFILE_ID);
        Integer profileType = bundle.getInt(PROFILE_TYPE);
        api.getChatPersonList(new ProfileId(profileId), new Callback<List<Person>>()
        {
            @Override
            public void success(final List<Person> chatPersonList, Response response)
            {
                adapter = new ChatPersonListAdapter(getActivity(), chatPersonList);
                listView.setAdapter(adapter);
                hideBusy();
            }

            @Override
            public void failure(RetrofitError error)
            {

                hideBusy();
                new MedicoCustomErrorHandler(getActivity()).handleError(error);
            }
        });
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getString(R.string.chat_person_list));
        registerChatMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
        registerChatMessage();
        TextView textviewTitle = (TextView) getActivity().findViewById(R.id.actionbar_textview);
        textviewTitle.setText(getActivity().getString(R.string.chat_person_list));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        deregisterChatMessage();

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        menu.clear();
//        inflater.inflate(R.menu.menu, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.add: {
//                setHasOptionsMenu(false);
//                Bundle bun = getActivity().getIntent().getExtras();
//                bun.putInt(PROFILE_ID,0);
//                getActivity().getIntent().putExtras(bun);
//                ParentFragment fragment = new PersonProfileEditView();
//                ((ManagePersonSettings)getActivity()).fragmentList.add(fragment);
//                fragment.setArguments(bun);
//                FragmentManager fragmentManger = getActivity().getFragmentManager();
//                fragmentManger.beginTransaction().add(R.id.service, fragment, PersonProfileEditView.class.getName()).addToBackStack(PersonProfileEditView.class.getName()).commit();
//
//            }
//            break;
//        }
//        return true;
//    }

    public void registerChatMessage()
    {
        if(mDownloadStateReceiver == null)
        {
            IntentFilter statusIntentFilter = new IntentFilter();
            statusIntentFilter.addAction(Constants.NEW_MESSAGE_ARRIVED);
            // Instantiates a new DownloadStateReceiver
            mDownloadStateReceiver = new MessageStateReceiver();
            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
        }
    }
    public void deregisterChatMessage()
    {
        if(mDownloadStateReceiver != null)
        {
            // Registers the DownloadStateReceiver and its intent filters
            LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).unregisterReceiver(mDownloadStateReceiver);
            mDownloadStateReceiver = null;
        }

    }
    public class MessageStateReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        public MessageStateReceiver()
        {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(adapter != null)
            {
                Bundle bundle = intent.getExtras();
                int[] ids = bundle.getIntArray(Constants.NEW_MESSAGE_IDS);
                int[] numberOfMessages = bundle.getIntArray(Constants.NEW_MESSAGE_NUMBERS);
                adapter.setNumberOfMessages(ids, numberOfMessages);
                Parcelable state = listView.onSaveInstanceState();
                adapter.notifyDataSetInvalidated();
                listView.onRestoreInstanceState(state);
            }

        }

    }


}
