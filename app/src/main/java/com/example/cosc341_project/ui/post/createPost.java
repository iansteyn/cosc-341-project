package com.example.cosc341_project.ui.post;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cosc341_project.R;
import com.example.cosc341_project.data_classes.LocationList;
import com.example.cosc341_project.data_classes.Post;
import com.example.cosc341_project.data_classes.UserList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import com.example.cosc341_project.data_classes.SightingPost;
import com.example.cosc341_project.data_classes.PostListManager;

public class createPost extends AppCompatActivity {

    ImageButton chooseImage;
    Button nextButton;
    Spinner locationSpinner;
    Button backButton;
    EditText description;
    Bitmap PostImage;

    int imageId;
    AlertDialog galleryDialog;


    String[] selectedTags;
    FloatingActionButton showTags;
    int index;
    TextView tagsText;
    EditText title;
    CheckBox ogopogoCheckBox;
    CheckBox sasquatchCheckBox;
    TextView locationLabel;

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // LAYOUT AND VARIABLES SETUP
        // --------------------------
        // get intent and set layout
        boolean creatingSightingPost = getIntent().getBooleanExtra("creatingSightingPost", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Find xml elements
        chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        locationSpinner = findViewById(R.id.locationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.location_options, // Ensure this array exists in strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        description = findViewById(R.id.editTextTextMultiLine2);
        title = findViewById(R.id.editTextText);
        ogopogoCheckBox = findViewById(R.id.ogopogoCheckBox);
        sasquatchCheckBox = findViewById(R.id.sasquatchCheckBox);
        locationLabel = findViewById(R.id.locationLabel);
        index = 0;

        // if this is a discussion post, remove image and location options
        if (! creatingSightingPost) {
            chooseImage.setVisibility(View.GONE);
            locationSpinner.setVisibility(View.GONE);
            locationLabel.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Create a Discussion");
        }
        else {
            chooseImage.setImageResource(R.drawable.placeholder_select_image);
            getSupportActionBar().setTitle("Report a Sighting");
        }

        // CONFIGURE BUTTONS
        // -----------------

        backButton.setOnClickListener(v -> {
            finish();
        });

        // configure nextButton text and action
        nextButton.setText("Done");
        nextButton.setOnClickListener(v -> {
            // get Post arguments
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String[] tags = getCheckedTags();

            // Validate title and description
            if (titleText.isEmpty() || descriptionText.isEmpty()) {
                Toast("Please enter both a description and title");
            }
            // otherwise, add and save post, and end activity
            else {
                PostListManager plm = PostListManager.getInstance(this);
                Post newPost;

                if (!creatingSightingPost) {
                    newPost = new Post(UserList.CURRENT_USER_ID, titleText, descriptionText, tags);
                }
                else {
                    String selectedLocation = locationSpinner.getSelectedItem().toString();
                    double[] coordinates = LocationList.getCoordinates(selectedLocation);

                    // Validate location
                    if (coordinates == null) {
                        Toast("Please select a valid location");
                        return;
                    }

                    newPost = new SightingPost(
                            UserList.CURRENT_USER_ID,
                            titleText,
                            descriptionText,
                            tags,
                            imageId,
                            selectedLocation,
                            coordinates[0],
                            coordinates[1]
                    );
                }

                plm.postList.add(newPost);
                plm.saveToFile(this);
                finish();
            }
        });

        // configure image chooser
        if (creatingSightingPost) {
            chooseImage.setOnClickListener(v -> {
                String[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Image");
                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        dispatchTakePictureIntent();
                    } else if (which == 1) {
                        dispatchChoosePictureIntent();
                    }
                });
                builder.show();
            });
        }
    }

    // TAG HELPER METHODS
    // --------------
    private String[] getCheckedTags() {

        String[] checkedTags;
        if (ogopogoCheckBox.isChecked() && sasquatchCheckBox.isChecked()) {
            checkedTags = new String[] {ogopogoCheckBox.getText().toString(), sasquatchCheckBox.getText().toString()};
        }
        else if (ogopogoCheckBox.isChecked()) {
            checkedTags = new String[] {ogopogoCheckBox.getText().toString()};
        }
        else if (sasquatchCheckBox.isChecked()) {
            checkedTags = new String[] {sasquatchCheckBox.getText().toString()};
        }
        else {
            checkedTags = new String[] {};
        }

        return checkedTags;
    }

    // IMAGE CHOOSING METHODS
    // ----------------------
    private void dispatchTakePictureIntent() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(camera_intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private void dispatchChoosePictureIntent() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image from gallery");

        View imageGalleryView = View.inflate(this, R.layout.image_gallery,null);
        builder.setView(imageGalleryView);

        galleryDialog = builder.create();

        /* Set onclick listeners for each image view in the gallery
         * This is shitty, shitty code. But it works.
         */
        imageGalleryView
                .findViewById(R.id.alienImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_alien);});
        imageGalleryView
                .findViewById(R.id.ogopogoGreenImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_ogopogo_green);});
        imageGalleryView
                .findViewById(R.id.bigfootBlurryImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_bigfoot_blurry);});
        imageGalleryView
                .findViewById(R.id.ogopogoHumpsImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_ogopogo_humps);});
        imageGalleryView
                .findViewById(R.id.ogopogoSturgeonImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_ogopogo_sturgeon);});
        imageGalleryView
                .findViewById(R.id.shuswaggiImageView)
                .setOnClickListener(v -> {imageGalleryAction(R.drawable.img_shuswaggi);});

        galleryDialog.show();
    }

    //helper method for above
    private void imageGalleryAction(int resId) {
        imageId = resId;
        chooseImage.setImageResource(imageId);
        galleryDialog.dismiss();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == 1) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            PostImage = photo;
            // Set the image in imageview for display
            chooseImage.setImageBitmap(photo);
            imageId = R.drawable.img_from_camera; // There is only one
        }
        if(requestCode == 2) {

            // I don't think any of the below code is needed anymore but Ima leave it here -- Ian
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Convert URI to Bitmap
                    Bitmap galleryImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    PostImage = galleryImage;
                    // Display the chosen image in the ImageButton
                    chooseImage.setImageBitmap(galleryImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Method for toasts
    void Toast(String a){

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this /* MyActivity */, a, duration);
        toast.show();
    }
}


