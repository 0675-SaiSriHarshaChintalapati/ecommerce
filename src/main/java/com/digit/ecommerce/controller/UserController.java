package com.digit.ecommerce.controller;

import com.digit.ecommerce.dto.LoginDTO;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.exception.UserAlreadyExistException;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.UserRepository;
import com.digit.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public UserDTO addUser(@RequestBody UserDTO userdto) {
        UserDTO savedUser = userService.saveUser(userdto);
        return savedUser;
    }

    @GetMapping("/read")
    public List<UserDTO> getAllUsers(@RequestHeader String token) {
        return userService.getUsers(token);
    }

    @GetMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){
        String s=userService.login(loginDTO);
        return s;
    }

    @GetMapping("/get")
    public UserDTO getUserById(@RequestHeader String token) {
        User user = userService.getUserByToken(token);
        return userService.convertToDTO(user);
    }

    @PutMapping("/update")
    public UserDTO updateUser(@RequestHeader String token, @RequestBody UserDTO userDTO) {
        User user = userService.convertToEntity(userDTO);
        User updatedUser = userService.updateUser(token,user);
        return userService.convertToDTO(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@RequestHeader String token ,@PathVariable Long id) {
        return userService.deleteUser(token,id);
    }

}

