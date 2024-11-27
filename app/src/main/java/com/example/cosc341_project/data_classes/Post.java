package com.example.cosc341_project.data_classes;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private int userId;
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private int numLikes;
    private int numDislikes;
    private String[] tags;
    private Comment[] comments;

    // GETTERS
    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public String[] getTags() {
        return tags;
    }

    public Comment[] getComments() {
        return comments;
    }


}
