package com.example.cosc341_project;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Register the callback
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Restrict the map to the Okanagan area
        LatLngBounds okanaganBounds = new LatLngBounds(
                new LatLng(49.5, -120.0), // Southwest corner
                new LatLng(50.5, -118.0)  // Northeast corner
        );

        // Restrict the camera to the Okanagan region
        mMap.setLatLngBoundsForCameraTarget(okanaganBounds);

        // Move the camera to Kelowna
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.9, -119.5), 10));

        // Add some markers
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.8801, -119.4436)) // Kelowna
                .title("Kelowna"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(49.9394, -119.3948)) // UBCO
                .title("UBCO"));

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Handle marker clicks
        mMap.setOnMarkerClickListener(marker -> {
            String title = marker.getTitle();
            Toast.makeText(this, "Clicked on: " + title, Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}
