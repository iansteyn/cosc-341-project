package com.example.cosc341_project.data_classes;

import android.content.Context;
import android.util.Log;


import com.example.cosc341_project.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>
 *     About PostListManager
 * </h2>
 * <p>
 *     This is a manager class for the ArrayList <code>postList</code>, which is basically supposed to
 *     be a global variable from which all our post data can be accessed for display and modification.
 *     This is possible because <code>PostListManager</code> is a "singleton"
 *     class, which means there can only be one instance of it at any given time. Because of this,
 *     you cannot construct the <code>PostListManager</code> directly, but you can access it like this:
 *     <pre>
 *     {@code
 *         PostListManager plm = PostListManager.getInstance(context);
 *         // for activity: context = this
 *         // for fragment: context = this.getContext()
 *     }
 *     </pre>
 *     If no instance exists, <code>PostListManager</code> will construct one by reading from the save
 *     file.
 * </p>
 * <p>
 *     From there I would recommend accessing the postList itself as <code>plm.postList</code>.
 * </p>
 * <p>
 *     It is possible that our Android app will use multiple threads, which could break this
 *     singleton approach. If we find weird things are happening, we may have to return to this and
 *     make it synchronized/threadsafe.
 * </p>
 * <hr>
 * <h2>
 *     Using postList
 * </h2>
 * <h4>
 *     General Notes
 * </h4>
 * <ol>
 *     <li>
 *         <b>SORT ORDER:</b> <code>postList</code> is sorted by the <code>timestamp</code> of the
 *         posts it contains.
 *     </li>
 *     <li>
 *         <b>ACCESS BY INDEX:</b> Posts no longer have a unique id. Instead, it is up to you to
 *         keep track of a post's position or index within <code>postList</code> to uniquely
 *         identify it. (The same is true of the comment list within posts themselves)
 *     </li>
 *     <li>
 *         <b>SAVING CHANGES !!!!!!!!!!!!!!</b> In general, you are allowed to make changes to
 *         <code>postList</code>. However, make sure that you call <code>plm.saveToFile(context)</code>
 *         before leaving the activity/fragment, or the changes may not be saved.
 *     </li>
 * </ol>
 * <h4>
 *     Adding or Removing a Post
 * </h4>
 * <p>
 *     Use ArrayList methods.
 * </p>
 * <pre>
 * {@code
 *     Post newPost = new Post(...); // or new SightingPost(...);
 *     plm.postList.add(newPost);
 *
 *     plm.postList.remove(index);
 * }
 * </pre>
 * <h4>
 *     Editing a post
 * </h4>
 * <pre>
 * {@code
 *     Post postToEdit = plm.postList.get(index);
 *     // [display all the info with post getters]
 *     postToEdit.setTitle(newTitle);
 *     postToEdit.setDescription(newDescription);
 *     // etc, see Post and SightingPost classes for all setters
 * }
 * </pre>
 * <h4>
 *     Changing Likes/Comments etc
 * </h4>
 * <pre>
 * {@code
 *     Post post = plm.postList.getPost(index);
 *     post.addLike();
 *     post.removeDislike();
 *     post.addComment(userId, text);
 *     post.removeComment(index);
 *     //etc, see Post class for all methods
 * }
 * </pre>
 *
 */
public final class PostListManager implements Serializable {

    // Class variables
    private static final String FILENAME = "postList.ser";
    private static PostListManager INSTANCE;

    // Instance variables
    public boolean instantiatedFromFile; //exists for testing purposes only
    public ArrayList<Post> postList;

    // Constructor
    private PostListManager(Context context) {
        // read post list from file
        try {
            FileInputStream fileIn = context.openFileInput(FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            postList = (ArrayList<Post>) in.readObject();
            in.close();
            fileIn.close();
            instantiatedFromFile = true;
        }
        // or initialize an empty one if the save file does not exist
        catch (FileNotFoundException fe) {
            Log.d("IAN", "postList.ser not found, initializing empty ArrayList.");
            postList = new ArrayList<Post>();
            instantiatedFromFile = false;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the one and only instance of the PostList class
     */
    public static PostListManager getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new PostListManager(context);
        }
        return INSTANCE;
    }

    /**
     * Call this method before leaving or finishing a fragment/activity to ensure
     * any changes to posts get saved to the file
     */
    public void saveToFile(Context context) {
        Log.d("IAN DEBUG", "saveToFile() is called.");
        try {
            FileOutputStream fileOut = context.openFileOutput(FILENAME, 0);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(postList);
            out.close();
            fileOut.close();
            Log.d("IAN DEBUG", "saveToFile() *seems* successful.");
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * This exists for testing purposes only.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void addFakePosts() {
        Post newPost1 = new SightingPost(
                5,
                "Baby Giant Salamander",
                "The mythic giant salamander has been spotted! Well, at least I think this is a baby giant salamander.",
                new String[] {"Ogopogo"},
                R.drawable.img_big_salamander_baby,
                "Okanagan Lake, Kelowna, BC",
                49.8801,
                -119.4954
        );
        newPost1.addComment(4, "Looks more like a baby medium salamander to me.");
        newPost1.addComment(6, "Yes! I knew it! Well done @Jeremy");
        newPost1.addComment(9, "Does it dwell in the lakes, as I do?");
        newPost1.addDislike(1);
        postList.add(newPost1);

        postList.add(new Post(
                0,
                "Do you think Ogogopo is Real?",
                "Like seriously guys. I know we're all believers here but do you really believe?",
                new String[] {"Ogopogo"}
        ));
        postList.add(new SightingPost(
                2,
                "CAUGHT ON TRAILCAM",
                "Check this out. Saw bigfoot on my trail cam near my cabin.",
                new String[] {"Sasquatch"},
                R.drawable.img_bigfoot_or_bear,
                "Bear Creek Provincial Park, West Kelowna, BC",
                49.9152,
                -119.5126
        ));
        postList.add(new SightingPost(
                9,
                "Greetings. Maybe Ogopogo?",
                "Greetings, fellows I am new to the area and this is my first sighting. Ogopogo, perhaps?",
                new String[] {"Ogopogo"},
                R.drawable.img_lake_monster,
                "Mission Creek, Kelowna, BC",
                49.8625,
                -119.4550
        ));

        Post newPost2 = new Post(
                7,
                "How did you first hear about the Ogopogo?",
                "Did anyone else have that weird book where his cousin is a dragon? Smh they don't even understand. Anyway how did you first hear about it?",
                new String[] {"Ogopogo"}
        );
        newPost2.addComment(8, "I first heard about it when I moved here in 2008. It's on so many logos.");
        newPost2.addComment(1, "@Awkwardfina more like ogoLOgo am i right?");
        newPost2.addComment(9, "I dwelt in this lake for millennia before he ever came along.");
        newPost2.addComment(7, "@Awkwardfina that's true, I hadn't thought of how common it is on Kelowna branding!");
        newPost2.addLike(8);
        newPost2.addLike(1);
        newPost2.addLike(9);
        newPost2.addLike(7);
        postList.add(newPost2);
    }
}
