package com.demo.jdbcgraphql.product;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
class ProductController {
    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    List<Product> getProducts() {
        return productService.getProducts();
    }

    @QueryMapping
    List<Product> getProductsByCategory(@Argument String category) {
        return productService.getProductsByCategory(category);
    }

    @MutationMapping
    void updateStock(@Argument String name, @Argument int newStock) {
        productService.updateStock(name, newStock);
    }

    @MutationMapping
    void receiveNewShipment(@Argument String name, @Argument int receivedQuantity) {
        productService.receiveNewShipment(name, receivedQuantity);
    }

    @MutationMapping
    void addNewProduct(@Argument String name, @Argument String category, @Argument int price, @Argument int stock) {
        productService.addNewProduct(name, category, price, stock);
    }
}
