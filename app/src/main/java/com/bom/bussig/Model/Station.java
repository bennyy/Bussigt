package com.bom.bussig.Model;

import java.util.ArrayList;

/**
 * Created by Mackan on 2014-09-06.
 */
public class Station {
    private int mLocationID;
    private String mName;
    private ArrayList<Departure> mDepartures;

    public Station(int LocationID, String Name){
        this.mLocationID = LocationID;
        this.mName = Name;
        this.mDepartures = new ArrayList<Departure>();
    }

    public int getLocationID(){
        return this.mLocationID;
    }

    public String getName(){
        return this.mName;
    }

    public ArrayList<Departure> getDepartures(){
        return this.mDepartures;
    }

    public void addDeparture(Departure departure){
        this.mDepartures.add(departure);
    }

}
