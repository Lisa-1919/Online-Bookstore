package org.onlineBookstore.controllers;

import org.onlineBookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    public RegistrationController() {
    }

    @GetMapping("/registration")
    public String registrationPage(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String address,
                                   @RequestParam String username, @RequestParam String password,
                                   @RequestParam String confirmPassword, Model model){
        if(!password.equals(confirmPassword)){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        userService.createUser(firstName, lastName, username, password, address);
        return "redirect:/home";
    }
}
