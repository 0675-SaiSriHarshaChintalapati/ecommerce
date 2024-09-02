package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
@ToString(exclude = {"wishlists", "cart", "order"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private LocalDate registeredDate = LocalDate.now();
    private LocalDate updatedDate = LocalDate.now();
    private String password;
    private String emailId;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Wishlist> wishlists;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @JsonManagedReference
    private List<Cart> cart;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @JsonManagedReference
    private List<Orders> order;

    public User(UserDTO userdto) {
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




//package com.digit.ecommerce.model;
//
//import com.digit.ecommerce.dto.UserDTO;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "user_details")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName;
//    private String lastName;
//    private LocalDate dob;
//    private LocalDate registeredDate = LocalDate.now();
//    private LocalDate updatedDate = LocalDate.now();
//    private String password;
//    private String emailId;
//    private String role;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
//    private List<Wishlist> wishlists;
//
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
//    @JsonManagedReference
//    private List<Cart> cart;
//
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
//    @JsonManagedReference
//    private List<Orders> order;
//
//    public User(UserDTO userdto) {
//        this.role = userdto.getRole();
//        this.emailId = userdto.getEmailId();
//        this.password = userdto.getPassword();
//        this.updatedDate = userdto.getUpdatedDate();
//        this.registeredDate = userdto.getUpdatedDate();
//        this.dob = userdto.getDob();
//        this.lastName = userdto.getLastName();
//        this.firstName = userdto.getFirstName();
//    }
//}
