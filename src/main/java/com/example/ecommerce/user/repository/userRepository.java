package com.example.ecommerce.user.repository;

import com.example.ecommerce.user.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface userRepository extends JpaRepository<user,Long> {

Optional<user> findByEmail(String email);
Optional<user> findByUsername(String username);
}
