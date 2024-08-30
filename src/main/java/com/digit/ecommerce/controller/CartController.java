package com.digit.ecommerce.controller;


import com.digit.ecommerce.dto.CartDTO;
import com.digit.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> addtoCart(@RequestHeader String token,@PathVariable Long bookId){
        return new ResponseEntity<>(cartService.addtoCart(token,bookId),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{cartId}")
    public String deleteCart(@PathVariable Long cartId){

        return cartService.removefromCart(cartId);
    }

    @DeleteMapping("/deletebyuserid")
    public String deleteByUserId(@RequestHeader String token){
        return cartService.removeByUserId(token);
    }


    @PutMapping("/update/{cartId}/{quantity}")
    public ResponseEntity<?> updateQunatity(@RequestHeader String token,@PathVariable Long cartId,@PathVariable Long quantity){
        return new ResponseEntity<>(cartService.updateQunatity(token, cartId, quantity),HttpStatus.CREATED);
    }

    @GetMapping("/cartforuser")
    public ResponseEntity<?>  getallcartitemsforuser(@RequestHeader String token){
        return new ResponseEntity<>(cartService.getAllCartItemsForUser(token),HttpStatus.CREATED);
    }

    @GetMapping("/cartforadmin")
    public ResponseEntity<?> getallcartitems(@RequestHeader String token){
        return new ResponseEntity<>(cartService.getAllCartItems(token),HttpStatus.CREATED);
    }
}