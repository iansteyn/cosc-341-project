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
    public SightingPost(int userId) {
        super(userId);
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
