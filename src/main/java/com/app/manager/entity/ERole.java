package com.app.manager.entity;

public enum ERole {
    ROLE_ADMIN(0, "Admin"),
    ROLE_TEACHER(1, "Teacher"),
    ROLE_STUDENT(2, "Student"),
    ROLE_USER(3, "User");

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
