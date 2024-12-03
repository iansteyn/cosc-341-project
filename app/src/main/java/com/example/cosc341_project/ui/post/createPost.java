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
import com.example.cosc341_project.data_classes.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Arrays;
import com.example.cosc341_project.data_classes.SightingPost;
import com.example.cosc341_project.data_classes.PostListManager;

public class createPost extends AppCompatActivity {

    ImageButton chooseImage;
    Button nextButton;
    EditText description;
    Bitmap PostImage;
    String[] tags;//Currently only four tags
    String[] selectedTags;
    FloatingActionButton showTags;
    int index;
    TextView tagsText;
    EditText title;

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get intent and set layout
        boolean sighting = getIntent().getBooleanExtra("sighting", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpost);

        // Find xml elementSs
        chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image
        nextButton = findViewById(R.id.startMap);
        description = findViewById(R.id.editTextTextMultiLine2);
        title = findViewById(R.id.editTextText);
        showTags = findViewById(R.id.showTags);
        tagsText = findViewById(R.id.tags);
        index =0;

        // set tags list
        tags = new String[]{"Ogopogo", "Bigfoot","Mothman","Wendigo"};
        selectedTags = new String[tags.length];
        Arrays.fill(selectedTags, " ");

        // configure nextButton's OnClick action
        String descriptionText;
        descriptionText = String.valueOf(description.getText());
        String titleText;
        titleText = String.valueOf(title.getText());

        if (!sighting) {
            chooseImage.setVisibility(View.GONE);
            nextButton.setText("Done");

            nextButton.setOnClickListener(v -> {
                int userId = 1;
                if(!(titleText.equals("")||descriptionText.equals(""))){

                    PostListManager plm = PostListManager.getInstance();
                    Post newPost = new Post(userId, titleText, descriptionText, tags);
                    plm.postList.add(newPost);
                    plm.saveToFile();
                    finish();}
            });
        }
        else {
            nextButton.setOnClickListener(v -> {

                Button next = new Button(this);
                next.setText("Done");
                EditText input = new EditText(this);

                //PostImage is bitmap variable
                if(!(titleText.equals("")||descriptionText.equals(""))){
                    next.setOnClickListener(v1 -> {
                        //will have method to create a post once the object is ready
                        //Currently a placeholder
                        //selectedTags for string array
                        String Location = String.valueOf(input.getText());
                        //public Post(int userId, String title, String description, String[] tags)
                        //User.getUserId()
                        int userId = 1;
                        PostListManager plm = PostListManager.getInstance();
                        SightingPost newPost = new SightingPost(userId, titleText, descriptionText, tags,PostImage.toString(),Location);
                        plm.postList.add(newPost);
                        plm.saveToFile();
                        finish();
                    });
                    input.setHint("Location");
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(input);
                    layout.addView(next);
                    builder1.setView(layout);
                    builder1.show();}
            });

        }

        showTags.setOnClickListener(v -> {

            tagsText.setText("Tags: "+ getArrayString(selectedTags));
            PopupMenu popupMenu = new PopupMenu(createPost.this, v);
            for (int i = 0; i < tags.length; i++) {
                popupMenu.getMenu().add(tags[i]);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                String tag = item.getTitle().toString();


                if (!haveTag(selectedTags,tag)) {
                    selectedTags[index] = tag;
                    tagsText.setText("Tags: "+ getArrayString(selectedTags));
                    index++;
                } else {
                    removeTag(selectedTags,tag);
                    tagsText.setText("Tags: "+ getArrayString(selectedTags));
                    index--;
                }
                return true;
            });

            popupMenu.show();

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

    // HELPER METHODS
    // --------------
    String getArrayString(String[] array){
        String string ="";
        for (int i = 0; i < tags.length; i++) {
            if(!array[i].equals(" ")) {
                string = string + " " + array[i]+",";
            }
        }
        return string;
    }

    boolean haveTag(String[] array, String string){
        for (String s : array) {
            if (s.equals(string)) {
                return true;
            }
        }
        return false;
    }

    void removeTag(String[] array, String value) {

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].equals(value)) {

                for (int j = i; j < array.length - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[array.length - 1] = " ";
                break;
            }
        }
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
            PostImage = photo;
            // Set the image in imageview for display
            chooseImage.setImageBitmap(photo);
        }
        if(requestCode == 2) {

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
        //Code for next button, will move to the map, (currently textbox placeholder)
        // and will once done create a post, the next button to get to the map will
        // not work if the user has not entered a photo


    }

}


