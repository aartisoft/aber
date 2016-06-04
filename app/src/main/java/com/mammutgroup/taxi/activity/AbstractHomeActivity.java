package com.mammutgroup.taxi.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This class provides base class for home activity with a map and drawer.
 *
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
        setupToolbar();
        accountHeader = buildAccountHeader();
        drawer = buildDrawer();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setHasOptionsMenu(true);
    }


    protected void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    protected AccountHeader buildAccountHeader() {
        //TODO change this
        User currentUser = UserConfig.getCurrentUser();

        String name = "mushtu";
        String email = "mushtu@gmail.com";
        Bitmap profilePhoto = null;

        if (currentUser != null) {

            String fullName = currentUser.getFullName();
            if (fullName != null && fullName != "")
                name = fullName;
            String email1 = currentUser.getEmail();
            if (email1 != null && email1 != "")
                email = email1;

            if (currentUser.getProfileImg() != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("media", Context.MODE_PRIVATE);
                profilePhoto = BitmapFactory.decodeFile(directory + "/" + currentUser.getProfileImg(), options);
            }
        }

        if (profilePhoto != null)
            drawerProfile = new ProfileDrawerItem().
                    withName(name).
                    withEmail(email).withIcon(profilePhoto);
        else
            drawerProfile = new ProfileDrawerItem().
                    withName(name).
                    withEmail(email);

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
     *
     * @return
     */
    protected int getLayoutResourceId() {
        return R.layout.activity_abstract_home;
    }

}
