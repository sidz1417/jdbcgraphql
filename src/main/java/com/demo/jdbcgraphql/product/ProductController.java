package com.demo.jdbcgraphql.product;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.matching;

@Controller
class ProductController {
    final ProductRepository productRepository;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    Product getProductByName(String name) {
        Product productWithName = new Product(name, null, null, null);
        ExampleMatcher nameMatcher = matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::exact);
        Example<Product> productExample = Example.of(productWithName, nameMatcher);
        return productRepository.findOne(productExample).isPresent() ? productRepository.findOne(productExample).get() : null;
    }

    @QueryMapping
    List<Product> filterProducts(@Argument ProductFilter filter) {
        Product product = new Product(filter.name(), filter.category(), filter.stock(), filter.price());
        Example<Product> productExample = Example.of(product);
        return productRepository.findAll(productExample);
    }

    @MutationMapping
    void updateStock(@Argument String name, @Argument int newStock) {
        Product product = getProductByName(name);
        product.setStock(newStock);
        productRepository.save(product);
    }

    @MutationMapping
    void receiveNewShipment(@Argument String name, @Argument int receivedQuantity) {
        Product product = getProductByName(name);
        product.setStock(product.getStock() + receivedQuantity);
        productRepository.save(product);
    }

    @MutationMapping
    void addNewProduct(@Argument String name, @Argument String category, @Argument int price, @Argument int stock) {
        productRepository.save(new Product(name, category, stock, price));
    }
}
