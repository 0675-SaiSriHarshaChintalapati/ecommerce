package com.digit.ecommerce.model;
import com.digit.ecommerce.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registeredDate = LocalDate.now();
    private LocalDate updatedDate = LocalDate.now();
    private String password;
    private String emailId;
    private String role;
    public User(UserDTO userdto) {
        this.id = userdto.getId();
        this.role = userdto.getRole();
        this.emailId = userdto.getEmailId();
        this.password = userdto.getPassword();
        this.updatedDate = userdto.getUpdatedDate();
        this.registeredDate = userdto.getUpdatedDate();
        this.dob = userdto.getDob();
        this.lastName = userdto.getLastName();
        this.firstName = userdto.getFirstName();
    }
}

