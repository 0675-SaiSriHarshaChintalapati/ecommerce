package com.digit.ecommerce.repository;

import com.digit.ecommerce.model.Books;
import com.digit.ecommerce.model.User;
import com.digit.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    //Optional<Wishlist> findById(Long id);
        Wishlist findByUserAndBook(User user, Books book);
    }



