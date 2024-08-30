package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.CartDTO;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.exception.AccessDeniedException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.Cart;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.CartRepository;
import com.digit.ecommerce.util.TokenUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    TokenUtility tokenUtility;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    BookRepository bookRepository;

    public CartDTO addtoCart(String token,Long bookId){
        //DataHolder dataHolder=tokenUtility.decode(token);
        Books book=bookService.getBookByID(bookId);
        User user=userService.getUserByToken(token);
        Cart cart=new Cart();
        cart.setUser(user);
        cart.setBook(book);
        cart.setQuantity(1L);
        cart.setTotalPrice(book.getBookPrice());
        Cart cartModel=cartRepository.save(cart);
        CartDTO cartDTO=carttoCartDTO(cartModel);
        return cartDTO;
    }

    public String removefromCart(Long cartId){
        cartRepository.deleteById(cartId);
        return "Successfully deleted cart with Id: "+cartId;
    }

    @Transactional
    public String removeByUserId(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        User user = userService.getUserByToken(token);
        List<Cart> cart = user.getCart();
        for(Cart c : cart){
            cartRepository.deleteAll(c.getId());
        }
        return "Removed ";
    }

    public CartDTO updateQunatity(String token,Long cartId,Long cartQuantity){
        Cart cart=cartRepository.findById(cartId).orElseThrow(()-> new RuntimeException("Card id doesn't exist!"));
        Books book=cart.getBook();
        if(cartQuantity>cart.getQuantity()){
            Long finalQunatity=cart.getQuantity()+cartQuantity;
            cart.setQuantity(finalQunatity);
            cart.setTotalPrice(cart.getTotalPrice()*finalQunatity);
            cartRepository.save(cart);
            CartDTO cartDTO=carttoCartDTO(cart);
            return cartDTO;
        }
        else{
            Long finalQunatity=cart.getQuantity()-cartQuantity;
            cart.setQuantity(finalQunatity);
            cart.setTotalPrice(cart.getTotalPrice()*finalQunatity);
            cartRepository.save(cart);
            CartDTO cartDTO=carttoCartDTO(cart);
            return cartDTO;
        }
    }

    public List<Cart> getAllCartItemsForUser(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        List<UserDTO> cart = userService.getUsersCart(token);
        List<Cart> userCartItems = new ArrayList<>();
        for (UserDTO userDTO : cart) {
            Cart userCartItem = cartRepository.findById(dataHolder.getId())
                    .orElseThrow(() -> new RuntimeException("User Id NOT found"));
            userCartItems.add(userCartItem);
        }
        return userCartItems;
    }

    public List<CartDTO> getAllCartItems(String token){
        DataHolder dataHolder = tokenUtility.decode(token);
        if(!dataHolder.getRole().equalsIgnoreCase("admin")){
            throw new AccessDeniedException("Only Admins can view all cart items");
        }
        List<Cart> carts=cartRepository.findAll();
        return carts.stream()
                .map(this::carttoCartDTO)
                .collect(Collectors.toList());
    }

    public Cart cartDTOtocart(CartDTO cartDTO){
        Cart cart=new Cart(cartDTO);
        return cart;
    }
    public CartDTO carttoCartDTO(Cart cart){
        CartDTO cartDTO=new CartDTO(cart);
        return cartDTO;
    }

}
