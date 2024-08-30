package com.digit.ecommerce.controller;

import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeorder")
    public Orders placeOrder(@RequestHeader("token") String token, @RequestBody String address) {
        return orderService.placeOrder(token, address);
    }

    @PutMapping("/cancelorder/{orderId}")
    public boolean cancelOrder(@RequestHeader("token") String token, @PathVariable Long orderId) {
        return orderService.cancelOrder(token, orderId);
    }

    @GetMapping("/getallorders")
    public List<Orders> getAllOrders(@RequestParam(defaultValue = "false") boolean cancel) {
        return orderService.getAllOrders(cancel);
    }

    @GetMapping("/getallordersforuser")
    public List<Orders> getAllOrdersForUser(@RequestHeader("token") String token) {
        return orderService.getAllOrdersForUser(token);
    }
}
