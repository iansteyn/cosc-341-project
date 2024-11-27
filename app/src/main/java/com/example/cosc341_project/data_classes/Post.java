package com.example.cosc341_project.data_classes;

import java.sql.Timestamp;

public class Post {
    private int postId;
    private int userId;
    private String title;
    private String description;
    private final Timestamp timestamp;
    private int numLikes;
    private int numDislikes;
    private String[] tags;
    private Comment[] comments;

    public Post() {
        numLikes = 0;
        numDislikes = 0;
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    // GETTERS
    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Timestamp getTimestamp() { return timestamp; }
    public int getNumLikes() { return numLikes; }
    public int getNumDislikes() { return numDislikes; }
    public String[] getTags() { return tags; }
    public Comment[] getComments() { return comments; }

    // SETTERS
    public void setPostId(int postId) {
        /* TEMPORARY until I figure out how to increment the postId better */
        this.postId = postId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addLike() {
        numLikes++;
    }

    public void removeLike() {
        numLikes--;
    }

    public void addDislike() {
        numDislikes++;
    }

    public void removeDislike() {
        numDislikes--;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
