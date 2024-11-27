package com.example.cosc341_project.data_classes;

import java.sql.Timestamp;
import java.util.LinkedList;


public class Post {
    // ATTRIBUTES
    private int postId;
    private final int userId;
    private final Timestamp timestamp;

    private String title;
    private String description;
    private int numLikes;
    private int numDislikes;

    public LinkedList<String> tags;
    public LinkedList<Comment> comments;

    // CONSTRUCTORS
    public Post(int userId) {
        this.userId = userId; //TODO: maybe check if userId exists on file?
        timestamp = new Timestamp(System.currentTimeMillis());

        numLikes = 0;
        numDislikes = 0;

        tags = new LinkedList<String>();
        comments = new LinkedList<Comment>();
    }

    // GETTERS
    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Timestamp getTimestamp() { return timestamp; }
    public int getNumLikes() { return numLikes; }
    public int getNumDislikes() { return numDislikes; }
    public LinkedList<String> getTags() { return tags; }
    public LinkedList<Comment> getComments() { return comments; }

    // SETTERS
    public void setPostId(int postId) {
        /* TEMPORARY until I figure out how to increment the postId better */
        this.postId = postId;
    }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    public void addLike() { numLikes++; }
    public void removeLike() { numLikes--; }
    public void addDislike() { numDislikes++; }
    public void removeDislike() { numDislikes--; }

    //TODO - should I have setters and getters for tags and comments?
}
