package com.medico.view;

import android.app.Fragment;
import android.webkit.CookieManager;

import com.medico.application.R;

import com.medico.application.MyApi;
import com.medico.util.PARAM;
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

    public boolean isChanged()
    {
        return false;
    }
    public boolean save()
    {
        return false;
    }
    public void update()   {    }
    public boolean canBeSaved()
    {
        return false;
    }
    public void setEditable(boolean editable)
    {
    }
}
