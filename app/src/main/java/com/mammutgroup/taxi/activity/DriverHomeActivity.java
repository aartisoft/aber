package com.mammutgroup.taxi.activity;

import android.Manifest;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.service.local.DriverLocationService;
import com.mammutgroup.taxi.service.local.LocationService;
import com.mammutgroup.taxi.service.local.ServiceNotStartedException;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author mushtu
 * @since 5/30/16.
 */
@RuntimePermissions
public class DriverHomeActivity extends AbstractHomeActivity implements OnLocationUpdatedListener {

    private final String TAG = DriverHomeActivity.class.getSimpleName();
    private final int ENABLE_GPS_REQUEST_CODE = 500;
    private final BasicLocation defaultMapZoomLocation = new BasicLocation(35.689197, 51.388974);// Tehran location
    private Driver driver; //todo initialize
    private GoogleMap map;
    private SwitchCompat statusSwitch;
    private LocationService locationService;
    private Marker driverMarker;
    private volatile boolean mapInitialized = false;
    private ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Connected to location service.");
            locationService = (LocationService) service;
            Location location = locationService.getLastLocation();
            if (location != null) {
                Log.d(TAG, "Last location: " + location.getLatitude() + "," + location.getLongitude());
                updateDriverLocation(location);
            }
            try {
                locationService.requestLocationUpdates(DriverHomeActivity.this);
            } catch (ServiceNotStartedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init driver
        driver = new Driver();
        //driver.setReadyForService(true);
        //driver.setLocation(new BasicLocation(35.689197,51.388974));
//        if (driver.isReadyForService())
//        {
//
//        }



    }

    /**
     * called after map component is ready.
     */
    private void initializeMap() {
        Log.d(TAG, "Initializing map.");
        LatLng zoomLatLng = new LatLng(defaultMapZoomLocation.getLatitude(), defaultMapZoomLocation.getLongitude());
        if (driver.getLocation() != null) {
            zoomLatLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(zoomLatLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoomLatLng, 12.0f), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                updateMarkerPosition();
                mapInitialized = true;
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setTitle(R.string.name_drawer_item_home);
    }


    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        System.out.println("VVVV:   " + item.getItemId());
//        if (item.getItemId() == R.id.mit_switch_service_state) {
//            menuSwitchServiceStateSelected(item);
//            return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.driver_home_menu, menu);
        RelativeLayout layout = (RelativeLayout) menu.findItem(R.id.mit_switch_service_state).getActionView();
        statusSwitch = (SwitchCompat) layout.findViewById(R.id.material_switch);
        statusSwitch.setChecked(false);
        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeDriverServiceState(b);
            }
        });
        statusSwitch.setChecked(driver.isReadyForService());
        return super.onCreateOptionsMenu(menu);
    }

    private void menuSwitchServiceStateSelected(MenuItem item) {
        SwitchCompat switchCompat = (SwitchCompat) item.getActionView().findViewById(R.id.material_switch);
    }

    @Override
    protected Drawer buildDrawer() {
        return new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(accountHeader) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.name_drawer_item_home).withIdentifier(1).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.name_drawer_section_account).withIdentifier(2).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.name_drawer_item_credit).withIdentifier(3).withLevel(2).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.name_drawer_item_logout).withIdentifier(4).withLevel(2).withSelectable(false)

                        )


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (iDrawerItem != null) {
                            Long id = iDrawerItem.getIdentifier();
                            if (id == 1) {

                            } else if (id == 2) {

                            } else if (id == 3) {

                            }
                        }

                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        initializeMap();
    }

    private void changeDriverServiceState(boolean inService) {

        if (inService) {
            if (!SmartLocation.with(this).location().state().isGpsAvailable())
                showGPSDisabledDialog();
            else
                makeDriverReadyForService();

        } else makeDriverOutOfService();

    }

    private void makeDriverReadyForService() {
        if (SmartLocation.with(this).location().state().isGpsAvailable()) {
            DriverHomeActivityPermissionsDispatcher.startLocationServiceWithCheck(this);
            safeBindLocationService();
            driver.setReadyForService(true);
            //todo more

        } else {
            statusSwitch.setChecked(false);
        }
    }

    private void makeDriverOutOfService()
    {
        if(driver.isReadyForService()) {
            // stop service
            safeUnbindLocationService();
            stopLocationService();
            // clean ui
            if(driverMarker != null) {
                driverMarker.remove();
                driverMarker = null;
            }
            driver.setReadyForService(false);
        }
    }


    private void showGPSDisabledDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.message_dialog_gps_not_available)
                .setCancelable(false)
                .setPositiveButton(R.string.txt_dialog_btn_enable_gps,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, ENABLE_GPS_REQUEST_CODE);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.txt_dialog_btn_cancel_enable_gps,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        statusSwitch.setChecked(false);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_GPS_REQUEST_CODE) {
            makeDriverReadyForService();
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void startLocationService() {
        Intent serviceIntent = new Intent(this, DriverLocationService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onPause() {
        safeUnbindLocationService();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // todo check location enabled
        if (driver.isReadyForService())
            safeBindLocationService();


    }

    private void safeUnbindLocationService() {
        if (locationService != null) {
            locationService.removeLocationUpdates(this);
            unbindService(locationServiceConnection);
            locationService = null;
        }

    }


    private void stopLocationService()
    {
        Intent service = new Intent(this, DriverLocationService.class);
        stopService(service);
    }

    private void safeBindLocationService() {
        if (locationService == null)
            bindService(new Intent(this, DriverLocationService.class), locationServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onLocationUpdated(Location location) {
        updateDriverLocation(location);
    }

    private synchronized void updateDriverLocation(Location location) {
        driver.setLocation(new BasicLocation(location));
        if (mapInitialized)
            updateMarkerPosition();
    }

    private synchronized void updateMarkerPosition() {
        if (driver.getLocation() == null) return;
        LatLng latLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
        if (driverMarker != null)
            driverMarker.setPosition(latLng);
        else {
            MarkerOptions markerOption = new MarkerOptions().position(latLng).title(getString(R.string.src))
                    .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon));
            if (map != null) {
                driverMarker = map.addMarker(markerOption);
            }
        }
    }



}
