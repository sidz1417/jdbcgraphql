package com.demo.jdbcgraphql.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

interface ProductRepository extends JpaRepository<Product, Integer>, QueryByExampleExecutor<Product> {
}
