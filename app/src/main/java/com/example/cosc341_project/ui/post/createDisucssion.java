package com.example.cosc341_project.ui.post;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cosc341_project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;

public class createDisucssion extends AppCompatActivity {
    Button done;
    EditText description;
    String[] tags;//Currently only four tags
    String[] selectedTags;
    FloatingActionButton showTags;
    int index;
    TextView tagsText;
    EditText title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_disucssion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



       // chooseImage = findViewById(R.id.SelectImage); // ImageButton to choose image
        done = findViewById(R.id.done1);
        description = findViewById(R.id.discussionText);
        title = findViewById(R.id.discussionTitle);
        showTags = findViewById(R.id.floatingActionButton);
        tagsText = findViewById(R.id.textView);
        index =0;

        tags = new String[]{"Ogopogo", "Bigfoot","Mothman","Windigo"};
        selectedTags = new String[tags.length];
        Arrays.fill(selectedTags, " ");
        showTags.setOnClickListener(v -> {

            tagsText.setText("Tags: "+ getArrayString(selectedTags));
            PopupMenu popupMenu = new PopupMenu(createDisucssion.this, v);
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



        //Code for next button, will move to the map, (currently textbox placeholder)
        // and will once done create a post, the next button to get to the map will
        // not work if the user has not entered a photo

        done.setOnClickListener(v -> {
            String descriptionText;
            descriptionText = String.valueOf(description.getText());
            String titleText;
            titleText = String.valueOf(title.getText());

            finish();
        });


    }

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


}
