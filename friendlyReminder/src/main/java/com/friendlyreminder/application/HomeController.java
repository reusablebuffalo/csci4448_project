package com.friendlyreminder.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @Value("${home.author}")
    private String author;

    @RequestMapping("/")
//    public String index() {
//        return "Greetings from Ian Smith";
//    }
    public String welcome(Model model) {
        model.addAttribute("author", author);
        return "home";
    }

}