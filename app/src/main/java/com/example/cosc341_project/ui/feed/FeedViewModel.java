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

    }

    public PostListManager getPostListManager() {
        return plm;
    }

    public LiveData<String> getText() {
        return mText;
    }
}