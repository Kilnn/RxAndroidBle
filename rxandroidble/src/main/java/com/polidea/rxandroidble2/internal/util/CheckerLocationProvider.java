package com.polidea.rxandroidble2.internal.util;


import bleshadow.javax.inject.Inject;

public class CheckerLocationProvider {

    private final LocationManagerWrapper locationManager;

    @Inject
    CheckerLocationProvider(LocationManagerWrapper locationManager) {
        this.locationManager = locationManager;
    }

    public boolean isLocationProviderEnabled() {
        return locationManager.isLocationProviderEnabled();
    }

}
