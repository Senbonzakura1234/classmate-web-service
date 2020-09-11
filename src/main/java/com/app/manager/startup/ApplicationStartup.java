package com.app.manager.startup;

import com.app.manager.entity.History;
import com.app.manager.startup.startupService.SeederService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
public class ApplicationStartup {
    @Autowired SeederService seederService;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @EventListener(ApplicationReadyEvent.class)
    public void Startup(){
        Seeder();
    }

    private void Seeder(){
        logger.info("Starting seeder");
        try {
            logger.info("Process checking migration history");
            var check = seederService.checkMigrationHistory();
            var result = check.getResult();
            if(result == History.EMigration.SEEDABLE){

                logger.info("Seeing Roles");
                seederService.generateRoles();

                logger.info("Seeing Category");
                seederService.generateCategory();

                logger.info("Seeing User");
                seederService.generateUser();

                logger.info("Seeing Course");
                seederService.generateCourse();

                logger.info("Seeing Student to course");
                seederService.generateStudentCourse();

                logger.info("Seeing Session");
                seederService.generateSession();

                logger.info("Seeing Post");
                seederService.generatePost();

                logger.info("Seeing Comment");
                seederService.generateComment();

                logger.info("Seed data success");
                seederService.updateMigrationHistory(History.EMigration.SEEDED);
            }else if(result == History.EMigration.NOT_SEEDED){
                logger.info("Please recreate the database, first");
                seederService.updateMigrationHistory(History.EMigration.NOT_SEEDED);
            }else if(result == History.EMigration.SEEDED){
                logger.info("Data already seeded, perform skip seeder");
            }else {
                logger.info("Error: " + check.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
        }
        logger.info("Seed ended");
    }
}
