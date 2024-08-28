package com.digit.ecommerce.service;
import com.digit.ecommerce.dto.DataHolder;
import com.digit.ecommerce.dto.LoginDTO;
import com.digit.ecommerce.dto.UserDTO;
import com.digit.ecommerce.exception.AccessDeniedException;
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
public class UserService implements UserInterface{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenUtility tokenUtility;
    public UserDTO saveUser(UserDTO userdto) {
        User userByUsername = userRepository.findByfirstName(userdto.getFirstName());
        User userByEmail = userRepository.findByemailId(userdto.getEmailId());
        if ((userByUsername != null) || (userByEmail != null)||userdto == null) {
            throw new UserAlreadyExistException("User Already Exists with that credential");
        }
        User user=convertToEntity(userdto);
        UserDTO userdto1= convertToDTO(user);
        userRepository.save(user);
        return userdto1;
    }

    public List<UserDTO> getUsers(String token) {
        DataHolder decode = tokenUtility.decode(token);
        if (!decode.getRole().equalsIgnoreCase("Admin")) {
            throw new AccessDeniedException("Access Denied");
        }
        List<User> allUserData = userRepository.findAll();
        return allUserData.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public User getUserByToken(String token) {
        DataHolder decode = tokenUtility.decode(token);
        Long id= decode.getId();
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(String token, User user) {
        DataHolder decode = tokenUtility.decode(token);
        Long id= decode.getId();
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

    public String deleteUser(String token,Long id) {
        DataHolder decode = tokenUtility.decode(token);
        if (!decode.getRole().equalsIgnoreCase("Admin")) {
            throw new AccessDeniedException("Access Denied");
        }
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
        UserDTO userDTO = new UserDTO(user);
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDob(user.getDob());
        userDTO.setEmailId(user.getEmailId());
        userDTO.setRole(user.getRole());
        userDTO.setPassword(user.getPassword());
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
        user.setPassword(userDTO.getPassword());

        return user;
    }

}

