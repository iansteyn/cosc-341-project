package com.example.cosc341_project;

import android.os.Bundle;
import android.util.Log;

import com.example.cosc341_project.data_classes.PostListManager;
import com.example.cosc341_project.data_classes.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cosc341_project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_map, R.id.navigation_post, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //TESTING PostListManager and posts
        PostListManager plm = PostListManager.getInstance();
        plm.addFakePosts();
        Log.d("IAN - TEST ADDFAKEPOSTS", plm.postList.toString());

        Post post0 = plm.postList.get(0);
        Log.d("IAN - before", post0.toString());
        post0.addLike();
        post0.addDislike();
        post0.addComment(8, "My first comment!");
        post0.setTags(new String[]{"new tag!"});
        post0.setTitle("modified title");
        post0.setDescription("Modified description");

        Post post0Modified = plm.postList.get(0);
        Log.d("IAN - after", post0Modified.toString());



    }

}