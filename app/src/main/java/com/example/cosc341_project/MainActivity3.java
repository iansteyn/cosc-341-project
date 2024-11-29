package com.example.cosc341_project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import android.R.attr;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity3 extends AppCompatActivity {


    ImageButton chooseImage;
    Button Map;
    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image
        Map = findViewById(R.id.startMap);


        Map.setOnClickListener(v -> {

            System.out.println("LOG");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            Button next = new Button(this);
            next.setText("Done");
            EditText input = new EditText(this);
            next.setOnClickListener(v1 -> {
                //will have method to create a post once the object is ready
                //createPost(bitmap,location,tags,description)
            });
            input.setHint("Location");
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(input);
            layout.addView(next);
            builder1.setView(layout);
            builder1.show();
                });

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
    private void dispatchTakePictureIntent() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(camera_intent, 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private void dispatchChoosePictureIntent(){
        Intent camera_intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        try {
            startActivityForResult(camera_intent, 2);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == 1) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            chooseImage.setImageBitmap(photo);
        }
        if(requestCode == 2) {

            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Convert URI to Bitmap
                    Bitmap galleryImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    // Display the chosen image in the ImageButton
                    chooseImage.setImageBitmap(galleryImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    }



