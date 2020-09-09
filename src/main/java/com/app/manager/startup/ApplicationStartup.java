package com.app.manager.startup;

import com.app.manager.startup.service.SeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
public class ApplicationStartup {
    @Autowired SeederService seederService;

    @EventListener(ApplicationReadyEvent.class)
    public void Startup(){
        Seeder();
    }

    private void Seeder(){
        try {
            seederService.generateRoles();
            seederService.generateCategory();
            seederService.generateUser();
            seederService.generateCourse();
            seederService.generateStudentCourse();
            seederService.generateSession();
            System.out.println("Seed data success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
