package com.demo.jdbcgraphql.product;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
interface ProductRepository extends ListCrudRepository<Product, Integer> {

    List<Product> findProductsByCategory(String category);

    @Query("SELECT * FROM product WHERE id = :id")
    Optional<Product> findProductById(@Param("id") int id);

    @Modifying
    @Query("UPDATE product SET stock = :newStock WHERE id = :id")
    void updateStock(@Param("id") int id, @Param("newStock") int newStock);
}
