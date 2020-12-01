package com.lucaryholt.ambrscope.LocationHandler;

import android.location.Location;

public class LocationHandler {

    private static Location currentPoint;

    public static Location getCurrentPoint() {
        return currentPoint;
    }

    public static void setCurrentPoint(Location location) {
        currentPoint = location;
    }

}
