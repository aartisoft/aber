package com.mammutgroup.taxi.model;

import android.location.Location;

/**
 * @author mushtu
 * @since 5/31/16.
 */
public class BasicLocation {

    private final Double latitude;
    private final Double longitude;
    private final Double altitude;
    private final Float accuracy;

    public BasicLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = null;
        this.accuracy = null;
    }

    public BasicLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        accuracy = location.getAccuracy();
        //todo
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }
}
