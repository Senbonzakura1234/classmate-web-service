package com.app.manager.model;

import com.app.manager.service.interfaceClass.SeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class Seeder {
    @Autowired SeederService seederService;

    public void Seed(){
        seederService.generateRoles();
        seederService.generateSubscription();
        seederService.generateCategory();
        seederService.generateUser();
    }
}
