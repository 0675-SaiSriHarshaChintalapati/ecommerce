package com.digit.ecommerce.repository;
import com.digit.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByfirstName(String firstname);
    User findByemailId(String emailid);
}


