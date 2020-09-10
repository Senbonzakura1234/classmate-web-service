package com.app.manager.startup.startupService;

import com.app.manager.entity.History;
import com.app.manager.model.returnResult.MigrationQueryResult;

public interface SeederService {
    MigrationQueryResult checkMigrationHistory();
    void generateRoles();
    void generateCategory();
    void generateUser();
    void generateCourse();
    void generateStudentCourse();
    void generateSession();
    void updateMigrationHistory(History.EMigration result);
}
