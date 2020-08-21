package com.app.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestErrorController {
    @GetMapping("/test/error/500")
    public void test500() throws Exception{
        throw new Exception("Internal Server Error");
    }
    @PostMapping("/test/error/405")
    public void test405(){
    }

    @RequestMapping("/test/error/400")
    public void test400(@SuppressWarnings({"unused", "RedundantSuppression"})
                            @RequestParam(value = "id") String id){
    }

    @RequestMapping("/test/unAuthorize/error/401")
    public void test401(){
    }
}
