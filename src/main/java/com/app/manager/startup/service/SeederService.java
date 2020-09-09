package com.app.manager.startup.service;

public interface SeederService {
    void generateRoles();
    void generateCategory();
    void generateUser();
    void generateCourse();
    void generateStudentCourse();
    void generateSession();
}
