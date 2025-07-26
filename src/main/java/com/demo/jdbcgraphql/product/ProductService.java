package com.demo.jdbcgraphql.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<Product> getProducts() {
        return productRepository.findAll();
    }

    List<Product> getProductsByCategory(String category) {
        return productRepository.findProductsByCategory(category);
    }

    void updateStock(int productid, int newStock) {
        productRepository.findProductById(productid)
                .orElseThrow(() -> new RuntimeException("Product " + productid + " does not exist"));
        productRepository.updateStock(productid, newStock);
    }

    void receiveNewShipment(int productid, int receivedQuantity) {
        Product product = productRepository.findProductById(productid)
                .orElseThrow(() -> new RuntimeException("Product " + productid + " does not exist"));
        productRepository.updateStock(productid, product.stock() + receivedQuantity);
    }

    void addNewProduct(String name, String category, int price, int stock) {
        productRepository.save(new Product(null, name, category, stock, price));
    }
}
