package com.twitter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AdminControlPanel {
    private static AdminControlPanel instance;
    private JFrame frame;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton analyzeButton;
    private JButton openUserViewButton;
    private JButton addUserToGroupButton; 
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
        addUserToGroupButton = new JButton("Add User to Group"); 

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

        addUserToGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = getSelectedUser();
                String selectedGroup = getSelectedGroup();
                if (selectedUser != null && selectedGroup != null) {
                    addUserToGroup(selectedUser, selectedGroup);
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(addUserButton);
        panel.add(addGroupButton);
        panel.add(analyzeButton);
        panel.add(openUserViewButton);
        panel.add(addUserToGroupButton); 

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(userTree), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
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
            UserGroup newGroup = new UserGroup(groupID);
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

    private String getSelectedUser() {
        Object selectedNode = userTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
        }
        return null;
    }

    private String getSelectedGroup() {
        Object selectedNode = userTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
        }
        return null;
    }

    private void addUserToGroup(String userID, String groupID) {
        Component userComponent = componentMap.get(userID);
        Component groupComponent = componentMap.get(groupID);

        if (userComponent instanceof User && groupComponent instanceof UserGroup) {
            User user = (User) userComponent;
            UserGroup group = (UserGroup) groupComponent;

            group.addUser(user);
            JOptionPane.showMessageDialog(frame, "User added to the group!");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid selection. Please select a user and a group.");
        }
    }

    public Component getUserByID(String userID) {
        return componentMap.get(userID);
    }

    public static AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    public static void main(String[] args) {
        AdminControlPanel.getInstance();
    }
}
