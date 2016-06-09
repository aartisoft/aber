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
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.model.DriverStatus;
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
    private Driver driver;
    //private GoogleMap map;
    private SwitchCompat statusSwitch;
    private LocationService locationService;
    private Marker driverMarker;
    private Marker passengerOriginMarker;
    private Marker passengerDestinationMarker;
    private volatile boolean mapInitialized = false;
    private DriverStatus driverStatusLocal;

    @Bind(R.id.fab_cancel_trip)
    FloatingActionButton fabCancelTrip;
    @Bind(R.id.fab_arrival_notify)
    FloatingActionButton fabArrivedNotification;
    @Bind(R.id.fab_start_trip)
    FloatingActionButton fabStartTrip;
    @Bind(R.id.fab_end_trip)
    FloatingActionButton fabEndTrip;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

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
        Log.d(TAG,"In on create!");
        ButterKnife.bind(this);
        driver = (Driver) UserConfig.getCurrentUser();
        driverStatusLocal = driver.getStatus();
        // init driver
        //driver = new Driver();
        //driver.setReadyForService(true);
        //driver.setLocation(new BasicLocation(35.689197,51.388974));
//        if (driver.isReadyForService())
//        {
//
//        }



    }

//    private void populateMap()
//    {
//        if(map == null)
//            throw new IllegalStateException("Map not ready!");
//        Log.d(TAG, "populating map.");
//        switch (driver.getStatus())
//        {
//            case ON_THE_ROAD_TO_PASSENGER:
//                populateMapOnTheRoadToPassenger();
//                break;
//            case OUT_OF_SERVICE:
//                populateMapOutOfService();
//                break;
//            case READY_FOR_SERVICE:
//                populateMapReadyForService();
//                break;
//            case SERVICING_PASSENGER:
//               populateMapServicingPassenger();
//                break;
//        }
//    }

    private void populateMapOnTheRoadToPassenger()
    {
        if(driverMarker != null)
            driverMarker.remove();
        driverMarker = null;
        map.clear();
        //TODO handle null case driver location!
        final LatLng origin = new LatLng(driver.getLocation().getLatitude(),driver.getLocation().getLongitude());
        final LatLng dest = new LatLng(driver.getCurrentOrder().getSourceCoordinateLat(),driver.getCurrentOrder().getGetSourceCoordinateLong());
        mapDownloadAndAddPolyline(origin,dest);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,padding);
        mapAnimateCamera(cu, new MapAnimationCallback() {
            @Override
            public void onFinish() {
                MarkerOptions passengerOriginMarkerOpt = new MarkerOptions().position(dest)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_passenger_location_marker))
                        .draggable(false);
                updateDriverMarkerPosition();
                if(passengerOriginMarker != null)
                    passengerOriginMarker.remove();
                passengerOriginMarker = map.addMarker(passengerOriginMarkerOpt);
            }
        });
    }

    private void populateMapReadyForService()
    {
        map.clear();
        LatLng zoomLatLng = new LatLng(defaultMapZoomLocation.getLatitude(), defaultMapZoomLocation.getLongitude());
        if (driver.getLocation() != null) {
            zoomLatLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
        }
        mapAnimateCamera(CameraUpdateFactory.newLatLngZoom(zoomLatLng, 12.0f), new MapAnimationCallback() {
            @Override
            public void onFinish() {
                updateDriverMarkerPosition();
            }
        });
    }

    private void populateMapOutOfService()
    {
        map.clear();
        LatLng zoomLatLng = new LatLng(defaultMapZoomLocation.getLatitude(), defaultMapZoomLocation.getLongitude());
        mapAnimateCamera(CameraUpdateFactory.newLatLngZoom(zoomLatLng, 12.0f), new MapAnimationCallback() {
            @Override
            public void onFinish() {

            }
        });
    }

    private void populateMapServicingPassenger()
    {
        map.clear();
        final LatLng origin = new LatLng(driver.getCurrentOrder().getSourceCoordinateLat(),driver.getCurrentOrder().getGetSourceCoordinateLong());
        final LatLng dest = new LatLng(driver.getCurrentOrder().getDestinationCoordinateLat(),driver.getCurrentOrder().getDestinationCoordinateLong());
        mapDownloadAndAddPolyline(origin,dest);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mapAnimateCamera(cu, new MapAnimationCallback() {
            @Override
            public void onFinish() {
                MarkerOptions passengerOriginMarkerOpt = new MarkerOptions().position(origin)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_origin_marker))
                        .draggable(false);
                MarkerOptions passengerDestMarkerOpt = new MarkerOptions().position(dest)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_destination_marker))
                        .draggable(false);
                updateDriverMarkerPosition();
                if(passengerOriginMarker != null)
                    passengerOriginMarker.remove();
                passengerOriginMarker = map.addMarker(passengerOriginMarkerOpt);
                if(passengerDestinationMarker != null)
                    passengerDestinationMarker.remove();
                passengerDestinationMarker = map.addMarker(passengerDestMarkerOpt);
            }
        });
    }



    /**
     * called after map component is ready.
     */
