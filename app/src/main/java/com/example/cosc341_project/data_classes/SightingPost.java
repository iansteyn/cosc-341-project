package com.example.cosc341_project.data_classes;

import java.util.Arrays;

/**
 * <p>
 *     Extends <code>Post</code>.
 * </p>
 * <p>
 *     Includes attributes <code>imageName</code>, <code>location</code>, <code>latitude</code>, and <code>longitude</code>, which have no
 *     default values and must be set with setters. The latitude and longitude fields are used to specify the exact geographical position of the sighting.
 * </p>
 */
public class SightingPost extends Post {
    // ATTRIBUTES
    private String imageName;
    private String location;
    private double latitude;
    private double longitude;

    // CONSTRUCTOR
    public SightingPost(
            int userId,
            String title,
            String description,
            String[] tags,
            String imageName,
            String location,
            double latitude,
            double longitude
    ) {
        super(userId, title, description, tags);
        this.imageName = imageName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // GETTERS
    public String getImageName() {
        return imageName;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // SETTERS
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
                "\nlatitude=" + latitude +
                "\nlongitude=" + longitude +
                "\n}";
    }
}
