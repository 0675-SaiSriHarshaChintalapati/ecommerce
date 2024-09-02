package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.AddressDTO;
import com.digit.ecommerce.dto.DataHolder;
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
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TokenUtility tokenUtility;

    @Override
    public ResponseEntity<?> placeOrder(String token, AddressDTO addressDTO) {
        User user = userService.getUserByToken(token);
        List<Cart> cartItems = cartService.getAllCartItemsForUserModel(token);

        String address = addressDTO.getStreet() + ", " + addressDTO.getCity() + ", " + addressDTO.getState() + " - " + addressDTO.getZip();

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

            // Update book quantity
            bookService.updateQuantity(token, cartItem.getBook().getId(), cartItem.getQuantity());
        }

        cartService.removeByUserId(token); // Clear the cart after placing the order

        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
    }

    @Override
    public boolean cancelOrder(String token, Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if ("placed".equalsIgnoreCase(order.getShippingStatus())) {
            order.setCancel(true);
            order.setStatus("cancelled");
            orderRepository.save(order);

            // Update book quantity
            bookService.updateQuantity(token, order.getBook().getId(), -order.getQuantity());

            return true;
        } else {
            throw new RuntimeException("Order cannot be cancelled as it is already " + order.getShippingStatus());
        }
    }


    @Override
    public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
        DataHolder dataHolder = tokenUtility.decode(token);
        if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
            throw new RoleNotAllowedException("Only admins can update the shipping status.");
        }

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
            order.setShippingStatus(newStatus);
            Orders updatedOrder = orderRepository.save(order);
            System.out.println("Updated Order: " + updatedOrder); // Debug log
            return updatedOrder;
        } else {
            throw new RuntimeException("Cannot change shipping status for cancelled orders.");
        }
    }


//    @Override
//    public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
//        DataHolder dataHolder = tokenUtility.decode(token);
//        if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
//            throw new RoleNotAllowedException("Only admins can update the shipping status.");
//        }
//
//        Orders order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
//
//        if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
//            order.setShippingStatus(newStatus);
//            return orderRepository.save(order);
//        } else {
//            throw new RuntimeException("Cannot change shipping status for cancelled orders.");
//        }
//    }

    @Override
    public List<Orders> getAllOrders(boolean cancel) {
        return orderRepository.findByCancel(cancel).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Orders> getAllOrdersForUser(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        Long userId = dataHolder.getId();
        return orderRepository.findByUserId(userId).stream().distinct().collect(Collectors.toList());
    }
}


