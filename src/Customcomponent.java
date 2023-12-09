package com.twitter;

public abstract class CustomComponent {
    private String userID;

    public CustomComponent(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
