package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.AddressDTO;
import com.digit.ecommerce.model.Orders;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderInterface {
    ResponseEntity<?> placeOrder(String token, AddressDTO addressDTO);
    boolean cancelOrder(String token, Long orderId);
    Orders updateShippingStatus(String token, Long orderId, String newStatus);
    List<Orders> getAllOrders(boolean cancel);
    List<Orders> getAllOrdersForUser(String token);
}