//package com.digit.ecommerce.service;
//
//import com.digit.ecommerce.dto.AddressDTO;
//import com.digit.ecommerce.dto.DataHolder;
//import com.digit.ecommerce.exception.RoleNotAllowedException;
//import com.digit.ecommerce.model.Cart;
//import com.digit.ecommerce.model.Orders;
//import com.digit.ecommerce.model.User;
//import com.digit.ecommerce.repository.OrderRepository;
//import com.digit.ecommerce.util.TokenUtility;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderService implements OrderInterface {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BookService bookService;
//
//    @Autowired
//    private TokenUtility tokenUtility;
//
//    @Override
//    public ResponseEntity<?> placeOrder(String token, AddressDTO addressDTO) {
//        User user = userService.getUserByToken(token);
//        List<Cart> cartItems = cartService.getAllCartItemsForUserModel(token);
//
//        String address = addressDTO.getStreet() + ", " + addressDTO.getCity() + ", " + addressDTO.getState() + " - " + addressDTO.getZip();
//
//        for (Cart cartItem : cartItems) {
//            Orders order = new Orders();
//            order.setUser(user);
//            order.setBook(cartItem.getBook());
//            order.setQuantity(cartItem.getQuantity());
//            order.setPrice(cartItem.getTotalPrice());
//            order.setAddress(address);
//            order.setOrderDate(LocalDate.now());
//            order.setCancel(false);
//            order.setStatus("ordered");
//            order.setShippingStatus("placed"); // Set initial shipping status
//
//            orderRepository.save(order);
//
//            // Update book quantity
//            bookService.updateQuantity(token, cartItem.getBook().getId(), cartItem.getQuantity());
//        }
//
//        cartService.removeByUserId(token); // Clear the cart after placing the order
//
//        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
//    }
//
//    @Override
//    public boolean cancelOrder(String token, Long orderId) {
//        Orders order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
//
//        if ("placed".equalsIgnoreCase(order.getShippingStatus())) {
//            order.setCancel(true);
//            order.setStatus("cancelled");
//            orderRepository.save(order);
//
//            // Update book quantity
//            bookService.updateQuantity(token, order.getBook().getId(), -order.getQuantity());
//
//            return true;
//        } else {
//            throw new RuntimeException("Order cannot be cancelled as it is already " + order.getShippingStatus());
//        }
//    }
//
//
//    @Override
//    public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
//        DataHolder dataHolder = tokenUtility.decode(token);
//        if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
//            throw new RoleNotAllowedException("Only admins can update the shipping status.");
//        }
//
//        Orders order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
//
//        if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
//            order.setShippingStatus(newStatus);
//            Orders updatedOrder = orderRepository.save(order);
//            System.out.println("Updated Order: " + updatedOrder); // Debug log
//            return updatedOrder;
//        } else {
//            throw new RuntimeException("Cannot change shipping status for cancelled orders.");
//        }
//    }
//
//
////    @Override
////    public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
////        DataHolder dataHolder = tokenUtility.decode(token);
////        if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
////            throw new RoleNotAllowedException("Only admins can update the shipping status.");
////        }
////
////        Orders order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
////
////        if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
////            order.setShippingStatus(newStatus);
////            return orderRepository.save(order);
////        } else {
////            throw new RuntimeException("Cannot change shipping status for cancelled orders.");
////        }
////    }
//
//    @Override
//    public List<Orders> getAllOrders(boolean cancel) {
//        return orderRepository.findByCancel(cancel).stream().distinct().collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Orders> getAllOrdersForUser(String token) {
//        DataHolder dataHolder = tokenUtility.decode(token);
//        Long userId = dataHolder.getId();
//        return orderRepository.findByUserId(userId).stream().distinct().collect(Collectors.toList());
//    }
//}
//
//
//
////package com.digit.ecommerce.service;
////
////import com.digit.ecommerce.dto.AddressDTO;
////import com.digit.ecommerce.dto.DataHolder;
////import com.digit.ecommerce.exception.RoleNotAllowedException;
////import com.digit.ecommerce.model.Cart;
////import com.digit.ecommerce.model.Orders;
////import com.digit.ecommerce.model.User;
////import com.digit.ecommerce.repository.OrderRepository;
////import com.digit.ecommerce.util.TokenUtility;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////public class OrderService implements OrderInterface {
////
////    @Autowired
////    private OrderRepository orderRepository;
////
////    @Autowired
////    private CartService cartService;
////
////    @Autowired
////    private UserService userService;
////
////    @Autowired
////    private BookService bookService;
////
////    @Autowired
////    private TokenUtility tokenUtility;
////
////    @Override
////    public ResponseEntity<?> placeOrder(String token, AddressDTO addressDTO) {
////        User user = userService.getUserByToken(token);
////        List<Cart> cartItems = cartService.getAllCartItemsForUserModel(token);
////
////        String address = addressDTO.getStreet() + ", " + addressDTO.getCity() + ", " + addressDTO.getState() + " - " + addressDTO.getZip();
////
////        for (Cart cartItem : cartItems) {
////            Orders order = new Orders();
////            order.setUser(user);
////            order.setBook(cartItem.getBook());
////            order.setQuantity(cartItem.getQuantity());
////            order.setPrice(cartItem.getTotalPrice());
////            order.setAddress(address);
////            order.setOrderDate(LocalDate.now());
////            order.setCancel(false);
////            order.setStatus("ordered");
////            order.setShippingStatus("placed"); // Set initial shipping status
////
////            orderRepository.save(order);
////
////            // Update book quantity
////            bookService.updateQuantity(token, cartItem.getBook().getId(), cartItem.getQuantity());
////        }
////
////        cartService.removeByUserId(token); // Clear the cart after placing the order
////
////        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
////    }
////
////    @Override
////    public boolean cancelOrder(String token, Long orderId) {
////        Orders order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
////
////        if ("placed".equalsIgnoreCase(order.getShippingStatus())) {
////            order.setCancel(true);
////            order.setStatus("cancelled");
////            orderRepository.save(order);
////
////            // Update book quantity
////            bookService.updateQuantity(token, order.getBook().getId(), -order.getQuantity());
////
////            return true;
////        } else {
////            throw new RuntimeException("Order cannot be cancelled as it is already " + order.getShippingStatus());
////        }
////    }
////
////    @Override
////    public Orders updateShippingStatus(String token, Long orderId, String newStatus) {
////        DataHolder dataHolder = tokenUtility.decode(token);
////        if (!"admin".equalsIgnoreCase(dataHolder.getRole())) {
////            throw new RoleNotAllowedException("Only admins can update the shipping status.");
////        }
////
////        Orders order = orderRepository.findById(orderId)
////                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
////
////        if (!"cancelled".equalsIgnoreCase(order.getStatus())) {
////            order.setShippingStatus(newStatus);
////            return orderRepository.save(order);
////        } else {
////            throw new RuntimeException("Cannot change shipping status for cancelled orders.");
////        }
////    }
////
////    @Override
////    public List<Orders> getAllOrders(boolean cancel) {
////        return orderRepository.findByCancel(cancel).stream().distinct().collect(Collectors.toList());
////    }
////
////    @Override
////    public List<Orders> getAllOrdersForUser(String token) {
////        DataHolder dataHolder = tokenUtility.decode(token);
////        Long userId = dataHolder.getId();
////        return orderRepository.findByUserId(userId).stream().distinct().collect(Collectors.toList());
////    }
////}
