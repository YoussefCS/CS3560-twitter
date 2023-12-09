package com.twitter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AdminControlPanel {
    private static AdminControlPanel instance;
    private JFrame frame;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton analyzeButton;
    private JButton openUserViewButton;
    private JButton verifyIDsButton;
    private JButton findLastUpdatedButton;
    private JTree userTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode root;

    private Map<String, Component> componentMap;

    private AdminControlPanel() {
        frame = new JFrame("Admin Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        userTree = new JTree(treeModel);

        addUserButton = new JButton("Add User");
        addGroupButton = new JButton("Add Group");
        analyzeButton = new JButton("Analyze");
        openUserViewButton = new JButton("Open User View");
        verifyIDsButton = new JButton("Verify IDs");
        findLastUpdatedButton = new JButton("Find Last Updated User");

        componentMap = new HashMap<>();

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = JOptionPane.showInputDialog(frame, "Enter User ID:");
                addUser(userID);
            }
        });

        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupID = JOptionPane.showInputDialog(frame, "Enter Group ID:");
                addGroup(groupID);
            }
        });

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyze();
            }
        });

        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = getSelectedUser();
                if (selectedUser != null) {
                    openUserView(selectedUser);
                }
            }
        });

        verifyIDsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyIDs();
            }
        });

        findLastUpdatedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findLastUpdatedUser();
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(addUserButton);
        panel.add(addGroupButton);
        panel.add(analyzeButton);
        panel.add(openUserViewButton);
        panel.add(verifyIDsButton);
        panel.add(findLastUpdatedButton);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(userTree), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    private void addUser(String userID) {
        if (!componentMap.containsKey(userID)) {
            User newUser = new User(userID);
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(userID);
            treeModel.insertNodeInto(userNode, root, root.getChildCount());
            componentMap.put(userID, newUser);
        } else {
            JOptionPane.showMessageDialog(frame, "User ID already exists!");
        }
    }

    private void addGroup(String groupID) {
        if (!componentMap.containsKey(groupID)) {
            UserGroup newGroup = new UserGroup(groupID, null);
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(groupID);
            treeModel.insertNodeInto(groupNode, root, root.getChildCount());
            componentMap.put(groupID, newGroup);
        } else {
            JOptionPane.showMessageDialog(frame, "Group ID already exists!");
        }
    }

    private void analyze() {
        AnalysisVisitor visitor = new AnalysisVisitor();
        for (Component component : componentMap.values()) {
            component.accept(visitor);
        }
        JOptionPane.showMessageDialog(frame,
                "Total Users: " + visitor.getTotalUsers() + "\n" +
                        "Total Groups: " + visitor.getTotalGroups() + "\n" +
                        "Total Tweets: " + visitor.getTotalTweets() + "\n" +
                        "Positive Tweets Percentage: " + visitor.getPositiveTweetsPercentage() + "%"
        );
    }

    private void openUserView(String userID) {
        User user = (User) componentMap.get(userID);
        if (user != null) {
            new UserView(user);
        }
    }

    private void verifyIDs() {
        boolean isValid = validateIDs();
        JOptionPane.showMessageDialog(frame, "All IDs are valid: " + isValid);
    }

    private boolean validateIDs() {
        Set<String> uniqueIDs = new HashSet<>();
        
        // Validate User IDs
        for (Component component : componentMap.values()) {
            if (component instanceof User) {
                String userID = ((User) component).getUserID();
                if (uniqueIDs.contains(userID) || userID.contains(" ")) {
                    return false;  // Invalid ID found
                }
                uniqueIDs.add(userID);
            }
        }

        // Validate Group IDs
        for (Component component : componentMap.values()) {
            if (component instanceof UserGroup) {
                String groupID = ((UserGroup) component).getUserID();
                if (uniqueIDs.contains(groupID) || groupID.contains(" ")) {
                    return false;  // Invalid ID found
                }
                uniqueIDs.add(groupID);
            }
        }

        return true;  // All IDs are valid
    }

    private void findLastUpdatedUser() {
        User lastUpdatedUser = findLastUpdatedUserLogic();
        if (lastUpdatedUser != null) {
            JOptionPane.showMessageDialog(frame, "Last Updated User: " + lastUpdatedUser.getUserID());
        } else {
            JOptionPane.showMessageDialog(frame, "No users found");
        }
    }

    private User findLastUpdatedUserLogic() {
        User lastUpdatedUser = null;
        long latestUpdateTime = 0;

        // Iterate over users to find the one with the latest lastUpdateTime
        for (Component component : componentMap.values()) {
            if (component instanceof User) {
                User currentUser = (User) component;
                long userLastUpdateTime = currentUser.getLastUpdateTime();

                if (userLastUpdateTime > latestUpdateTime) {
                    lastUpdatedUser = currentUser;
                    latestUpdateTime = userLastUpdateTime;
                }
            }
        }

        return lastUpdatedUser;
    }

    private String getSelectedUser() {
        Object selectedNode = userTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
        }
        return null;
    }

    public static void main(String[] args) {
        AdminControlPanel.getInstance();
    }
}
