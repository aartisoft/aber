package com.mammutgroup.taxi.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;
import com.mammutgroup.taxi.util.DirectionsJSONParser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    protected GoogleMap map;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }

    protected class DownloadTask extends AsyncTask<String, Void, String> {

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
    protected class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
            if(map != null)
                map.addPolyline(lineOptions);
            else
                Log.d(AbstractHomeActivity.class.getSimpleName(),"map is null!!!!");
        }
    }

    protected void mapDownloadAndAddPolyline(LatLng origin, LatLng destination)
    {
        String url = getDirectionsUrl(origin, destination);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
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
            Log.d(getString(R.string.download_exception), e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
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

    protected void mapAnimateCamera(CameraUpdate cameraUpdate,MapAnimationCallback callback)
    {
        map.animateCamera(cameraUpdate,callback);
    }

    protected abstract class MapAnimationCallback implements GoogleMap.CancelableCallback
    {

        @Override
        public void onCancel() {

        }
    }

}
