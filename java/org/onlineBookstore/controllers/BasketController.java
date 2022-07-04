package org.onlineBookstore.controllers;

import org.onlineBookstore.entities.Basket;
import org.onlineBookstore.entities.User;
import org.onlineBookstore.repo.BasketRepository;
import org.onlineBookstore.services.BasketService;
import org.onlineBookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserService userService;

    public BasketController() {
    }

    @GetMapping("/user/basket")
    public String basket(Model model) {
        User user = userService.getAuthorizedUser();
        Basket basket = basketService.findByUserId(user.getId());
        model.addAttribute("basket", basket);
        return "basket";
    }

}
