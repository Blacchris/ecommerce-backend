package com.springdev.Repository;

import com.springdev.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceBetween(BigDecimal bigDecimal, BigDecimal bigDecimal1);

    List<Product> findByCategory_Name(String electronics);
//    Optional<Product> findByUser(long Id, long sellerId);
//    Optional<List<Product>> findAllByUser(long sellerId);
}
