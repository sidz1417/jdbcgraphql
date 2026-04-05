package com.demo.jdbcgraphql.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<Product> getProducts() {
        return productRepository.findAll();
    }

    List<Product> getProductsByCategory(String category) {
        return productRepository.findProductByCategory(category);
    }

    void updateStock(String name, int newStock) {
        Product product = productRepository.findProductByName(name);
        product.setStock(newStock);
        productRepository.save(product);
    }

    void receiveNewShipment(String name, int receivedQuantity) {
        Product product = productRepository.findProductByName(name);
        product.setStock(product.getStock() + receivedQuantity);
        productRepository.save(product);
    }

    void addNewProduct(String name, String category, int price, int stock) {
        productRepository.save(new Product(name, category, stock, price));
    }
}
