package com.mammutgroup.taxi.service.remote.rest.api.vehicle.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.BaseDto;

/**
 * Created by Iraj on 6/10/2016.
 */
public class TaxiLatLng extends BaseDto {

    @SerializedName("latitude")
    private double lat;
    @SerializedName("longitude")
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
