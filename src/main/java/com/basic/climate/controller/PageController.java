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

    @GetMapping("/comparsion")
    public String comparsionPage() {return "forward:/pages/f4.html";}

    @GetMapping("/annual")
    public String annualPage() {return "forward:/pages/f5.html";}

    @GetMapping("/anomaly")
    public String anomalyPage() {return "forward:/pages/f6.html";}

    @GetMapping("/top&bottom")
    public String topPage() {return "forward:/pages/f7.html";}

    @GetMapping("amplitude")
    public String amplitudePage() {return "forward:/pages/f8.html";}
}
