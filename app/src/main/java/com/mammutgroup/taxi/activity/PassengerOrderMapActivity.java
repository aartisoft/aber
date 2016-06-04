package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import com.mikepenz.materialdrawer.Drawer;

public class PassengerOrderMapActivity extends AbstractHomeActivity {

    private Order order;
    private Marker originMarker;
    private Marker destinationMarker;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.order = (Order) getIntent().getSerializableExtra("order");
    }

    @Override
    protected Drawer buildDrawer() {
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        final LatLng origin = new LatLng(order.getSourceCoordinateLat(),order.getDestinationCoordinateLong());
        final LatLng dest = new LatLng(order.getDestinationCoordinateLat(),order.getDestinationCoordinateLong());
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                MarkerOptions originMarkerOption = new MarkerOptions().position(origin).title(getString(R.string.src))
                        .draggable(false);
                MarkerOptions destMarkerOption = new MarkerOptions().position(dest).title(getString(R.string.src))
                        .draggable(false);
                originMarker = map.addMarker(originMarkerOption);
                destinationMarker = map.addMarker(destMarkerOption);
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
