package com.bom.bussig.Data.Location;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;

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
        this.mProvider = mLocationManager.getBestProvider(criteria, true);
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
