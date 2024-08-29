package com.digit.ecommerce.model;

import com.digit.ecommerce.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;
    private double price;
    private int quantity;
    private String address;
    private boolean cancel;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;




    public Orders(OrderDTO orderDTO, User user, Books book) {
        this.orderDate = LocalDate.now();
        this.price = book.getBookPrice();
        this.quantity = orderDTO.getQty();
        this.address = orderDTO.getAddress();
        this.cancel = false;
        this.status = "ordered";
        this.user = user;
        this.book = book;
    }
}
