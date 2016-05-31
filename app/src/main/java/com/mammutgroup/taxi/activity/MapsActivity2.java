package com.mammutgroup.taxi.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.mammutgroup.taxi.model.TaxiItem;
import com.mammutgroup.taxi.util.DirectionsJSONParser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity2 extends AbstractHomeActivity implements LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int LOCATION_REQ_CODE = 11;
    private static final String TAG = MapsActivity2.class.getSimpleName();
    private GoogleMap map;
    private Marker sourceMarker;
    private Marker destinationMarker;
    private GoogleApiClient googleApiClient;
    HashMap<LatLng, Marker> allTaxisLatLng = new HashMap<>();
    Polyline polyline;

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setCurrentLocation();
        }
    };

    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

//        setContentView(R.layout.activity_maps2);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapFragment.setHasOptionsMenu(true);
//        this.setTitle("SALAAM");
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setTitle("map title");
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
    protected void onStart() {
        super.onStart();
        registerReceiver(locationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationReceiver);
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
    }

    LatLngBounds mapBounds;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mClusterManager = new ClusterManager<TaxiItem>(this, map);
        map.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new TaxiRendered());

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                mapBounds = map.getProjection().getVisibleRegion().latLngBounds;
                markNearbyTaxi();
            }
        });
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();// OnConnectionFailedListener
        }
        googleApiClient.connect();
        map.setOnMapClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    String languageToLoad = "fa_IR";
    Locale fa_locale = new Locale(languageToLoad);

    @Override
    public void onMapClick(LatLng latLng) {
        if (sourceMarker == null) {
            Geocoder geoCoder = new Geocoder(getBaseContext(), fa_locale);
            String addressLine = "";
            try {
                List<Address> fromLocation = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                addressLine = fromLocation.get(0).getAddressLine(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MarkerOptions markerOption = new MarkerOptions().position(latLng).title(getString(R.string.src))
                    .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)).snippet(addressLine);
            sourceMarker = map.addMarker(markerOption);
        } else if (destinationMarker == null) {
            Geocoder geoCoder = new Geocoder(getBaseContext(), fa_locale);
            String addressLine = "";
            try {
                List<Address> fromLocation = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                addressLine = fromLocation.get(0).getAddressLine(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MarkerOptions markerOption = new MarkerOptions().position(latLng).title(getString(R.string.dest))
                    .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)).snippet(addressLine);
            destinationMarker = map.addMarker(markerOption);
            LatLng origin = sourceMarker.getPosition();
            LatLng dest = destinationMarker.getPosition();

            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);

        }
    }

    private ClusterManager<TaxiItem> mClusterManager;

    private void markNearbyTaxi() {
        List<TaxiItem> nearByTaxis = getNearbyTaxiLocation();
        mClusterManager.addItems(nearByTaxis);
        mClusterManager.cluster();
//        for (Marker taxiMarker : allTaxisLatLng.values()) {
//            taxiMarker.remove();
//        }
//        for (final LatLng taxiLocation : nearByTaxis) {
//            final MarkerOptions markerOptions = new MarkerOptions().position(
//                    new LatLng(taxiLocation.latitude, taxiLocation.longitude))
//                    .title("Taxi")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon))
//                    .snippet("Taxi");
//            Marker marker = map.addMarker(markerOptions);
//            allTaxisLatLng.put(taxiLocation, marker);
//        }
    }

    private List<TaxiItem> getNearbyTaxiLocation() {
        Random random = new Random();
        if (mapBounds == null)
            mapBounds = map.getProjection().getVisibleRegion().latLngBounds;
        double neLat = mapBounds.northeast.latitude;
        double swLat = mapBounds.southwest.latitude;
        double latBound = neLat - swLat;
        double neLng = mapBounds.northeast.longitude;
        double swLng = mapBounds.southwest.longitude;
        double lngBound = neLng - swLng;
        int taxiCount = random.nextInt(6) + 1;
        List<TaxiItem> latLngs = new ArrayList<>();
        for (LatLng latLng : allTaxisLatLng.keySet()) {
//            if (latLng.latitude < neLat && latLng.latitude > swLat && latLng.longitude < neLng && latLng.longitude > swLng)
            latLngs.add(new TaxiItem(latLng, R.drawable.taxi_icon));
        }
        for (int i = 0; i < taxiCount; i++) {
            Random r = new Random();
            double lng = swLng + (lngBound) * r.nextDouble();
            double lat = swLat + (latBound) * r.nextDouble();
            LatLng latLng = new LatLng(lat, lng);
            latLngs.add(new TaxiItem(latLng, R.drawable.taxi_icon));
        }
        return latLngs;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Vibrator systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        systemService.vibrate(200);

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        polyline.remove();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng origin = sourceMarker.getPosition();
        LatLng dest = destinationMarker.getPosition();
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String a[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(a, LOCATION_REQ_CODE);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
        setCurrentLocation();

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    ProgressDialog progressDialog;

    private void setCurrentLocation() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String a[] = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(a, LOCATION_REQ_CODE);
                return;
            }
        } else
            return;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            setDefaultLocation();
            return;
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        if (lastLocation == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("در حال اتصال به GPS");
            progressDialog.show();
            return;
        }

        final LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        Geocoder geoCoder = new Geocoder(getBaseContext(), fa_locale);
        String addressLine = "";
        try {
            List<Address> fromLocation = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            addressLine = fromLocation.get(0).getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkerOptions markerOption = new MarkerOptions().position(latLng).title(getString(R.string.src))
                .draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)).snippet(addressLine);
        sourceMarker = map.addMarker(markerOption);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        googleApiClient, mLocationRequest, this);
                setCurrentLocation();
            } else {
                setDefaultLocation();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    float tehranLat = 35.6937869f;
    float tehranLng = 51.4392857f;

    private void setDefaultLocation() {
        LatLng latLng = new LatLng(tehranLat, tehranLng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Suspended", Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed", Toast.LENGTH_LONG);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            setCurrentLocation();
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "mode=driving&sensor=true";


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parseRoute the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parseRoute(jObject);

                String sDistance = parseDistance(jObject);

                Log.d("XXYY", sDistance);

                System.out.println("my distance : " + sDistance);
//                Toast.makeText(this,sDistance , Toast.LENGTH_LONG);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        private String parseDistance(JSONObject jObject) throws JSONException {
            JSONArray routes = jObject.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);

            JSONArray legs = route.getJSONArray("legs");

            JSONObject steps = legs.getJSONObject(0);

            JSONObject distance = steps.getJSONObject("distance");
            return distance.getString("text");
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            polyline = map.addPolyline(lineOptions);
        }
    }

    public class TaxiRendered extends DefaultClusterRenderer<TaxiItem> {

        private final ImageView mImageView = new ImageView(getApplicationContext());
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());

        public TaxiRendered() {
            super(getApplicationContext(), map, mClusterManager);
        }


        @Override
        protected void onBeforeClusterItemRendered(TaxiItem item,
                                                   MarkerOptions markerOptions) {

            mImageView.setImageResource(item.icon);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_icon)).title("TAXI");
        }
    }
}