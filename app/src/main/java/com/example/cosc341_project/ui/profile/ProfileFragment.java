package com.example.cosc341_project.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

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
    private ArrayList<Post> postList;
    private  ArrayList <Post> userPost;
    private TextView profileNumPosts;

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
        postList = plm.postList;

       userPost = new ArrayList<>();

        for (Post post: postList){
            if (post.getUserId() == currentUser.getUserId()){
                userPost.add(post);
            }
        }

        final ImageView profilePic = binding.imageViewProfilePic;
        profilePic.setImageResource(currentUser.getProfilePicId());

        final TextView textView = binding.textProfileUsername;
        textView.setText(currentUser.getUserName());
       // profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        profileNumPosts = binding.textViewNumOfPosts;
        profileNumPosts.setText("You have made "+userPost.size()+" posts:");

        addUserPostsToProfile(inflater, userPost);

        return root;
    }

    private void addUserPostsToProfile(LayoutInflater inflater, ArrayList<Post> posts){

        postsContainer.removeAllViews();
        for (Post post: posts){
            if (post.getUserId() == currentUser.getUserId()){

                View userPostView = inflater.inflate(R.layout.profile_post_item, postsContainer, false);

                TextView title = userPostView.findViewById(R.id.postTitle);
                title.setText(post.getTitle());

                TextView description = userPostView.findViewById(R.id.postDescription);
                description.setText(post.getDescription());

                TextView timestamp = userPostView.findViewById(R.id.postTimestamp);
                timestamp.setText(post.getTimestamp().toString().substring(0,10));

                TextView location = userPostView.findViewById(R.id.postLocation);
                TextView type = userPostView.findViewById(R.id.postType);

                if (post instanceof SightingPost) {
                    SightingPost tempPost = (SightingPost)post;
                    location.setText(tempPost.getLocation());
                    type.setText("Sighting");
                }
                else {
                    location.setVisibility(View.GONE);
                    type.setText("Discussion");
                }

                Button deleteButton = userPostView.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Delete Post:")
                                .setMessage("Are you sure you want to delete this post:\n"+post.getTitle())
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        posts.remove(post);
                                        postList.remove(post);
                                        profileNumPosts.setText("You have made "+posts.size()+" posts:");
                                        Toast.makeText(getContext(), "Post deleted successfully.", Toast.LENGTH_LONG).show();
                                        addUserPostsToProfile(inflater, posts);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });


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