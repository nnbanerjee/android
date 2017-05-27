package com.medicohealthcare.util;


import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.medicohealthcare.adapter.PlaceAutocompleteAdapter;
import com.medicohealthcare.model.Clinic1;
import com.medicohealthcare.model.Person;

import java.util.StringTokenizer;

/**
 * Created by Narendra on 17-03-2017.
 */

public class GeoUtility implements GoogleApiClient.OnConnectionFailedListener
{
    Activity activity;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    public TextView countryView;
    public TextView cityView;
    private Object profile;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    public static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(7.798000, 68.14712), new LatLng(37.090000, 97.34466));
//    LocationService locationService = LocationService.getLocationManager(activity);

    public GeoUtility(final Activity activity, AutoCompleteTextView geoField, final TextView country, final TextView city, Button deletebutton, Button currentbutton, final Object profile)
    {
        this.activity = activity;
        mGoogleApiClient = GeoClient.getInstance(activity).getGeoApiClient();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.

        mAutocompleteView = geoField;
        // Register a listener that receives callbacks when a suggestion has been selected

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        // Retrieve the TextViews that will display details and attributions of the selected place.

        this.countryView = (TextView) country;

        this.cityView = (TextView) city;



        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover

        // the entire world.

        mAdapter = new PlaceAutocompleteAdapter(activity, mGoogleApiClient, BOUNDS_INDIA,

                null);
        mAutocompleteView.setAdapter(mAdapter);



        // Set up the 'clear text' button that clears the text in the autocomplete view

        Button clearButton = (Button) deletebutton;

        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                mAutocompleteView.setText("");
                countryView.setText("");
                cityView.setText("");

            }

        });
        Button currentButton = (Button) currentbutton;

        currentButton.setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View v)
            {
                LocationService location = LocationService.getLocationManager(activity);
                mAutocompleteView.setText(location.partialAddress);
                countryView.setText(location.country);
                cityView.setText(location.city);

                if(profile instanceof Person)
                {
                    Person person = (Person)profile;
                    person.setAddress(location.completeAddress);
                    person.setRegion(location.region);
                    person.setLocationLat(location.latitude);
                    person.setLocationLong(location.longitude);
                    person.setCity(location.city);
                    person.setCountry(location.country);
                    person.setIsoCountry(location.countryCode);
                }
                else if(profile instanceof Clinic1)
                {
                    Clinic1 person = (Clinic1)profile;
                    person.address = location.completeAddress;
                    person.locationLat = location.latitude;
                    person.locationLong = location.longitude;
                    person.city = location.city;
                    person.country = location.country;
                    person.isoCountry=location.countryCode;
                }

            }

        });
        this.profile = profile;

    }



    /**

     * Listener that handles selections from suggestions from the AutoCompleteTextView that

     * displays Place suggestions.

     * Gets the place id of the selected item and issues a request to the Places Geo Data API

     * to retrieve more details about the place.

     *

     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,

     * String...)

     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener

            = new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /*

         Retrieve the place ID of the selected item from the Adapter.

         The adapter stores each Place suggestion in a AutocompletePrediction from which we

         read the place ID and title.

          */

            final AutocompletePrediction item = mAdapter.getItem(position);

            final String placeId = item.getPlaceId();

            final CharSequence primaryText = item.getPrimaryText(null);



//            Log.i(TAG, "Autocomplete item selected: " + primaryText);



        /*

         Issue a request to the Places Geo Data API to retrieve a Place object with additional

         details about the place.

          */

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi

                    .getPlaceById(mGoogleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);



//            Toast.makeText(activity, "Clicked: " + primaryText,
//
//                    Toast.LENGTH_SHORT).show();

//            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);

        }

    };



    /**

     * Callback for results from a Places Geo Data API query that shows the first place result in

     * the details view on screen.

     */

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback

            = new ResultCallback<PlaceBuffer>() {

        @Override

        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {

                // Request did not complete successfully

//                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());

                places.release();

                return;

            }

            // Get the Place object from the buffer.

            final Place place = places.get(0);
            String address = place.getAddress().toString();
            String countryString = "";
            String regionString = "";
            String cityString = "";

            StringTokenizer tokenizer = new StringTokenizer(address,",");
            for (;tokenizer.hasMoreTokens();)
            {
                cityString = regionString;
                regionString = countryString;
                countryString = tokenizer.nextToken().trim();
            }
            countryView.setText(countryString);
            cityView.setText(cityString);
            if(profile instanceof Person)
            {
                Person person = (Person)profile;
                person.setAddress(address);
                person.setLocationLat(place.getLatLng().latitude);
                person.setRegion(regionString);
                person.setLocationLong(place.getLatLng().longitude);
                person.setCity(cityString);
                person.setCountry(countryString);

            }
            else if(profile instanceof Clinic1)
            {
                Clinic1 person = (Clinic1)profile;
                person.address = address;
                person.locationLat = place.getLatLng().latitude;
                person.locationLong = place.getLatLng().longitude;
                person.city = cityString;
                person.country = countryString;
            }
            places.release();
//            profile.setCountry(getCode(countryString));
        }

    };



//    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
//
//                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
//
////        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,websiteUri));
//
//        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
//
//                websiteUri));
//
//
//
//    }



    /**

     * Called when the Activity could not connect to Google Play services and the auto manager

     * could resolve the error automatically.

     * In this case the API is not available and notify the user.

     *

     * @param connectionResult can be inspected to determine the cause of the failure

     */

    @Override

    public void onConnectionFailed(ConnectionResult connectionResult) {


//
//        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
//
//                + connectionResult.getErrorCode());



        // TODO(Developer): Check error code and notify the user of error state and resolution.

//        Toast.makeText(activity,
//
//                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
//
//                Toast.LENGTH_SHORT).show();

    }
}