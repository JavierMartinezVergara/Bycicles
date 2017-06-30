package com.android.javier.bycicles;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.RoadsApi;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.SnappedPoint;
import com.google.maps.model.SpeedLimit;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.LongSparseArray;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double la = 0.0;
    double lo = 0.0;
    private Marker marcador;


/**    private GeoApiContext mContext;

 /**
 * The number of points allowed per API request. This is a fixed value.
 */
    /** private static final int PAGE_SIZE_LIMIT = 100;

     /**
     * Define the number of data points to re-send at the start of subsequent requests. This helps
     * to influence the API with prior data, so that paths can be inferred across multiple requests.
     * You should experiment with this value for your use-case.
     */
    /**private static final int PAGINATION_OVERLAP = 5;

     /**
     * Icon cache for {@link #generateSpeedLimitMarker}.
     */
    /**private LongSparseArray<BitmapDescriptor> mSpeedIcons = new LongSparseArray<>();
     private IconGenerator mIconGenerator;

     private ProgressBar mProgressBar;

     List<LatLng> mCapturedLocations;
     List<SnappedPoint> mSnappedPoints;
     Map<String, SpeedLimit> mPlaceSpeeds;

     AsyncTask<Void, Void, List<SnappedPoint>> mTaskSnapToRoads =
     new AsyncTask<Void, Void, List<SnappedPoint>>() {
    @Override protected void onPreExecute() {
    mProgressBar.setVisibility(View.VISIBLE);
    mProgressBar.setIndeterminate(true);
    }

    @Override protected List<SnappedPoint> doInBackground(Void... params) {
    try {
    return snapToRoads(mContext);
    } catch (final Exception ex) {
    toastException(ex);
    ex.printStackTrace();
    return null;
    }
    }

    @Override protected void onPostExecute(List<SnappedPoint> snappedPoints) {
    mSnappedPoints = snappedPoints;
    mProgressBar.setVisibility(View.INVISIBLE);

    findViewById(R.id.speed_limits).setEnabled(true);

    com.google.android.gms.maps.model.LatLng[] mapPoints =
    new com.google.android.gms.maps.model.LatLng[mSnappedPoints.size()];
    int i = 0;
    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
    for (SnappedPoint point : mSnappedPoints) {
    mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.location.lat,
    point.location.lng);
    bounds.include(mapPoints[i]);
    i += 1;
    }

    mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
    }
    };

     AsyncTask<Void, Integer, Map<String, SpeedLimit>> mTaskSpeedLimits =
     new AsyncTask<Void, Integer, Map<String, SpeedLimit>>() {
     private List<MarkerOptions> markers;

     @Override protected void onPreExecute() {
     markers = new ArrayList<>();
     mProgressBar.setIndeterminate(true);    // Just until we know how much to Geocode
     mProgressBar.setProgress(0);
     mProgressBar.setVisibility(View.VISIBLE);
     }

     @Override protected Map<String, SpeedLimit> doInBackground(Void... params) {
     Map<String, SpeedLimit> placeSpeeds = null;
     try {
     placeSpeeds = getSpeedLimits(mContext, mSnappedPoints);
     publishProgress(0, placeSpeeds.size());

     // Generate speed limit icons, with geocoded labels.
     Set<String> visitedPlaceIds = new HashSet<>();
     for (SnappedPoint point : mSnappedPoints) {
     if (!visitedPlaceIds.contains(point.placeId)) {
     visitedPlaceIds.add(point.placeId);

     GeocodingResult geocode = geocodeSnappedPoint(mContext, point);
     publishProgress(visitedPlaceIds.size());

     // As each place has been geocoded, we'll use the name of the place
     // as the marker title, so tapping the marker will display the address.
     markers.add(generateSpeedLimitMarker(
     placeSpeeds.get(point.placeId).speedLimit, point, geocode));
     }
     }
     } catch (Exception ex) {
     toastException(ex);
     ex.printStackTrace();
     }

     return placeSpeeds;
     }

     @Override protected void onProgressUpdate(Integer... values) {
     mProgressBar.setProgress(values[0]);
     if (values.length > 1) {
     mProgressBar.setIndeterminate(false);
     mProgressBar.setMax(values[1]);
     }
     }

     @Override protected void onPostExecute(Map<String, SpeedLimit> speeds) {
     for (MarkerOptions marker : markers) {
     mMap.addMarker(marker);
     }
     mProgressBar.setVisibility(View.INVISIBLE);
     mPlaceSpeeds = speeds;
     }
     };





     /**
      * Manipulates the map once available.
      * This callback is triggered when the map is ready to be used.
      * This is where we can add markers or lines, add listeners or move the camera. In this case,
      * we just add a marker near Sydney, Australia.
      * If Google Play services is not installed on the device, the user will be prompted to install
      * it inside the SupportMapFragment. This method will only be triggered once the user has
      * installed Google Play services and returned to the app.
     */


