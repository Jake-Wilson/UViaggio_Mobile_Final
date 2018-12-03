package edu.virginia.cs.uviaggio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.sql.Time;
import java.util.Timer;

public class GpsActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Double lat, lon;
    private String name;
    LocationManager locationManager;
    LocationListener locationListenerGPS;
    Double currentLat;
    Double currentLon;
    private static final int GPSPermission = 1;
    public TextView finishText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        Intent in = this.getIntent();

        lat = Double.valueOf(in.getStringExtra("lat"));
        lon = Double.valueOf(in.getStringExtra("lon"));
        name = in.getStringExtra("name");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        finishText = findViewById(R.id.finishTime);
        Button startTrack = findViewById(R.id.track);
        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setText("Stop Tracking");
                startTracking(v);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        LatLng notSydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(notSydney).title(name)).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(notSydney,17));

    }
    public void startTracking(View view){
        Button b = (Button) view;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setText("Start Tracking");
                //ToDo: stop tracking
                finishText.setText("Tracking Stopped");
                locationManager.removeUpdates(locationListenerGPS);
                Log.d("Updates stopped","yes");
            }
        });
        Long startTime = System.currentTimeMillis();
        Long endTime;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},GPSPermission);
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                Log.d("Lat in locationChanged", currentLat.toString());
                Log.d("Lon in LocationChanged", currentLon.toString());
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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListenerGPS);
            if(currentLat != null && (lat-currentLat)+(lon-currentLon) <= 1 ){
                endTime = System.currentTimeMillis();
                finishText.setText(endTime.toString());
                finishText.setVisibility(View.VISIBLE);
                locationManager.removeUpdates(locationListenerGPS);
            }
        }


    }
}
