package com.demo.jdbcgraphql.product;

record ProductFilter(String category, Integer stock, String name, Integer price) {
}
