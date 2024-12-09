# Ogo
_COSC 341 - Human Computer Interaction_
<br>
_Project Step 4 - High Fidelity Prototyping_
<br>
_Completed December 2024._

🔗 [YouTube walkthrough](https://youtu.be/_cBcPYcGz-A?si=Xq6PlLvFQMPvTU8j)

## Description of System

***Ogo*** is a prototype Android mobile app built with Java, which allows users to share and view posts related to local Okanagan cryptids such as the Ogopogo and the Sasquatch. The system is built around four primary tasks:

### Major Tasks

1. **Report a Sighting.** Allow users to create and delete a “reported sighting” post. These include title, text description, location, photo (added from camera or gallery), and tags.

2. **Create a discussion thread.** Allow users to create and delete a “discussion” post. These posts include a discussion topic, text description from the original user, and tags. 

3. **Interact with posts in the feed.** Allows users to view and interact with posts in the main feed. Posts can be liked, disliked, reported, and commented on. They can also be filtered by their type or tags, and sorted by “date” or “most liked”.

4. **Interact with the map.** Allow users to see the pinned location of previously reported sightings on an interactive map. Clicking on a pin shows more details about a post, and allows users to jump to its location in Google Maps.

### Extra/Secondary Tasks

In the end, we found that creating the two types of posts were very similar tasks. However, we feel that we added more than enough overall features and functionality to warrant full marks for this section. The prototype gives a good indication of the overall feel of using the app.

- **Commenting** \- Allow users to view comments on a post, make comments, and delete comments. This basically implemented what we originally thought discussions would be.  
- **Persistent saving** \- newly created posts are saved in the phone’s file system, so when you turn the emulated phone “off” and on again, the posts will still be there.  
- **Profile tab** \- users can view a personal bio and a summary of the posts they have made.  
- **Navbar** \- for clear app navigation

### Partially implemented but incomplete features

- Filtering on the map tab (buttons are there, we just ran out of time to fix a bug)  
- Linking personal profile and map to the feed (we envisioned clicking on a post in one tab and getting to it in another tab).  
- Downloading the map for offline use (buttons are there, but nothing gets downloaded)

### Features we planned on, but didn’t quite get to

- Editing a post or comment, reporting comments, saving posts

## Design Principles

### Visibility

- **Example 1:** Map markers are bright red and easy to spot. You’ll know where to click.  
- **Example 2:** You can always see where you are in the app. The current tab is highlighted in the navbar, and there is a clear title at the top in the action bar.

- **Other examples**

  - Posts in the feed include timestamps and clear indicators of interactions (likes, dislikes, comments).  
  - The map displays visible markers for sightings  
  - Generally Very visible buttons 

### Feedback

- **Example 1:** Toasts display error messages if a user doesn’t provide the correct inputs. E.g. If they select no filters on the feed, or if they don’t enter title, description or location for post creation.

- **Example 2:** Checkboxes make it immediately clear which options a user has selected. E.g. Selecting filters in the feed or tags in post creation.

### Constraints

- **Example 1:** Users don’t search for tags or other filtering options; they are predefined. (see screenshot next to Feedback Example 2, above).   
- **Example 2:** Location selection is constrained to predefined options in a spinner to reduce errors.
- **Other examples:**
  - The map enforces boundaries to make it only local to the okanagan area, and is centered on Kelowna by default  
  - Liking a post will remove your dislike, you can only choose one

### Consistency

- **Example 1:** All buttons use the same color (with the exception of like and dislike but this is because the like button turns green when selected so we kept them purple to avoid confusion). This helps a user easily identify and recognize the buttons.   
- **Example 2:** Buttons that serve similar purposes use the same language across the app to avoid confusion. This can be observed on the buttons used to remove a comment and a post.  
- **Other Examples**
  - Action bar to title each page  
  - General colour and font consistency  
  - General interface design aligns with popular platforms ensuring familiarity for users

### Affordances

- **Example 1:** The map markers afford clicking, and the resulting pop-ups intuitively display detailed post information, enhancing the interaction appearance.
- **Example 2 (left):** Icons on the bottom navigation bar are familiar and their purposes match a user's natural expectations for what these buttons might do.  

### Simplicity

- **Example 1:** Simple but informative layout for comment section. Displays the basic information of the post so the user is sure they are in the comments section for the intended post, but does not display excess information that would clutter the comment section view.  
- **Example 2 (yellow)**: Simple layout for the map view. Avoids being cluttered by hiding filter options in a closed tab that the user can open and close when necessary. 
- **Other Examples**
  - Minimalistic layout for the feed and map ensures users can easily navigate and focus on the main content.  
  - Filters are accessible and straightforward, with clearly labeled options.  
  - Excessive information is generally avoided in the interface to prevent overwhelming users, focusing on functionality

### Matching

- **Example 1:** Dropdown menu in the filter tab on the map uses language familiar to the user that is similar to what you would see on other apps. 

- **Example 2:** Error messages use real world language that is clear and familiar to a user, rather than technical debugging codes. 

### Help

- **Example 1:** Error messages are clear and descriptive, explicitly stating what a user needs to do to complete their intended task.   
- **Example 2:** Fields where users are supposed to enter a value have hints that direct a user to what they are supposed to enter.

### Other Design Principles

- **Aesthetics** \- We think it looks really nice and clean\! It is visually pleasing and has a unifying aesthetic.   
- **Minimizing memory load** \- The interface is similar to other forum/social interfaces, which helps users recognize how to use it.  
- **Diagnose/recover errors** \- Users can delete posts and comments.  
- **Control and freedom** \- Post creation and pop-up dialogues can be exited.  
- **Safety** \- Confirmation dialogues with the option to cancel are displayed to the user before they can create, report, or delete a post or comment.
