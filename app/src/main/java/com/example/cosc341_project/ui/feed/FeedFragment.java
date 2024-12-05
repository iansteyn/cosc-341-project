package com.example.cosc341_project.ui.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.zip.Inflater;

public class FeedFragment extends Fragment {

    FeedViewModel feedViewModel;
    private User currentUser = UserList.get(UserList.CURRENT_USER_ID);
    private LinearLayout postsContainer;
    private LinearLayout commentSection;
    private PostListManager plm;
    private ArrayList<Post> posts; // Stores posts loaded from the post list manager.
    private HashSet<Integer> checkedButtonIDs; // Stores the IDs of buttons checked in the filter view. Set as a class variable so upon reloading, previously selected options remain selected.
    private int selectedOrderOption;

    private FragmentFeedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FeedViewModel feedViewModel =
                new ViewModelProvider(this).get(FeedViewModel.class);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        postsContainer = root.findViewById(R.id.postsContainer);
        commentSection = root.findViewById(R.id.commentSection);

        plm = PostListManager.getInstance(this.getContext());
        if (plm.postList.isEmpty()) {
            plm.addFakePosts();
        }
        posts = plm.postList;

        checkedButtonIDs = new HashSet<>();

        postsContainer = binding.postsContainer;
        commentSection = binding.commentSection;

        addPostsToScrollView(posts, inflater, false);
        // final TextView textView = binding.textFeed;
        // feedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void addPostsToScrollView(ArrayList<Post> postList, LayoutInflater inflater, boolean reload) {

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

