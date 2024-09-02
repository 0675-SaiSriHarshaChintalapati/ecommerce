package com.digit.ecommerce.repository;

import com.digit.ecommerce.model.AddImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ImageRepository extends JpaRepository<AddImage,Long> {
}
