package com.mammutgroup.taxi.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.service.local.DriverLocationService;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import io.nlopez.smartlocation.SmartLocation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author mushtu
 * @since 5/30/16.
 */
@RuntimePermissions
public class DriverHomeActivity extends AbstractHomeActivity {

    private final int ENABLE_GPS_REQUEST_CODE = 500;
    private Driver driver; //todo initialize
    private GoogleMap map;
    private final BasicLocation defaultMapZoomLocation = new BasicLocation(35.689197,51.388974);// Tehran location
    private SwitchCompat statusSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init driver
        driver = new Driver();
        //driver.setReadyForService(true);
        //driver.setLocation(new BasicLocation(35.689197,51.388974));

    }

    private void initializeMap()
    {
        LatLng latLng = new LatLng(defaultMapZoomLocation.getLatitude(),defaultMapZoomLocation.getLongitude());
        if(driver.getLocation() != null) {
            latLng = new LatLng(driver.getLocation().getLatitude(), driver.getLocation().getLongitude());

        }
        final MarkerOptions markerOption = new MarkerOptions().position(latLng).title(getString(R.string.src))
                .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                if(driver.getLocation() != null)
                    map.addMarker(markerOption);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.driver_home_menu, menu);
        RelativeLayout layout = (RelativeLayout) menu.findItem(R.id.mit_switch_service_state).getActionView();
        statusSwitch = (SwitchCompat) layout.findViewById(R.id.material_switch);
        statusSwitch.setChecked(driver.isReadyForService());
        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeDriverServiceState(b);
            }
        });

        return super.onCreateOptionsMenu(menu);
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

        if(inService)
        {
            if(!SmartLocation.with(this).location().state().isGpsAvailable())
                showGPSDisabledDialog();

        }else{

        }


    }

    private void makeDriverReadyForServiceAfterReturningFromSettings()
    {
        if(SmartLocation.with(this).location().state().isGpsAvailable())
        {
            DriverHomeActivityPermissionsDispatcher.startLocationServiceWithCheck(this);
            //todo more

        }else
        {
            //todo
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
                                startActivityForResult(callGPSSettingIntent,ENABLE_GPS_REQUEST_CODE);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.txt_dialog_btn_cancel_enable_gps,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_GPS_REQUEST_CODE) {
            makeDriverReadyForServiceAfterReturningFromSettings();
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void startLocationService()
    {
        Intent serviceIntent = new Intent(this, DriverLocationService.class);
        startService(serviceIntent);
    }

}
