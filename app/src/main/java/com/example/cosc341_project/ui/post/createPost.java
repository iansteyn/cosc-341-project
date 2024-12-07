package com.example.cosc341_project.ui.post;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cosc341_project.R;
import com.example.cosc341_project.data_classes.Post;
import com.example.cosc341_project.data_classes.UserList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import com.example.cosc341_project.data_classes.SightingPost;
import com.example.cosc341_project.data_classes.PostListManager;

public class createPost extends AppCompatActivity {

    ImageButton chooseImage;
    Button nextButton;
    Button backButton;
    Button locationButton;
    EditText description;
    Bitmap PostImage;

    int imageId;
    AlertDialog galleryDialog;

//    String[] tags;//Currently only four tags //TODO remove
    String[] selectedTags;
    String location;
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
        setContentView(R.layout.createpost);

        // Find xml elements
        chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        locationButton = findViewById(R.id.Location);
        description = findViewById(R.id.editTextTextMultiLine2);
        title = findViewById(R.id.editTextText);
        ogopogoCheckBox = findViewById(R.id.ogopogoCheckBox);
        sasquatchCheckBox = findViewById(R.id.sasquatchCheckBox);
        locationLabel = findViewById(R.id.locationLabel);
        index = 0;
        location = "";

        // if this is a discussion post, remove image and location options
        if (! creatingSightingPost) {
            chooseImage.setVisibility(View.GONE);
            locationButton.setVisibility(View.GONE);
            locationLabel.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Create a Discussion");
        }
        else {
            getSupportActionBar().setTitle("Report a Sighting");
        }

        //TODO remove
        // set tags list
//        tags = new String[]{"ogopogo", "sasquatch"};
//        selectedTags = new String[tags.length];
//        Arrays.fill(selectedTags, " ");

        // CONFIGURE BUTTONS
        // -----------------

        locationButton.setOnClickListener(v -> {
            // TODO (Mehdi)- get location data
            location = "placeholder";
        });

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

            //if location is empty, show error message
            if (location.isEmpty()&&creatingSightingPost) {
                Toast("Please enter a Location");
            }else{
            // if fields are empty, show error message
            if (titleText.isEmpty() || descriptionText.isEmpty()) {
                Toast("Please enter both a description and title");
            }
            // otherwise, add and save post, and end activity
            else {
                PostListManager plm = PostListManager.getInstance(this);

                Post newPost;
                if (! creatingSightingPost) {
                    newPost = new Post(UserList.CURRENT_USER_ID, titleText, descriptionText, tags);
                }
                else {
                    // TODO (Mehdi)- get location data
                    String location = "Placeholder Location";
                    double latitude = 49.8801;
                    double longitude = -119.4436;
                    newPost = new SightingPost(
                            UserList.CURRENT_USER_ID,
                            titleText,
                            descriptionText,
                            tags,
                            imageId,
                            location,
                            latitude,
                            longitude
                    );
                }

                plm.postList.add(newPost);
                Log.d("IAN DEBUG", "postList after adding in createPost:\n" + plm.postList.toString());
                plm.saveToFile(this);
                finish();
            }}
        });

        // configure the tags button
        //TODO remove
//        showTags.setOnClickListener(v -> {
//
//            tagsText.setText("Tags: "+ getArrayString(selectedTags));
//            PopupMenu popupMenu = new PopupMenu(createPost.this, v);
//            for (int i = 0; i < tags.length; i++) {
//                popupMenu.getMenu().add(tags[i]);
//            }
//
//            popupMenu.setOnMenuItemClickListener(item -> {
//                String tag = item.getTitle().toString();
//
//                if (!haveTag(selectedTags,tag)) {
//                    selectedTags[index] = tag;
//                    tagsText.setText("Tags: "+ getArrayString(selectedTags));
//                    index++;
//                } else {
//                    removeTag(selectedTags,tag);
//                    tagsText.setText("Tags: "+ getArrayString(selectedTags));
//                    index--;
//                }
//                return true;
//            });
//
//            popupMenu.show();
//
//        });

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

//    String getArrayString(String[] array){
//        String string ="";
//        for (int i = 0; i < tags.length; i++) {
//            if(!array[i].equals(" ")) {
//                string = string + " " + array[i]+",";
//            }
//        }
//        return string;
//    }
//
//    boolean haveTag(String[] array, String string){
//        for (String s : array) {
//            if (s.equals(string)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    void removeTag(String[] array, String value) {
//
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] != null && array[i].equals(value)) {
//
//                for (int j = i; j < array.length - 1; j++) {
//                    array[j] = array[j + 1];
//                }
//                array[array.length - 1] = " ";
//                break;
//            }
//        }
//    }

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


