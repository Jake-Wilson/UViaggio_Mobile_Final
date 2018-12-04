package edu.virginia.cs.uviaggio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Security;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

public class GpsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Double lat, lon;
    private String name;
    LocationManager locationManager;
    LocationListener locationListenerGPS;
    Double currentLat;
    Double currentLon;
    long classStart;
    private static final int GPSPermission = 1;
    long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPSPermission);
        }
        Intent in = this.getIntent();

        lat = Double.valueOf(in.getStringExtra("lat"));
        lon = Double.valueOf(in.getStringExtra("lon"));
        classStart = in.getLongExtra("start", 0);
        name = in.getStringExtra("name");
        if (ContextCompat.checkSelfPermission(GpsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button startTrack = findViewById(R.id.track);
        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setText("Cancel Tracking");
                startTracking(v);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng notSydney = new LatLng(lat, lon);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                Log.d("Lat in locationChanged", currentLat.toString());
                Log.d("Lon in LocationChanged", currentLon.toString());

                float[] dist = {0};
                if (currentLat != null && currentLon != null) {
                    Location.distanceBetween(currentLat, currentLon, lat, lon, dist);
                    Log.d("Current lat: ", String.valueOf(currentLat));
                    Log.d("Current lon: ", String.valueOf(currentLon));
                    Log.d("Dist btwn: ", String.valueOf(dist[0]));
                    if (dist[0] <= 27) {
                        long endTime = System.currentTimeMillis();
                        Log.d("Done tracking location", "nowwwww");
                        locationManager.removeUpdates(locationListenerGPS);
                        Intent routeData = new Intent();
                        long totalTime = (classStart) - (endTime - startTime);
                        Log.d("leaveTime is: ", String.valueOf(totalTime));
                        routeData.putExtra("leaveTime", totalTime);
                        routeData.putExtra("name", name);
                        setResult(RESULT_OK, routeData);
                        finish();
                    }
                }
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

        mMap.addMarker(new MarkerOptions().position(notSydney).title(name)).showInfoWindow();
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException e){
            Log.e("error", e.getStackTrace().toString());
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(new Criteria(), false));
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You")).showInfoWindow();
        LatLng midpoint = new LatLng((notSydney.latitude + location.getLatitude()) / 2, (notSydney.longitude + location.getLongitude()) / 2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 5));

    }

    public void startTracking(View view) {
        Button b = (Button) view;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setText("Start Tracking");
                //ToDo: stop tracking
                locationManager.removeUpdates(locationListenerGPS);
                Log.d("Updates stopped", "yes");
            }
        });
        startTime = System.currentTimeMillis();
    }
}
