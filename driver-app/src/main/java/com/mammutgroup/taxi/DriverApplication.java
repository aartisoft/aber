package com.mammutgroup.taxi;

import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.mammutgroup.taxi.commons.config.UserConfig;
import com.mammutgroup.taxi.model.Driver;

import java.util.Locale;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class DriverApplication extends MultiDexApplication {

    private final static String TAG = DriverApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"on create!");
        UserConfig.getInstance().loadConfig(this, Driver.class);
        Locale locale = new Locale(UserConfig.getInstance().getLocale());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
