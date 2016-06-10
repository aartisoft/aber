package com.mammutgroup.taxi;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.service.remote.rest.mock.MockTaxiRestClient;

import java.util.Locale;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class TaxiApplication extends MultiDexApplication {

    public static volatile Context context;
    private static TaxiRestClient taxiRestClient;

    private static volatile boolean applicationInited = false;


    @Override
    public void onCreate() {
        super.onCreate();
        taxiRestClient = new MockTaxiRestClient();
        context = getApplicationContext();
        /*Locale locale = new Locale("fa");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/
    }

    public static void initApplication()
    {
        if(applicationInited)
            return;

        applicationInited = true;
        UserConfig.loadConfig();

    }

    public static TaxiRestClient restClient()
    {
        return taxiRestClient;
    }
}
