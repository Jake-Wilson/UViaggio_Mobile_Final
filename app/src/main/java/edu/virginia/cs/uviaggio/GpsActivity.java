package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GpsActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Double lat, lon;
    private String name;
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
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        LatLng notSydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(notSydney).title(name)).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(notSydney,17));

    }
}
