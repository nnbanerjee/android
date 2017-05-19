/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.medico.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.medico.application.MyApi;
import com.medico.model.ChatMessageCounts;
import com.medico.model.MessageRequest;
import com.medico.util.PARAM;
import com.medico.util.ServerConnectionAdapter;
import com.medico.view.home.HomeActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This service pulls RSS content from a web site URL contained in the incoming Intent (see
 * onHandleIntent()). As it runs, it broadcasts its status using LocalBroadcastManager; any
 * component that wants to see the status should implement a subclass of BroadcastReceiver and
 * register to receive broadcast Intents with category = CATEGORY_DEFAULT and action
 * Constants.BROADCAST_ACTION.
 *
 */
public class ChatServer extends IntentService implements PARAM
{
    // Used to write to the system log from this class.
    public static final String LOG_TAG = "RSSPullService";

    private MyApi api;

    // Defines and instantiates an object for handling status updates.
//    private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);
    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public ChatServer()
    {

        super("ChatService");
        ServerConnectionAdapter adapter = ServerConnectionAdapter.getServerAdapter(null);
        api = adapter.getServerAPI();
        setIntentRedelivery(true);
    }

    /**
     * In an IntentService, onHandleIntent is run on a background thread.  As it
     * runs, it broadcasts its current status using the LocalBroadcastManager.
     * @param workIntent The Intent that starts the IntentService. This Intent contains the
     * URL of the web site from which the RSS parser gets data.
     */
    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        Cursor cursor = null;

        /*
         * A block that tries to connect to the Picasa featured picture URL passed as the "data"
         * value in the incoming Intent. The block throws exceptions (see the end of the block).
         */
           // If the connection is an HTTP connection, continue
            if (api != null)
            {

                // Broadcasts an Intent indicating that processing has started.
//                mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_STARTED);

                /*
                 * Queries the content provider to see if this URL was read previously, and when.
                 * The content provider throws an exception if the URI is invalid.
                 */
                Bundle bundle = workIntent.getExtras();
                int profileId = bundle.getInt(PARAM.PROFILE_ID);

                api.getNewMessageCountsByRecipient(new MessageRequest(profileId),new Callback<List<ChatMessageCounts>>()
                {
                    @Override
                    public void success(List<ChatMessageCounts> messagecount, Response response)
                    {
                        int[] ids = new int[messagecount.size()];
                        int[] numberOfMessages = new int[messagecount.size()];
                        for(int i = 0; i < ids.length; i++ )
                        {
                            ids[i] = messagecount.get(i).senderId;
                            numberOfMessages[i] = messagecount.get(i).noOfNewMessages;
                        }
                        Intent localIntent = new Intent(Constants.NEW_MESSAGE_ARRIVED);
                        Bundle bundle = new Bundle();
                        bundle.putIntArray(Constants.NEW_MESSAGE_IDS,ids);
                        bundle.putIntArray(Constants.NEW_MESSAGE_NUMBERS,numberOfMessages);
                        localIntent.putExtras(bundle);
                        localIntent.setAction(Constants.NEW_MESSAGE_ARRIVED);
                        LocalBroadcastManager.getInstance(HomeActivity.getParentAtivity()).sendBroadcast(localIntent);
                    }

                    @Override
                    public void failure(RetrofitError error)
                    {
                        error.printStackTrace();
                    }
                });

                // Reports that the feed retrieval is complete.
//                mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_COMPLETE);
            }
    }

}
