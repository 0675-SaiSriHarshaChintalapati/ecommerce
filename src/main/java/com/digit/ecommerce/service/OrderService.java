package com.digit.ecommerce.service;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.OrderDTO;
import com.digit.ecommerce.exception.OrderNotFoundException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.Cart;
import com.digit.ecommerce.model.Orders;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.CartRepository;
import com.digit.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digit.ecommerce.util.TokenUtility;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    TokenUtility tokenUtility;


    public Orders placeOrder(String token, String address) {
       // DataHolder dataHolder=tokenUtility.decode(token);
//      Long userId=dataHolder.getId();
        List<Cart> cartItems = cartService.getAllCartItemsForUser(token);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Orders order = new Orders();

        order.setOrderDate(LocalDate.now());
        order.setStatus("ordered");
        order.setAddress(address);
        double totalPrice = 0;
        for (Cart cartItem : cartItems) {
            Books book = cartItem.getBook();
            totalPrice += book.getBookPrice() * cartItem.getQuantity();
            cartRepository.delete(cartItem); // Remove item from cart after placing order
        }

        order.setPrice(totalPrice);
        orderRepository.save(order);

        return order;
    }

    public boolean cancelOrder(String token, Long orderId) {
       DataHolder dataHolder=tokenUtility.decode(token);
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
        DataHolder dataHolder=tokenUtility.decode(token);
        Long userId = dataHolder.getId();
        return orderRepository.findByUserId(userId);
    }


}
