package com.example.cosc341_project.data_classes;

public class FileOps {

//    public static void saveUserListToFile(userlist) {
//        //what data structure should the userlist be? I'm thinking hashmap
//    }
//
//    public static void savePostListToFile() {
//
//    }

//    public static <returnType> readUserListFromFile() {
//
//    }

//    public static <container for Post> readPostListFromFile() {
//        // read serialized post objects from the file
//    }
}

/* OK so I need:
1. post file
3. user file

Just realized something... in order to convert between file and object I need to do one of two things:
1. Have constructors that set every aspect of a post so that it can be translated from array to object
2. Serialize the actual objects themselves and have a different way of loading/unloading the data
    --> the problem with this is that it is not human readable which makes things a lot harder testing-wise
    --> but because of how I built the post class with strong encapsulation this makes more sense I guess
    --> the other challenge with this is that I planned on accessing and overwriting posts by postId, but this now seems less realistic

    I could just make it so each activity reads the entire postlist, do all ops on that, and then save an entirely new postList on activity close
    But this seems so clearly bad. First, it doesn't guarantee user actions are committed - if the app crashes things might have changes.
    It depends on the user's ability to use things properly. Second, it seems bad performance-wise to read an entire file... although I guess
    it is improved if comment-liking etc don't require the whole file-read....

    So maybe it's not THAT bad, it's just kind of volatile and harder to test. Great. I guess this is why Firebase is asynchronous, right?
    The user doesn't have to wait for you.

    I mean, we do just kinda have to get it working for the demo so maybe this is just the way to go. Store it in a data structure.
 */