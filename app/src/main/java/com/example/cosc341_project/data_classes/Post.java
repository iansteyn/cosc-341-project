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
}
