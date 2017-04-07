package com.medico.view;

import android.app.Fragment;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.medico.application.MyApi;
import com.medico.application.R;
import com.medico.model.Country;
import com.medico.model.ProfileId;
import com.medico.util.PARAM;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Narendra on 27-01-2017.
 */
public class ParentFragment extends Fragment implements PARAM
{

    public MyApi api;
    public static List<Country> countriesList = null;
    public Fragment fragment;
    @Override
    public void onStart()
    {
        super.onStart();
        fragment = this;
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

        if(countriesList == null)
            loadSupportedCountryList();

    }

    private void loadSupportedCountryList()
    {
        api.getSupportedCountryList(new ProfileId(0), new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries, Response response) {
                if (countries != null && countries.size() > 0) {
                    countriesList =  countries;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.Failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    public List<Country> getSupportedCountriesList()
    {
        return countriesList;
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
