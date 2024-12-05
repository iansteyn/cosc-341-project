package com.example.cosc341_project.data_classes;

import android.util.Log;

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
 *         PostListManager plm = PostListManager.getInstance();
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
 *         <code>postList</code>. However, make sure that you call <code>plm.saveToFile()</code>
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
    private PostListManager() {
        // read post list from file
        try {
            FileInputStream fileIn = new FileInputStream(FILENAME);
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the one and only instance of the PostList class
     */
    public static PostListManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostListManager();
        }
        return INSTANCE;
    }

    /**
     * Call this method before leaving or finishing a fragment/activity to ensure
     * any changes to posts get saved to the file
     */
    public void saveToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(postList);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * This exists for testing purposes only.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Add fake posts for testing purposes
     */
    public void addFakePosts() {
        postList.add(new SightingPost(
                0,
                "Unbelievable! Just Saw Ogopogo at Okanagan Lake!",
                "You won't believe what I just saw! A massive creature with humps moving in the water near Okanagan Beach—it's Ogopogo! I'm still in shock!",
                new String[]{"ogopogo"},
                "ogopogo_sighting.jpg",
                "Okanagan Lake, Kelowna, BC",
                49.8801,
                -119.4954
        ));

        postList.add(new SightingPost(
                1,
                "No Way! Sasquatch Spotted Near Mission Creek!",
                "I can't believe my eyes! I was hiking near Mission Creek, and this huge, hairy figure dashed across the trail. It was so fast—I swear it was Sasquatch!",
                new String[]{"sasquatch"},
                "sasquatch_sighting.jpg",
                "Mission Creek, Kelowna, BC",
                49.8625,
                -119.4550
        ));

        postList.add(new SightingPost(
                2,
                "Terrifying Encounter at Myra Canyon—Wendigo?!",
                "This was absolutely terrifying! It was late evening at Myra Canyon when I saw a ghostly figure lurking in the shadows. It looked like something straight out of a nightmare—could it have been a Wendigo?!",
                new String[]{"wendigo"},
                "wendigo_sighting.jpg",
                "Myra Canyon, Kelowna, BC",
                49.7574,
                -119.2720
        ));

        postList.add(new SightingPost(
                3,
                "I Saw Bigfoot at Bear Creek Park—Still Shaking!",
                "I can't believe it! I just saw something massive and furry moving in the woods at Bear Creek Park. It wasn't a bear—it was Bigfoot! My heart is still pounding!",
                new String[]{"bigfoot"},
                "bigfoot_sighting.jpg",
                "Bear Creek Provincial Park, West Kelowna, BC",
                49.9152,
                -119.5126
        ));

        postList.add(new SightingPost(
                4,
                "Red-Eyed Winged Creature at Knox Mountain—Mothman?!",
                "I'm freaking out! Near the top of Knox Mountain, I saw a giant winged creature with glowing red eyes. It was watching me! Could this have been the Mothman?! I'm still trying to process what happened.",
                new String[]{"mothman"},
                "mothman_sighting.jpg",
                "Knox Mountain, Kelowna, BC",
                49.9041,
                -119.4936
        ));
    }
}