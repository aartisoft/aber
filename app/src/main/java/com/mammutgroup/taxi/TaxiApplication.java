package com.mammutgroup.taxi;

import android.app.Application;
import com.mammutgroup.taxi.service.remote.rest.TaxiRestClient;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class TaxiApplication extends Application {

    private static TaxiRestClient taxiRestClient;

    @Override
    public void onCreate() {
        super.onCreate();
        taxiRestClient = new TaxiRestClient();
    }

    public static TaxiRestClient restClient()
    {
        return taxiRestClient;
    }
}