/*    private void initializeMap() {
        Log.d(TAG, "Initializing map.");

        LatLng zoomLatLng = new LatLng(defaultMapZoomLocation.getLatitude(), defaultMapZoomLocation.getLongitude());
        if (driver.getLocation() != null) {
            zoomLatLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(zoomLatLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoomLatLng, 12.0f), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                updateDriverMarkerPosition();
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
    }*/


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
        statusSwitch.setChecked(driver.getStatus() != DriverStatus.OUT_OF_SERVICE);
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
                        new PrimaryDrawerItem().withName(R.string.name_drawer_item_orders).withIdentifier(2).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.name_drawer_section_account).withIdentifier(3).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.name_drawer_item_credit).withIdentifier(300).withLevel(2).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.name_drawer_item_logout).withIdentifier(301).withLevel(2).withSelectable(false)
                        )
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        if (iDrawerItem != null) {
                            Long id = iDrawerItem.getIdentifier();
                            if (id == 1) {

                            } else if (id == 2) {
                                // open orders list
                                openOrderListActivity();

                            } else if (id == 3) {


                            }else if( id == 5)
                            {

                            }
                        }

                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(false)
                .build();


    }

    private void openOrderListActivity()
    {
        if(driver.getStatus() == DriverStatus.READY_FOR_SERVICE) {
            Intent intent = new Intent(DriverHomeActivity.this, OrderRequestsActivity.class);
            startActivity(intent);
        }
        /*else
        {
            new MaterialDialog.Builder(this)
                    .content(R.string.message_diaolg_not_allowed)
                    .positiveText("Ok")
                    .build().show();
        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        updateView();
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
            //driver.setReadyForService(true);
            driver.setStatus(DriverStatus.READY_FOR_SERVICE);
            //todo more

        } else {
            statusSwitch.setChecked(false);
        }
    }

    private void makeDriverOutOfService()
    {
        if(driver.getStatus() != DriverStatus.READY_FOR_SERVICE)
        {
            // can not be out of service
           showCannotBeOutOfServiceDialog();
        }else
        {
            // stop service
            safeUnbindLocationService();
            stopLocationService();
            // clean ui
            if(driverMarker != null) {
                driverMarker.remove();
                driverMarker = null;
            }
            driver.setStatus(DriverStatus.OUT_OF_SERVICE);
            //driver.setReadyForService(false);
        }

    }

    private void showCannotBeOutOfServiceDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.message_dialog_cannot_be_outof_service);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
        driverStatusLocal = driver.getStatus();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume called!");
        super.onResume();
        if(driver.getStatus() != driverStatusLocal)
            updateView();
        // todo check location enabled
        if (driver.getStatus() != DriverStatus.OUT_OF_SERVICE)
            safeBindLocationService();
    }

//    private void handleStatusOnTheRoadToPassenger()
//    {
//        // TODO update toolbar for showing current status
//        if(driver.getCurrentOrder() == null)
//            throw new IllegalStateException();
//        LatLng origin = new LatLng(driver.getLocation().getLatitude(),driver.getLocation().getLongitude());
//        LatLng dest = new LatLng(driver.getCurrentOrder().getSourceCoordinateLat(),driver.getCurrentOrder().getGetSourceCoordinateLong());
//
//
//        MarkerOptions markerOption = new MarkerOptions().position(dest)
//                .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_passenger_location_marker));
//
//        String url = getDirectionsUrl(origin, dest);
//        DownloadTask downloadTask = new DownloadTask();
//        downloadTask.execute(url);
//    }

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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_driver_home;
    }

    private synchronized void updateDriverLocation(Location location) {
        driver.setLocation(new BasicLocation(location));
        updateDriverMarkerPosition();
    }

    private synchronized void updateDriverMarkerPosition() {
        if (driver.getLocation() == null) return;
        LatLng latLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());
        if (driverMarker != null)
            driverMarker.setPosition(latLng);
        else {
            MarkerOptions markerOption = new MarkerOptions().position(latLng)
                    .draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon));
            if (map != null) {
                driverMarker = map.addMarker(markerOption);
                Log.d(TAG,"Driver marker added!");
            }
        }
    }

    private void populateFabOnTheRoadToPassenger()
    {
        floatingActionsMenu.setVisibility(View.VISIBLE);
        fabEndTrip.setVisibility(View.GONE);
        fabStartTrip.setVisibility(View.VISIBLE);
        fabArrivedNotification.setVisibility(View.VISIBLE);
        fabCancelTrip.setVisibility(View.VISIBLE);
    }

    private void populateFabOutOfService()
    {
        floatingActionsMenu.setVisibility(View.GONE);
    }

    private void populateFabReadyForService()
    {
        floatingActionsMenu.setVisibility(View.GONE);
    }

    private void populateFabServicingPassenger()
    {
        floatingActionsMenu.setVisibility(View.VISIBLE);
        fabEndTrip.setVisibility(View.VISIBLE);
        fabStartTrip.setVisibility(View.GONE);
        fabArrivedNotification.setVisibility(View.GONE);
        fabCancelTrip.setVisibility(View.GONE);
    }

    @OnClick(R.id.fab_end_trip)
    void endTrip()
    {

    }

    @OnClick(R.id.fab_start_trip)
    void startTrip()
    {

    }

    @OnClick(R.id.fab_arrival_notify)
    void notifyArrived()
    {

    }

    @OnClick(R.id.fab_cancel_trip)
    void cancelTrip()
    {

    }

    private void updateView()
    {
        Log.d(TAG, "updating view.");
        switch (driver.getStatus())
        {
            case ON_THE_ROAD_TO_PASSENGER:
                Log.d(TAG,"Road to Passenger Sate.");
                populateFabOnTheRoadToPassenger();
                populateMapOnTheRoadToPassenger();
                break;
            case OUT_OF_SERVICE:
                Log.d(TAG,"Out of Service Sate.");
                populateFabOutOfService();
                populateMapOutOfService();
                break;
            case READY_FOR_SERVICE:
                Log.d(TAG,"Ready for Service Sate.");
                populateFabReadyForService();
                populateMapReadyForService();
                break;
            case SERVICING_PASSENGER:
                Log.d(TAG,"Servicing Passenger Sate.");
                populateFabServicingPassenger();
                populateMapServicingPassenger();
                break;
        }
    }

}
