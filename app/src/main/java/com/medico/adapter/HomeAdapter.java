package com.medico.adapter;

import android.app.Activity;
import android.widget.BaseAdapter;

import com.medico.application.MainActivity;
import com.medico.application.MyApi;

/**
 * Created by Narendra on 05-04-2017.
 */

public abstract class HomeAdapter extends BaseAdapter
{
    MyApi api;
    Activity activity;
    HomeAdapter adapter;

    public HomeAdapter(Activity activity) {
        adapter = this;
        this.activity = activity;
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestInterceptor.RequestFacade request) {
//                        // assuming `cookieKey` and `cookieValue` are not null
//                        String cookie = CookieManager.getInstance().getCookie("PLAY_SESSION");
//                        request.addHeader("Cookie", "PLAY_SESSION" + "=" + cookie);
//                    }
//                })
//                .setEndpoint(activity.getResources().getString(R.string.base_url))
//                .setClient(new OkClient())
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
        api = MainActivity.api;
    }
    public void callBack(int id, Object source, Object parameter)
    {

    }
}
