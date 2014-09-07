package com.bom.bussig.Helpers;

import com.mattiasbergstrom.resrobot.RouteSegment;

/**
 * Created by Benjamin on 2014-09-06.
 */
public class LineHelper {
    private int currentIndex;
    private RouteSegment routeSegment;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public RouteSegment getRouteSegment() {
        return routeSegment;
    }

    public void setRouteSegment(RouteSegment routeSegment) {
        this.routeSegment = routeSegment;
    }

    public LineHelper(int currentIndex, RouteSegment routeSegment) {

        this.currentIndex = currentIndex;
        this.routeSegment = routeSegment;
    }
}
