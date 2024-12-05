package com.example.cosc341_project.data_classes;

import java.io.Serializable;

/**
 * <h1>
 *     User
 * </h1>
 * <p>
 *     Currently, <code>User</code> objects are read-only after initialization.
 *     You should never have to initialize a user yourself, as the user list is statically created
 *     in the <code>UserList</code> class.
 * </p>
 * <h4>
 *     Example of accessing a user's profile picture for display
 * </h4>
 * <pre>
 * {@code
 *     // Assuming you already defined:
 *     // - an ImageView called myImageView
 *     // - a User called user
 *     myImageView.setImageResource(user.getProfilePicId());
 * }
 * </pre>
 * <p>
 *     Note: Not all images will be the same aspect ratio. Please account for this.
 * </p>
 *
 *
 */
public class User implements Serializable {
    // ATTRIBUTES
    private final int userId;
    private String userName;
    private int profilePicId;

    // CONSTRUCTOR
    protected User(int userId, String userName, int profilePicId) {
        this.userId = userId;
        this.userName = userName;
        this.profilePicId = profilePicId;
    }

    // GETTERS
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public int getProfilePicId() {
        return profilePicId;
    }

    // TO STRING

    @Override
    public String toString() {
        return "User{" +
                "\nuserId=" + userId +
                "\nuserName='" + userName + '\'' +
                "\nprofilePicId=" + profilePicId +
                "\n}";
    }
}
