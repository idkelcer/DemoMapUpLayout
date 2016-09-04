package com.example.hp.demouplayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout bottomSheet;

    BottomSheetBehavior bsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheet = (LinearLayout)findViewById(R.id.bottomSheet);
        bsb = BottomSheetBehavior.from(bottomSheet);

        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        setupMap();
    }

    private void setupMap() {

        mMap.getUiSettings().setZoomControlsEnabled(true);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
            //return;
        }*/


        LatLng latLng = new LatLng(-12.046374, -77.042793);
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(marker);

        latLng = new LatLng(-12.046374, -77.052792);
        marker = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.addMarker(marker);

        latLng = new LatLng(-12.056375, -77.042793);
        marker = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.addMarker(marker);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {

                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(bsb.getState() == BottomSheetBehavior.STATE_EXPANDED || bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }
}
