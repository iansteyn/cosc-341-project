package com.example.cosc341_project.data_classes;

import junit.framework.TestCase;

public class PostTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddComment() {
        Post post = new Post(
                3,
                "Title",
                "Description",
                new String[] {"tag1", "tag2"}
        );

        assertTrue(post.getComments().isEmpty());
        assertTrue(post.addComment(8, "Comment text"));

        Comment newComment = post.getComments().get(0);
        assertNotNull(newComment);
        assertEquals(8,newComment.getUserId());
        assertEquals("Comment text", newComment.getText());
    }

    public void testRemoveComment() {
        Post post = new Post(
                3,
                "Title",
                "Description",
                new String[] {"tag1", "tag2"}
        );
        post.addComment(8, "Comment text 0");
        post.addComment(9, "Comment text 1");
        post.addComment(10, "Comment text 2");

        assertEquals(3, post.getComments().size());

        post.removeComment(1);

        assertEquals(2, post.getComments().size());
        assertEquals(10, post.getComments().get(1).getUserId());
    }

    public void testAddLikeRemoveDislike() {
        Post post = new Post(
                3,
                "Title",
                "Description",
                new String[] {"tag1", "tag2"}
        );
        assertEquals(0, post.getNumLikes());
        post.addLike(4);
        assertEquals(1, post.getNumLikes());
        post.addLike(4);
        assertEquals(1, post.getNumLikes());
        post.addLike(3);
        assertEquals(2, post.getNumLikes());

        //test full capabilities of addLike and indirectly test removeDislike
        post.addDislike(5);
        post.addLike(5);
        assertEquals(0, post.getNumDislikes());
    }

    public void testAddDislikeRemoveLike() {
        Post post = new Post(
                3,
                "Title",
                "Description",
                new String[] {"tag1", "tag2"}
        );
        assertEquals(0, post.getNumDislikes());
        post.addDislike(4);
        assertEquals(1, post.getNumDislikes());
        post.addDislike(4);
        assertEquals(1, post.getNumDislikes());
        post.addDislike(3);
        assertEquals(2, post.getNumDislikes());

        //test full capabilities of addDislike and indirectly test removeLike
        post.addLike(5);
        post.addDislike(5);
        assertEquals(0, post.getNumLikes());
    }
}