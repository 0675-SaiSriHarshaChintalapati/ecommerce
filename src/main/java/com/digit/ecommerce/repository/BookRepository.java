package com.digit.ecommerce.repository;

import com.digit.ecommerce.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    @Transactional
    Books findBybookName(String bookName);

//    @Transactional
//    Books getByQuantity(Long id);
}


