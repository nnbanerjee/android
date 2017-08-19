package com.medicohealthcare.application;

import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by Narendra on 18-08-2017.
 */

public abstract class ApiCallback implements Callback<T>
{
    private static List<ApiCallback> mList = new ArrayList<>();

    private boolean isCanceled = false;
    private Object mTag = null;

    public static void cancelAll()
    {
        Iterator<ApiCallback> iterator = mList.iterator();
        while (iterator.hasNext()){
            iterator.next().isCanceled = true;
            iterator.remove();
        }
    }

    public static void cancel(Object tag) {
        if (tag != null) {
            Iterator<ApiCallback> iterator = mList.iterator();
            ApiCallback item;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (tag.equals(item.mTag)) {
                    item.isCanceled = true;
                    iterator.remove();
                }
            }
        }
    }

    public ApiCallback()
    {
        mList.add(this);
    }

    public ApiCallback(Object tag) {
        mTag = tag;
        mList.add(this);
    }

    public void cancel() {
        isCanceled = true;
        mList.remove(this);
    }

    @Override
    public final void success(T t, Response response) {
        if (!isCanceled)
            onSuccess(t, response);
        mList.remove(this);
    }
    public abstract void onSuccess(T t, Response response);

    public abstract void onFailure(RetrofitError error);

}
