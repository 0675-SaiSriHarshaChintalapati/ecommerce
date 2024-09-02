package com.digit.ecommerce.dto;


import com.digit.ecommerce.model.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "First name is mandatory")
    @Size(max = 255, message = "First name must be less than 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 255, message = "Last name must be less than 255 characters")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is mandatory")
    private LocalDate dob;

    private LocalDate registeredDate = LocalDate.now();

    private LocalDate updatedDate;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email ID is mandatory")
    @Email(message = "Email should be valid")
    private String emailId;

    @NotBlank(message = "Role is mandatory")
    //changes
    private String role;


    public UserDTO(User user) {
        this.role = user.getRole();
        this.emailId = user.getEmailId();
        this.password = user.getPassword();
        this.updatedDate = user.getUpdatedDate();
        this.registeredDate = user.getUpdatedDate();
        this.dob = user.getDob();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
    }
}

