package com.example.cosc341_project.ui.feed;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.LinkedList;

import com.example.cosc341_project.data_classes.*;

public class FeedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final PostListManager plm;

    public FeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");

        plm = PostListManager.getInstance();

        plm.addFakePosts();
        String [] sightingPostTags = {"sasquatch", "sighting"};

        Post tempPost = plm.postList.get(0);
        tempPost.addComment(123, "This is an example comment");
        plm.postList.add(new SightingPost(123, "Sighting post", "Example description", sightingPostTags, "nullimage", "Kelowna, BC"));

    }

    public LiveData<String> getText() {
        return mText;
    }
}