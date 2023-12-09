package com.twitter;

public interface Visitor {
    void visit(User user);

    void visit(UserGroup userGroup);
}
