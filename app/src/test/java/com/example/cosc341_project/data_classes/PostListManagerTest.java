package com.example.cosc341_project.data_classes;

import junit.framework.TestCase;

public class PostListManagerTest extends TestCase {

    static PostListManager plm;

    void initialize() {

    }
    public void testGetInstance() {
        PostListManager plm = PostListManager.getInstance();
        assertTrue(plm.postList.isEmpty());
    }

    public void testSaveToFile() {
    }
}