package com.example.cosc341_project.data_classes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * <h2>
 *     Post
 * </h2>
 * <p>
 *     This is a data class for storing information related to posts. It is a supertype of
 *     <code>sightingPost</code>
 * </p>
 * <h3>
 *     Creating a Post
 * </h3>
 * <p>The only constructor available is:</p>
 * <pre>
 * {@code
 *     public Post(int userId, String title, String description, String[] tags)
 * }
 * </pre>
 * <h3>
 *     Accessing and Modifying Attributes
 * </h3>
 * <ul>
 *     <li>
 *         All attributes are protected --- you cannot access them directly.
 *     </li>
 *     <li>
 *         All attributes have getters.
 *         <b>Please do not modify the values or objects returned by the getters.</b>
 *         Use the setter or other modification method built into the <code>Post</code> class.
 *         <ul><li>
 *             There are also two special getters called <code>getNumLikes</code> and
 *             <code>getNumDislikes</code> which calculate the number of users who have liked or
 *             disliked this post.
 *        </li></ul>
 *     </li>
 *     <li>
 *         Not all attributes have setters.
 *         <ul>
 *             <li>
 *                 Both <code>UserId</code>, and <code>timestamp</code> are final (immutable).
 *                 <code>UserId</code> is specified at construction, while <code>timestamp</code>
 *                 is automatically set to the current time.
 *             </li>
 *             <li>
 *                 <code>likedBy</code>, <code>dislikedBy</code> and <code>comments</code> are
 *                 initialized as empty lists of their respective types. These all have
 *                 <b><i>add</i></b> and <b><i>remove</i></b> methods, most of which require an
 *                 integer <code>userId</code> to be passed to them. See <code>// SETTERS </code>
 *                 below.
 *             </li>
 *             <li>
 *                 <code>title</code>, <code>description</code> and <code>tags</code> must be
 *                 specified at construction, but they can also be modified with setters.
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 *
 */
public class Post implements Serializable {
    // ATTRIBUTES
    protected final int userId;
    protected final Timestamp timestamp;

    protected String title;
    protected String description;
    protected String[] tags;

    protected LinkedList<Integer> likedBy;
    protected LinkedList<Integer> dislikedBy;
    protected ArrayList<Comment> comments;

    // CONSTRUCTORS
    public Post(int userId, String title, String description, String[] tags) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.tags = tags;

        timestamp = new Timestamp(System.currentTimeMillis());

        likedBy = new LinkedList<Integer>();
        dislikedBy = new LinkedList<Integer>();
        comments = new ArrayList<Comment>();
    }

    // GETTERS
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Timestamp getTimestamp() { return timestamp; }
    public int getNumLikes() { return likedBy.size(); }
    public int getNumDislikes() { return dislikedBy.size(); }
    public String[] getTags() { return tags; }
    public ArrayList<Comment> getComments() { return comments; }

    // SETTERS
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTags(String[] tags) { this.tags = tags; }

    public void addLike(int userId) {
        if (! likedBy.contains(userId)) {
            removeDislike(userId);
            likedBy.add(userId);
        }
    }

    public void removeLike(int userId) {
        if (likedBy.contains(userId)) {
            likedBy.remove(Integer.valueOf(userId));
        }
    }

    public void addDislike(int userId) {
        if (! dislikedBy.contains(userId)) {
            removeLike(userId);
            dislikedBy.add(userId);
        }
    }

    public void removeDislike(int userId) {
        if (dislikedBy.contains(userId)) {
            dislikedBy.remove(Integer.valueOf(userId));
        }
    }

    /**
     * Adds a comment to this post
     * @param userId id of user making comment
     * @param text Body of the comment
     * @return <code>true</code> if successful, <code>false</code> otherwise
     */
    public boolean addComment(int userId, String text)
    {
        Comment newComment = new Comment(userId, text);
        return comments.add(newComment);
    }

    /**
     * <p> Removes comment at <code>index</code> in <code>comments</code> </p>
     * <p> Throws <code>IndexOutOfBoundsException</code> if unsuccessful </p>
     */
    public void removeComment(int index)
    {
        comments.remove(index);
    }

    // TO STRING
    @Override
    public String toString() {
        return "Post{" +
                "\nuserId=" + userId +
                "\ntimestamp=" + timestamp +
                "\ntitle='" + title + '\'' +
                "\ndescription='" + description + '\'' +
                "\ntags=" + Arrays.toString(tags) +
                "\nnumLikes=" + getNumLikes() +
                "\nnumDislikes=" + getNumDislikes() +
                "\ncomments=" + comments +
                "\n}";
    }
}