package com.twitter;

import java.util.ArrayList;
import java.util.List;

public class UserGroup implements Component {
    private String groupID;
    private List<Component> members;

    public UserGroup(String groupID) {
        this.groupID = groupID;
        members = new ArrayList<>();
    }

    public void addUser(User user) {
        members.add(user);
    }

    public void addComponent(Component component) {
        members.add(component);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Component member : members) {
            member.accept(visitor);
        }
    }
}
