package com.demo.jdbcgraphql.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
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

    @BeforeEach
    void setup() throws Exception {
        productRepository.deleteAll();
        dataLoader.run();
    }

    @Test
    void shouldReturnAllProducts() {
        String document = """
                query {
                    getProducts{
                        id,
                        name,
                        category,
                        stock,
                        price
                      }
                }
                """;

        graphQlTester.document(document)
                .execute()
                .path("getProducts")
                .entityList(Product.class)
                .hasSize(30);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Electronics", "Books", "Home & Kitchen", "Sports", "Fashion"})
    void shouldReturnProductsByCategory(String category) {
        String document = String.format("""
                query {
                    getProductsByCategory(category:"%s"){
                        id
                        name
                        category
                        stock
                        price
                    }
                }
                """, category);

        graphQlTester.document(document)
                .execute()
                .path("getProductsByCategory")
                .entityList(Product.class)
                .hasSizeGreaterThan(0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stockUpdateScenarios")
    void shouldUpdateStockLevels(String testCase, String mutation, int expectedStock) {
        String productName = "iPhone 14 Pro";

        graphQlTester.document(mutation).execute();

        Product productAfterUpdate = productRepository.findProductByName(productName);

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
        int totalProductsAfterUpdate = productRepository.findAll().size();
        Product newProduct = productRepository.findProductByName("corn chips");

        assertThat(totalProductsBeforeUpdate).isEqualTo(30);
        assertThat(totalProductsAfterUpdate).isEqualTo(31);
        assertAll(
                "new product added successfully",
                () -> assertThat(newProduct.getName()).isEqualTo("corn chips"),
                () -> assertThat(newProduct.getCategory()).isEqualTo("Snacks"),
                () -> assertThat(newProduct.getStock()).isEqualTo(12),
                () -> assertThat(newProduct.getPrice()).isEqualTo(23)
        );
    }

    static Stream<Arguments> stockUpdateScenarios() {
        return Stream.of(
                Arguments.of(
                        "updateStock",
                        """
                                mutation{
                                   updateStock(newStock:453, name:"iPhone 14 Pro")
                                 }
                                """,
                        453
                ),
                Arguments.of(
                        "receiveNewShipment",
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
