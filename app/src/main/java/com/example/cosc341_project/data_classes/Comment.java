package com.example.cosc341_project.data_classes;

import java.sql.Timestamp;

public class Comment {
    protected int commentId;
    protected int userId;
    protected String text;
    protected Timestamp timestamp;

    /**
     * This constructor is protected because it is only used in the <code>Post</code> class.
     * It is not intended for use outside of this package. If you want to add comments
     * to a post, you have to do so through the <code>addComment</code> method from the <code>Post</code> class.
     */
    protected Comment(int commentId, int userId, String text, Timestamp timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }
}
