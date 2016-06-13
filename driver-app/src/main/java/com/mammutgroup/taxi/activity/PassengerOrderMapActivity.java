package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
//import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.mammutgroup.taxi.commons.activity.AbstractHomeActivity;
import com.mammutgroup.taxi.commons.config.UserConfig;
import com.mammutgroup.taxi.commons.service.remote.rest.api.order.model.Order;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.model.DriverStatus;
import com.mikepenz.materialdrawer.Drawer;

public class PassengerOrderMapActivity extends AbstractHomeActivity {

    private Order order;
    private Marker originMarker;
    private Marker destinationMarker;
    private GoogleMap map;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = (Driver) UserConfig.getInstance().getCurrentUser();
        this.order = (Order) getIntent().getSerializableExtra("order");
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.mit_take_passenger:
                sendTripAcceptedByDriver();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                return true;
        }

        return true;
    }

    private void sendTripAcceptedByDriver() {
/*
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.message_progress_working)
                .cancelable(false)
                .progress(true, 0)
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                // on success
                driver.setStatus(DriverStatus.ON_THE_ROAD_TO_PASSENGER);
                driver.setCurrentOrder(order);
                transitToHome();
            }
        },5000);*/

        driver.setStatus(DriverStatus.ON_THE_ROAD_TO_PASSENGER);
        driver.setCurrentOrder(order);
        transitToHome();

        //TODO

    }

    @Override
    protected Drawer buildDrawer() {
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        final LatLng origin = new LatLng(order.getSourceCoordinateLat(), order.getDestinationCoordinateLong());
        final LatLng dest = new LatLng(order.getDestinationCoordinateLat(), order.getDestinationCoordinateLong());
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        // begin new code:
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                MarkerOptions originMarkerOption = new MarkerOptions().position(origin).title(getString(R.string.src))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_origin_marker))
                        .draggable(false);
                MarkerOptions destMarkerOption = new MarkerOptions().position(dest).title(getString(R.string.dest))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_destination_marker))
                        .draggable(false);
                originMarker = map.addMarker(originMarkerOption);
                destinationMarker = map.addMarker(destMarkerOption);
                originMarker.showInfoWindow();

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    private void transitToHome()
    {
        Intent intent = new Intent(this,DriverHomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("order",order);
        startActivity(intent);
        finish();
    }

}
