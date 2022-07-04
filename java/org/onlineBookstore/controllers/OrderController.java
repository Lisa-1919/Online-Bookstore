package org.onlineBookstore.controllers;

import org.onlineBookstore.services.BasketService;
import org.onlineBookstore.services.OrderService;
import org.onlineBookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @Autowired
    private UserService userservice;

    @Autowired
    private BasketService basketService;

    public OrderController() {
    }

    @PostMapping("/user/order/create/{id}")
    public String order(@PathVariable(value = "id") long id,@RequestParam(name = "payment") String payment,
                        @RequestParam(name="shipping") String shipping, Model model){
        orderservice.create(id, payment, shipping);
        return"redirect:/store/catalog";
    }
}
