package com.example.cosc341_project.data_classes;

public class User {
    // ATTRIBUTES
    private final int userId;
    private String userName;
    private String profilePicName; //not sure about this data type yet, we're still figuring out images

    // CONSTRUCTOR
    public User(int userId, String userName, String profilePicName) {
        this.userId = userId;
        this.userName = userName;
        this.profilePicName = profilePicName;
    }

    // GETTERS
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getProfilePicName() {
        return profilePicName;
    }

    // TO STRING

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", profilePicName='" + profilePicName + '\'' +
                '}';
    }
}
