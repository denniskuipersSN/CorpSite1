package com.globex.web.controller;

import com.globex.web.model.message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    @Autowired
    public message Message;

    @GetMapping("/")
    public String index(Model model) {
        logger.debug("Welcome to globex.com...");
        model.addAttribute("msg", Message.getMessage());
        return "index";

    }

    //public String getMessage() {return "we're not evil";}

}
