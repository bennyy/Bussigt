package com.bom.bussig.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mackan on 2014-09-06.
 */
public class Station implements Serializable{
    private int mLocationID;
    private double mLatitude;
    private double mLongitude;
    private String mName;
    private ArrayList<Departure> mDepartures;

    public Station(int LocationID, String Name){
        this.mLocationID = LocationID;
        this.mName = Name;
        this.mDepartures = new ArrayList<Departure>();
    }

    public Station(int LocationID, String Name, double Longitude, double Latitude){
        this.mLocationID = LocationID;
        this.mName = Name;
        this.mLatitude = Latitude;
        this.mLongitude = Longitude;
        this.mDepartures = new ArrayList<Departure>();
    }

    public int getLocationID(){
        return this.mLocationID;
    }

    public String getName(){
        return this.mName;
    }

    public double getLatitude(){
        return this.mLatitude;
    }

    public double getLongitude(){
        return this.mLongitude;
    }

    public ArrayList<Departure> getDepartures(){
        return this.mDepartures;
    }

    public void addDeparture(Departure departure){
        this.mDepartures.add(departure);
    }

}
