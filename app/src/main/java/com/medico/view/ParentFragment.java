package com.medico.view;

import android.app.Fragment;
import android.webkit.CookieManager;

import com.mindnerves.meidcaldiary.R;

import Application.MyApi;
import Utils.PARAM;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Narendra on 27-01-2017.
 */
public class ParentFragment extends Fragment implements PARAM
{

    public MyApi api;

    @Override
    public void onStart()
    {
        super.onStart();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        // assuming `cookieKey` and `cookieValue` are not null
                        String cookie = CookieManager.getInstance().getCookie("PLAY_SESSION");
                        request.addHeader("Cookie", "PLAY_SESSION" + "=" + cookie);
                    }
                })
                .setEndpoint(this.getResources().getString(R.string.base_url))
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        api = restAdapter.create(MyApi.class);
    }

    protected boolean isChanged()
    {
        return false;
    }
    protected boolean save()
    {
        return false;
    }
    protected void update()   {    }
    protected boolean canBeSaved()
    {
        return false;
    }
    protected void setEditable(boolean editable)
    {
    }
}
