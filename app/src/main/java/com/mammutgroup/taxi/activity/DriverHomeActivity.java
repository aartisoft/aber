package com.mammutgroup.taxi.activity;

import android.os.Bundle;
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
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * @author mushtu
 * @since 5/30/16.
 */
public class DriverHomeActivity extends AbstractHomeActivity {

    private Driver driver; //todo initialize
    private GoogleMap map;
    private final BasicLocation defaultMapZoomLocation = new BasicLocation(35.689197,51.388974);// Tehran location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init driver
        driver = new Driver();
        driver.setReadyForService(true);
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
        SwitchCompat switchCompat = (SwitchCompat) layout.findViewById(R.id.material_switch);
        switchCompat.setChecked(driver.isReadyForService());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        //todo call api
        // stop services
        driver.setReadyForService(false);

    }


}
