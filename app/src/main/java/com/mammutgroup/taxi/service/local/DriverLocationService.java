package com.mammutgroup.taxi.service.local;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.service.remote.rest.TaxiRestClient;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import retrofit.RetrofitError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author mushtu
 * @since 6/1/16.
 */
public class DriverLocationService extends Service implements OnLocationUpdatedListener {

    private final String TAG = DriverLocationService.class.getSimpleName();
    private final long  UPDATE_INTERVAL = 30000; // 30 seconds
    private final float MIN_DISTANCE_UPDATE = 10; // 10 meter
    private final int MAX_LOCATION_BUFFER = 100;
    private final int SENDER_THREAD_POLL_TIMEOUT = 1; //1 sec

    private LocationGooglePlayServicesProvider provider;
    private SmartLocation smartLocation;
    private TaxiRestClient restClient ;
    private final ArrayBlockingQueue<BasicLocation> locations = new ArrayBlockingQueue<BasicLocation>(MAX_LOCATION_BUFFER);
    private volatile boolean isRunning = false;
    private Thread sender ;
    private final IBinder binder = new LocalBinder();
    private final List<OnLocationUpdatedListener> updatedListeners = new ArrayList<OnLocationUpdatedListener>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"Bind the new client.");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        smartLocation = new SmartLocation.Builder(this).logging(true).build();
        restClient = TaxiApplication.restClient();
        Log.d(TAG,"Service created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning)
        {
            LocationParams params = new LocationParams.Builder()
                    .setInterval(UPDATE_INTERVAL)
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setDistance(MIN_DISTANCE_UPDATE).build();
            smartLocation.location(provider).start(this);
            isRunning = true;
            startSenderThread();
            Log.d(TAG,"Service started.");
        }
        return START_STICKY;
    }

    private void startSenderThread()
    {
        sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        BasicLocation location = locations.poll(SENDER_THREAD_POLL_TIMEOUT, TimeUnit.SECONDS);
                        if(location != null) {
                            try {
                                restClient.vehicleService().sendLocation(location);
                            }catch (RetrofitError error)
                            {
                                //todo handle error and backup lost location data
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG,"Sender stopped.");

            }
        });
        sender.start();
    }

    private void forceStopSenderThread()
    {
        if(sender != null && sender.isAlive())
            sender.interrupt();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        SmartLocation.with(this).location().stop();
        super.onDestroy();
        Log.i(TAG,"Service stopped.");
    }

    @Override
    public void onLocationUpdated(Location location) {
        // call listeners
        for(OnLocationUpdatedListener listener : updatedListeners)
            listener.onLocationUpdated(location);

        try {
            //TODO: what will happen if caller get blocked ????
            locations.put(new BasicLocation(location));
            Log.d(TAG,"Location Update:(" + location.getLatitude()+","+location.getLongitude()+")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder implements LocationService
    {
        private DriverLocationService locationService;
        public LocalBinder()
        {
            this.locationService = DriverLocationService.this;
        }


        @Override
        public Location getLastLocation() {
            return smartLocation.location(provider).getLastLocation();
        }

        @Override
        public void requestLocationUpdates(OnLocationUpdatedListener listener) throws ServiceNotStartedException {
            if(!isRunning)
                throw new ServiceNotStartedException();
            updatedListeners.add(listener);
        }

        @Override
        public void removeLocationUpdates(OnLocationUpdatedListener listener) {
            updatedListeners.remove(listener);
        }


    }
}
