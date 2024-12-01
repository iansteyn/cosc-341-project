/* NOTE: You might see errors in this file that say something like:
 * "Cannot access com.example.cosc341_project.data_classes.Post"
 * and related "cannot resolve symbol" for superclass variables.
 * It's lying. It can access it. This is an editor bug with Android Studio,
 * not an actual error. This code compiles and runs.
 */
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
    private String imageName;
    private String location;

    // CONSTRUCTOR
    public SightingPost(
            int userId,
            String title,
            String description,
            String[] tags,
            String imageName,
            String location
    ) {
        super(userId, title, description, tags);
        this.imageName = imageName;
        this.location = location;
    }

    //GETTERS
    public String getImageName() {
        return imageName;
    }
    public String getLocation() {
        return location;
    }

    // SETTERS
    public void setImageName(String imageName) {
        this.imageName = imageName;
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
                "\nnumLikes=" + numLikes +
                "\nnumDislikes=" + numDislikes +
                "\ncomments=" + comments +
                "\nimageName='" + imageName + '\'' +
                "\nlocation='" + location + '\'' +
                "\n}";
    }
}
