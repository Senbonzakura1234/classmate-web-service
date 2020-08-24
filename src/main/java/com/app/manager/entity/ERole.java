package com.app.manager.entity;

public enum ERole {
    ALL(0, "All"),
    ROLE_ADMIN(1, "Admin"),
    ROLE_TEACHER(2, "Teacher"),
    ROLE_STUDENT(3, "Student"),
    ROLE_USER(4, "User");

    private final int value;
    private final String name;

    ERole(int value, String name) {
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
