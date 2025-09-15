package com.run.runners.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/competitions")
    public String competitions() {
        return "competitions";
    }

    @GetMapping("/my-running")
    public String myRunning() {
        return "my-running";
    }

    @GetMapping("/community")
    public String community() {
        return "community";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }
}