package com.bom.bussig.Data.Location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Mackan on 2014-09-06.
 */
public class LocationService {
    private LocationManager mLocationManager;
    private Context mContext;
    private String mProvider;

    // Creates a new instance of the LocationService.
    // Will take the best available provider by default.
    public LocationService(Context context){
        this.mContext = context;
        this.mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        this.mProvider = mLocationManager.getBestProvider(criteria, false);
        //Force the location manager to update its position
        this.getLocationManager().requestLocationUpdates(this.mProvider,60000,0,new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    //Get the last known location
    public android.location.Location getLocation(){
        return this.getLocationManager().getLastKnownLocation(this.getProvider());
    }

    public LocationManager getLocationManager(){
        return this.mLocationManager;
    }

    public void setLocationManager(LocationManager locationManager){
        this.mLocationManager = locationManager;
    }

    public void setProvider(String provider){
        this.mProvider = provider;
    }

    public String getProvider(){
        return this.mProvider;
    }
}
