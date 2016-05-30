package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * This class provides base class for home activity with a map and drawer.
 * @author mushtu
 * @since 5/29/16.
 */
public abstract class AbstractHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected AccountHeader accountHeader;
    protected Drawer drawer;
    protected IProfile drawerProfile;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        accountHeader = buildAccountHeader();
        drawer = buildDrawer();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setHasOptionsMenu(true);
    }


    protected AccountHeader buildAccountHeader()
    {
        drawerProfile = new ProfileDrawerItem().
            withName("mushtu").
            withEmail("mushtu@gmail.com");

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        drawerProfile
                )
                .build();
    }


    protected abstract Drawer buildDrawer();


    /**
     * default layout resource id.
     * In case of overriding the method your layout must at least contains toolbar widget and map fragment.
     * @return
     */
    protected int getLayoutResourceId(){
        return R.layout.activity_abstract_home;
    }

}
