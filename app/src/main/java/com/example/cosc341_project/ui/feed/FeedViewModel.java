package com.example.cosc341_project.ui.feed;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.LinkedList;

//import com.example.cosc341_project.data_classes.Post;

public class FeedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final LinkedList <Post> postData;

    public FeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");

        postData = new LinkedList<>();

        LinkedList<String> tags1 = new LinkedList<>();
        tags1.add("ogopogo");
        LinkedList<String> tags2 = new LinkedList<>();
        tags2.add("sasquatch");
        LinkedList<String> tags3 = new LinkedList<>();
        tags3.add("ogopogo");
        tags3.add("sighting");

        postData.add(new Post("user1", "Title1", "Description1", 1, 1, tags1));
        postData.add(new Post("user2", "Title2", "Description2", 2, 2, tags2));
        postData.add(new Post("user3", "Title3", "Description3", 3, 3, tags3));

    }

    public LinkedList<Post> getPosts() {
        return postData;
    }

    public LiveData<String> getText() {
        return mText;
    }
}