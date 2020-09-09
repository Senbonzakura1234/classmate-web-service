package com.app.manager.startup.startupService;

public interface SeederService {
    void generateRoles();
    void generateCategory();
    void generateUser();
    void generateCourse();
    void generateStudentCourse();
    void generateSession();
}
