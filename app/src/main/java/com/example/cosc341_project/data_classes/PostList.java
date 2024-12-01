package com.example.cosc341_project.data_classes;

import java.util.ArrayList;

// This is a container class for an arraylist containing posts.
// Posts are uniquely identified by their position in the ArrayList. Same with comments.
// It is up to the implementer to keep track of a given post's position in the arraylist

/**
 * This is a wrapper class for an ArrayList called postList. It is a singleton, which means
 * there can only be one instance of it at any given time.
 *
 * We will see if it is threadsafe.
 */
public final class PostList {

    private static PostList INSTANCE;
    public ArrayList<Post> postList;

    private PostList() {
        postList = new ArrayList<Post>(); // TODO: read from file
    }

    public static PostList getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PostList();
        }
        return INSTANCE;
    }

    public void save() {
        //TODO
    }
}

// As Evan, I want to:
/*

POST CREATION
PostList postList = new PostList();
Post newPost = new Post(...);
postList.add(newPost)

// before finishing the activity
postList.save();

EDIT POST
PostList postList = new PostList();
Post post = postList.getPost(index);
    get all the info
    post.setString()
    post.setTitle()
    post.setStuff()

// before finishing the activity
postList.save()

LIKE or COMMENT
PostList
PostList postList = new postList();
Post post = postList.getPost(index);
    post.addLike()
    post.addComment() etc
postList.save()
 */
