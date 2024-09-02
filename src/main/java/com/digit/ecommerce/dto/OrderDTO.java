package com.digit.ecommerce.dto;

import com.digit.ecommerce.model.Orders;
import lombok.Data;

@Data
public class OrderDTO {
    private Long bookId;
    private Long qty;
    private String address;


    public OrderDTO() {
    }


    public OrderDTO(Orders orders) {
        this.bookId = orders.getBook().getId();
        this.qty = orders.getQuantity();
        this.address = orders.getAddress();
    }
}
