package com.digit.ecommerce.service;
import com.digit.ecommerce.config.PasswordEncryption;
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

    @Autowired
    PasswordEncryption passwordEncryption;

    public UserDTO saveUser(UserDTO userdto) {
        User userByUsername = userRepository.findByfirstName(userdto.getFirstName());
        User userByEmail = userRepository.findByemailId(userdto.getEmailId());
        if ((userByUsername != null) || (userByEmail != null)||userdto == null) {
            throw new UserAlreadyExistException("User Already Exists with that credential");
        }

        User user=convertToEntity(userdto);
        UserDTO userdto1= convertToDTO(user);
        user.setPassword(passwordEncryption.passwordEncoder().encode(user.getPassword()));
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

    public List<UserDTO> getUsersCart(String token) {
        DataHolder dataHolder=tokenUtility.decode(token);
        List<User> allUserData = userRepository.findAll();
        return allUserData.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public User getUserByToken(String token) {
        DataHolder decode = tokenUtility.decode(token);
        Long id = decode.getId();
        return userRepository.findById(id).orElse(null);
    }


    //    public User getUserByToken(String token) {
//        DataHolder decode = tokenUtility.decode(token);
//        Long id= decode.getId();
//        return userRepository.findById(id).orElse(null);
//    }
    @Override
    public User updateUser(String token, User user) {
        DataHolder decode = tokenUtility.decode(token);
        Long id= decode.getId();
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser!= null) {
            if(user.getFirstName()!=null) {
                existingUser.setFirstName(user.getFirstName());
            }
            if(user.getLastName()!=null) {
                existingUser.setLastName(user.getLastName());
            }
            if(user.getDob()!=null) {
                existingUser.setDob(user.getDob());
            }
            if(user.getUpdatedDate()!=null) {
                existingUser.setUpdatedDate(LocalDate.now());
            }
            if(user.getPassword()!=null) {
                existingUser.setPassword(user.getPassword());
            }
            if(user.getEmailId()!=null) {
                existingUser.setEmailId(user.getEmailId());
            }
            if(user.getRole()!=null) {
                existingUser.setRole(user.getRole());
            }
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
    String rawPassword = loginDTO.getPassword();

    User user = userRepository.findByemailId(userEmail);
    if (user != null) {
        boolean isPasswordMatch = passwordEncryption.verifyPassword(rawPassword, user.getPassword());
        if (isPasswordMatch) {
            String token = tokenUtility.getToken(user.getId(), user.getRole());
            return token;
        } else {
            throw new AuthenticationException("User or password invalid!");
        }
    } else {
        throw new AuthenticationException("User does not exist");
    }
}

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO(user);
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
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        user.setEmailId(userDTO.getEmailId());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());
        return user;
    }

}

