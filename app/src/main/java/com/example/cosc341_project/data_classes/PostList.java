package com.example.cosc341_project.data_classes;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * This is a wrapper class for an ArrayList called <code>postList</code>. It is a "singleton", which
 * means there can only be one instance of it at any given time.
 *
 * Because of this, <code>postList</code> is basically a global variable. You cannot construct it
 * directly, but you can access it with a call to <code>getInstance</code> like this:
 * PostListWrapper plw = PostListWrapper.getInstance();
 *
 * From there I would
 *
 * We will see if it is threadsafe.
 */
public final class PostList implements Serializable {

    // Class variables
    private static final String FILENAME = "postList.ser";
    private static PostList INSTANCE;

    // Instance variables
    public ArrayList<Post> postList;

    // Constructor
    private PostList() {
        // read post list from file
        try {
            FileInputStream fileIn = new FileInputStream(FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            postList = (ArrayList<Post>) in.readObject();
            in.close();
            fileIn.close();
        }
        // or initialize an empty one if the save file does not exist
        catch (FileNotFoundException fe) {
            Log.d("IAN", "postList.ser not found, initializing empty ArrayList.");
            postList = new ArrayList<Post>();
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
