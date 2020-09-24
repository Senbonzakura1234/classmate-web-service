package com.app.manager.entity;

public enum EVisibility {
    PUBLIC(0, "Public"),
    TEACHER(1, "All Teacher"),
    COURSE(2, "Classmate and teacher only"),
    TEACHERCOURSE(3, "Teacher of course only"),
    PRIVATE(4, "Private");

    private final int value;
    private final String name;

    EVisibility(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
