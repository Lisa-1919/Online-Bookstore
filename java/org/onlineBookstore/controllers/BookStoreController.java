package org.onlineBookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookStoreController {

    public BookStoreController() {
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/store/{option}")
    public String option(@PathVariable(value = "option") String option, Model model) {
        if (option.equals("payment")) {
            return "payment";
        } else if (option.equals("shipping")) {
            return "shipping";
        } else {
            return "redirect:/catalog";
        }
    }

}
