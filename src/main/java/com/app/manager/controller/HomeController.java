package com.app.manager.controller;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    public String index() {
        return "views/home";
    }
}
