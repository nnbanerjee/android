package com.medico.util;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

/**
 * Created by Narendra on 18-03-2017.
 */

public class GeoClient implements GoogleApiClient.OnConnectionFailedListener
{
    private static GeoClient geoInstance = null;
    private GoogleApiClient mGoogleApiClient;
    private Activity activity;

    private GeoClient(Activity activity, int clientId)
    {
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(activity).enableAutoManage((AppCompatActivity)activity, clientId /* clientId */, this)

                .addApi(Places.GEO_DATA_API)

                .build();
    }
    public static GeoClient getInstance(Activity activity)
    {
        if(geoInstance == null)
            geoInstance = new GeoClient(activity, 0);

        return geoInstance;
    }
    public GoogleApiClient getGeoApiClient()
    {
        return mGoogleApiClient;
    }

    /**

     * Called when the Activity could not connect to Google Play services and the auto manager

     * could resolve the error automatically.

     * In this case the API is not available and notify the user.

     *

     * @param connectionResult can be inspected to determine the cause of the failure

     */
    @Override

    public void onConnectionFailed(ConnectionResult connectionResult) {

        // TODO(Developer): Check error code and notify the user of error state and resolution.

        Toast.makeText(activity,

                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),

                Toast.LENGTH_SHORT).show();

    }
}
