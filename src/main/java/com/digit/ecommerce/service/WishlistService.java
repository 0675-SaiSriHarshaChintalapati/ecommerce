package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.WishlistDto;
import com.digit.ecommerce.exception.AccessDeniedException;
import com.digit.ecommerce.exception.BookAlreadyExistsException;
import com.digit.ecommerce.exception.DataNotFoundException;
import com.digit.ecommerce.exception.RoleNotAllowedException;
import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.model.Wishlist;
import com.digit.ecommerce.repository.BookRepository;
import com.digit.ecommerce.repository.WishlistRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    UserService userService;

    @Autowired
    BookRepository bookRepository;


    @Autowired
    TokenUtility tokenUtility;


    public String addProduct(WishlistDto wishlistDto, String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        if(dataHolder.getRole().equalsIgnoreCase("Admin"))
            throw new AccessDeniedException("Not able to access");
        User user = userService.getUserByToken(token);
        Books book = bookRepository.findById(wishlistDto.getBookId())
                .orElseThrow(() -> new DataNotFoundException("Book not found"));

        Wishlist existingWishlist = wishlistRepository.findByUserAndBook(user, book);
        if (existingWishlist != null) {
            throw new BookAlreadyExistsException("Book already added");
        }
        user.setId(user.getId());
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setBook(book);

        wishlistRepository.save(wishlist);

        return "Book with id " + wishlistDto.getBookId() + " added successfully";
    }


    public String removeProduct(long id,String token) {
        DataHolder dataHolder = tokenUtility.decode(token);
        if(dataHolder.getRole().equalsIgnoreCase("Admin"))
            throw new AccessDeniedException("Not able to access");
        else {
            Wishlist delete_user = wishlistRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("product is not wishlisted"));
            wishlistRepository.deleteById(delete_user.getId());
            return "Product with " + id + " has been removed from Your Wishlist ";
        }
    }
    public List<WishlistDto> getWishListAll(String token) {

        DataHolder dataHolder = tokenUtility.decode(token);
        if ((dataHolder.getRole().equalsIgnoreCase("Admin") || dataHolder.getRole().equalsIgnoreCase("User")) && token != null) {
             if(dataHolder.getRole().equalsIgnoreCase("User")) {
                 List<Wishlist> retr = wishlistRepository.findAll();
                 List<Wishlist> retrieve1 = new ArrayList<>();
                 for (Wishlist c : retr) {
                     User user = c.getUser();
                     if (user.getId().equals(dataHolder.getId())) {
                         retrieve1.add(c);
                     }
                 }
                 List<WishlistDto> wishlistDtos = retrieve1.stream()
                         .map(WishlistDto::new)
                         .toList();
                 return wishlistDtos;
             }
             else {
                 List<Wishlist> retrieve = wishlistRepository.findAll();
                 List<WishlistDto> wishlistDtos = retrieve.stream()
                         .map(WishlistDto::new)
                         .collect(Collectors.toList());

                 return wishlistDtos;
             }
            }
        else {
            throw new RoleNotAllowedException("please login to access the data");
        }
    }



}
