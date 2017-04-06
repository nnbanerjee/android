package com.medico.adapter;

import android.app.Activity;
import android.webkit.CookieManager;
import android.widget.BaseAdapter;

import com.medico.application.MyApi;
import com.medico.application.R;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Narendra on 05-04-2017.
 */

public abstract class HomeAdapter extends BaseAdapter
{
    MyApi api;
    Activity activity;

    public HomeAdapter(Activity activity) {
        this.activity = activity;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        // assuming `cookieKey` and `cookieValue` are not null
                        String cookie = CookieManager.getInstance().getCookie("PLAY_SESSION");
                        request.addHeader("Cookie", "PLAY_SESSION" + "=" + cookie);
                    }
                })
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
    }
    public void callBack(int id, Object source, Object parameter)
    {

    }
}
