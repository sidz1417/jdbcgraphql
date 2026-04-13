package com.demo.jdbcgraphql.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DataLoader dataLoader;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18");

    Product getProductByName(String name) {
        Product productWithName = new Product(name, null, null, null);
        ExampleMatcher nameMatcher = matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::exact);
        Example<Product> productExample = Example.of(productWithName, nameMatcher);
        return productRepository.findOne(productExample).isPresent() ? productRepository.findOne(productExample).get() : null;
    }

    String queryWithProductFilter(String filter) {
        return String.format("""
                query {
                  filterProducts(filter: %s) {
                    name
                    price
                    stock
                    category
                  }
                }
                """, filter);
    }

    @BeforeEach
    void setup() throws Exception {
        productRepository.deleteAll();
        dataLoader.run();
    }

    @Test
    void shouldReturnFilteredProducts() {
        String queryWithCategory = queryWithProductFilter("{category: \"Electronics\"}");
        String queryWithName = queryWithProductFilter("{category: \"Samsung Galaxy S23\"}");
        String queryAll = queryWithProductFilter("{}");

        graphQlTester.document(queryAll)
                .execute()
                .path("filterProducts")
                .entityList(Product.class)
                .hasSize(30);

        graphQlTester.document(queryWithCategory)
                .execute()
                .path("filterProducts[*].category") // Navigate to the specific field in the list
                .entityList(String.class)         // Convert the result to a List of Strings
                .satisfies(categories -> {        // Use a consumer for custom assertions
                    assert categories
                            .stream()
                            .allMatch("Electronics"::equals) : "Not all categories were 'Electronics'. Found: " + categories; // Standard Java Stream check
                });

        graphQlTester.document(queryWithName)
                .execute()
                .path("filterProducts[*].name") // Navigate to the specific field in the list
                .entityList(String.class)
                .satisfies(names -> {        // Use a consumer for custom assertions
                    assert names
                            .stream()
                            .allMatch("Samsung Galaxy S23"::equals) : "Not all product names were 'Samsung Galaxy S23'. Found: " + names; // Standard Java Stream check
                });

    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("stockUpdateScenarios")
    void shouldUpdateStockLevels(String testDescription, String name, String mutation, int expectedStock) {
        graphQlTester.document(mutation).execute();
        Product productAfterUpdate = getProductByName(name);
        assertThat(productAfterUpdate.getStock()).isEqualTo(expectedStock);
    }

    @Test
    void shouldAddNewProduct() {
        int totalProductsBeforeUpdate = productRepository.findAll().size();
        String document = """
                mutation{
                  addNewProduct(name:"corn chips", category:"Snacks", stock: 12, price:23)
                }
                """;
        graphQlTester.document(document).execute();
        assertThat(productRepository.findAll()).hasSize(totalProductsBeforeUpdate + 1);
        assertThat(getProductByName("corn chips")).isNotNull();
    }

    static Stream<Arguments> stockUpdateScenarios() {
        return Stream.of(
                Arguments.of(
                        "updateStock",
                        "iPhone 14 Pro",
                        """
                                mutation{
                                   updateStock(newStock:453, name:"iPhone 14 Pro")
                                 }
                                """,
                        453
                ),
                Arguments.of(
                        "receiveNewShipment",
                        "iPhone 14 Pro",
                        """
                                mutation{
                                   receiveNewShipment(name:"iPhone 14 Pro",receivedQuantity:30)
                                 }
                                """,
                        80
                )
        );
    }

}
