    package com.digit.ecommerce.dto;


    import com.digit.ecommerce.model.Books;
    import com.digit.ecommerce.model.Cart;
    import com.digit.ecommerce.model.User;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    @NoArgsConstructor
    @Getter
    @Setter
    public class CartDTO {

        private User user;
        private Books book;
        private Long quantity;
        private Long totalPrice;

        public CartDTO(Cart cart) {
            this.user=cart.getUser();
            this.book=cart.getBook();
            this.quantity=cart.getQuantity();
            this.totalPrice=cart.getTotalPrice();
        }

    }

