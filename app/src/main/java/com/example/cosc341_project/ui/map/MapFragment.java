package com.example.cosc341_project.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosc341_project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Padding to stop the zoom button from being blocked
        mMap.setPadding(0, 0, 0, 150);

        LatLngBounds okanaganBounds = new LatLngBounds(
                new LatLng(49.3, -120.2), // Expanded southwest corner
                new LatLng(50.7, -117.8)  // Expanded northeast corner
        );
        mMap.setLatLngBoundsForCameraTarget(okanaganBounds);

        // Move the camera to a default location within the Okanagan
        LatLng kelowna = new LatLng(49.8801, -119.4436);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna, 10));

        // Add mock cryptid sightings for testing
        addMockCryptidSightings();
    }

    // Method to add cryptid sightings dynamically
    private void addMockCryptidSightings() {
        // Mock list of sightings with addresses and cryptid names
        List<String[]> sightings = List.of(
                new String[]{"Kelowna, BC", "Ogopogo"},
                new String[]{"Lake Country, BC", "Shuswaggi"},
                new String[]{"Osoyoos, BC", "Giant Salamander"}
        );

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        for (String[] sighting : sightings) {
            String address = sighting[0];
            String cryptidName = sighting[1];

            try {
                List<Address> geoResults = geocoder.getFromLocationName(address, 1);
                if (geoResults != null && !geoResults.isEmpty()) {
                    Address location = geoResults.get(0);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Add marker with cryptid name
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(cryptidName)
                            .snippet("Spotted at: " + address));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
