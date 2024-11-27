package com.example.cosc341_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity3 extends AppCompatActivity {


    ImageButton chooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image

        chooseImage.setOnClickListener(v -> {
            String[] options = {"Take Photo", "Choose from Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Image");
            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    Uri selectedImage = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                        chooseImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (which == 1) {

                      }
            });
            builder.show();
        });
    }

}
