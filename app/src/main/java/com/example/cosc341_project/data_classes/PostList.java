package com.example.cosc341_project.data_classes;

import java.util.ArrayList;
import java.util.LinkedList;

//note: I could potentially just make the read and save methods part of this

// should this be a singleton? maybe

// This is a container class for an arraylist containing posts. Posts are uniquely identified by their
// position in the ArrayList. Same with comments.

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

//what if i just take away postId and index only by list position
//why does tags need to be a linkedList? if it doesn't get dynamically updated it might as well be an array

// also hmm... I want to access by postId, but also sort by timestamp... and hide certain types of posts
// what am I doing more... access by index or removing posts?