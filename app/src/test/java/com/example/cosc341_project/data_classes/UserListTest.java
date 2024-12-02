package com.example.cosc341_project.data_classes;

import junit.framework.TestCase;

public class UserListTest extends TestCase {

    public void testGet_userExists() {
        int realUserId = 0;
        User actualUser = UserList.get(realUserId);

        assertEquals(0, actualUser.getUserId());
        assertEquals("bobby_mohamed", actualUser.getUserName());
        assertEquals("pfp0.png", actualUser.getProfilePicName());
    }

    public void testGet_userDoesNotExist() {
        int fakeUserId = 17;
        User user = UserList.get(fakeUserId);

        assertNull(user);
    }

    public void testGet_currentUser() {
        User currentUser = UserList.get(UserList.CURRENT_USER_ID);
        assertEquals(10, currentUser.getUserId());
    }
}
