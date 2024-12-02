package com.example.cosc341_project.data_classes;

import junit.framework.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PostListManagerTest extends TestCase {

    public void testSingletonProperties() {
        PostListManager plm = PostListManager.getInstance();
        PostListManager plm2 = PostListManager.getInstance();

        // check that two variables refer to same instance
        assertEquals(plm, plm2);

        plm.postList.add(new Post(
                0,
                "Placeholder Title",
                "Placeholder description",
                new String[] {"tag1, tag2"}
        ));

        //check that this is still true after changes to one
        assertEquals(plm, plm2);

        //check that new references created after modification still point to same instance
        PostListManager plm3 = PostListManager.getInstance();
        assertEquals(plm, plm3);

        PostListManager.destroyInstance();
    }

    public void testSaveAndReload() {

        // make sure save file is deleted so this test runs properly
        Path path = Paths.get("postList.ser");
        try {
            Files.deleteIfExists(path);
        }
        catch (IOException e) {
            //do nothing
        }

        // If there is no save file, an empty list should be created
        PostListManager plm = PostListManager.getInstance();
        assertFalse(plm.instantiatedFromFile);
        assertTrue(plm.postList.isEmpty());

        // once posts are added, the list should no longer be empty
        plm.postList.add(new Post(
                0,
                "Placeholder Title",
                "Placeholder description",
                new String[] {"tag1, tag2"}
        ));
        assertFalse(plm.postList.isEmpty());

        // save to file to complete our work, destroy instance and create new reference
        plm.saveToFile();
        PostListManager.destroyInstance();
        plm = null;

        PostListManager plm2 = PostListManager.getInstance();

        // Check that it was instantiated from file and that postList isn't empty
        assertTrue(plm2.instantiatedFromFile);
        assertFalse(plm2.postList.isEmpty());
        assertEquals(0, plm2.postList.get(0).getUserId());

        // delete file again so it doesn't end up in our code files by accident
        try {
            Files.deleteIfExists(path);
        }
        catch (IOException e) {
            //do nothing
        }
    }
}