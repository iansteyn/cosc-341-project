package com.example.cosc341_project.data_classes;

import java.util.ArrayList;
import java.util.LinkedList;

// Consider making this a singleton

// This is a container class for an arraylist containing posts.
// Posts are uniquely identified by their position in the ArrayList. Same with comments.
// It is up to the implementer to keep track of a given post's position in the arraylist

public class PostList {
    // ATTRIBUTES
    public ArrayList<Post> postList;

    // CONSTRUCTOR

    // METHODS
    public void addPost(int userId) {
        Post newPost = new Post(userId);
        postList.add(newPost);
    }

    public void addSightingPost(int userId) {
        Post newPost = new SightingPost(userId);
        postList.add(newPost);
    }

    public void removePost(int index) {
        postList.remove(index);
    }

    public void save() {
        //TODO
    }

}

// As Evan, I want to:
/*

POST CREATION
PostList postList = new postList();
addPost(all post info)

// before finishing the activity
postList.save();

EDIT POST
PostList postList = new postList();
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
