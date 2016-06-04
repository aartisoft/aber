package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.mammutgroup.taxi.service.remote.rest.api.order.model.Order;
import com.mikepenz.materialdrawer.Drawer;

public class PassengerOrderMapActivity extends AbstractHomeActivity {

    private Order order;

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



    }
}
