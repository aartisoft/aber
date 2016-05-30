package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.GoogleMap;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * @author mushtu
 * @since 5/30/16.
 */
public class DriverHomeActivity extends AbstractHomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        new PrimaryDrawerItem().withName(R.string.name_drawer_item_settings).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.name_drawer_item_logout).withIdentifier(3).withSelectable(false)
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
        //todo
    }


    private void changeDriverServiceState(boolean inService) {

    }
}
