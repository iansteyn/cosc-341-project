package com.example.cosc341_project.data_classes;

import java.util.Arrays;

/**
 * <h1>
 *     SightingPost
 * </h1>
 * <p>
 *     Extends <code>Post</code>.
 * </p>
 * <p>
 *     Includes attributes <code>imageId</code> and <code>location</code>, which must be specified
 *     on construction, but can also be modified with setters.
 * </p>
 * <h4>
 *     Example of accessing a post's image for display
 * </h4>
 * <pre>
 * {@code
 *     // Assuming you already defined:
 *     // - an ImageView called myImageView
 *     // - a Post called post
 *     myImageView.setImageResource(post.getImageId());
 * }
 * </pre>
 * <p>
 *     Note: profile pictures are always square.
 * </p>
 */
public class SightingPost extends Post {
    // ATTRIBUTES
    private int imageId;
    private String location;

    // CONSTRUCTOR
    public SightingPost(
            int userId,
            String title,
            String description,
            String[] tags,
            int imageId,
            String location
    ) {
        super(userId, title, description, tags);
        this.imageId = imageId;
        this.location = location;
    }

    //GETTERS
    public int getImageId() {
        return imageId;
    }
    public String getLocation() {
        return location;
    }

    // SETTERS
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    // TO STRING
    @Override
    public String toString() {
        return "SightingPost{" +
                "\nuserId=" + userId +
                "\ntimestamp=" + timestamp +
                "\ntitle='" + title + '\'' +
                "\ndescription='" + description + '\'' +
                "\ntags=" + Arrays.toString(tags) +
                "\nnumLikes=" + getNumLikes() +
                "\nnumDislikes=" + getNumDislikes() +
                "\ncomments=" + comments +
                "\nimageName=" + imageId +
                "\nlocation='" + location + '\'' +
                "\n}";
    }
}
