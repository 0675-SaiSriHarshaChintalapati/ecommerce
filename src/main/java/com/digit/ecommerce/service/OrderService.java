
package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.OrderDTO;
import com.digit.ecommerce.exception.OrderNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.Cart;
import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.OrderRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

    @Service
    public class OrderService {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private CartService cartService;

        @Autowired
        private UserService userService;

        @Autowired
        private TokenUtility tokenUtility;

        public ResponseEntity<?> placeOrder(String token, String address) {
            User user = userService.getUserByToken(token);
            List<Cart> cartItems = cartService.getAllCartItemsForUserModel(token);

            for (Cart cartItem : cartItems) {
                Orders order = new Orders();
                order.setUser(user);
                order.setBook(cartItem.getBook());
                order.setQuantity(cartItem.getQuantity());
                order.setPrice(cartItem.getTotalPrice());
                order.setAddress(address);
                order.setOrderDate(LocalDate.now());
                order.setCancel(false);
                order.setStatus("ordered");
                order.setShippingStatus("placed"); // Set initial shipping status

                orderRepository.save(order);
            }

            cartService.removeByUserId(token); // Clear the cart after placing the order

            return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
        }



        public boolean cancelOrder(String token, Long orderId) {
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            if ("placed".equalsIgnoreCase(order.getShippingStatus())) {
                order.setCancel(true);
                order.setStatus("cancelled");
                orderRepository.save(order);
                return true;
            } else {
                throw new RuntimeException("Order cannot be cancelled as it is already " + order.getShippingStatus());
            }
        }

        public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
            DataHolder dataHolder = tokenUtility.decode(token);
            if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
                throw new RoleNotAllowedException("Only admins can update the shipping status.");
            }

            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

            order.setShippingStatus(newStatus);
            return orderRepository.save(order);
        }




        public List<Orders> getAllOrders(boolean cancel) {

            return orderRepository.findByCancel(cancel);
        }

        public List<Orders> getAllOrdersForUser(String token) {
            DataHolder dataHolder = tokenUtility.decode(token);
            Long userId = dataHolder.getId();
            return orderRepository.findByUserId(userId);
        }

    }

