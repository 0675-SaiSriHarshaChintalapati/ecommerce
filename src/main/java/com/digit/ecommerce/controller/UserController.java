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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public UserDTO addUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return userService.convertToDTO(savedUser);
    }

    @GetMapping("/read")
    public List<UserDTO> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){
        String s=userService.login(loginDTO);
        return s;
    }

    @GetMapping("/read/{id}")
    public UserDTO getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        return userService.convertToDTO(user);
    }

    @PutMapping("/update/{id}")
    public UserDTO updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        User user = userService.convertToEntity(userDTO);
        User updatedUser = userService.updateUser(id, user);
        return userService.convertToDTO(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);
    }

}

