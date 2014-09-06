package com.bom.bussig.Model;

/**
 * Created by Mackan on 2014-09-06.
 */
public class Departure {
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
