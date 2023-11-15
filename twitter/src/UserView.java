package com.twitter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView {
    private static final java.util.List<UserView> userViews = new java.util.ArrayList<>();
    private User user;
    private JFrame frame;
    private JTextArea followTextArea; 
    private JList<String> followersList;
    private JTextArea tweetTextArea;
    private JList<String> newsFeedList;

    public UserView(User user) {
        this.user = user;
        if (!isUserViewOpen(user)) {
            userViews.add(this);
            initialize();
        }
    }

    private boolean isUserViewOpen(User user) {
        for (UserView userView : userViews) {
            if (userView.user.equals(user)) {
                return true;
            }
        }
        return false;
    }

    private void initialize() {
        frame = new JFrame(user.getID() + "'s User View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
    
        // Follow User Section
        JTextArea followTextArea = new JTextArea();
        JButton followButton = new JButton("Follow User");
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetUserID = followTextArea.getText();
                User targetUser = (User) AdminControlPanel.getInstance().getUserByID(targetUserID);
                if (targetUser != null) {
                    user.followUser(targetUser);
                    updateFollowersList();
                } else {
                    JOptionPane.showMessageDialog(frame, "User not found!");
                }
            }
        });
    
        // Following List Section
        followersList = new JList<>();
        TitledBorder followersTitle = BorderFactory.createTitledBorder("Following");
        JPanel followersPanel = new JPanel(new BorderLayout());
        followersPanel.setBorder(followersTitle);
        followersPanel.add(new JScrollPane(followersList), BorderLayout.CENTER);
    
        // Tweet Section
        tweetTextArea = new JTextArea();
        JButton postTweetButton = new JButton("Post Tweet");
        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweet = tweetTextArea.getText();
                if (!tweet.isEmpty()) {
                    user.postTweet(tweet);
                    updateTweetTextArea();
                    updateNewsFeedList();
                }
            }
        });
    
        // News Feed Section
        newsFeedList = new JList<>();
    
        // Layout
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(followTextArea); // Follow Text Area
        panel.add(followButton); // Follow Button
        panel.add(followersPanel); // Following List
        panel.add(new JPanel()); // Empty panel for spacing
        panel.add(new JScrollPane(tweetTextArea)); // Tweet Text Area
        panel.add(postTweetButton); // Post Tweet Button
        panel.add(new JScrollPane(newsFeedList)); // News Feed List
    
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
    
        updateFollowersList();
        updateTweetTextArea();
        updateNewsFeedList();
    
        frame.setVisible(true);
    }    
 

    private void updateNewsFeedList() {
        DefaultListModel<String> newsFeedModel = new DefaultListModel<>();
        newsFeedModel.addElement("News Feed:");

        for (String tweet : user.getNewsFeed()) {
            String formattedTweet = user.getID() + ": " + tweet;
            newsFeedModel.addElement(formattedTweet);
        }

        newsFeedList.setModel(newsFeedModel);
    }

    private void updateTweetTextArea() {
        StringBuilder tweetText = new StringBuilder("\n");
        for (String tweet : user.getNewsFeed()) {
            tweetText.append(user.getID()).append(": ").append(tweet).append("\n");
        }
        tweetTextArea.setText(tweetText.toString());
        tweetTextArea.append("\n");
    }

    private void updateFollowersList() {
        DefaultListModel<String> followersModel = new DefaultListModel<>();
        for (User follower : user.getFollowers()) {
            followersModel.addElement(follower.getID());
        }
        followersList.setModel(followersModel);
    }
}
