package edu.virginia.cs.uviaggio;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

//        Intent in = this.getIntent();
//        Log.d("Received name : ", in.getStringExtra("name"));
//        Log.d("Received lat : ", in.getStringExtra("lat"));
//        Log.d("Received lon : ", (in.getStringExtra("lon")==null)?"Why is lon null":in.getStringExtra("lon"));
//        lat = Double.valueOf(in.getStringExtra("lat"));
//        lon = Double.valueOf(in.getStringExtra("lon"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
