package com.medico.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Narendra on 17-03-2017.
 */

public class LocationService extends  Notifier implements LocationListener {

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
    private final static boolean forceNetwork = false;
    private static LocationService instance = null;

    private LocationManager locationManager;

    public Location location;

    public double longitude;
    public double latitude;
    public String city,country,region,countryCode;
    public Address address;
    public String completeAddress = "";


    boolean isGPSEnabled, isNetworkEnabled,locationServiceAvailable;
    Activity context;

    /**
     * Singleton implementation
     * @return
     */
    public static LocationService getLocationManager(Activity context)
    {
        if (instance == null)
        {
            instance = new LocationService(context);
        }
        return instance;
    }

    /**
     * Local constructor
     */
    private LocationService( Activity context )     {
        this.context = context;
        initLocationService(context);
    }



    /**
     * Sets up location service after permissions is granted
     */
//    @TargetApi(23)
    private void initLocationService(Activity context)
    {
        if ( Build.VERSION.SDK_INT >= 23 && !PermissionManager.getInstance(context).hasPermission(PermissionManager.LOCATION))
        {
            return  ;
        }

        try
        {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(this.locationManager != null)
            {
                // Get GPS and network status
                this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (forceNetwork) isGPSEnabled = false;

                if (!isNetworkEnabled && !isGPSEnabled)    {
                    // cannot get location
                    this.locationServiceAvailable = false;
                }
                else
                {
                    this.locationServiceAvailable = true;

                    if (isNetworkEnabled)
                    {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this,null);
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        if (locationManager != null) {
//                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        }
                    }//end if

//                    else if (isGPSEnabled) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                        if (locationManager != null) {
//                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////                            updateCoordinates();
//                        }
//                    }
                }
            }
        }
        catch (SecurityException ex)
        {
//            LogService.log( "Error creating location service: " + ex.getMessage() );

        }
    }
//    private void initLocationServiceAPI16(final Context context)
//    {
//        int GPSoff = 0;
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return  ;
//        }
//        else
//        {
//            try
//            {
//                GPSoff = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            }
//            catch (Settings.SettingNotFoundException e)
//            {
//                e.printStackTrace();
//            }
//            if (GPSoff == 0)
//            {
//                new AlertDialog.Builder(context)
//                        .setTitle("Timeout connessione")
//                        .setMessage("Connessione lenta o non funzionante")
//                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override public void onClick(DialogInterface dialog, int which) {
//                                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                context.startActivity(onGPS);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        }
//
//        try
//        {
//            this.longitude = 0.0;
//            this.latitude = 0.0;
//            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            if(this.locationManager != null)
//            {
//                // Get GPS and network status
//                this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//                if (forceNetwork) isGPSEnabled = false;
//
//                if (!isNetworkEnabled && !isGPSEnabled)    {
//                    // cannot get location
//                    this.locationServiceAvailable = false;
//                }
//                //else
//                {
//                    this.locationServiceAvailable = true;
//
//                    if (isNetworkEnabled) {
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        if (locationManager != null) {
//                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                            updateCoordinates();
//                        }
//                    }//end if
//
//                    if (isGPSEnabled) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//
//                        if (locationManager != null) {
//                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            updateCoordinates();
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex)  {
////            LogService.log( "Error creating location service: " + ex.getMessage() );
//
//        }
//    }
//    private void updateCoordinates()
//    {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//    }

    @Override
    public void onLocationChanged(Location location)     {
        // do stuff here with location object
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String cityName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0)
            {
//                System.out.println(addresses.get(0).getLocality());
                address = addresses.get(0);
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    completeAddress = completeAddress + address.getAddressLine(i) + ", ";
                cityName = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                region = addresses.get(0).getAdminArea();
                countryCode = addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

         city =  cityName;
//        Toast.makeText(context, "New GPS location:" + cityName + " " + region + " " + country
//                + String.format("%9.6f", location.getLatitude()) + ", "
//                + String.format("%9.6f", location.getLongitude()) + "\n" , Toast.LENGTH_LONG).show();
        notifyListeners(PARAM.LOCATION_UPDATED,this, city);
    }

    public void onProviderDisabled(String provider)
    {

    }

    public void onProviderEnabled(String provider)
    {

    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }




}
