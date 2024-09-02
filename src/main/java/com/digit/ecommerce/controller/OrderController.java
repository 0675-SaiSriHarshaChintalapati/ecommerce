package com.digit.ecommerce.controller;

import com.digit.ecommerce.dto.AddressDTO;
import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeorder")

    public ResponseEntity<?> placeOrder(@RequestHeader("token") String token, @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(orderService.placeOrder(token, addressDTO), HttpStatus.CREATED);
    }

    @PutMapping("/cancelorder/{orderId}")
    public boolean cancelOrder(@RequestHeader("token") String token, @PathVariable Long orderId) {
        return orderService.cancelOrder(token, orderId);
    }

    @PutMapping("/updateShippingStatus/{orderId}")
    public ResponseEntity<Orders> updateShippingStatus(@RequestHeader("token") String token, @PathVariable Long orderId, @RequestParam String newStatus) {
        Orders updatedOrder = orderService.updateShippingStatus(token, orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
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
