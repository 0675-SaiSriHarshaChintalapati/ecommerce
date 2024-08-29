package com.digit.ecommerce.repository;

import com.digit.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCancel(boolean cancel);
    List<Orders> findByUserId(Long userId);
}

