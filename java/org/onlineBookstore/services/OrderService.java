package org.onlineBookstore.services;

import org.onlineBookstore.entities.Basket;
import org.onlineBookstore.entities.Order;
import org.onlineBookstore.entities.OrderDetails;
import org.onlineBookstore.entities.Product;
import org.onlineBookstore.repo.OrderDetailsRepository;
import org.onlineBookstore.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private BasketService basketService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public void create(long userId, String payment, String shipping){
        Order order = new Order(payment, shipping, 0);
        if(payment.equals("uponReceipt")){
            order.setPaymentOption("При получении");
            order.setStatus("Не оплачен");
        }else{
            order.setPaymentOption("Онлайн");
            order.setStatus("Оплачен");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if(shipping.equals("courier")){
            order.setShippingCost(5);
            c.add(Calendar.DATE, 1);
            order.setAddress(userService.findById(userId).getAddress());
        }else if(shipping.equals("self_drop1")){
            c.add(Calendar.DATE, 3);
            order.setAddress("ул. Уборевича д.77");

        } else{
            c.add(Calendar.DATE, 3);
            order.setAddress("ул. Я. Коласа д.25");
        }
        order.setShippingDate(c.getTime());
        Basket basket = basketService.findByUserId(userId);
        List<OrderDetails> details = new ArrayList<>();
        double sum = 0;
        for(Product product: basketService.products(basket.getId())){
            OrderDetails orderDetails = new OrderDetails(product, 1, product.getPrice());
            orderDetailsRepository.save(orderDetails);
            details.add(orderDetails);
            sum += orderDetails.getPrice();
            basketService.deleteProduct(basket, product);
        }
        order.setOrderDetails(details);
        order.setOrderPrice(sum);
        order.setUser(userService.findById(userId));
        orderRepository.save(order);
    }
}
