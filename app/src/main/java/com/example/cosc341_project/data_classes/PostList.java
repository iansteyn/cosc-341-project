package com.example.cosc341_project.data_classes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public final class PostList implements Serializable {

    private static final String FILENAME = "postList.ser";
    private static PostList INSTANCE;
    public ArrayList<Post> postList;

    private PostList() {
        // read post list from file
        try {
            FileInputStream fileIn = new FileInputStream(FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            postList = (ArrayList<Post>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the one and only instance of the PostList class
     */
    public static PostList getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PostList();
        }
        return INSTANCE;
    }

    /**
     * Call this method before leaving or finishing a fragment/activity to ensure
     * any changes to posts get saved to the file
     */
    public void saveToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(postList);
            out.close();
            fileOut.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
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
