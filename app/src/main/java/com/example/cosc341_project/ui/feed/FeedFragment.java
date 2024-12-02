package com.example.cosc341_project.ui.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cosc341_project.R;
import com.example.cosc341_project.databinding.FragmentFeedBinding;
import com.example.cosc341_project.data_classes.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedFragment extends Fragment {

    FeedViewModel feedViewModel;
    LinearLayout postsContainer;
    private PostListManager plm;
    private ArrayList<Post> posts; // Stores posts loaded from the post list manager.
    private final HashSet<Post> likedPosts = new HashSet<>(); // Stores posts that the user has already liked.
    private final HashSet<Post> dislikedPosts = new HashSet<>(); // Stores posts that the user has already disliked.
    private HashSet<Integer> checkedButtonIDs; // Stores the IDs of buttons checked in the filter view. Set as a class variable so upon reloading, previously selected options remain selected.

    private FragmentFeedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FeedViewModel feedViewModel =
                new ViewModelProvider(this).get(FeedViewModel.class);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        postsContainer = root.findViewById(R.id.postsContainer);

        plm = feedViewModel.getPostListManager();
        posts = plm.postList;

        checkedButtonIDs = new HashSet<>();
        Log.e("Posts List Acquired?", "true");

        postsContainer = binding.postsContainer;

        addPostsToScrollView(posts, inflater, false);
        // final TextView textView = binding.textFeed;
        // feedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void addPostsToScrollView(ArrayList<Post> postList, LayoutInflater inflater, boolean reload) {
        Log.e("Adding posts to scroll view?", "true");
        postsContainer.removeAllViews(); // Safety Check: Clear current scroll view.

        // Initialize filter view.
        View filterView = inflater.inflate(R.layout.filter_view, postsContainer, false);
        postsContainer.addView(filterView);

        // Initialize filter buttons.
        Button applyButton = filterView.findViewById(R.id.applyButton);
        Button selectAllButton = filterView.findViewById(R.id.selectAllButton);

        // List of CheckBox IDs from filter view.
        ArrayList<Integer> filterBoxIDs = new ArrayList<>();

        // Initialize all CheckBoxs for filter view and add IDs to filterBoxIDs.
        CheckBox sightingBox = filterView.findViewById(R.id.checkBoxSighting);
        filterBoxIDs.add(sightingBox.getId());
        CheckBox discussionBox = filterView.findViewById(R.id.checkBoxDiscussion);
        filterBoxIDs.add(discussionBox.getId());
        CheckBox ogopogoBox = filterView.findViewById(R.id.checkBoxOgopogo);
        filterBoxIDs.add(ogopogoBox.getId());
        CheckBox sasquatchBox = filterView.findViewById(R.id.checkBoxSasquatch);
        filterBoxIDs.add(sasquatchBox.getId());

        // Set all to be checked by default (only upon first load, i.e., not a reload).
        if (!reload) {
            for (Integer id : filterBoxIDs) {
                CheckBox tempBox = filterView.findViewById(id);
                tempBox.setChecked(true);
                checkedButtonIDs.add(id);
            }
        }

        // Initialize spinner for order options.
        Spinner orderBySpinner = filterView.findViewById(R.id.orderBySpinner);

        // Set spinner options via ArrayAdapter.
        ArrayAdapter <CharSequence>orderByAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.orderByOptions, android.R.layout.simple_spinner_item
        );
        orderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderBySpinner.setAdapter(orderByAdapter);

        // Set the option showing as selected to the users most recent selection, defaults to 0.
        int selectedOrderOption = 0;
        orderBySpinner.setSelection(selectedOrderOption);

        // Ensure that if CheckBox was unselected, when the feed reloads it remains unselected.
        // Skip upon first load, does not effect anything if it runs on first load, but unnecessary.
        if (reload) {
            for (Integer id: checkedButtonIDs){
                CheckBox tempBox = filterView.findViewById(id);
                tempBox.setChecked(true);
            }
        }

        // On click listener for 'Select All' button.
        selectAllButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /* ------ Testing new option.
                sightingBox.setChecked(true);
                discussionBox.setChecked(true);
                ogopogoBox.setChecked(true);
                sasquatchBox.setChecked(true);
                */
                for (Integer id: filterBoxIDs){
                    CheckBox tempBox = filterView.findViewById(id);
                    tempBox.setChecked(true);
                    checkedButtonIDs.add(id);
                }
            }
        });

        // Set on click listener for 'Apply' button.
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Post> filteredPosts = new ArrayList<>();
                LinkedList<String> selectedFilters = new LinkedList<>(); // List to store selected filters.

                // Iterate through each CheckBox to add selected filters to 'selectedFilters'.
                for (Integer id: filterBoxIDs){
                    CheckBox tempBox = filterView.findViewById(id);
                    if (tempBox.isChecked()){
                        selectedFilters.add(tempBox.getText().toString().trim().toLowerCase()); // All comparisons between selected filters and tags done after trimming and decapitalization.
                        checkedButtonIDs.add(tempBox.getId());
                    }
                    else {
                        checkedButtonIDs.remove(tempBox.getId()); // Remove unselected filters from the set of ones.
                    }
                }

                // Stop user from apply a set of 0 filters.
                if (checkedButtonIDs.isEmpty()){
                    Toast.makeText(getContext(), "Must selected at least 1 filter.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Add all posts containing a match with any selected filter to the set of filtered posts.
                for (Post post: posts){
                    String [] tags = post.getTags();
                    for (String s: tags){
                        if (selectedFilters.contains(s.trim().toLowerCase())){
                            filteredPosts.add(post);
                            break;
                        }
                    }
                }

                // Temporary**
                // Order the list of selected posts by the specified order option.
                int selectedOrder = orderBySpinner.getSelectedItemPosition();
                if (selectedOrder == 1){
                    filteredPosts = filteredPosts.stream()
                            .sorted(Comparator.comparingInt(Post :: getNumLikes).reversed())
                            .collect(Collectors.toCollection(ArrayList<Post> :: new));
                }
                addPostsToScrollView(filteredPosts, inflater, true); // Pass true for the reload.
            }
        });

        // Set all posts in the scroll view.
        for (Post post : postList) {

            View postView = inflater.inflate(R.layout.post_item, postsContainer, false);

            TextView postUserName = postView.findViewById(R.id.postUsername);
            postUserName.setText("placeHolder");

            TextView postTitle = postView.findViewById(R.id.postTitle);
            postTitle.setText(post.getTitle());

            TextView postDescription = postView.findViewById(R.id.postDescription);
            postDescription.setText(post.getDescription());

            TextView postTags = postView.findViewById(R.id.tags);
            String tags = ""; // Initialize the 'tags' String. This will be what the users sees under 'Tags'.
            String [] tagList = post.getTags();
            for (String s: tagList){
                tags += "#"+s +", "; // Probably should use StringBuilder, just not very familiar. Will look into.
            }

            tags = tags.substring(0, tags.length()-2); // Format correct, again would probably be avoided using StringBuilder.
            postTags.setText(tags);

            TextView postLikes = postView.findViewById(R.id.postLikes);
            String likesStmt = "Likes: "+String.valueOf(post.getNumLikes());
            postLikes.setText(likesStmt);

            TextView postDislikes = postView.findViewById(R.id.postDislikes);
            String dislikesStmt = "Dislikes: "+String.valueOf(post.getNumDislikes());
            postDislikes.setText(dislikesStmt);

            // Initialization of buttons for liking / disliking.
            Button likeButton = postView.findViewById(R.id.likeButton);
            Button dislikeButton = postView.findViewById(R.id.dislikeButton);

            //
            if (likedPosts.contains(post)){
                int color = ContextCompat.getColor(getContext(), R.color.flatgreen);
                likeButton.setBackgroundColor(color);
            }
            if (dislikedPosts.contains(post)){
                int color = ContextCompat.getColor(getContext(), R.color.flatred);
                likeButton.setBackgroundColor(color);
            }

            // Set on click listener for 'Like' button.
            likeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    /*
                    * For liking / disliking posts, the following logic
                    * ensures that a user cannot like and dislike the same post.
                    * For example, when a user likes a post, it gets added to their
                    * liked posts list. If they then try to dislike that same post,
                    * the program automatically unlikes the post before apply the dislike.
                    * Same goes vice versa.
                    * */
                    if (likedPosts.contains(post)) {
                        post.removeLike();
                        String updatedLikesStmt = "Likes: " + String.valueOf(post.getNumLikes());
                        postLikes.setText(updatedLikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                        likeButton.setBackgroundColor(color);
                        likedPosts.remove(post);
                    }
                    else {
                        if (dislikedPosts.contains(post)){
                            post.removeDislike();
                            String updatedDislikesStmt = "Dislikes: " + String.valueOf(post.getNumDislikes());
                            postDislikes.setText(updatedDislikesStmt);
                            int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                            dislikeButton.setBackgroundColor(color);
                            dislikedPosts.remove(post);
                        }
                        post.addLike();
                        String updatedLikesStmt = "Likes: " + String.valueOf(post.getNumLikes());
                        postLikes.setText(updatedLikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.flatgreen);
                        likeButton.setBackgroundColor(color);
                        likedPosts.add(post);
                    }
                }
            });

            // Set on click listener for 'Dislike' button.
            dislikeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if (dislikedPosts.contains(post)) {
                        post.removeDislike();
                        String updatedDislikesStmt = "Dislikes: " + String.valueOf(post.getNumDislikes());
                        postDislikes.setText(updatedDislikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                        dislikeButton.setBackgroundColor(color);
                        dislikedPosts.remove(post);
                    }
                    else {
                        if (likedPosts.contains(post)){
                            post.removeLike();
                            String updatedLikesStmt = "Likes: " + String.valueOf(post.getNumLikes());
                            postLikes.setText(updatedLikesStmt);
                            int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                            likeButton.setBackgroundColor(color);
                            likedPosts.remove(post);
                        }
                        post.addDislike();
                        String updatedDislikesStmt = "Dislikes: " + String.valueOf(post.getNumDislikes());
                        postDislikes.setText(updatedDislikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.flatred);
                        dislikeButton.setBackgroundColor(color);
                        dislikedPosts.add(post);
                    }
                }
            });

            // Add the post view to the posts container (LinearLayout) in the scoll view.
            postsContainer.addView(postView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
