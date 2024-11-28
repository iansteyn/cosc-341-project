package com.example.cosc341_project.data_classes;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <h4> Notes </h4>
 * <p>
 *     This is a data class for storing information related to posts. It is a supertype of <code>sightingPost</code>
 * </p>
 * <ul>
 *     <li> All attributes are private. </li>
 *     <li> All attributes have getters. </li>
 *     <li>
 *         Not all attributes have setters.
 *         <ul>
 *             <li> <code>PostId</code>, <code>UserId</code>, and <code>timestamp</code> are final (immutable) and initialized at construction. </li>
 *             <li> <code>numDislikes</code>, <code>numLikes</code> and <code>comments</code> are initialized as 0, 0 and empty list. These all have <b><i>add</i></b> and <b><i>remove</i></b> methods. </li>
 *             <li> There are no default initialized values for <code>title</code>, <code>description</code> and <code>tags</code>. The user is forced to use their <b><i>setters</i></b> to set these even if they are supposed to be empty. </li>
 *
 *         </ul>
 *     </li>
 * </ul>
 *
 */
public class Post {
    // ATTRIBUTES
    private int postId; // will probably also be final later on
    private final int userId;
    private final Timestamp timestamp;

    private String title;
    private String description;
    private LinkedList<String> tags;

    private int numLikes;
    private int numDislikes;
    private LinkedList<Comment> comments;

    // CONSTRUCTORS
    public Post(int userId) {
        this.userId = userId; //TODO: maybe check if userId exists on file?
        timestamp = new Timestamp(System.currentTimeMillis());

        numLikes = 0;
        numDislikes = 0;
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
    public void setTags(LinkedList<String> tags) { this.tags = tags; }

    public void addLike() { numLikes++; }
    public void removeLike() { numLikes--; }
    public void addDislike() { numDislikes++; }
    public void removeDislike() { numDislikes--; }

    public boolean addComment(Comment newComment) {
        // Returns true if successful, false otherwise
        return comments.add(newComment);
    }
    public boolean removeComment(int commentId) {
        // Returns true if successful, false otherwise

        Iterator<Comment> commentsIterator = comments.iterator();

        while (commentsIterator.hasNext()) {
            if (commentsIterator.next().commentId == commentId) {
                commentsIterator.remove();
                return true;
            }
        }

        return false;
    }
}

// THOUGHTS
/*
 * The "editing mode" of Evan's stuff will get the whole post object passed to it.
 * Modifications are made, and then that post object is saved using 'saveEditedPost'
 */
