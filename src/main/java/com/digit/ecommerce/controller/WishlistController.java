package com.digit.ecommerce.controller;

import com.digit.ecommerce.dto.WishlistDto;
import com.digit.ecommerce.model.Wishlist;
import com.digit.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @PostMapping("/add")
    public String addProduct(@RequestBody WishlistDto wishlistDto,@RequestHeader String token) {
        return wishlistService.addProduct(wishlistDto,token);
    }

    @DeleteMapping("/{wishlist_id}")
    public ResponseEntity<?> removeProduct(@PathVariable Long wishlist_id,@RequestHeader String token){
        return new ResponseEntity<>(wishlistService.removeProduct(wishlist_id,token), HttpStatus.FOUND);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<?>> getWishListAll(@RequestHeader String token)
    {
        return new ResponseEntity<>(wishlistService.getWishListAll(token),HttpStatus.OK);
    }

}
