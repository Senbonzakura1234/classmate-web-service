package com.app.manager.entity;

public enum EVisibility {
    PUBLIC(0, "Public"),
    COURSE(1, "Classmate and teacher only"),
    TEACHER(2, "Teacher only"),
    PRIVATE(3, "Private");

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
