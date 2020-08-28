package com.app.manager.controller;

import com.app.manager.model.Seeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired Seeder seeder;

    @RequestMapping({"/home", "/"})
    public String index() {
//        seeder.Seed(); // uncomment this to seed data, comment it again when done seeding
        return "views/home";
    }
}
