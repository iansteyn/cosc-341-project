package com.example.cosc341_project.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cosc341_project.R;
import com.example.cosc341_project.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private GoogleMap mMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set up ViewModel
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        // Inflate the layout and binding
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up the download button
        Button downloadButton = binding.downloadMapButton;
        downloadButton.setOnClickListener(v -> downloadVisibleMapArea());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial map settings
        LatLngBounds okanaganBounds = new LatLngBounds(
                new LatLng(49.3, -120.2), // Expanded southwest corner
                new LatLng(50.7, -117.8)  // Expanded northeast corner
        );
        mMap.setLatLngBoundsForCameraTarget(okanaganBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.8801, -119.4436), 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Padding for zoom buttons
        mMap.setPadding(0, 0, 0, 150);
    }

    private void downloadVisibleMapArea() {
        if (mMap != null) {
            // Get visible map area bounds
            LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            // Show download message
            String boundsInfo = String.format(
                    "Southwest: %s, Northeast: %s",
                    visibleBounds.southwest.toString(),
                    visibleBounds.northeast.toString()
            );
            Toast.makeText(getContext(), "Downloading map for: " + boundsInfo, Toast.LENGTH_SHORT).show();

            // Simulate map download
            simulateMapDownload(visibleBounds);
        }
    }

    private void simulateMapDownload(LatLngBounds bounds) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Toast.makeText(getContext(), "Map downloaded successfully!", Toast.LENGTH_SHORT).show();
        }, 3000);
    }
}
