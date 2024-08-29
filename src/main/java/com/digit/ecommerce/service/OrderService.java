package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.OrderDTO;
import com.digit.ecommerce.exception.OrderNotFoundException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    public Orders placeOrder(String token, OrderDTO orderDTO) {

        Books book = bookRepository.findById(orderDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + orderDTO.getBookId()));


        Long userId = extractUserIdFromToken(token);
        User user = new User();
        user.setId(userId);

        Orders order = new Orders(orderDTO, user, book);
        return orderRepository.save(order);
    }

    public boolean cancelOrder(String token, Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        order.setCancel(true);
        order.setStatus("cancelled");
        orderRepository.save(order);
        return true;
    }

    public List<Orders> getAllOrders(boolean cancel) {
        return orderRepository.findByCancel(cancel);
    }

    public List<Orders> getAllOrdersForUser(String token) {
        Long userId = extractUserIdFromToken(token);
        return orderRepository.findByUserId(userId);
    }

    private Long extractUserIdFromToken(String token) {
        // Implement token parsing logic here
        return 1L; // Example userId
    }
}
