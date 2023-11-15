package com.twitter;

public interface Component {
    void accept(Visitor visitor);
}
