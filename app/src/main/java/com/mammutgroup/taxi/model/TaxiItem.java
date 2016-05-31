package com.mammutgroup.taxi.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Iraj on 5/31/2016.
 */
public class TaxiItem implements ClusterItem {
    private final LatLng mPosition;
    public int icon;

    public TaxiItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public TaxiItem(LatLng latLng , int icon) {
        this.mPosition = latLng; this.icon = icon;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}