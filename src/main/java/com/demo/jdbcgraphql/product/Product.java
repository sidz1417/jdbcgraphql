package com.demo.jdbcgraphql.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String category;
    Integer stock;
    Integer price;

    public Product(String name, String category, Integer stock, Integer price) {
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(category, product.category) && Objects.equals(stock, product.stock) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, stock, price);
    }

    Integer getId() {
        return id;
    }

    void setId(Integer id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getCategory() {
        return category;
    }

    void setCategory(String category) {
        this.category = category;
    }

    Integer getStock() {
        return stock;
    }

    void setStock(Integer stock) {
        this.stock = stock;
    }

    Integer getPrice() {
        return price;
    }

    void setPrice(Integer price) {
        this.price = price;
    }
}
