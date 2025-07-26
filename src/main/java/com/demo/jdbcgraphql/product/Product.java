package com.demo.jdbcgraphql.product;

import org.springframework.data.annotation.Id;

record Product(
        @Id
        Integer id,
        String name,
        String category,
        int stock,
        int price
) {
}
