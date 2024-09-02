package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.OrderDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"user", "book"})
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;
    private Long price;
    private Long quantity;
    private String address;
    private boolean cancel;
    private String status;
    private String shippingStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Books book;
}

//
//
////package com.digit.ecommerce.model;
////
////import com.digit.ecommerce.dto.OrderDTO;
////import com.fasterxml.jackson.annotation.JsonIgnore;
////import lombok.AllArgsConstructor;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////
////import jakarta.persistence.*;
////import java.time.LocalDate;
////
////@Data
////@AllArgsConstructor
////@NoArgsConstructor
////@Entity
////public class Orders {
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////    private LocalDate orderDate;
////    private Long price;
////    private Long quantity;
////    private String address;
////    private boolean cancel;
////    private String status;
////    private String shippingStatus; // New column
////
////    @ManyToOne
////    @JoinColumn(name = "user_id")
////    @JsonIgnore
////    private User user;
////
////    @ManyToOne
////    @JoinColumn(name = "book_id")
////    private Books book;
////
////    public Orders(OrderDTO orderDTO, User user, Books book) {
////        this.orderDate = LocalDate.now();
////        this.price = book.getBookPrice();
////        this.quantity = orderDTO.getQty();
////        this.address = orderDTO.getAddress();
////        this.cancel = false;
////        this.status = "ordered";
////        this.shippingStatus = "placed"; // Set initial shipping status
////        this.user = user;
////        this.book = book;
////    }
////}
