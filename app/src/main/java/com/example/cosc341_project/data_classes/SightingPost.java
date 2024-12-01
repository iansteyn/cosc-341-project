/* NOTE: You might see errors in this file that say something like:
 * "Cannot access com.example.cosc341_project.data_classes.Post"
 * It's lying. It can access it. This is an editor bug with Android Studio,
 * not an actual error.
 */
package com.example.cosc341_project.data_classes;

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
}
