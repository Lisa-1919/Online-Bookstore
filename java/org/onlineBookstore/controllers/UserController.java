package org.onlineBookstore.controllers;

import org.onlineBookstore.entities.Order;
import org.onlineBookstore.entities.OrderDetails;
import org.onlineBookstore.entities.Product;
import org.onlineBookstore.entities.User;
import org.onlineBookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public UserController() {
    }

    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("user", userService.getAuthorizedUser());
        if (userService.getRoleName().equals("user")) {
            return "account";
        } else {
            return "account_admin";
        }
    }

    @GetMapping("/account/admin")
    public String accountAdmin(Model model) {
        model.addAttribute("user", userService.getAuthorizedUser());
        return "account";
    }

    @GetMapping("/user/products/purchased")
    public String products(Model model){
        User user = userService.getAuthorizedUser();
        List<OrderDetails> products = new ArrayList<>();
        for(Order order: user.getOrders()){
            for(OrderDetails orderDetails : order.getOrderDetails()){
                products.add(orderDetails);
            }
        }
        model.addAttribute("orders", user.getOrders());
        return "purchased_product";
    }
}
