package com.bom.bussig.Model;

import java.io.Serializable;

/**
 * Created by Mackan on 2014-09-06.
 */
public class Departure implements Serializable{
    private int mNumber;
    private long mMinutesToNextBus;
    private String mDirection;

    public Departure(){}

    public Departure(int Number, String Direction, long MinutesToNextBus){
        this.mNumber = Number;
        this.mDirection = Direction;
        this.mMinutesToNextBus = MinutesToNextBus;
    }

    public int getNumber(){
        return this.mNumber;
    }

    public void setNumber(int Number){
        this.mNumber = Number;
    }

    public String getmDirection(){
        return this.mDirection;
    }

    public void setmDirection(String Direction){
        this.mDirection = Direction;
    }

    public long getmMinutesToNextBus(){
        return this.mMinutesToNextBus;
    }

    public void setmMinutesToNextBus(long MinutesToNextBus){
        this.mMinutesToNextBus = MinutesToNextBus;
    }
}
