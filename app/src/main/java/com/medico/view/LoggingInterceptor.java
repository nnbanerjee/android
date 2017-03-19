package com.medico.view;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;



/**
 * Created by Narendra on 07-02-2016.
 */
public class LoggingInterceptor implements Interceptor {

        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();

                   /* request = request.newBuilder().removeHeader("Content-Type").build();
                    request = request.newBuilder().addHeader("Content-Type", "application/json").build();*/


            Log.d("OkHttp", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Log.d("OkHttp", String.format("Body is %s ",
                    request.body().toString()));


            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("OkHttp", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;

    }
}