/**
 private List<LatLng> loadGpxData(XmlPullParser parser, InputStream gpxIn)
 throws XmlPullParserException, IOException {
 List<LatLng> latLngs = new ArrayList<>();   // List<> as we need subList for paging later
 parser.setInput(gpxIn, null);
 parser.nextTag();

 while (parser.next() != XmlPullParser.END_DOCUMENT) {
 if (parser.getEventType() != XmlPullParser.START_TAG) {
 continue;
 }

 if (parser.getName().equals("wpt")) {
 // Save the discovered lat/lon attributes in each <wpt>
 latLngs.add(new LatLng(
 Double.valueOf(parser.getAttributeValue(null, "lat")),
 Double.valueOf(parser.getAttributeValue(null, "lon"))));
 }
 // Otherwise, skip irrelevant data
 }

 return latLngs;
 }

 /**
 * Handles the GPX button-click event, running the demo snippet {@link #loadGpxData}.
 */
    /** public void onGpxButtonClick(View view) {
     try {
     mCapturedLocations = loadGpxData(Xml.newPullParser(),
     getResources().openRawResource(R.raw.gpx_data));
     findViewById(R.id.snap_to_roads).setEnabled(true);

     LatLngBounds.Builder builder = new LatLngBounds.Builder();
     PolylineOptions polyline = new PolylineOptions();

     for (LatLng ll : mCapturedLocations) {
     com.google.android.gms.maps.model.LatLng mapPoint =
     new com.google.android.gms.maps.model.LatLng(ll.lat, ll.lng);
     builder.include(mapPoint);
     polyline.add(mapPoint);
     }

     mMap.addPolyline(polyline.color(Color.RED));
     mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
     } catch (XmlPullParserException | IOException e) {
     e.printStackTrace();
     toastException(e);
     }
     }

     /**
     * Snaps the points to their most likely position on roads using the Roads API.
     */
    /**private List<SnappedPoint> snapToRoads(GeoApiContext context) throws Exception {
     List<SnappedPoint> snappedPoints = new ArrayList<>();

     int offset = 0;
     while (offset < mCapturedLocations.size()) {
     // Calculate which points to include in this request. We can't exceed the APIs
     // maximum and we want to ensure some overlap so the API can infer a good location for
     // the first few points in each request.
     if (offset > 0) {
     offset -= PAGINATION_OVERLAP;   // Rewind to include some previous points
     }
     int lowerBound = offset;
     int upperBound = Math.min(offset + PAGE_SIZE_LIMIT, mCapturedLocations.size());

     // Grab the data we need for this page.
     LatLng[] page = mCapturedLocations
     .subList(lowerBound, upperBound)
     .toArray(new LatLng[upperBound - lowerBound]);

     // Perform the request. Because we have interpolate=true, we will get extra data points
     // between our originally requested path. To ensure we can concatenate these points, we
     // only start adding once we've hit the first new point (i.e. skip the overlap).
     SnappedPoint[] points = RoadsApi.snapToRoads(context, true, page).await();
     boolean passedOverlap = false;
     for (SnappedPoint point : points) {
     if (offset == 0 || point.originalIndex >= PAGINATION_OVERLAP) {
     passedOverlap = true;
     }
     if (passedOverlap) {
     snappedPoints.add(point);
     }
     }

     offset = upperBound;
     }

     return snappedPoints;
     }

     /**
     * Handles the Snap button-click event, running the demo snippet {@link #snapToRoads}.
     */
    /**public void onSnapToRoadsButtonClick(View view) {
     mTaskSnapToRoads.execute();
     }

     /**
     * Retrieves speed limits for the previously-snapped points. This method is efficient in terms
     * of quota usage as it will only query for unique places.
     *
     * Note: Speed Limit data is only available with an enabled Maps for Work API key.
     */
    /**private Map<String, SpeedLimit> getSpeedLimits(GeoApiContext context, List<SnappedPoint> points)
     throws Exception {
     Map<String, SpeedLimit> placeSpeeds = new HashMap<>();

     // Pro tip: save on quota by filtering to unique place IDs
     for (SnappedPoint point : points) {
     placeSpeeds.put(point.placeId, null);
     }

     String[] uniquePlaceIds =
     placeSpeeds.keySet().toArray(new String[placeSpeeds.keySet().size()]);

     // Loop through the places, one page (API request) at a time.
     for (int i = 0; i < uniquePlaceIds.length; i += PAGE_SIZE_LIMIT) {
     String[] page = Arrays.copyOfRange(uniquePlaceIds, i,
     Math.min(i + PAGE_SIZE_LIMIT, uniquePlaceIds.length));

     // Execute!
     SpeedLimit[] placeLimits = RoadsApi.speedLimits(context, page).await();
     for (SpeedLimit sl : placeLimits) {
     placeSpeeds.put(sl.placeId, sl);
     }
     }

     return placeSpeeds;
     }

     /**
     * Geocodes a Snapped Point using the Place ID.
     */
    /**private GeocodingResult geocodeSnappedPoint(GeoApiContext context, SnappedPoint point) throws Exception {
     GeocodingResult[] results = GeocodingApi.newRequest(context)
     .place(point.placeId)
     .await();

     if (results.length > 0) {
     return results[0];
     }
     return null;
     }

     /**
     * Handles the Speed Limit button-click event, running the demo snippets {@link #getSpeedLimits}
     * and {@link #geocodeSnappedPoint} behind a progress dialog.
     */
    /**public void onSpeedLimitButtonClick(View view) {
     mTaskSpeedLimits.execute();
     }

     /**
     * Generates a marker that looks like a speed limit sign.
     */
    /**private MarkerOptions generateSpeedLimitMarker(double speed, SnappedPoint point,
     GeocodingResult geocode) {
     if (mIconGenerator == null) {
     mIconGenerator = new IconGenerator(getApplicationContext());
     mIconGenerator
     .setContentView(getLayoutInflater().inflate(R.layout.speed_limit_view, null));
     mIconGenerator.setBackground(null);
     }

     // Cache icons.
     long speedLabel = Math.round(speed);
     BitmapDescriptor icon = mSpeedIcons.get(speedLabel);
     if (icon == null) {
     icon = BitmapDescriptorFactory
     .fromBitmap(mIconGenerator.makeIcon(String.valueOf(speedLabel)));
     mSpeedIcons.put(speedLabel, icon);
     }

     return new MarkerOptions()
     .icon(icon)
     .position(new com.google.android.gms.maps.model.LatLng(
     point.location.lat, point.location.lng))
     .flat(true)
     .title(geocode != null
     ? geocode.formattedAddress
     : point.placeId);
     }

     /** Helper for toasting exception messages on the UI thread. */
    /**private void toastException(final Exception ex) {
     runOnUiThread(new Runnable() {
    @Override public void run() {
    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
    }
    });
     }



     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //    mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        //  mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Ubicationseg();




        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera


    }

    private void marcadorUbicacion(double lat, double lon) {
        com.google.android.gms.maps.model.LatLng coordenadas = new com.google.android.gms.maps.model.LatLng(lat, lon);
        CameraUpdate miUbication = CameraUpdateFactory.newLatLngZoom(coordenadas, 20);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Mi ubicacion")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(miUbication);


    }

    private void actualizarUbication(Location location) {
        if (location != null) {
            la = location.getLatitude();
            lo = location.getLongitude();
            marcadorUbicacion(la, lo);
        }


    }


    LocationListener locListener =new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            actualizarUbication(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private void Ubicationseg() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        actualizarUbication(location1);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500,0,  locListener);
    }

}
