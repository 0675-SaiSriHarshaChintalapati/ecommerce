package com.digit.ecommerce.repository;

import com.digit.ecommerce.model.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.id= :ids")
    void deleteAll(@Param("ids") Long ids);
}
