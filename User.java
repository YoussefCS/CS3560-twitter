package com.twitter;

import java.util.ArrayList;
import java.util.List;

public class User implements CustomComponent, Observer {
    private String userID;
    private List<User> followers;
    private List<String> newsFeed;
    private long creationTime;
    private long lastUpdateTime;

    public User(String userID) {
        super(userID);
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = 0;  // Initialize to 0
        followers = new ArrayList<>();
        newsFeed = new ArrayList<>();
    }

    public void postTweet(String tweet) {
        newsFeed.add(tweet);
        notifyFollowers(tweet);
    }

    public void followUser(User targetUser) {
        if (!followers.contains(targetUser)) {
            followers.add(targetUser);
            targetUser.addObserver(this);  // Add this user as an observer to the target user
        }
    }

    @Override
    public void update(String tweet) {
        System.out.println(userID + " received an update: " + tweet);
        // You can add more logic here based on what you want to do when an update is received
    }

    private void notifyFollowers(String tweet) {
        for (User follower : followers) {
            if (follower.isFollowing(this)) {
                follower.updateNewsFeed(userID, tweet);
            }
        }
    }

    private void updateNewsFeed(String senderID, String tweet) {
        String formattedTweet = senderID + ": " + tweet;
        newsFeed.add(formattedTweet);
        update(formattedTweet);  // Notify observers (if any) about the new tweet
    }

    public boolean isFollowing(User targetUser) {
        return followers.contains(targetUser);
    }

    private void addObserver(Observer observer) {
        // Add an observer to this user
    }

    public List<String> getNewsFeed() {
        return newsFeed;
    }

    public int getNewsFeedSize() {
        return newsFeed.size();
    }

    public List<User> getFollowers() {
        return followers;
    }

    public String getID() {
        return userID;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // Getter for creationTime
    public long getCreationTime() {
        return creationTime;
    }

    // Getter for lastUpdateTime
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}
