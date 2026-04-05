package com.demo.jdbcgraphql.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findProductByCategory(String category);

    Product findProductByName(String name);
}
