package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * @author mushtu
 * @since 5/30/16.
 */
public class DriverHomeActivity extends AbstractHomeActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.name_drawer_item_home);
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
                        if (iDrawerItem != null)
                        {
                            Long id = iDrawerItem.getIdentifier();
                            if(id == 1)
                            {

                            }else if(id == 2)
                            {

                            }else if(id == 3)
                            {

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
}
