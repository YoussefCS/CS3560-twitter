package com.twitter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UserGroup extends CustomComponent {
    private String groupID;
    private ImageIcon icon;
    private List<CustomComponent> children;
    private long creationTime;


    public UserGroup(String groupID, ImageIcon icon) {
        super(groupID);
        this.creationTime = System.currentTimeMillis();
        children = new ArrayList<>();
    }

    public String getGroupID() {
        return groupID;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public List<CustomComponent> getChildren() {
        return children;
    }

    public void add(CustomComponent component) {
        children.add(component);
    }

    // Getter for creationTime
    public long getCreationTime() {
        return creationTime;
    }
}
