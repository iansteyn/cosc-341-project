package com.example.cosc341_project.data_classes;

import java.util.Arrays;

/**
 * <p>
 *     Extends <code>Post</code>.
 * </p>
 * <p>
 *     Includes attributes <code>imageName</code> and <code>location</code>, which have no
 *     default values and must be set with setters. Also, currently they are both Strings,
 *     this may change in the future; it's just a place-holder data type until we figure out
 *     more details.
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
