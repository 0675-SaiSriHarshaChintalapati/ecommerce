package com.digit.ecommerce.service;
import com.digit.ecommerce.dto.LoginDTO;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.exception.AuthenticationException;
import com.digit.ecommerce.exception.UserAlreadyExistException;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.repository.UserRepository;
import com.digit.ecommerce.util.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenUtility tokenUtility;
    public User saveUser(User user) {
        User userByUsername = userRepository.findByfirstName(user.getFirstName());
        User userByEmail = userRepository.findByemailId(user.getEmailId());
        if ((userByUsername != null) || (userByEmail != null)) {
            throw new UserAlreadyExistException("User Already Exists with that credential");
        }
        return userRepository.save(user);
    }

    public List<UserDTO> getUsers() {
        List<User> find= userRepository.findAll();
        return find.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setDob(user.getDob());
            existingUser.setUpdatedDate(LocalDate.now());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmailId(user.getEmailId());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        }
        return null;
    }

    public String deleteUser(long id) {
        userRepository.deleteById(id);
        return "User removed !! " + id;
    }

    public String login(LoginDTO loginDTO) {
        String userEmail = loginDTO.getEmailId();
        String userPassword = loginDTO.getPassword();
        User check = userRepository.findByemailId(userEmail);
        if (check != null) {
            if (userEmail.equals(check.getEmailId()) && (userPassword.equals(check.getPassword()))) {
                String token = tokenUtility.getToken(check.getId(), check.getRole());
                return token;
            }
            throw new AuthenticationException("User or password invalid!");
        } else {
            throw new AuthenticationException("User does not exist");
        }
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDob(user.getDob());
        userDTO.setEmailId(user.getEmailId());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        user.setEmailId(userDTO.getEmailId());
        user.setRole(userDTO.getRole());
        return user;
    }

}

