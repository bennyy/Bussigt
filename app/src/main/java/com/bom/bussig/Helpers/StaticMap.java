package com.bom.bussig.Helpers;

import android.location.Location;

/**
 * Created by Oskar on 2014-09-06.
 */
public class StaticMap {

    String url;
    public StaticMap(Location currentLocation, Coordinate stationCoordinates) {
        url = buildUrl(currentLocation, stationCoordinates);

    }

    public String getUrl(){
        return url;
    }

    private String buildUrl(Location currentLocation, Coordinate stationCoordinates) {

        return "http://maps.googleapis.com/maps/api/staticmap?" +
                "size=700x355" +
                "&markers=color:blue%7Clabel:S%7C" +
                currentLocation.getLatitude() + "," +
                currentLocation.getLongitude() +
                "&markers=color:red%7Clabel:X%7C" +
                stationCoordinates.getY() + "," +
                stationCoordinates.getX() +
                "&key=AIzaSyDvjJbCT-MFD3y_Wie5i7JTLZ8H5thSf8Y";
    }


}
