package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.CartDTO;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.exception.*;
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

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    public String addtoCart(String token, Long bookId) {
        DataHolder dataHolder = tokenUtility.decode(token);
        Books book1 = bookRepository.findById(bookId).orElse(null);
        List<Cart> allCarts=cartRepository.findAll();
        Long count=0L;
        for(Cart c:allCarts){
            if(c.getUser().getId().equals(dataHolder.getId())&&(c.getBook().getId().equals(bookId))){
                c.setQuantity(c.getQuantity()+1);
                c.setTotalPrice(c.getBook().getBookPrice()*c.getQuantity());
                cartRepository.save(c);
                count++;
            }
        }
        if(count==1){
            return "UserId with Book Id you mentioned is already Present in Cart and Updated Successfully";
        }
            if (book1 != null) {
                Books book = bookService.getBookByID(bookId);
                User user = userService.getUserByToken(token);
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setBook(book);
                cart.setQuantity(1L);
                cart.setTotalPrice(book.getBookPrice());
                Cart cartModel = cartRepository.save(cart);
                CartDTO cartDTO = carttoCartDTO(cartModel);
                return String.valueOf(cartDTO);
            } else {
                throw new BookIdNotFoundException("Book Id Not Found.");
            }
        }

    public String removefromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartIdNotFoundException("Cart Id Not Found"));
        cartRepository.deleteById(cartId);
        return "Successfully deleted cart with Id: " + cartId;
    }

    @Transactional
    public String removeByUserId(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        User user = userService.getUserByToken(token);
        List<Cart> cart = user.getCart();
        for (Cart c : cart) {
            cartRepository.deleteAll(c.getId());
        }
        return "Removed Cart items with id :" + dataHolder.getId();
    }

    public CartDTO updateQunatity(String token, Long cartId, Long cartQuantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Card id doesn't exist!"));
        Books book = cart.getBook();
        if (book.getBookQuantity() >= cart.getQuantity() + cartQuantity) {
            Long finalQunatity = cart.getQuantity() + cartQuantity;
            cart.setQuantity(finalQunatity);
            cart.setTotalPrice(book.getBookPrice() * finalQunatity);
            cartRepository.save(cart);
            CartDTO cartDTO = carttoCartDTO(cart);
            return cartDTO;

        } else {
            throw new BookLimitException("Required Book Quanity is greater than Available Book Qunatity");
        }
    }

        public String decreaseQuantity(String token, long cartId, long update_quantity) {
            Cart requiredCart = cartRepository.findById(cartId).orElseThrow(() -> new CartIdNotFoundException("Cart id doesn't exist!"));
            Books requiredbook = requiredCart.getBook();

            requiredCart.setQuantity(requiredCart.getQuantity()-update_quantity);
            requiredCart.setTotalPrice(requiredbook.getBookPrice()* (requiredCart.getQuantity())-update_quantity);

            cartRepository.save(requiredCart);

            return "Quantity decreased for the cart_id: "+cartId;
        }



    public List<CartDTO> getAllCartItemsForUser(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        List<Cart> cart=cartRepository.findAll();
        List<Cart> userCartItems = new ArrayList<>();
        List<CartDTO> cartDTO = new ArrayList<>();
        for (Cart c : cart) {
            if(c.getUser().getId().equals(dataHolder.getId())){
                userCartItems.add(c);
            }
        }
        for (Cart c : userCartItems) {
            cartDTO.add(carttoCartDTO(c));
        }
        return cartDTO;
    }
    public List<Cart> getAllCartItemsForUserModel(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        List<Cart> cart=cartRepository.findAll();
        List<Cart> userCartItems = new ArrayList<>();
        for (Cart c : cart) {
            if(c.getUser().getId().equals(dataHolder.getId())){
                userCartItems.add(c);
            }
        }
        return userCartItems;
    }
    public List<CartDTO> getAllCartItems(String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        List<Cart> userCartItems = new ArrayList<>();
        if (dataHolder.getRole().equalsIgnoreCase("admin")) {
            List<Cart> cart = cartRepository.findAll();
            userCartItems.addAll(cart);
            List<CartDTO> cartDTO = new ArrayList<>();
            for (Cart c : userCartItems) {
                cartDTO.add(carttoCartDTO(c));
            }
            return cartDTO;
        }
        else{
            throw new RoleNotAllowedException("User Has No Access To See The All Cart Details.");
        }
    }

    public CartDTO carttoCartDTO(Cart cart){
        CartDTO cartDTO=new CartDTO(cart);
        return cartDTO;
    }
}