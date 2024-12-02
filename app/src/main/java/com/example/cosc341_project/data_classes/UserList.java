package com.example.cosc341_project.data_classes;

/**
 * <h2> AHAHAHA HARDCODED DATA BABY </h2>
 * <h3> Ok what is this </h3>
 * <p>
 *     This is a read-only data class that stores all our user data statically, so you don't even
 *     have to create an instance to use it. No save files needed.
 * </p>
 *
 * <h6>Access user with id 0:</h6>
 * <pre>
 * {@code
 *     int userId = 0;
 *     User user = UserList.get(userId);
 * }
 * </pre>
 *
 * <h6> Access "current" user (whose profile we will display): </h6>
 * <pre>
 * {@code
 *     int userId = UserList.CURRENT_USER_ID;
 *     User user = UserList.get(userId);
 * }
 * </pre>
 *
 * <p><i>
 *     (Alright so in reality this would be terrible. If we were making an even slightly more complex
 *     system I would make this a singleton manager class and store users in a hashmap and give the
 *     ability to add or remove users... but I don't need to!)
 * </i></p>
 */
public class UserList {

    // STATIC ATTRIBUTES
    public static final int CURRENT_USER_ID = 10;

    private static final User[] users = new User[] {
        new User(0, "bobby_mohamed", "pfp0.png"),
        new User(1, "yoMamma", "pfp1.png"),
        new User(2, "bigfoot_fanatic", "pfp2.png"),
        new User(3, "OgopoStop", "pfp3.png"),
        new User(4, "Mothman2000", "pfp4.png"),
        new User(5, "Jeremy", "pfp5.png"),
        new User(6, "girl", "pfp6.png"),
        new User(7, "boy", "pfp7.jpg"),
        new User(8, "Awkwardfina", "pfp8.jpg"),
        new User(9, "lakedweller69", "pfp9.jpg"),
        new User(10, "Bill", "pfp10.png")
    };

    // STATIC METHODS
    public static User get(int userId) {
        if (0 <= userId && userId <= 10) {
            return users[userId];
        }
        else {
            return null;
        }
    }

}