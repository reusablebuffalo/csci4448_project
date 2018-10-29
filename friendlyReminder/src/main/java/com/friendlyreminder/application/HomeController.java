package com.friendlyreminder.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @Value("${home.message}")
    private String message;

    @RequestMapping("/")
//    public String index() {
//        return "Greetings from Ian Smith";
//    }
    public String welcome(Model model) {
        model.addAttribute("message", message);
        return "home";
    }

}