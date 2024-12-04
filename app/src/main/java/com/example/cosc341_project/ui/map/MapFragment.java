package com.example.cosc341_project.ui.map;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cosc341_project.R;
import com.example.cosc341_project.data_classes.Post;
import com.example.cosc341_project.data_classes.PostListManager;
import com.example.cosc341_project.data_classes.SightingPost;
import com.example.cosc341_project.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Handler;
import android.os.Looper;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private PostListManager postListManager;

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

        // Initialize PostListManager instance
        postListManager = PostListManager.getInstance();

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

        // Add markers for each post
        addPostMarkers();

        // Set a click listener for markers
        mMap.setOnMarkerClickListener(marker -> {
            Post post = (Post) marker.getTag();
            if (post != null) {
                // Show custom popup with post information
                showPostPopup(post);
            }
            return false;
        });
    }

    private void addPostMarkers() {
        for (Post post : postListManager.postList) {
            // Assuming each Post has latitude and longitude properties (modify accordingly)
            if (post instanceof SightingPost) {
                SightingPost sightingPost = (SightingPost) post;
                LatLng postLocation = new LatLng(sightingPost.getLatitude(), sightingPost.getLongitude());

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(postLocation)
                        .title(sightingPost.getTitle()));
                marker.setTag(sightingPost);
            }
        }
    }

    private void showPostPopup(Post post) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.activity_marker_info_popup, null);

        // Set up the content in the popup view
        TextView addressTextView = popupView.findViewById(R.id.popup_address);
        addressTextView.setText("Address: " + ((SightingPost) post).getLocation());

        TextView titleTextView = popupView.findViewById(R.id.popup_title);
        titleTextView.setText("Title: " + post.getTitle());

        TextView dateTextView = popupView.findViewById(R.id.popup_date);
        dateTextView.setText("Date: " + post.getTimestamp().toString());

        Button detailsButton = popupView.findViewById(R.id.popup_details_button);
        detailsButton.setOnClickListener(v -> {
            // Handle details button click
            Toast.makeText(getContext(), "Opening post details...", Toast.LENGTH_SHORT).show();
            // Add logic to navigate to post details if needed
        });

        // Create and show the popup dialog
        new AlertDialog.Builder(getContext())
                .setView(popupView)
                .setCancelable(true)
                .show();
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
