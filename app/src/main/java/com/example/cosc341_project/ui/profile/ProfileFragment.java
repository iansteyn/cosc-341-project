package com.example.cosc341_project.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cosc341_project.R;
import com.example.cosc341_project.data_classes.Post;
import com.example.cosc341_project.data_classes.PostListManager;
import com.example.cosc341_project.data_classes.SightingPost;
import com.example.cosc341_project.data_classes.User;
import com.example.cosc341_project.data_classes.UserList;
import com.example.cosc341_project.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private User currentUser = UserList.get(UserList.CURRENT_USER_ID);
    private LinearLayout postsContainer;
    private PostListManager plm;
    private ArrayList<Post> posts;

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        postsContainer = root.findViewById(R.id.userPostsContainer);

        plm = PostListManager.getInstance(this.getContext());
        if (plm.postList.isEmpty()) {
            plm.addFakePosts();
        }
        posts = plm.postList;
        String [] tags = {"Sasquatch"};
        posts.add(new Post(10, "Is bigfoot a sasquatch?", "Is bigfoot a sasquatch of is a sasquatch a bigfoot?", tags)); // testing

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        addUserPostsToProfile(inflater, posts);

        return root;
    }

    private void addUserPostsToProfile(LayoutInflater inflater, ArrayList<Post> posts){

        for (int i = posts.size() -1; i>= 0; i--){
            if (posts.get(i).getUserId() == currentUser.getUserId()){

                View userPostView = inflater.inflate(R.layout.profile_post_item, postsContainer, false);

                TextView title = userPostView.findViewById(R.id.postTitle);
                title.setText(posts.get(i).getTitle());

                TextView description = userPostView.findViewById(R.id.postDescription);
                description.setText(posts.get(i).getDescription());

                TextView timestamp = userPostView.findViewById(R.id.postTimestamp);
                timestamp.setText(posts.get(i).getTimestamp().toString().substring(0,10));

                TextView location = userPostView.findViewById(R.id.postLocation);
                TextView type = userPostView.findViewById(R.id.postType);

                if (posts.get(i) instanceof SightingPost) {
                    SightingPost tempPost = (SightingPost)posts.get(i);
                    location.setText(tempPost.getLocation());
                    type.setText("Sighting");
                }
                else {
                    location.setVisibility(View.GONE);
                    type.setText("Discussion");
                }

                postsContainer.addView(userPostView);

            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}