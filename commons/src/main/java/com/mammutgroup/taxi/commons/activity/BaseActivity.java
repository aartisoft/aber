package com.mammutgroup.taxi.commons.activity;

import android.support.v7.app.AppCompatActivity;
import com.mammutgroup.taxi.commons.config.UserConfig;

/**
 * Base activity for activities in application
 * @author mushtu
 * @since 6/11/16.
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onStop() {
        UserConfig.getInstance().saveConfig(this);
        super.onStop();
    }
}
