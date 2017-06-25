package com.medicohealthcare.util;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.medicohealthcare.application.MyApi;
import com.medicohealthcare.application.R;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

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
//        String hostname = "medicohealthcareservices.com";
//        CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                .add(hostname, "sha1/MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzVThZ8Br0br8CEfTIX9W" +
//                        "+CAJY57w/XnxFSZEzp74WAu/eauqQOKwA5l2CI0jDKIdMN8xp7zDLOKvyVGw1QER" +
//                        "NHrog8Q15NiByGhg7CxhaQht6ps8nqIr9waxruGG5L+HcFtxIdJi7vdpAnGqwzrj" +
//                        "OYpb1kKsTIwz6yX5xroyR1Xw6PzzRRcQOnltDQXHO+IAVTH/rlmDcMGtm763zEcz" +
//                        "gLRhX8IIS1ceHDg4Ofwl2NRjo8DHjVBo/oKEO1Z5fCljNwcCkUKzy0MZtu3Mdp2T" +
//                        "4EigSHjdcACSnVNwegyfwg1g6cw8QtDY15s3izyoR4DapOyHqGLsk06Mr1d+2pHX" +
//                        "oQIDAQAB")
//                .build();

        CookieManager cookieManager = new CookieManager(null, null);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.setCertificatePinner(certificatePinner);
        okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(2, TimeUnit.MINUTES);
        okHttpClient.setCookieHandler(cookieManager);
        Client client = new OkClient(okHttpClient);
        Gson gson = new GsonBuilder().create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(activity.getResources().getString(R.string.base_url))
                .setConverter(new MedicoGsonConverter(activity,gson))
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
