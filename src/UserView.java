package com.twitter;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserView {
    private static final List<UserView> userViews = new ArrayList<>();
    private User user;
    private JFrame frame;
    private JPanel followersPanel;
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

        followersPanel = new JPanel();
        followersList = new JList<>();
        tweetTextArea = new JTextArea();
        JButton followButton = new JButton("Follow User");
        JButton postTweetButton = new JButton("Post Tweet");
        newsFeedList = new JList<>();

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Following");
        followersPanel.setBorder(titledBorder);
        followersPanel.add(new JScrollPane(followersList));

        followButton.addActionListener(e -> {
            String targetUserID = JOptionPane.showInputDialog(frame, "Enter the user ID to follow:");
            User targetUser = (User) AdminControlPanel.getInstance().getUserByID(targetUserID);
            if (targetUser != null) {
                user.followUser(targetUser);
                updateFollowersList();
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!");
            }
        });

        postTweetButton.addActionListener(e -> {
            String tweet = tweetTextArea.getText();
            if (!tweet.isEmpty()) {
                user.postTweet(tweet);
                updateTweetTextArea();
                updateNewsFeedList();
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(followersPanel);
        panel.add(new JScrollPane(tweetTextArea));
        panel.add(followButton);
        panel.add(postTweetButton);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(new JScrollPane(newsFeedList), BorderLayout.SOUTH);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder tweetText = new StringBuilder("\n");
        tweetText.append("Creation Time: ").append(dateFormat.format(user.getCreationTime())).append("\n");
        tweetText.append("Last Update Time: ").append(dateFormat.format(user.getLastUpdateTime())).append("\n");

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
