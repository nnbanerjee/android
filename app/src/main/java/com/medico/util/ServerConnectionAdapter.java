package com.medico.util;

import android.app.Activity;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Created by Narendra on 13-04-2017.
 */

public class ServerConnectionAdapter
{
    private Activity activity;
    MyApi api;
    private static ServerConnectionAdapter adapter = null;

    private ServerConnectionAdapter(Activity activity)
    {
        CookieManager cookieManager = new CookieManager(null, null);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCookieHandler(cookieManager);
        Client client = new OkClient(okHttpClient);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(client).build();

        api = restAdapter.create(MyApi.class);
    }
    public static ServerConnectionAdapter getServerAdapter(Activity activity)
    {
        if(adapter == null)
            adapter = new ServerConnectionAdapter(activity);
        return adapter;
    }
    public MyApi getServerAPI()
    {
        return api;
    }
}
