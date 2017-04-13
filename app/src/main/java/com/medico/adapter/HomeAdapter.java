package com.medico.adapter;

import android.app.Activity;
import android.widget.BaseAdapter;

import com.medico.application.MyApi;
import com.medico.util.ServerConnectionAdapter;

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
        api = api = ServerConnectionAdapter.getServerAdapter(activity).getServerAPI();
    }
    public void callBack(int id, Object source, Object parameter)
    {

    }

}
