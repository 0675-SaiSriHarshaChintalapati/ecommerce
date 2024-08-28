package com.digit.ecommerce.service;

import com.digit.ecommerce.dto.LoginDTO;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.model.User;

import java.util.List;

public interface UserInterface {
    UserDTO saveUser(UserDTO userdto);
    public List<UserDTO> getUsers(String token);
    public User getUserByToken(String token);
    public User updateUser(String token, User user);
    public String deleteUser(String token,Long id);
    public String login(LoginDTO loginDTO);
    public UserDTO convertToDTO(User user);
    public User convertToEntity(UserDTO userDTO);
}
