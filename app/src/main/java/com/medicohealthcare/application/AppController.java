package com.medicohealthcare.application;

/**
 * Created by MNT on 17-Feb-15.
 */

import android.app.Application;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;



    private String baseUrl = "";


    public static synchronized AppController getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

    }


}