                // Order the list of selected posts by the specified order option.
                int selectedOrder = orderBySpinner.getSelectedItemPosition();
                selectedOrderOption = selectedOrder;
                if (selectedOrder == 1){
                    filteredPosts = filteredPosts.stream()
                            .sorted(Comparator.comparingInt(Post :: getNumLikes))
                            .collect(Collectors.toCollection(ArrayList<Post> :: new));
                }
                addPostsToScrollView(filteredPosts, inflater, true); // Pass true for the reload.
            }
        });

        // Set all posts in the scroll view.
        for (int i = postList.size() - 1; i >= 0; i--) {

            View postView;
            if (postList.get(i) instanceof SightingPost ) {
                SightingPost sightingTempPost = (SightingPost)postList.get(i); // Used to access location and image getters in sighting post.
                postView = inflater.inflate(R.layout.post_item, postsContainer, false);

                ImageView image = postView.findViewById(R.id.imageView);
                image.setImageResource(sightingTempPost.getImageId());

                TextView location = postView.findViewById(R.id.postLocation);
                location.setText(sightingTempPost.getLocation());

            }
            else {
                postView = inflater.inflate(R.layout.discussion_post_item, postsContainer, false);
            }
            TextView postUserName = postView.findViewById(R.id.postUsername);
            postUserName.setText(UserList.get(postList.get(i).getUserId()).getUserName());

            TextView postTitle = postView.findViewById(R.id.postTitle);
            postTitle.setText(postList.get(i).getTitle());

            int finalI = i;
            postTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Report Post:")
                            .setMessage("Are you sure you want to report this post:\n"+postList.get(finalI).getTitle()+"\nBy: "+UserList.get(postList.get(finalI).getUserId()).getUserName())
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Report submitted. We will look into this matter shortly.", Toast.LENGTH_LONG).show();
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

            TextView postDescription = postView.findViewById(R.id.postDescription);
            postDescription.setText(postList.get(i).getDescription());

            TextView timestamp = postView.findViewById(R.id.postTimestamp);
            timestamp.setText(postList.get(i).getTimestamp().toString().substring(0, 10));

            TextView postTags = postView.findViewById(R.id.tags);
            String tags = ""; // Initialize the 'tags' String. This will be what the users sees under 'Tags'.
            String [] tagList = postList.get(i).getTags();
            for (String s: tagList){
                tags += "#"+s +", ";
            }

            tags = tags.substring(0, tags.length()-2); // Format correct, again would probably be avoided using StringBuilder.
            postTags.setText(tags);

            TextView postLikes = postView.findViewById(R.id.postLikes);
            String likesStmt = "Likes: "+String.valueOf(postList.get(i).getNumLikes());
            postLikes.setText(likesStmt);

            TextView postDislikes = postView.findViewById(R.id.postDislikes);
            String dislikesStmt = "Dislikes: "+String.valueOf(postList.get(i).getNumDislikes());
            postDislikes.setText(dislikesStmt);

            // Initialization of buttons for liking / disliking.
            Button likeButton = postView.findViewById(R.id.likeButton);
            Button dislikeButton = postView.findViewById(R.id.dislikeButton);

            if (postList.get(i).isLikedBy(currentUser.getUserId())){
                int color = ContextCompat.getColor(getContext(), R.color.flatgreen);
                likeButton.setBackgroundColor(color);
            }
            if (postList.get(i).isDislikedBy(currentUser.getUserId())){
                int color = ContextCompat.getColor(getContext(), R.color.flatred);
                dislikeButton.setBackgroundColor(color);
            }

            Button commentButton = postView.findViewById(R.id.commentButton);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postsContainer.setVisibility(View.GONE);
                    commentSection.setVisibility(View.VISIBLE);
                    Log.e("Comment Visibility", "Visbile");
                    addCommentAndView(postList.get(finalI), inflater);
                }
            });

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
                    if (postList.get(finalI).isLikedBy(currentUser.getUserId())) {
                        postList.get(finalI).removeLike(currentUser.getUserId());
                        String updatedLikesStmt = "Likes: " + String.valueOf(postList.get(finalI).getNumLikes());
                        postLikes.setText(updatedLikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                        likeButton.setBackgroundColor(color);
                    }
                    else {
                        if (postList.get(finalI).isDislikedBy(currentUser.getUserId())){
                            postList.get(finalI).removeDislike(currentUser.getUserId());
                            String updatedDislikesStmt = "Dislikes: " + String.valueOf(postList.get(finalI).getNumDislikes());
                            postDislikes.setText(updatedDislikesStmt);
                            int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                            dislikeButton.setBackgroundColor(color);
                        }
                        postList.get(finalI).addLike(currentUser.getUserId());
                        String updatedLikesStmt = "Likes: " + String.valueOf(postList.get(finalI).getNumLikes());
                        postLikes.setText(updatedLikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.flatgreen);
                        likeButton.setBackgroundColor(color);
                    }
                }
            });

            // Set on click listener for 'Dislike' button.
            dislikeButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if (postList.get(finalI).isDislikedBy(currentUser.getUserId())) {
                        postList.get(finalI).removeDislike(currentUser.getUserId());
                        String updatedDislikesStmt = "Dislikes: " + String.valueOf(postList.get(finalI).getNumDislikes());
                        postDislikes.setText(updatedDislikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                        dislikeButton.setBackgroundColor(color);
                    }
                    else {
                        if (postList.get(finalI).isLikedBy(currentUser.getUserId())){
                            postList.get(finalI).removeLike(currentUser.getUserId());
                            String updatedLikesStmt = "Likes: " + String.valueOf(postList.get(finalI).getNumLikes());
                            postLikes.setText(updatedLikesStmt);
                            int color = ContextCompat.getColor(getContext(), R.color.purple_500);
                            likeButton.setBackgroundColor(color);
                        }
                        postList.get(finalI).addDislike(currentUser.getUserId());
                        String updatedDislikesStmt = "Dislikes: " + String.valueOf(postList.get(finalI).getNumDislikes());
                        postDislikes.setText(updatedDislikesStmt);
                        int color = ContextCompat.getColor(getContext(), R.color.flatred);
                        dislikeButton.setBackgroundColor(color);
                    }
                }
            });

            // Add the post view to the posts container (LinearLayout) in the scoll view.
            postsContainer.addView(postView);
        }
    }

    public void addCommentAndView(Post post, LayoutInflater inflater) {

        // Set up the posts basic info to display above comment section.
        ImageView profilePic = commentSection.findViewById(R.id.profilePic);
        // Logic for setting image.

        TextView title = commentSection.findViewById(R.id.postTitle);
        title.setText(post.getTitle());

        TextView username = commentSection.findViewById(R.id.postUsername);
        username.setText(UserList.get(post.getUserId()).getUserName());

        TextView timestamp = commentSection.findViewById(R.id.postTimestamp);
        timestamp.setText(post.getTimestamp().toString());

        TextView description = commentSection.findViewById(R.id.postDescription);
        description.setText(post.getDescription());

        TextView location = commentSection.findViewById(R.id.postLocation);

        if (post instanceof SightingPost){
            location.setText(((SightingPost) post).getLocation());
            // Add image setting logic.
        }
        else {
            location.setVisibility(View.GONE);
        }

        EditText commentInput = commentSection.findViewById(R.id.comment_input);
        Button newComment = commentSection.findViewById(R.id.button_post_comment);
        Button closeComments = commentSection.findViewById(R.id.button_comment_close);
        LinearLayout commentsContainer = commentSection.findViewById(R.id.comments_container);

        ArrayList<Comment> comments = post.getComments();

        // Add all comments to the comment section.
        for (int i = comments.size() - 1; i >= 0; i--){
            View commentView = inflater.inflate(R.layout.comment_item, commentSection, false);

            TextView commentUserName = commentView.findViewById(R.id.commenter_username);
            commentUserName.setText(currentUser.getUserName());

            TextView commentContent = commentView.findViewById(R.id.comment_content);
            commentContent.setText(comments.get(i).getText());

            TextView commentTimeStamp = commentView.findViewById(R.id.comment_timestamp);
            commentTimeStamp.setText(comments.get(i).getTimestamp().toString().substring(0,10));

            Button deleteCommentButton = commentView.findViewById(R.id.deleteButton);
            if (currentUser.getUserId() == comments.get(i).getUserId()) {
                int finalI = i;
                deleteCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create and display an AlertDialog for confirmation
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Delete Comment")
                                .setMessage("Are you sure you want to delete this comment?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        comments.remove(comments.get(finalI));
                                        commentsContainer.removeView(commentView);
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
            }
            else {
                deleteCommentButton.setVisibility(View.GONE);
            }

            commentsContainer.addView(commentView);
        }

        newComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComment = commentInput.getText().toString();
                ArrayList<Comment> updatedComments = post.getComments();
                if (!newComment.isEmpty()){
                    post.addComment(currentUser.getUserId(), newComment); // 1 as temp or userId.

                    View newCommentView = inflater.inflate(R.layout.comment_item, commentSection, false);

                    TextView commentUserName = newCommentView.findViewById(R.id.commenter_username);
                    commentUserName.setText(currentUser.getUserName());

                    TextView commentContent = newCommentView.findViewById(R.id.comment_content);
                    commentContent.setText(updatedComments.get(updatedComments.size() - 1).getText());

                    TextView commentTimeStamp = newCommentView.findViewById(R.id.comment_timestamp);
                    commentTimeStamp.setText(updatedComments.get(updatedComments.size()-1).getTimestamp().toString().substring(0,10));

                    Button deleteCommentButton = newCommentView.findViewById(R.id.deleteButton);
                    deleteCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create and display an AlertDialog for confirmation
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Delete Comment")
                                    .setMessage("Are you sure you want to delete this comment?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            comments.remove(newComment);
                                            commentsContainer.removeView(newCommentView);
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

                    commentsContainer.addView(newCommentView, 0);
                    commentInput.setText("");
                }
                else{
                    Toast.makeText(getContext(), "No comment entered. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentsContainer.removeAllViews();
                commentSection.setVisibility(View.GONE);
                postsContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onDestroyView() {
        plm.saveToFile(this.getContext());
        super.onDestroyView();
        binding = null;
    }

}
