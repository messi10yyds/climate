package com.basic.climate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/index")
    public String home() {
        return "index.html";
    }

    @GetMapping("/trend")
    public String trendPage() {
        return "forward:/pages/f1.html";
    }

    @GetMapping("/distribution")
    public String distributionPage() {
        return "forward:/pages/f2.html";
    }

    @GetMapping("/extreme")
    public String extremePage() {
        return "forward:/pages/f3.html";
    }
}
