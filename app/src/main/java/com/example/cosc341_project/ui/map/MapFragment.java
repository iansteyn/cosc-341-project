package com.example.cosc341_project.ui.map;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private PostListManager postListManager;
    private List<Marker> markerList = new ArrayList<>();

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
        postListManager = PostListManager.getInstance(this.getContext());

        // Set up the download button
        Button downloadButton = binding.downloadMapButton;
        downloadButton.setOnClickListener(v -> downloadVisibleMapArea());

        // Set up the filter button
        Button filterButton = binding.filterButton;
        filterButton.setOnClickListener(v -> showFilterDialog());

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
        if (mMap == null) {
            Log.d("MapDebug", "GoogleMap instance is null");
            return;
        }

        markerList.clear(); // Clear any existing markers

        for (Post post : postListManager.postList) {
            Log.d("MapDebug", "Processing post: " + post.toString());

            // Check if the post is a SightingPost
            if (post instanceof SightingPost) {
                SightingPost sightingPost = (SightingPost) post;

                // Log location details
                Log.d("MapDebug", "Location for post: " + sightingPost.getLocation());
                Log.d("MapDebug", "Coordinates: " + sightingPost.getLatitude() + ", " + sightingPost.getLongitude());

                // Add a marker for the SightingPost
                LatLng postLocation = new LatLng(sightingPost.getLatitude(), sightingPost.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(postLocation)
                        .title(sightingPost.getTitle()));
                marker.setTag(sightingPost); // Attach the SightingPost as a tag
                markerList.add(marker);
            } else {
                // Log for non-SightingPost objects
                Log.d("MapDebug", "Skipping non-SightingPost: " + post.getTitle());
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

        ImageView imageView = popupView.findViewById(R.id.popup_image);
        imageView.setImageResource(((SightingPost) post).getImageId());

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

    private void showFilterDialog() {
        // Create an AlertDialog for filtering options
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter Markers");

        // Inflate the filter layout
        View filterView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(filterView);

        // Set up date filter spinner
        Spinner dateSpinner = filterView.findViewById(R.id.date_filter_spinner);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_filter_options, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        // Set up creature filter spinner
        Spinner creatureSpinner = filterView.findViewById(R.id.creature_filter_spinner);
        ArrayAdapter<CharSequence> creatureAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.creature_filter_options, android.R.layout.simple_spinner_item);
        creatureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creatureSpinner.setAdapter(creatureAdapter);

        // Set up filter button actions
        builder.setPositiveButton("Apply", (dialog, which) -> {
            String selectedDateFilter = dateSpinner.getSelectedItem().toString();
            String selectedCreatureFilter = creatureSpinner.getSelectedItem().toString();
            applyFilters(selectedDateFilter, selectedCreatureFilter);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the filter dialog
        builder.create().show();
    }

    private void applyFilters(String dateFilter, String creatureFilter) {
        // Clear current markers
        mMap.clear();
        markerList.clear();

        for (Post post : postListManager.postList) {
            if (post instanceof SightingPost) {
                SightingPost sightingPost = (SightingPost) post;

                // Apply date filter (this is a placeholder, implement the actual date logic)
                boolean dateMatches = true;
                if (dateFilter.equals("Today")) {
                    // Check if the post's timestamp is from today
                    dateMatches = /* implement date matching logic here */ true;
                }
                // Add other date filters as needed

                // Apply creature filter
                boolean creatureMatches = creatureFilter.equals("All") ||
                        Arrays.asList(sightingPost.getTags())
                                .stream()
                                .anyMatch(tag -> tag.equalsIgnoreCase(creatureFilter));
                // If both filters match, add the marker
                if (dateMatches && creatureMatches) {
                    LatLng postLocation = new LatLng(sightingPost.getLatitude(), sightingPost.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(postLocation)
                            .title(sightingPost.getTitle()));
                    marker.setTag(sightingPost);
                    markerList.add(marker);
                }

            }
        }
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
