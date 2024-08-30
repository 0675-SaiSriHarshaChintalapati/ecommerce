package com.digit.ecommerce.model;


import com.digit.ecommerce.dto.CartDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "cartref")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "book_id")
    private Books book;

    private Long quantity;
    private Long totalPrice;

    public Cart(CartDTO cartDTO) {
        this.user=cartDTO.getUser();
        this.book=cartDTO.getBook();
        this.quantity=cartDTO.getQuantity();
        this.totalPrice=cartDTO.getTotalPrice();
    }
}


