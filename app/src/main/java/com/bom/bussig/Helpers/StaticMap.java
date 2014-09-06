package com.bom.bussig.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oskar on 2014-09-06.
 */
public class StaticMap {

    String url;
    public StaticMap(Coordinate currentLocation, Coordinate stationLocation) {
        url = buildUrl(currentLocation, stationLocation);

    }

    public Bitmap getImage() {
        return downloadImage(url);
    }

    public String getUrl(){
        return url;
    }

    private String buildUrl(Coordinate currentLocation, Coordinate stationLocation) {

        return "http://maps.googleapis.com/maps/api/staticmap?" +
                "size=800x400" +
                "&markers=color:blue%7Clabel:S%7C" +
                currentLocation.getY() + "," +
                currentLocation.getX() +
                "&markers=color:red%7Clabel:X%7C" +
                stationLocation.getY() + "," +
                stationLocation.getX() +
                "&key=AIzaSyDvjJbCT-MFD3y_Wie5i7JTLZ8H5thSf8Y";
    }

    private Bitmap downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
