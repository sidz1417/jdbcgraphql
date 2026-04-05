package com.demo.jdbcgraphql.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class DataLoader implements CommandLineRunner {

    final ProductRepository productRepository;

    DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Electronics Category
        productRepository.save(new Product("iPhone 14 Pro", "Electronics", 50, 999));
        productRepository.save(new Product("Samsung Galaxy S23", "Electronics", 45, 899));
        productRepository.save(new Product("MacBook Pro M2", "Electronics", 30, 1499));
        productRepository.save(new Product("Dell XPS 15", "Electronics", 25, 1299));
        productRepository.save(new Product("AirPods Pro", "Electronics", 100, 249));
        productRepository.save(new Product("Sony WH-1000XM4", "Electronics", 40, 349));
        productRepository.save(new Product("iPad Air", "Electronics", 60, 599));
        productRepository.save(new Product("Nintendo Switch", "Electronics", 75, 299));

        // Books Category
        productRepository.save(new Product("The Midnight Library", "Books", 200, 15));
        productRepository.save(new Product("Atomic Habits", "Books", 150, 20));
        productRepository.save(new Product("Project Hail Mary", "Books", 120, 18));
        productRepository.save(new Product("The Psychology of Money", "Books", 180, 17));
        productRepository.save(new Product("Spring Boot in Action", "Books", 50, 45));
        productRepository.save(new Product("Clean Code", "Books", 45, 35));

        // Home & Kitchen
        productRepository.save(new Product("KitchenAid Mixer", "Home & Kitchen", 30, 299));
        productRepository.save(new Product("Ninja Air Fryer", "Home & Kitchen", 65, 119));
        productRepository.save(new Product("Instant Pot", "Home & Kitchen", 80, 89));
        productRepository.save(new Product("Dyson V15", "Home & Kitchen", 25, 699));
        productRepository.save(new Product("Coffee Maker", "Home & Kitchen", 90, 79));
        productRepository.save(new Product("Robot Vacuum", "Home & Kitchen", 40, 249));

        // Sports & Outdoors
        productRepository.save(new Product("Yoga Mat", "Sports", 150, 29));
        productRepository.save(new Product("Dumbbells Set", "Sports", 35, 149));
        productRepository.save(new Product("Tennis Racket", "Sports", 45, 89));
        productRepository.save(new Product("Basketball", "Sports", 100, 25));
        productRepository.save(new Product("Running Shoes", "Sports", 75, 129));

        // Fashion
        productRepository.save(new Product("Leather Wallet", "Fashion", 100, 49));
        productRepository.save(new Product("Sunglasses", "Fashion", 60, 159));
        productRepository.save(new Product("Watch", "Fashion", 40, 199));
        productRepository.save(new Product("Backpack", "Fashion", 85, 79));
        productRepository.save(new Product("Winter Jacket", "Fashion", 50, 189));
    }
}
