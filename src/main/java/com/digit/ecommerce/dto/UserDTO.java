package com.digit.ecommerce.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String emailId;
    private String role;
}

