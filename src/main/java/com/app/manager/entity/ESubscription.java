package com.app.manager.entity;

public enum ESubscription {
    FREE(0, "FREE", 0, 0,
        15, 30,
        2, 5,
            false),
    PREMIUM(1, "PREMIUM", 15, 0,
        30, 120,
        5, 10,
            true),
    ULTIMATE(2, "ULTIMATE", 50, 0,
        0, 0,
        0, 0,
            true);

    private final int level;
    private final String name;
    private final double price; //dollar per month
    private final double discount;
    private final int max_student;
    private final int max_course_duration; // days
    private final int max_session_duration; // hours
    private final int max_exercise_per_session;
    private final boolean allow_face_check;

    ESubscription(int level, String name, double price, double discount,
                  int max_student, int max_course_duration, int max_session_duration,
                  int max_exercise_per_session, boolean allow_face_check) {
        this.level = level;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.max_student = max_student;
        this.max_course_duration = max_course_duration;
        this.max_session_duration = max_session_duration;
        this.max_exercise_per_session = max_exercise_per_session;
        this.allow_face_check = allow_face_check;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public int getMax_student() {
        return max_student;
    }

    public int getMax_course_duration() {
        return max_course_duration;
    }

    public int getMax_session_duration() {
        return max_session_duration;
    }

    public int getMax_exercise_per_session() {
        return max_exercise_per_session;
    }

    public boolean isAllow_face_check() {
        return allow_face_check;
    }
}
