package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("quiz")
public class QuizController {
    @GetMapping
    public String showHomePage() {
        return "home";
    }

}
